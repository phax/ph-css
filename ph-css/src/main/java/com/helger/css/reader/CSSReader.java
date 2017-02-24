/**
 * Copyright (C) 2014-2017 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.css.reader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.charset.CharsetManager;
import com.helger.commons.charset.EUnicodeBOM;
import com.helger.commons.collection.ArrayHelper;
import com.helger.commons.concurrent.SimpleReadWriteLock;
import com.helger.commons.io.IHasInputStream;
import com.helger.commons.io.IHasReader;
import com.helger.commons.io.provider.IReaderProvider;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.commons.io.stream.NonBlockingPushbackInputStream;
import com.helger.commons.io.stream.NonBlockingStringReader;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.commons.io.streamprovider.StringInputStreamProvider;
import com.helger.commons.io.streamprovider.StringReaderProvider;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.handler.CSSHandler;
import com.helger.css.handler.DoNothingCSSParseExceptionCallback;
import com.helger.css.handler.ICSSParseExceptionCallback;
import com.helger.css.handler.LoggingCSSParseExceptionCallback;
import com.helger.css.parser.CSSCharStream;
import com.helger.css.parser.CSSNode;
import com.helger.css.parser.CSSParseHelper;
import com.helger.css.parser.CharStream;
import com.helger.css.parser.ParseException;
import com.helger.css.parser.ParserCSS30;
import com.helger.css.parser.ParserCSS30TokenManager;
import com.helger.css.parser.ParserCSSCharsetDetector;
import com.helger.css.parser.ParserCSSCharsetDetectorTokenManager;
import com.helger.css.reader.errorhandler.ICSSInterpretErrorHandler;
import com.helger.css.reader.errorhandler.ICSSParseErrorHandler;
import com.helger.css.reader.errorhandler.LoggingCSSInterpretErrorHandler;
import com.helger.css.reader.errorhandler.ThrowingCSSParseErrorHandler;

/**
 * This is the central user class for reading and parsing CSS from different
 * sources. This class reads full CSS declarations only. To read only a
 * declaration list (like from an HTML <code>&lt;style&gt;</code> attribute) the
 * {@link CSSReaderDeclarationList} is available.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class CSSReader
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (CSSReader.class);
  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock ();

  // Use the ThrowingCSSParseErrorHandler for maximum backward compatibility
  @GuardedBy ("s_aRWLock")
  private static ICSSParseErrorHandler s_aDefaultParseErrorHandler = new ThrowingCSSParseErrorHandler ();

  // Use the LoggingCSSParseExceptionHandler for maximum backward compatibility
  @GuardedBy ("s_aRWLock")
  private static ICSSParseExceptionCallback s_aDefaultParseExceptionHandler = new LoggingCSSParseExceptionCallback ();

  @GuardedBy ("s_aRWLock")
  private static ICSSInterpretErrorHandler s_aDefaultInterpretErrorHandler = new LoggingCSSInterpretErrorHandler ();

  @PresentForCodeCoverage
  private static final CSSReader s_aInstance = new CSSReader ();

  private CSSReader ()
  {}

  /**
   * @return The default CSS parse error handler. May be <code>null</code>. For
   *         backwards compatibility reasons this is be default an instance of
   *         {@link ThrowingCSSParseErrorHandler}.
   */
  @Nullable
  public static ICSSParseErrorHandler getDefaultParseErrorHandler ()
  {
    return s_aRWLock.readLocked ( () -> s_aDefaultParseErrorHandler);
  }

  /**
   * Set the default CSS parse error handler (for recoverable errors).
   *
   * @param aDefaultParseErrorHandler
   *        The new default error handler to be used. May be <code>null</code>
   *        to indicate that no special error handler should be used.
   */
  public static void setDefaultParseErrorHandler (@Nullable final ICSSParseErrorHandler aDefaultParseErrorHandler)
  {
    s_aRWLock.writeLocked ( () -> s_aDefaultParseErrorHandler = aDefaultParseErrorHandler);
  }

  /**
   * @return The default CSS parse exception handler. May not be
   *         <code>null</code>. For backwards compatibility reasons this is be
   *         default an instance of {@link LoggingCSSParseExceptionCallback}.
   * @since 3.7.4
   */
  @Nonnull
  public static ICSSParseExceptionCallback getDefaultParseExceptionHandler ()
  {
    return s_aRWLock.readLocked ( () -> s_aDefaultParseExceptionHandler);
  }

  /**
   * Set the default CSS parse exception handler (for unrecoverable errors).
   *
   * @param aDefaultParseExceptionHandler
   *        The new default exception handler to be used. May not be
   *        <code>null</code>.
   * @since 3.7.4
   */
  public static void setDefaultParseExceptionHandler (@Nonnull final ICSSParseExceptionCallback aDefaultParseExceptionHandler)
  {
    ValueEnforcer.notNull (aDefaultParseExceptionHandler, "DefaultParseExceptionHandler");

    s_aRWLock.writeLocked ( () -> s_aDefaultParseExceptionHandler = aDefaultParseExceptionHandler);
  }

  /**
   * @return The default interpret error handler to handle interpretation errors
   *         in successfully parsed CSS. Never <code>null</code>.
   * @since 5.0.2
   */
  @Nonnull
  public static ICSSInterpretErrorHandler getDefaultInterpretErrorHandler ()
  {
    return s_aRWLock.readLocked ( () -> s_aDefaultInterpretErrorHandler);
  }

  /**
   * Set the default interpret error handler to handle interpretation errors in
   * successfully parsed CSS.
   *
   * @param aDefaultErrorHandler
   *        The default error handler to be used. May not be <code>null</code>.
   * @since 5.0.2
   */
  public static void setDefaultInterpretErrorHandler (@Nonnull final ICSSInterpretErrorHandler aDefaultErrorHandler)
  {
    ValueEnforcer.notNull (aDefaultErrorHandler, "DefaultErrorHandler");

    s_aRWLock.writeLocked ( () -> s_aDefaultInterpretErrorHandler = aDefaultErrorHandler);
  }

  /**
   * Main reading of the CSS
   *
   * @param aCharStream
   *        The stream to read from. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        A custom handler for recoverable errors. May be <code>null</code>.
   * @param aCustomExceptionHandler
   *        A custom handler for unrecoverable errors. May not be
   *        <code>null</code>.
   * @param bBrowserCompliantMode
   *        <code>true</code> for browser compliant parsing, <code>false</code>
   *        for default parsing.
   * @return <code>null</code> if parsing failed with an unrecoverable error
   *         (and no throwing exception handler is used), or <code>null</code>
   *         if a recoverable error occurred and no
   *         {@link com.helger.css.reader.errorhandler.ThrowingCSSParseErrorHandler}
   *         was used or non-<code>null</code> if parsing succeeded.
   */
  @Nullable
  private static CSSNode _readStyleSheet (@Nonnull final CharStream aCharStream,
                                          @Nonnull final ECSSVersion eVersion,
                                          @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                          @Nonnull final ICSSParseExceptionCallback aCustomExceptionHandler,
                                          final boolean bBrowserCompliantMode)
  {
    try
    {
      switch (eVersion)
      {
        case CSS21:
        case CSS30:
        {
          final ParserCSS30TokenManager aTokenHdl = new ParserCSS30TokenManager (aCharStream);
          aTokenHdl.setCustomErrorHandler (aCustomErrorHandler);
          final ParserCSS30 aParser = new ParserCSS30 (aTokenHdl);
          aParser.setCustomErrorHandler (aCustomErrorHandler);
          aParser.setBrowserCompliantMode (bBrowserCompliantMode);
          // Main parsing
          return aParser.styleSheet ();
        }
        default:
          throw new IllegalArgumentException ("Unsupported CSS version " + eVersion);
      }
    }
    catch (final ParseException ex)
    {
      // Unrecoverable error
      aCustomExceptionHandler.onException (ex);
      return null;
    }
    catch (final Throwable ex)
    {
      // As e.g. indicated by https://github.com/phax/ph-css/issues/9
      aCustomExceptionHandler.onException (new ParseException (ex.getMessage ()));
      return null;
    }
  }

  /**
   * Check if the passed CSS file can be parsed without error
   *
   * @param aFile
   *        The file to be parsed. May not be <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used for reading the CSS file in case neither a
   *        <code>@charset</code> rule nor a BOM is present. May not be
   *        <code>null</code>.
   * @param eVersion
   *        The CSS version to be used for scanning. May not be
   *        <code>null</code>.
   * @return <code>true</code> if the file can be parsed without error,
   *         <code>false</code> if not
   */
  public static boolean isValidCSS (@Nonnull final File aFile,
                                    @Nonnull final Charset aFallbackCharset,
                                    @Nonnull final ECSSVersion eVersion)
  {
    return isValidCSS (new FileSystemResource (aFile), aFallbackCharset, eVersion);
  }

  /**
   * Check if the passed CSS resource can be parsed without error
   *
   * @param aRes
   *        The resource to be parsed. May not be <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used for reading the CSS file in case neither a
   *        <code>@charset</code> rule nor a BOM is present. May not be
   *        <code>null</code>.
   * @param eVersion
   *        The CSS version to be used for scanning. May not be
   *        <code>null</code>.
   * @return <code>true</code> if the file can be parsed without error,
   *         <code>false</code> if not
   */
  public static boolean isValidCSS (@Nonnull final IReadableResource aRes,
                                    @Nonnull final Charset aFallbackCharset,
                                    @Nonnull final ECSSVersion eVersion)
  {
    ValueEnforcer.notNull (aRes, "Resource");
    ValueEnforcer.notNull (aFallbackCharset, "FallbackCharset");
    ValueEnforcer.notNull (eVersion, "Version");

    final Reader aReader = aRes.getReader (aFallbackCharset);
    if (aReader == null)
    {
      s_aLogger.warn ("Failed to open CSS reader " + aRes);
      return false;
    }
    return isValidCSS (aReader, eVersion);
  }

  /**
   * Check if the passed input stream can be resembled to valid CSS content.
   * This is accomplished by fully parsing the CSS file each time the method is
   * called. This is similar to calling
   * {@link #readFromStream(IHasInputStream,Charset, ECSSVersion)} and checking
   * for a non-<code>null</code> result.
   *
   * @param aIS
   *        The input stream to use. Is automatically closed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>true</code> if the CSS is valid according to the version,
   *         <code>false</code> if not
   */
  public static boolean isValidCSS (@Nonnull @WillClose final InputStream aIS,
                                    @Nonnull final Charset aFallbackCharset,
                                    @Nonnull final ECSSVersion eVersion)
  {
    ValueEnforcer.notNull (aIS, "InputStream");
    ValueEnforcer.notNull (aFallbackCharset, "FallbackCharset");

    return isValidCSS (StreamHelper.createReader (aIS, aFallbackCharset), eVersion);
  }

  /**
   * Check if the passed String can be resembled to valid CSS content. This is
   * accomplished by fully parsing the CSS file each time the method is called.
   * This is similar to calling
   * {@link #readFromString(String, Charset, ECSSVersion)} and checking for a
   * non-<code>null</code> result.
   *
   * @param sCSS
   *        The CSS string to scan. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>true</code> if the CSS is valid according to the version,
   *         <code>false</code> if not
   */
  public static boolean isValidCSS (@Nonnull final String sCSS, @Nonnull final ECSSVersion eVersion)
  {
    ValueEnforcer.notNull (sCSS, "CSS");

    return isValidCSS (new NonBlockingStringReader (sCSS), eVersion);
  }

  /**
   * Check if the passed reader can be resembled to valid CSS content. This is
   * accomplished by fully parsing the CSS each time the method is called. This
   * is similar to calling
   * {@link #readFromStream(IHasInputStream, Charset, ECSSVersion)} and checking
   * for a non-<code>null</code> result.
   *
   * @param aReader
   *        The reader to use. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>true</code> if the CSS is valid according to the version,
   *         <code>false</code> if not
   */
  public static boolean isValidCSS (@Nonnull @WillClose final Reader aReader, @Nonnull final ECSSVersion eVersion)
  {
    ValueEnforcer.notNull (aReader, "Reader");
    ValueEnforcer.notNull (eVersion, "Version");

    try
    {
      final CSSCharStream aCharStream = new CSSCharStream (aReader);
      final CSSNode aNode = _readStyleSheet (aCharStream,
                                             eVersion,
                                             getDefaultParseErrorHandler (),
                                             new DoNothingCSSParseExceptionCallback (),
                                             false);
      return aNode != null;
    }
    finally
    {
      StreamHelper.close (aReader);
    }
  }

  /**
   * Read the CSS from the passed String using a byte stream.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CascadingStyleSheet readFromString (@Nonnull final String sCSS,
                                                    @Nonnull final Charset aFallbackCharset,
                                                    @Nonnull final ECSSVersion eVersion)
  {
    return readFromStringStream (sCSS,
                                 new CSSReaderSettings ().setFallbackCharset (aFallbackCharset)
                                                         .setCSSVersion (eVersion));
  }

  /**
   * Read the CSS from the passed String using a byte stream.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CascadingStyleSheet readFromString (@Nonnull final String sCSS,
                                                    @Nonnull final Charset aFallbackCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromStringStream (sCSS,
                                 new CSSReaderSettings ().setFallbackCharset (aFallbackCharset)
                                                         .setCSSVersion (eVersion)
                                                         .setCustomErrorHandler (aCustomErrorHandler));
  }

  /**
   * Read the CSS from the passed String using a byte stream.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CascadingStyleSheet readFromString (@Nonnull final String sCSS,
                                                    @Nonnull final Charset aFallbackCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromStringStream (sCSS,
                                 new CSSReaderSettings ().setFallbackCharset (aFallbackCharset)
                                                         .setCSSVersion (eVersion)
                                                         .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed String using a byte stream.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.7.3
   */
  @Nullable
  public static CascadingStyleSheet readFromString (@Nonnull final String sCSS,
                                                    @Nonnull final Charset aFallbackCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                    @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromStringStream (sCSS,
                                 new CSSReaderSettings ().setFallbackCharset (aFallbackCharset)
                                                         .setCSSVersion (eVersion)
                                                         .setCustomErrorHandler (aCustomErrorHandler)
                                                         .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed String using a byte stream.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be
   *        <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.8.2
   */
  @Nullable
  public static CascadingStyleSheet readFromStringStream (@Nonnull final String sCSS,
                                                          @Nonnull final CSSReaderSettings aSettings)
  {
    return readFromStream (new StringInputStreamProvider (sCSS, aSettings.getFallbackCharset ()), aSettings);
  }

  /**
   * Read the CSS from the passed String using a character stream. An eventually
   * contained <code>@charset</code> rule is ignored.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.7.3
   */
  @Nullable
  public static CascadingStyleSheet readFromString (@Nonnull final String sCSS, @Nonnull final ECSSVersion eVersion)
  {
    return readFromStringReader (sCSS, new CSSReaderSettings ().setCSSVersion (eVersion));
  }

  /**
   * Read the CSS from the passed String using a character stream. An eventually
   * contained <code>@charset</code> rule is ignored.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.7.3
   */
  @Nullable
  public static CascadingStyleSheet readFromString (@Nonnull final String sCSS,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromStringReader (sCSS,
                                 new CSSReaderSettings ().setCSSVersion (eVersion)
                                                         .setCustomErrorHandler (aCustomErrorHandler));
  }

  /**
   * Read the CSS from the passed String using a character stream. An eventually
   * contained <code>@charset</code> rule is ignored.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.7.3
   */
  @Nullable
  public static CascadingStyleSheet readFromString (@Nonnull final String sCSS,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromStringReader (sCSS,
                                 new CSSReaderSettings ().setCSSVersion (eVersion)
                                                         .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed String using a character stream. An eventually
   * contained <code>@charset</code> rule is ignored.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.7.3
   */
  @Nullable
  public static CascadingStyleSheet readFromString (@Nonnull final String sCSS,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                    @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromStringReader (sCSS,
                                 new CSSReaderSettings ().setCSSVersion (eVersion)
                                                         .setCustomErrorHandler (aCustomErrorHandler)
                                                         .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed String using a character stream. An eventually
   * contained <code>@charset</code> rule is ignored.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be
   *        <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.8.2
   */
  @Nullable
  public static CascadingStyleSheet readFromStringReader (@Nonnull final String sCSS,
                                                          @Nonnull final CSSReaderSettings aSettings)
  {
    return readFromReader (new StringReaderProvider (sCSS), aSettings);
  }

  /**
   * Read the CSS from the passed File.
   *
   * @param aFile
   *        The file containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CascadingStyleSheet readFromFile (@Nonnull final File aFile,
                                                  @Nonnull final Charset aFallbackCharset,
                                                  @Nonnull final ECSSVersion eVersion)
  {
    return readFromFile (aFile,
                         new CSSReaderSettings ().setFallbackCharset (aFallbackCharset).setCSSVersion (eVersion));
  }

  /**
   * Read the CSS from the passed File.
   *
   * @param aFile
   *        The file containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CascadingStyleSheet readFromFile (@Nonnull final File aFile,
                                                  @Nonnull final Charset aFallbackCharset,
                                                  @Nonnull final ECSSVersion eVersion,
                                                  @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromFile (aFile,
                         new CSSReaderSettings ().setFallbackCharset (aFallbackCharset)
                                                 .setCSSVersion (eVersion)
                                                 .setCustomErrorHandler (aCustomErrorHandler));
  }

  /**
   * Read the CSS from the passed File.
   *
   * @param aFile
   *        The file containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CascadingStyleSheet readFromFile (@Nonnull final File aFile,
                                                  @Nonnull final Charset aFallbackCharset,
                                                  @Nonnull final ECSSVersion eVersion,
                                                  @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromFile (aFile,
                         new CSSReaderSettings ().setFallbackCharset (aFallbackCharset)
                                                 .setCSSVersion (eVersion)
                                                 .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed File.
   *
   * @param aFile
   *        The file containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CascadingStyleSheet readFromFile (@Nonnull final File aFile,
                                                  @Nonnull final Charset aFallbackCharset,
                                                  @Nonnull final ECSSVersion eVersion,
                                                  @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                  @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromFile (aFile,
                         new CSSReaderSettings ().setFallbackCharset (aFallbackCharset)
                                                 .setCSSVersion (eVersion)
                                                 .setCustomErrorHandler (aCustomErrorHandler)
                                                 .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed File.
   *
   * @param aFile
   *        The file containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be
   *        <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.8.2
   */
  @Nullable
  public static CascadingStyleSheet readFromFile (@Nonnull final File aFile, @Nonnull final CSSReaderSettings aSettings)
  {
    return readFromStream (new FileSystemResource (aFile), aSettings);
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}. If the CSS contains
   * an explicit charset, the whole CSS is parsed again, with the charset found
   * inside the file, so the passed {@link IHasInputStream} must be able to
   * create a new input stream on second invocation!
   *
   * @param aISP
   *        The input stream provider to use. Must be able to create new input
   *        streams on every invocation, in case an explicit charset node was
   *        found. May not be <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CascadingStyleSheet readFromStream (@Nonnull final IHasInputStream aISP,
                                                    @Nonnull final Charset aFallbackCharset,
                                                    @Nonnull final ECSSVersion eVersion)
  {
    return readFromStream (aISP,
                           new CSSReaderSettings ().setFallbackCharset (aFallbackCharset).setCSSVersion (eVersion));
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}. If the CSS contains
   * an explicit charset, the whole CSS is parsed again, with the charset found
   * inside the file, so the passed {@link IHasInputStream} must be able to
   * create a new input stream on second invocation!
   *
   * @param aISP
   *        The input stream provider to use. Must be able to create new input
   *        streams on every invocation, in case an explicit charset node was
   *        found. May not be <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CascadingStyleSheet readFromStream (@Nonnull final IHasInputStream aISP,
                                                    @Nonnull final Charset aFallbackCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromStream (aISP,
                           new CSSReaderSettings ().setFallbackCharset (aFallbackCharset)
                                                   .setCSSVersion (eVersion)
                                                   .setCustomErrorHandler (aCustomErrorHandler));
  }

  private static final class InputStreamAndCharset implements IHasInputStream
  {
    private final InputStream m_aIS;
    private final Charset m_aCharset;

    public InputStreamAndCharset (@Nullable final InputStream aIS, @Nullable final Charset aCharset)
    {
      m_aIS = aIS;
      m_aCharset = aCharset;
    }

    @Nullable
    public InputStream getInputStream ()
    {
      return m_aIS;
    }

    public boolean hasInputStream ()
    {
      return m_aIS != null;
    }

    @Nullable
    public Charset getCharset ()
    {
      return m_aCharset;
    }
  }

  /**
   * Open the {@link InputStream} provided by the passed {@link IHasInputStream}
   * . If a BOM is present in the {@link InputStream} it is read and if possible
   * the charset is automatically determined from the BOM.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @return <code>null</code> if no InputStream could be opened, the pair with
   *         non-<code>null</code> {@link InputStream} and a potentially
   *         <code>null</code> {@link Charset} otherwise.
   */
  @Nullable
  private static InputStreamAndCharset _getInputStreamWithoutBOM (@Nonnull final IHasInputStream aISP)
  {
    // Try to open input stream
    final InputStream aIS = aISP.getInputStream ();
    if (aIS == null)
      return null;

    // Check for BOM
    final int nMaxBOMBytes = EUnicodeBOM.getMaximumByteCount ();
    final NonBlockingPushbackInputStream aPIS = new NonBlockingPushbackInputStream (aIS, nMaxBOMBytes);
    try
    {
      final byte [] aBOM = new byte [nMaxBOMBytes];
      final int nReadBOMBytes = aPIS.read (aBOM);
      Charset aDeterminedCharset = null;
      if (nReadBOMBytes > 0)
      {
        // Some byte BOMs were read
        final EUnicodeBOM eBOM = EUnicodeBOM.getFromBytesOrNull (ArrayHelper.getCopy (aBOM, 0, nReadBOMBytes));
        if (eBOM == null)
        {
          // Unread the whole BOM
          aPIS.unread (aBOM, 0, nReadBOMBytes);
        }
        else
        {
          // Unread the unnecessary parts of the BOM
          final int nBOMBytes = eBOM.getByteCount ();
          if (nBOMBytes < nReadBOMBytes)
            aPIS.unread (aBOM, nBOMBytes, nReadBOMBytes - nBOMBytes);

          // Use the Charset of the BOM - maybe null!
          aDeterminedCharset = eBOM.getCharset ();
        }
      }
      return new InputStreamAndCharset (aPIS, aDeterminedCharset);
    }
    catch (final IOException ex)
    {
      s_aLogger.error ("Failed to determine BOM", ex);
      return null;
    }
  }

  /**
   * Determine the charset to read the CSS file. The logic is as follows:
   * <ol>
   * <li>Determine the charset used to read the @charset from the stream. If a
   * BOM is present and a matching Charset is present, this charset is used. As
   * a fallback the CSS file is initially read with ISO-8859-1.</li>
   * <li>If the CSS content contains a valid @charset rule, the defined charset
   * is returned even if a different BOM is present.</li>
   * <li>If the CSS content does not contain a valid @charset rule than the
   * charset of the BOM is returned (if any).</li>
   * <li>Otherwise <code>null</code> is returned.</li>
   * </ol>
   *
   * @param aISP
   *        The input stream provider to read from. May not be <code>null</code>
   *        .
   * @return <code>null</code> if the input stream could not be opened or if
   *         neither a BOM nor a charset is specified. Otherwise a non-
   *         <code>null</code> Charset is returned.
   * @throws IllegalStateException
   *         if an invalid charset is supplied
   */
  @Nullable
  public static Charset getCharsetDeclaredInCSS (@Nonnull final IHasInputStream aISP)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");

    // Open input stream
    final InputStreamAndCharset aISAndBOM = _getInputStreamWithoutBOM (aISP);
    if (aISAndBOM == null || !aISAndBOM.hasInputStream ())
    {
      // Failed to open stream, so no charset!
      return null;
    }

    final InputStream aIS = aISAndBOM.getInputStream ();
    final Charset aBOMCharset = aISAndBOM.getCharset ();
    Charset aStreamCharset = aBOMCharset;
    if (aStreamCharset == null)
    {
      // Always read as ISO-8859-1 as everything contained in the CSS charset
      // declaration can be handled by this charset
      // A known problem is when the file is UTF-16, UTF-16BE, UTF-16LE etc.
      // encoded. In this case a BOM must be present to read the file correctly!
      aStreamCharset = StandardCharsets.ISO_8859_1;
    }

    final Reader aReader = StreamHelper.createReader (aIS, aStreamCharset);
    try
    {
      // Read with the Stream charset
      final CSSCharStream aCharStream = new CSSCharStream (aReader);
      final ParserCSSCharsetDetectorTokenManager aTokenHdl = new ParserCSSCharsetDetectorTokenManager (aCharStream);
      final ParserCSSCharsetDetector aParser = new ParserCSSCharsetDetector (aTokenHdl);
      final String sCharsetName = aParser.styleSheetCharset ().getText ();
      if (sCharsetName == null)
      {
        // No charset specified - use the one from the BOM (may be null)
        return aISAndBOM.getCharset ();
      }
      // Remove leading and trailing quotes from value
      final String sPlainCharsetName = CSSParseHelper.extractStringValue (sCharsetName);
      final Charset aReadCharset = CharsetManager.getCharsetFromName (sPlainCharsetName);

      if (aBOMCharset != null && !aBOMCharset.equals (aReadCharset))
      {
        // BOM charset different from read charset
        s_aLogger.warn ("The charset found in the CSS data (" +
                        aReadCharset.name () +
                        ") differs from the charset determined by the BOM (" +
                        aBOMCharset.name () +
                        ") -> Using the read charset");
      }

      return aReadCharset;
    }
    catch (final ParseException ex)
    {
      // Should never occur, as the parse exception is caught inside the
      // grammar!
      throw new IllegalStateException ("Failed to parse CSS charset definition", ex);
    }
    catch (final Throwable ex)
    {
      // As e.g. indicated by https://github.com/phax/ph-css/issues/9
      throw new IllegalStateException ("Failed to parse CSS charset definition", ex);
    }
    finally
    {
      StreamHelper.close (aReader);
    }
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}. If the CSS contains
   * an explicit charset, the whole CSS is parsed again, with the charset found
   * inside the file, so the passed {@link IHasInputStream} must be able to
   * create a new input stream on second invocation!
   *
   * @param aISP
   *        The input stream provider to use. Must be able to create new input
   *        streams on every invocation, in case an explicit charset node was
   *        found. May not be <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CascadingStyleSheet readFromStream (@Nonnull final IHasInputStream aISP,
                                                    @Nonnull final Charset aFallbackCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromStream (aISP,
                           new CSSReaderSettings ().setFallbackCharset (aFallbackCharset)
                                                   .setCSSVersion (eVersion)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}. If the CSS contains
   * an explicit charset, the whole CSS is parsed again, with the charset found
   * inside the file, so the passed {@link IHasInputStream} must be able to
   * create a new input stream on second invocation!
   *
   * @param aISP
   *        The input stream provider to use. Must be able to create new input
   *        streams on every invocation, in case an explicit charset node was
   *        found. May not be <code>null</code>.
   * @param aFallbackCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CascadingStyleSheet readFromStream (@Nonnull final IHasInputStream aISP,
                                                    @Nonnull final Charset aFallbackCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                    @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromStream (aISP,
                           new CSSReaderSettings ().setFallbackCharset (aFallbackCharset)
                                                   .setCSSVersion (eVersion)
                                                   .setCustomErrorHandler (aCustomErrorHandler)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}. If the CSS contains
   * an explicit charset, the whole CSS is parsed again, with the charset found
   * inside the file, so the passed {@link IHasInputStream} must be able to
   * create a new input stream on second invocation!
   *
   * @param aISP
   *        The input stream provider to use. Must be able to create new input
   *        streams on every invocation, in case an explicit charset node was
   *        found. May not be <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be
   *        <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.8.2
   */
  @Nullable
  public static CascadingStyleSheet readFromStream (@Nonnull final IHasInputStream aISP,
                                                    @Nonnull final CSSReaderSettings aSettings)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");
    ValueEnforcer.notNull (aSettings, "Settings");

    Charset aCharsetToUse;

    // Check if the CSS contains a declared charset or as an alternative use the
    // Charset from the BOM
    Charset aDeclaredCharset;
    try
    {
      aDeclaredCharset = getCharsetDeclaredInCSS (aISP);
    }
    catch (final IllegalStateException ex)
    {
      // Failed to parse CSS at a very low level
      return null;
    }
    if (aDeclaredCharset != null)
    {
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Reading CSS definition again with explicit charset '" + aDeclaredCharset.name () + "'");
      aCharsetToUse = aDeclaredCharset;
    }
    else
    {
      // No charset declared - use fallback
      aCharsetToUse = aSettings.getFallbackCharset ();
    }

    // Open input stream
    final InputStreamAndCharset aISAndBOM = _getInputStreamWithoutBOM (aISP);
    if (aISAndBOM == null || !aISAndBOM.hasInputStream ())
    {
      // Failed to open stream!
      return null;
    }

    final InputStream aIS = aISAndBOM.getInputStream ();
    final Reader aReader = StreamHelper.createReader (aIS, aCharsetToUse);
    final ECSSVersion eVersion = aSettings.getCSSVersion ();
    try
    {
      final CSSCharStream aCharStream = new CSSCharStream (aReader);
      aCharStream.setTabSize (aSettings.getTabSize ());

      // Use the default CSS parse error handler if none is provided
      ICSSParseErrorHandler aRealParseErrorHandler = aSettings.getCustomErrorHandler ();
      if (aRealParseErrorHandler == null)
        aRealParseErrorHandler = getDefaultParseErrorHandler ();

      // Use the default CSS exception handler if none is provided
      ICSSParseExceptionCallback aRealParseExceptionHandler = aSettings.getCustomExceptionHandler ();
      if (aRealParseExceptionHandler == null)
        aRealParseExceptionHandler = getDefaultParseExceptionHandler ();

      final boolean bBrowserCompliantMode = aSettings.isBrowserCompliantMode ();

      final CSSNode aNode = _readStyleSheet (aCharStream,
                                             eVersion,
                                             aRealParseErrorHandler,
                                             aRealParseExceptionHandler,
                                             bBrowserCompliantMode);

      // Failed to parse content as CSS?
      if (aNode == null)
        return null;

      // Get the interpret error handler
      ICSSInterpretErrorHandler aRealInterpretErrorHandler = aSettings.getInterpretErrorHandler ();
      if (aRealInterpretErrorHandler == null)
        aRealInterpretErrorHandler = getDefaultInterpretErrorHandler ();

      // Convert the AST to a domain object
      return CSSHandler.readCascadingStyleSheetFromNode (eVersion, aNode, aRealInterpretErrorHandler);
    }
    finally
    {
      StreamHelper.close (aReader);
    }
  }

  /**
   * Read the CSS from the passed {@link IReaderProvider}. If the CSS contains
   * an explicit <code>@charset</code> rule, it is ignored and the charset used
   * to create the reader is used instead!
   *
   * @param aRP
   *        The reader provider to use. The reader is retrieved exactly once and
   *        closed anyway. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.7.3
   */
  @Nullable
  public static CascadingStyleSheet readFromReader (@Nonnull final IHasReader aRP,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                    @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromReader (aRP,
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomErrorHandler (aCustomErrorHandler)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link IReaderProvider}. If the CSS contains
   * an explicit <code>@charset</code> rule, it is ignored and the charset used
   * to create the reader is used instead! Also the fallback charset from the
   * {@link CSSReaderSettings} is ignored.
   *
   * @param aRP
   *        The reader provider to use. The reader is retrieved exactly once and
   *        closed anyway. May not be <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be
   *        <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.8.2
   */
  @Nullable
  public static CascadingStyleSheet readFromReader (@Nonnull final IHasReader aRP,
                                                    @Nonnull final CSSReaderSettings aSettings)
  {
    ValueEnforcer.notNull (aRP, "ReaderProvider");
    ValueEnforcer.notNull (aSettings, "Settings");

    // Create the reader
    final Reader aReader = aRP.getReader ();
    if (aReader == null)
    {
      // Failed to open reader
      return null;
    }

    // No charset determination, as the Reader already has an implicit Charset

    final ECSSVersion eVersion = aSettings.getCSSVersion ();
    try
    {
      final CSSCharStream aCharStream = new CSSCharStream (aReader);
      aCharStream.setTabSize (aSettings.getTabSize ());

      // Use the default CSS parse error handler if none is provided
      ICSSParseErrorHandler aRealParseErrorHandler = aSettings.getCustomErrorHandler ();
      if (aRealParseErrorHandler == null)
        aRealParseErrorHandler = getDefaultParseErrorHandler ();

      // Use the default CSS exception handler if none is provided
      ICSSParseExceptionCallback aRealParseExceptionHandler = aSettings.getCustomExceptionHandler ();
      if (aRealParseExceptionHandler == null)
        aRealParseExceptionHandler = getDefaultParseExceptionHandler ();

      final boolean bBrowserCompliantMode = aSettings.isBrowserCompliantMode ();

      final CSSNode aNode = _readStyleSheet (aCharStream,
                                             eVersion,
                                             aRealParseErrorHandler,
                                             aRealParseExceptionHandler,
                                             bBrowserCompliantMode);

      // Failed to parse content as CSS?
      if (aNode == null)
        return null;

      // Get the interpret error handler
      ICSSInterpretErrorHandler aRealInterpretErrorHandler = aSettings.getInterpretErrorHandler ();
      if (aRealInterpretErrorHandler == null)
        aRealInterpretErrorHandler = getDefaultInterpretErrorHandler ();

      // Convert the AST to a domain object
      return CSSHandler.readCascadingStyleSheetFromNode (eVersion, aNode, aRealInterpretErrorHandler);
    }
    finally
    {
      StreamHelper.close (aReader);
    }
  }
}
