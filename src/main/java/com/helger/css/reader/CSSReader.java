/**
 * Copyright (C) 2006-2014 phloc systems (www.phloc.com)
 * Copyright (C) 2014 Philip Helger (www.helger.com)
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
import java.io.PushbackInputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotations.Nonempty;
import com.helger.commons.annotations.PresentForCodeCoverage;
import com.helger.commons.charset.CCharset;
import com.helger.commons.charset.CharsetManager;
import com.helger.commons.charset.EUnicodeBOM;
import com.helger.commons.collections.ArrayHelper;
import com.helger.commons.collections.pair.ReadonlyPair;
import com.helger.commons.io.IInputStreamProvider;
import com.helger.commons.io.IReadableResource;
import com.helger.commons.io.IReaderProvider;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.streamprovider.StringInputStreamProvider;
import com.helger.commons.io.streamprovider.StringReaderProvider;
import com.helger.commons.io.streams.NonBlockingStringReader;
import com.helger.commons.io.streams.StreamUtils;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.handler.CSSHandler;
import com.helger.css.handler.DoNothingCSSParseExceptionHandler;
import com.helger.css.handler.ICSSParseExceptionHandler;
import com.helger.css.handler.LoggingCSSParseExceptionHandler;
import com.helger.css.parser.CSSCharStream;
import com.helger.css.parser.CSSNode;
import com.helger.css.parser.CharStream;
import com.helger.css.parser.ParseException;
import com.helger.css.parser.ParseUtils;
import com.helger.css.parser.ParserCSS21;
import com.helger.css.parser.ParserCSS21TokenManager;
import com.helger.css.parser.ParserCSS30;
import com.helger.css.parser.ParserCSS30TokenManager;
import com.helger.css.parser.ParserCSSCharsetDetector;
import com.helger.css.parser.ParserCSSCharsetDetectorTokenManager;
import com.helger.css.reader.errorhandler.ICSSParseErrorHandler;
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
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  // Use the ThrowingCSSParseErrorHandler for maximum backward compatibility
  @GuardedBy ("s_aRWLock")
  private static ICSSParseErrorHandler s_aDefaultParseErrorHandler = ThrowingCSSParseErrorHandler.getInstance ();

  // Use the LoggingCSSParseExceptionHandler for maximum backward compatibility
  @GuardedBy ("s_aRWLock")
  private static ICSSParseExceptionHandler s_aDefaultParseExceptionHandler = new LoggingCSSParseExceptionHandler ();

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
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
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aDefaultParseErrorHandler;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
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
    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aDefaultParseErrorHandler = aDefaultParseErrorHandler;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * @return The default CSS parse exception handler. May not be
   *         <code>null</code>. For backwards compatibility reasons this is be
   *         default an instance of {@link LoggingCSSParseExceptionHandler}.
   * @since 3.7.4
   */
  @Nonnull
  public static ICSSParseExceptionHandler getDefaultParseExceptionHandler ()
  {
    s_aRWLock.readLock ().lock ();
    try
    {
      return s_aDefaultParseExceptionHandler;
    }
    finally
    {
      s_aRWLock.readLock ().unlock ();
    }
  }

  /**
   * Set the default CSS parse exception handler (for unrecoverable errors).
   *
   * @param aDefaultParseExceptionHandler
   *        The new default exception handler to be used. May not be
   *        <code>null</code>.
   * @since 3.7.4
   */
  public static void setDefaultParseExceptionHandler (@Nonnull final ICSSParseExceptionHandler aDefaultParseExceptionHandler)
  {
    ValueEnforcer.notNull (aDefaultParseExceptionHandler, "DefaultParseExceptionHandler");

    s_aRWLock.writeLock ().lock ();
    try
    {
      s_aDefaultParseExceptionHandler = aDefaultParseExceptionHandler;
    }
    finally
    {
      s_aRWLock.writeLock ().unlock ();
    }
  }

  /**
   * Main reading of the CSS
   *
   * @param aStream
   *        The stream to read from. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        A custom handler for recoverable errors. May be <code>null</code>.
   * @param aCustomExceptionHandler
   *        A custom handler for unrecoverable errors. May not be
   *        <code>null</code>.
   * @return <code>null</code> if parsing failed with an unrecoverable error
   *         (and no throwing exception handler is used), or <code>null</code>
   *         if a recoverable error occurred and no
   *         {@link com.helger.css.reader.errorhandler.ThrowingCSSParseErrorHandler}
   *         was used or non-<code>null</code> if parsing succeeded.
   */
  @Nullable
  private static CSSNode _readStyleSheet (@Nonnull final CharStream aStream,
                                          @Nonnull final ECSSVersion eVersion,
                                          @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                          @Nonnull final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    switch (eVersion)
    {
      case CSS21:
      {
        final ParserCSS21TokenManager aTokenHdl = new ParserCSS21TokenManager (aStream);
        final ParserCSS21 aParser = new ParserCSS21 (aTokenHdl);
        aParser.setCustomErrorHandler (aCustomErrorHandler);
        try
        {
          // Main parsing
          return aParser.styleSheet ();
        }
        catch (final ParseException ex)
        {
          // Unrecoverable error
          aCustomExceptionHandler.onException (ex);
          return null;
        }
      }
      case CSS30:
      {
        final ParserCSS30TokenManager aTokenHdl = new ParserCSS30TokenManager (aStream);
        final ParserCSS30 aParser = new ParserCSS30 (aTokenHdl);
        aParser.setCustomErrorHandler (aCustomErrorHandler);
        try
        {
          // Main parsing
          return aParser.styleSheet ();
        }
        catch (final ParseException ex)
        {
          // Unrecoverable error
          aCustomExceptionHandler.onException (ex);
          return null;
        }
      }
      default:
        throw new IllegalArgumentException ("Unsupported CSS version " + eVersion);
    }
  }

  /**
   * Check if the passed CSS file can be parsed without error
   *
   * @param aFile
   *        The file to be parsed. May not be <code>null</code>.
   * @param sCharset
   *        The charset to be used for reading the CSS file in case neither a
   *        <code>@charset</code> rule nor a BOM is present. May not be
   *        <code>null</code>.
   * @param eVersion
   *        The CSS version to be used for scanning. May not be
   *        <code>null</code>.
   * @return <code>true</code> if the file can be parsed without error,
   *         <code>false</code> if not
   * @throws IllegalArgumentException
   *         if the passed charset is unknown
   */
  @Deprecated
  public static boolean isValidCSS (@Nonnull final File aFile,
                                    @Nonnull final String sCharset,
                                    @Nonnull final ECSSVersion eVersion)
  {
    return isValidCSS (new FileSystemResource (aFile), sCharset, eVersion);
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
   * @param sCharset
   *        The charset to be used for reading the CSS file in case neither a
   *        <code>@charset</code> rule nor a BOM is present. May not be
   *        <code>null</code>.
   * @param eVersion
   *        The CSS version to be used for scanning. May not be
   *        <code>null</code>.
   * @return <code>true</code> if the file can be parsed without error,
   *         <code>false</code> if not
   * @throws IllegalArgumentException
   *         if the passed charset is unknown
   */
  @Deprecated
  public static boolean isValidCSS (@Nonnull final IReadableResource aRes,
                                    @Nonnull final String sCharset,
                                    @Nonnull final ECSSVersion eVersion)
  {
    final Charset aFallbackCharset = CharsetManager.getCharsetFromName (sCharset);
    return isValidCSS (aRes, aFallbackCharset, eVersion);
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
   * {@link #readFromStream(IInputStreamProvider, String, ECSSVersion)} and
   * checking for a non-<code>null</code> result.
   *
   * @param aIS
   *        The input stream to use. Is automatically closed. May not be
   *        <code>null</code>.
   * @param sCharset
   *        The charset to be used in case neither a <code>@charset</code> rule
   *        nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>true</code> if the CSS is valid according to the version,
   *         <code>false</code> if not
   */
  @Deprecated
  public static boolean isValidCSS (@Nonnull @WillClose final InputStream aIS,
                                    @Nonnull @Nonempty final String sCharset,
                                    @Nonnull final ECSSVersion eVersion)
  {
    ValueEnforcer.notNull (aIS, "InputStream");
    ValueEnforcer.notEmpty (sCharset, "Charset");

    return isValidCSS (StreamUtils.createReader (aIS, sCharset), eVersion);
  }

  /**
   * Check if the passed input stream can be resembled to valid CSS content.
   * This is accomplished by fully parsing the CSS file each time the method is
   * called. This is similar to calling
   * {@link #readFromStream(IInputStreamProvider,Charset, ECSSVersion)} and
   * checking for a non-<code>null</code> result.
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

    return isValidCSS (StreamUtils.createReader (aIS, aFallbackCharset), eVersion);
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
   * {@link #readFromStream(IInputStreamProvider, Charset, ECSSVersion)} and
   * checking for a non-<code>null</code> result.
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
                                             DoNothingCSSParseExceptionHandler.getInstance ());
      return aNode != null;
    }
    finally
    {
      StreamUtils.close (aReader);
    }
  }

  /**
   * Read the CSS from the passed String using a byte stream.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param sCharset
   *        The charset name to be used in case neither a <code>@charset</code>
   *        rule nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  @Deprecated
  public static CascadingStyleSheet readFromString (@Nonnull final String sCSS,
                                                    @Nonnull final String sCharset,
                                                    @Nonnull final ECSSVersion eVersion)
  {
    return readFromString (sCSS, sCharset, eVersion, null, null);
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
    return readFromString (sCSS, aFallbackCharset, eVersion, null, null);
  }

  /**
   * Read the CSS from the passed String using a byte stream.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param sCharset
   *        The charset name to be used in case neither a <code>@charset</code>
   *        rule nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  @Deprecated
  public static CascadingStyleSheet readFromString (@Nonnull final String sCSS,
                                                    @Nonnull final String sCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromString (sCSS, sCharset, eVersion, aCustomErrorHandler, null);
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
    return readFromString (sCSS, aFallbackCharset, eVersion, aCustomErrorHandler, null);
  }

  /**
   * Read the CSS from the passed String using a byte stream.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param sCharset
   *        The charset name to be used in case neither a <code>@charset</code>
   *        rule nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  @Deprecated
  public static CascadingStyleSheet readFromString (@Nonnull final String sCSS,
                                                    @Nonnull final String sCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromString (sCSS, sCharset, eVersion, null, aCustomExceptionHandler);
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
                                                    @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromString (sCSS, aFallbackCharset, eVersion, null, aCustomExceptionHandler);
  }

  /**
   * Read the CSS from the passed String using a byte stream.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param sCharset
   *        The charset name to be used in case neither a <code>@charset</code>
   *        rule nor a BOM is present. May not be <code>null</code>.
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
  @Deprecated
  public static CascadingStyleSheet readFromString (@Nonnull final String sCSS,
                                                    @Nonnull final String sCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                    @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (new StringInputStreamProvider (sCSS, sCharset),
                           sCharset,
                           eVersion,
                           aCustomErrorHandler,
                           aCustomExceptionHandler);
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
                                                    @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (new StringInputStreamProvider (sCSS, aFallbackCharset),
                           aFallbackCharset,
                           eVersion,
                           aCustomErrorHandler,
                           aCustomExceptionHandler);
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
    return readFromReader (new StringReaderProvider (sCSS),
                           eVersion,
                           (ICSSParseErrorHandler) null,
                           (ICSSParseExceptionHandler) null);
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
    return readFromReader (new StringReaderProvider (sCSS),
                           eVersion,
                           aCustomErrorHandler,
                           (ICSSParseExceptionHandler) null);
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
                                                    @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromReader (new StringReaderProvider (sCSS),
                           eVersion,
                           (ICSSParseErrorHandler) null,
                           aCustomExceptionHandler);
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
                                                    @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromReader (new StringReaderProvider (sCSS), eVersion, aCustomErrorHandler, aCustomExceptionHandler);
  }

  /**
   * Read the CSS from the passed File.
   *
   * @param aFile
   *        The file containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param sCharset
   *        The charset name to be used in case neither a <code>@charset</code>
   *        rule nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  @Deprecated
  public static CascadingStyleSheet readFromFile (@Nonnull final File aFile,
                                                  @Nonnull final String sCharset,
                                                  @Nonnull final ECSSVersion eVersion)
  {
    return readFromFile (aFile, sCharset, eVersion, null, null);
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
    return readFromFile (aFile, aFallbackCharset, eVersion, null, null);
  }

  /**
   * Read the CSS from the passed File.
   *
   * @param aFile
   *        The file containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param sCharset
   *        The charset name to be used in case neither a <code>@charset</code>
   *        rule nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  @Deprecated
  public static CascadingStyleSheet readFromFile (@Nonnull final File aFile,
                                                  @Nonnull final String sCharset,
                                                  @Nonnull final ECSSVersion eVersion,
                                                  @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromFile (aFile, sCharset, eVersion, aCustomErrorHandler, null);
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
    return readFromFile (aFile, aFallbackCharset, eVersion, aCustomErrorHandler, null);
  }

  /**
   * Read the CSS from the passed File.
   *
   * @param aFile
   *        The file containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param sCharset
   *        The charset name to be used in case neither a <code>@charset</code>
   *        rule nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  @Deprecated
  public static CascadingStyleSheet readFromFile (@Nonnull final File aFile,
                                                  @Nonnull final String sCharset,
                                                  @Nonnull final ECSSVersion eVersion,
                                                  @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (new FileSystemResource (aFile), sCharset, eVersion, null, aCustomExceptionHandler);
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
                                                  @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (new FileSystemResource (aFile), aFallbackCharset, eVersion, null, aCustomExceptionHandler);
  }

  /**
   * Read the CSS from the passed File.
   *
   * @param aFile
   *        The file containing the CSS to be parsed. May not be
   *        <code>null</code>.
   * @param sCharset
   *        The charset name to be used in case neither a <code>@charset</code>
   *        rule nor a BOM is present. May not be <code>null</code>.
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
  @Deprecated
  public static CascadingStyleSheet readFromFile (@Nonnull final File aFile,
                                                  @Nonnull final String sCharset,
                                                  @Nonnull final ECSSVersion eVersion,
                                                  @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                  @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (new FileSystemResource (aFile),
                           sCharset,
                           eVersion,
                           aCustomErrorHandler,
                           aCustomExceptionHandler);
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
                                                  @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (new FileSystemResource (aFile),
                           aFallbackCharset,
                           eVersion,
                           aCustomErrorHandler,
                           aCustomExceptionHandler);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}. If the CSS
   * contains an explicit charset, the whole CSS is parsed again, with the
   * charset found inside the file, so the passed {@link IInputStreamProvider}
   * must be able to create a new input stream on second invocation!
   *
   * @param aISP
   *        The input stream provider to use. Must be able to create new input
   *        streams on every invocation, in case an explicit charset node was
   *        found. May not be <code>null</code>.
   * @param sCharset
   *        The charset name to be used in case neither a <code>@charset</code>
   *        rule nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  @Deprecated
  public static CascadingStyleSheet readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                    @Nonnull final String sCharset,
                                                    @Nonnull final ECSSVersion eVersion)
  {
    return readFromStream (aISP, sCharset, eVersion, null, null);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}. If the CSS
   * contains an explicit charset, the whole CSS is parsed again, with the
   * charset found inside the file, so the passed {@link IInputStreamProvider}
   * must be able to create a new input stream on second invocation!
   *
   * @param aISP
   *        The input stream provider to use. Must be able to create new input
   *        streams on every invocation, in case an explicit charset node was
   *        found. May not be <code>null</code>.
   * @param sCharset
   *        The charset name to be used in case neither a <code>@charset</code>
   *        rule nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  @Deprecated
  public static CascadingStyleSheet readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                    @Nonnull final String sCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromStream (aISP, sCharset, eVersion, aCustomErrorHandler, null);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}. If the CSS
   * contains an explicit charset, the whole CSS is parsed again, with the
   * charset found inside the file, so the passed {@link IInputStreamProvider}
   * must be able to create a new input stream on second invocation!
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
  public static CascadingStyleSheet readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                    @Nonnull final Charset aFallbackCharset,
                                                    @Nonnull final ECSSVersion eVersion)
  {
    return readFromStream (aISP, aFallbackCharset, eVersion, null, null);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}. If the CSS
   * contains an explicit charset, the whole CSS is parsed again, with the
   * charset found inside the file, so the passed {@link IInputStreamProvider}
   * must be able to create a new input stream on second invocation!
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
  public static CascadingStyleSheet readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                    @Nonnull final Charset aFallbackCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromStream (aISP, aFallbackCharset, eVersion, aCustomErrorHandler, null);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}. If the CSS
   * contains an explicit charset, the whole CSS is parsed again, with the
   * charset found inside the file, so the passed {@link IInputStreamProvider}
   * must be able to create a new input stream on second invocation!
   *
   * @param aISP
   *        The input stream provider to use. Must be able to create new input
   *        streams on every invocation, in case an explicit charset node was
   *        found. May not be <code>null</code>.
   * @param sCharset
   *        The charset name to be used in case neither a <code>@charset</code>
   *        rule nor a BOM is present. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  @Deprecated
  public static CascadingStyleSheet readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                    @Nonnull final String sCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (aISP, sCharset, eVersion, null, aCustomExceptionHandler);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}. If the CSS
   * contains an explicit charset, the whole CSS is parsed again, with the
   * charset found inside the file, so the passed {@link IInputStreamProvider}
   * must be able to create a new input stream on second invocation!
   *
   * @param aISP
   *        The input stream provider to use. Must be able to create new input
   *        streams on every invocation, in case an explicit charset node was
   *        found. May not be <code>null</code>.
   * @param sCharset
   *        The charset name to be used in case neither a <code>@charset</code>
   *        rule nor a BOM is present. May not be <code>null</code>.
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
  @Deprecated
  public static CascadingStyleSheet readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                    @Nonnull final String sCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                    @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    final Charset aFallbackCharset = CharsetManager.getCharsetFromName (sCharset);
    return readFromStream (aISP, aFallbackCharset, eVersion, aCustomErrorHandler, aCustomExceptionHandler);
  }

  /**
   * Open the {@link InputStream} provided by the passed
   * {@link IInputStreamProvider}. If a BOM is present in the
   * {@link InputStream} it is read and if possible the charset is automatically
   * determined from the BOM.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @return <code>null</code> if no InputStream could be opened, the pair with
   *         non-<code>null</code> {@link InputStream} and a potentially
   *         <code>null</code> {@link Charset} otherwise.
   */
  @Nullable
  private static ReadonlyPair <InputStream, Charset> _getInputStreamWithoutBOM (@Nonnull final IInputStreamProvider aISP)
  {
    // Try to open input stream
    final InputStream aIS = aISP.getInputStream ();
    if (aIS == null)
      return null;

    // Check for BOM
    final int nMaxBOMBytes = EUnicodeBOM.getMaximumByteCount ();
    final PushbackInputStream aPIS = new PushbackInputStream (aIS, nMaxBOMBytes);
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
      return new ReadonlyPair <InputStream, Charset> (aPIS, aDeterminedCharset);
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
   * @throws IllegalArgumentException
   *         if an invalid charset is supplied
   */
  @Nullable
  public static Charset getCharsetDeclaredInCSS (@Nonnull final IInputStreamProvider aISP)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");

    // Open input stream
    final ReadonlyPair <InputStream, Charset> aISAndBOM = _getInputStreamWithoutBOM (aISP);
    if (aISAndBOM == null || aISAndBOM.getFirst () == null)
    {
      // Failed to open stream, so no charset!
      return null;
    }

    final InputStream aIS = aISAndBOM.getFirst ();
    final Charset aBOMCharset = aISAndBOM.getSecond ();
    Charset aStreamCharset = aBOMCharset;
    if (aStreamCharset == null)
    {
      // Always read as ISO-8859-1 as everything contained in the CSS charset
      // declaration can be handled by this charset
      // A known problem is when the file is UTF-16, UTF-16BE, UTF-16LE etc.
      // encoded. In this case a BOM must be present to read the file correctly!
      aStreamCharset = CCharset.CHARSET_ISO_8859_1_OBJ;
    }

    try
    {
      // Read with the Stream charset
      final CSSCharStream aCharStream = new CSSCharStream (StreamUtils.createReader (aIS, aStreamCharset));
      final ParserCSSCharsetDetectorTokenManager aTokenHdl = new ParserCSSCharsetDetectorTokenManager (aCharStream);
      final ParserCSSCharsetDetector aParser = new ParserCSSCharsetDetector (aTokenHdl);
      final String sCharsetName = aParser.styleSheetCharset ().getText ();
      if (sCharsetName == null)
      {
        // No charset specified - use the one from the BOM (may be null)
        return aISAndBOM.getSecond ();
      }
      // Remove leading and trailing quotes from value
      final String sPlainCharsetName = ParseUtils.extractStringValue (sCharsetName);
      final Charset aReadCharset = CharsetManager.getCharsetFromName (sPlainCharsetName);

      if (aBOMCharset != null && !aBOMCharset.equals (aReadCharset))
      {
        // BOM charset different from read charset
        s_aLogger.warn ("The charset found in the CSS data (" +
                        aReadCharset.name () +
                        ") differs from the charset determined by the BOM (" +
                        aBOMCharset.name () +
                        ")!");
      }

      return aReadCharset;
    }
    catch (final ParseException ex)
    {
      // Should never occur, as the parse exception is caught inside the
      // grammar!
      throw new IllegalStateException ("Failed to parse CSS charset definition", ex);
    }
    finally
    {
      StreamUtils.close (aIS);
    }
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}. If the CSS
   * contains an explicit charset, the whole CSS is parsed again, with the
   * charset found inside the file, so the passed {@link IInputStreamProvider}
   * must be able to create a new input stream on second invocation!
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
  public static CascadingStyleSheet readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                    @Nonnull final Charset aFallbackCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (aISP, aFallbackCharset, eVersion, null, aCustomExceptionHandler);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}. If the CSS
   * contains an explicit charset, the whole CSS is parsed again, with the
   * charset found inside the file, so the passed {@link IInputStreamProvider}
   * must be able to create a new input stream on second invocation!
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
  public static CascadingStyleSheet readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                    @Nonnull final Charset aFallbackCharset,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                    @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");
    ValueEnforcer.notNull (aFallbackCharset, "FallbackCharset");
    ValueEnforcer.notNull (eVersion, "Version");

    Charset aCharsetToUse;

    // Check if the CSS contains a declared charset or as an alternative use the
    // Charset from the BOM
    final Charset aDeclaredCharset = getCharsetDeclaredInCSS (aISP);
    if (aDeclaredCharset != null)
    {
      if (s_aLogger.isDebugEnabled ())
        s_aLogger.debug ("Reading CSS definition again with explicit charset '" + aDeclaredCharset.name () + "'");
      aCharsetToUse = aDeclaredCharset;
    }
    else
    {
      // No charset declared - use fallback
      aCharsetToUse = aFallbackCharset;
    }

    // Open input stream
    final ReadonlyPair <InputStream, Charset> aISAndBOM = _getInputStreamWithoutBOM (aISP);
    if (aISAndBOM == null || aISAndBOM.getFirst () == null)
    {
      // Failed to open stream!
      return null;
    }

    final InputStream aIS = aISAndBOM.getFirst ();

    try
    {
      final CSSCharStream aCharStream = new CSSCharStream (StreamUtils.createReader (aIS, aCharsetToUse));

      // Use the default CSS parse error handler if none is provided
      final ICSSParseErrorHandler aRealErrorHandler = aCustomErrorHandler == null ? getDefaultParseErrorHandler ()
                                                                                 : aCustomErrorHandler;
      // Use the default CSS exception handler if none is provided
      final ICSSParseExceptionHandler aRealExceptionHandler = aCustomExceptionHandler == null ? getDefaultParseExceptionHandler ()
                                                                                             : aCustomExceptionHandler;
      final CSSNode aNode = _readStyleSheet (aCharStream, eVersion, aRealErrorHandler, aRealExceptionHandler);

      // Failed to interpret content as CSS?
      if (aNode == null)
        return null;

      // Convert the AST to a domain object
      return CSSHandler.readCascadingStyleSheetFromNode (eVersion, aNode);
    }
    finally
    {
      StreamUtils.close (aIS);
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
  public static CascadingStyleSheet readFromReader (@Nonnull final IReaderProvider aRP,
                                                    @Nonnull final ECSSVersion eVersion,
                                                    @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                    @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    ValueEnforcer.notNull (aRP, "ReaderProvider");
    ValueEnforcer.notNull (eVersion, "Version");

    // Create the reader
    final Reader aReader = aRP.getReader ();
    if (aReader == null)
    {
      // Failed to open reader
      return null;
    }

    // No charset determination, as the Reader already has an implicit Charset

    try
    {
      final CSSCharStream aCharStream = new CSSCharStream (aReader);
      // Use the default CSS parse error handler if none is provided
      final ICSSParseErrorHandler aRealErrorHandler = aCustomErrorHandler == null ? getDefaultParseErrorHandler ()
                                                                                 : aCustomErrorHandler;
      // Use the default CSS exception handler if none is provided
      final ICSSParseExceptionHandler aRealExceptionHandler = aCustomExceptionHandler == null ? getDefaultParseExceptionHandler ()
                                                                                             : aCustomExceptionHandler;
      final CSSNode aNode = _readStyleSheet (aCharStream, eVersion, aRealErrorHandler, aRealExceptionHandler);

      // Failed to interpret content as CSS?
      if (aNode == null)
        return null;

      // Convert the AST to a domain object
      return CSSHandler.readCascadingStyleSheetFromNode (eVersion, aNode);
    }
    finally
    {
      StreamUtils.close (aReader);
    }
  }
}
