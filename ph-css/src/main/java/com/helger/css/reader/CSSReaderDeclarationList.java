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
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.concurrent.SimpleReadWriteLock;
import com.helger.commons.io.IHasInputStream;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.commons.io.stream.NonBlockingStringReader;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.handler.CSSHandler;
import com.helger.css.handler.DoNothingCSSParseExceptionCallback;
import com.helger.css.handler.ICSSParseExceptionCallback;
import com.helger.css.handler.LoggingCSSParseExceptionCallback;
import com.helger.css.parser.CSSCharStream;
import com.helger.css.parser.CSSNode;
import com.helger.css.parser.CharStream;
import com.helger.css.parser.ParseException;
import com.helger.css.parser.ParserCSS30;
import com.helger.css.parser.ParserCSS30TokenManager;
import com.helger.css.reader.errorhandler.ICSSInterpretErrorHandler;
import com.helger.css.reader.errorhandler.ICSSParseErrorHandler;
import com.helger.css.reader.errorhandler.LoggingCSSInterpretErrorHandler;
import com.helger.css.reader.errorhandler.ThrowingCSSParseErrorHandler;

/**
 * This is the central user class for reading and parsing partial CSS from
 * different sources. This class reads CSS style declarations as used in HTML
 * <code>&lt;style&gt;</code> attributes only.
 *
 * @author Philip Helger
 */
@Immutable
public final class CSSReaderDeclarationList
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (CSSReaderDeclarationList.class);
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
  private static final CSSReaderDeclarationList s_aInstance = new CSSReaderDeclarationList ();

  private CSSReaderDeclarationList ()
  {}

  /**
   * @return The default CSS parse error handler. May be <code>null</code>. For
   *         backwards compatibility reasons this is be default an instance of
   *         {@link ThrowingCSSParseErrorHandler}.
   * @since 3.7.4
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
   * @since 3.7.4
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
   * @return <code>null</code> if parsing failed with an unrecoverable error
   *         (and no throwing exception handler is used), or <code>null</code>
   *         if a recoverable error occurred and no
   *         {@link com.helger.css.reader.errorhandler.ThrowingCSSParseErrorHandler}
   *         was used or non-<code>null</code> if parsing succeeded.
   */
  @Nullable
  private static CSSNode _readStyleDeclaration (@Nonnull final CharStream aCharStream,
                                                @Nonnull final ECSSVersion eVersion,
                                                @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                @Nonnull final ICSSParseExceptionCallback aCustomExceptionHandler)
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
        try
        {
          // Main parsing
          return aParser.styleDeclarationList ();
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
   * @param aCharset
   *        The charset to be used for reading the CSS file. May not be
   *        <code>null</code>.
   * @param eVersion
   *        The CSS version to be used for scanning. May not be
   *        <code>null</code>.
   * @return <code>true</code> if the file can be parsed without error,
   *         <code>false</code> if not
   */
  public static boolean isValidCSS (@Nonnull final File aFile,
                                    @Nonnull final Charset aCharset,
                                    @Nonnull final ECSSVersion eVersion)
  {
    return isValidCSS (new FileSystemResource (aFile), aCharset, eVersion);
  }

  /**
   * Check if the passed CSS resource can be parsed without error
   *
   * @param aRes
   *        The resource to be parsed. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used for reading the CSS file. May not be
   *        <code>null</code>.
   * @param eVersion
   *        The CSS version to be used for scanning. May not be
   *        <code>null</code>.
   * @return <code>true</code> if the file can be parsed without error,
   *         <code>false</code> if not
   */
  public static boolean isValidCSS (@Nonnull final IReadableResource aRes,
                                    @Nonnull final Charset aCharset,
                                    @Nonnull final ECSSVersion eVersion)
  {
    ValueEnforcer.notNull (aRes, "Resource");
    ValueEnforcer.notNull (aCharset, "Charset");
    ValueEnforcer.notNull (eVersion, "Version");

    final Reader aReader = aRes.getReader (aCharset);
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
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>true</code> if the CSS is valid according to the version,
   *         <code>false</code> if not
   */
  public static boolean isValidCSS (@Nonnull @WillClose final InputStream aIS,
                                    @Nonnull final Charset aCharset,
                                    @Nonnull final ECSSVersion eVersion)
  {
    ValueEnforcer.notNull (aIS, "InputStream");
    ValueEnforcer.notNull (aCharset, "Charset");

    return isValidCSS (StreamHelper.createReader (aIS, aCharset), eVersion);
  }

  /**
   * Check if the passed String can be resembled to valid CSS content. This is
   * accomplished by fully parsing the CSS file each time the method is called.
   * This is similar to calling {@link #readFromString(String, ECSSVersion)} and
   * checking for a non-<code>null</code> result.
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
      final CSSNode aNode = _readStyleDeclaration (aCharStream,
                                                   eVersion,
                                                   getDefaultParseErrorHandler (),
                                                   new DoNothingCSSParseExceptionCallback ());
      return aNode != null;
    }
    finally
    {
      StreamHelper.close (aReader);
    }
  }

  @Nullable
  public static CSSDeclarationList readFromString (@Nonnull final String sCSS, @Nonnull final ECSSVersion eVersion)
  {
    return readFromReader (new NonBlockingStringReader (sCSS), new CSSReaderSettings ().setCSSVersion (eVersion));
  }

  @Nullable
  public static CSSDeclarationList readFromString (@Nonnull final String sCSS,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromReader (new NonBlockingStringReader (sCSS),
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomErrorHandler (aCustomErrorHandler));
  }

  @Nullable
  public static CSSDeclarationList readFromString (@Nonnull final String sCSS,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromReader (new NonBlockingStringReader (sCSS),
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  @Nullable
  public static CSSDeclarationList readFromString (@Nonnull final String sCSS,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromReader (new NonBlockingStringReader (sCSS),
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomErrorHandler (aCustomErrorHandler)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  @Nullable
  public static CSSDeclarationList readFromFile (@Nonnull final File aFile,
                                                 @Nonnull final Charset aCharset,
                                                 @Nonnull final ECSSVersion eVersion)
  {
    return readFromReader (new FileSystemResource (aFile).getReader (aCharset),
                           new CSSReaderSettings ().setCSSVersion (eVersion));
  }

  @Nullable
  public static CSSDeclarationList readFromFile (@Nonnull final File aFile,
                                                 @Nonnull final Charset aCharset,
                                                 @Nonnull final ECSSVersion eVersion,
                                                 @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromReader (new FileSystemResource (aFile).getReader (aCharset),
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomErrorHandler (aCustomErrorHandler));
  }

  @Nullable
  public static CSSDeclarationList readFromFile (@Nonnull final File aFile,
                                                 @Nonnull final Charset aCharset,
                                                 @Nonnull final ECSSVersion eVersion,
                                                 @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromReader (new FileSystemResource (aFile).getReader (aCharset),
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  @Nullable
  public static CSSDeclarationList readFromFile (@Nonnull final File aFile,
                                                 @Nonnull final Charset aCharset,
                                                 @Nonnull final ECSSVersion eVersion,
                                                 @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                 @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromReader (new FileSystemResource (aFile).getReader (aCharset),
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomErrorHandler (aCustomErrorHandler)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CSSDeclarationList readFromStream (@Nonnull final IHasInputStream aISP,
                                                   @Nonnull final Charset aCharset,
                                                   @Nonnull final ECSSVersion eVersion)
  {
    return readFromStream (aISP, aCharset, eVersion, null, null);
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CSSDeclarationList readFromStream (@Nonnull final IHasInputStream aISP,
                                                   @Nonnull final Charset aCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromStream (aISP, aCharset, eVersion, aCustomErrorHandler, null);
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CSSDeclarationList readFromStream (@Nonnull final IHasInputStream aISP,
                                                   @Nonnull final Charset aCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromStream (aISP, aCharset, eVersion, null, aCustomExceptionHandler);
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
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
  public static CSSDeclarationList readFromStream (@Nonnull final IHasInputStream aISP,
                                                   @Nonnull final Charset aCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");

    final InputStream aIS = aISP.getInputStream ();
    if (aIS == null)
      return null;
    return readFromReader (StreamHelper.createReader (aIS, aCharset),
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomErrorHandler (aCustomErrorHandler)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading
   *        - independent of success or error. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.7.4
   */
  @Nullable
  public static CSSDeclarationList readFromStream (@Nonnull @WillClose final InputStream aIS,
                                                   @Nonnull final Charset aCharset,
                                                   @Nonnull final ECSSVersion eVersion)
  {
    return readFromReader (StreamHelper.createReader (aIS, aCharset),
                           new CSSReaderSettings ().setCSSVersion (eVersion));
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading
   *        - independent of success or error. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.7.4
   */
  @Nullable
  public static CSSDeclarationList readFromStream (@Nonnull @WillClose final InputStream aIS,
                                                   @Nonnull final Charset aCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromReader (StreamHelper.createReader (aIS, aCharset),
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomErrorHandler (aCustomErrorHandler));
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading
   *        - independent of success or error. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.7.4
   */
  @Nullable
  public static CSSDeclarationList readFromStream (@Nonnull @WillClose final InputStream aIS,
                                                   @Nonnull final Charset aCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromReader (StreamHelper.createReader (aIS, aCharset),
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading
   *        - independent of success or error. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
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
   * @since 3.7.4
   */
  @Nullable
  public static CSSDeclarationList readFromStream (@Nonnull @WillClose final InputStream aIS,
                                                   @Nonnull final Charset aCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    ValueEnforcer.notNull (aIS, "InputStream");

    return readFromReader (StreamHelper.createReader (aIS, aCharset),
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomErrorHandler (aCustomErrorHandler)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link Reader}.
   *
   * @param aReader
   *        The reader to use. Will be closed automatically after reading -
   *        independent of success or error. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CSSDeclarationList readFromReader (@Nonnull @WillClose final Reader aReader,
                                                   @Nonnull final ECSSVersion eVersion)
  {
    return readFromReader (aReader, new CSSReaderSettings ().setCSSVersion (eVersion));
  }

  /**
   * Read the CSS from the passed {@link Reader}.
   *
   * @param aReader
   *        The reader to use. Will be closed automatically after reading -
   *        independent of success or error. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the
   *        recoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CSSDeclarationList readFromReader (@Nonnull @WillClose final Reader aReader,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromReader (aReader,
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomErrorHandler (aCustomErrorHandler));
  }

  /**
   * Read the CSS from the passed {@link Reader}.
   *
   * @param aReader
   *        The reader to use. Will be closed automatically after reading -
   *        independent of success or error. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CSSDeclarationList readFromReader (@Nonnull @WillClose final Reader aReader,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromReader (aReader,
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link Reader}.
   *
   * @param aReader
   *        The reader to use. Will be closed automatically after reading -
   *        independent of success or error. May not be <code>null</code>.
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
  public static CSSDeclarationList readFromReader (@Nonnull @WillClose final Reader aReader,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromReader (aReader,
                           new CSSReaderSettings ().setCSSVersion (eVersion)
                                                   .setCustomErrorHandler (aCustomErrorHandler)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link Reader}.
   *
   * @param aReader
   *        The reader to use. Will be closed automatically after reading -
   *        independent of success or error. May not be <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be
   *        <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.8.2
   */
  @Nullable
  public static CSSDeclarationList readFromReader (@Nonnull @WillClose final Reader aReader,
                                                   @Nonnull final CSSReaderSettings aSettings)
  {
    ValueEnforcer.notNull (aReader, "Reader");
    ValueEnforcer.notNull (aSettings, "Settings");

    final ECSSVersion eVersion = aSettings.getCSSVersion ();
    try
    {
      final CSSCharStream aCharStream = new CSSCharStream (aReader);

      // Use the default CSS parse error handler if none is provided
      ICSSParseErrorHandler aRealParseErrorHandler = aSettings.getCustomErrorHandler ();
      if (aRealParseErrorHandler == null)
        aRealParseErrorHandler = getDefaultParseErrorHandler ();

      // Use the default CSS exception handler if none is provided
      ICSSParseExceptionCallback aRealParseExceptionHandler = aSettings.getCustomExceptionHandler ();
      if (aRealParseExceptionHandler == null)
        aRealParseExceptionHandler = getDefaultParseExceptionHandler ();

      final CSSNode aNode = _readStyleDeclaration (aCharStream,
                                                   eVersion,
                                                   aRealParseErrorHandler,
                                                   aRealParseExceptionHandler);

      // Failed to parse content as CSS?
      if (aNode == null)
        return null;

      // Get the interpret error handler
      ICSSInterpretErrorHandler aRealInterpretErrorHandler = aSettings.getInterpretErrorHandler ();
      if (aRealInterpretErrorHandler == null)
        aRealInterpretErrorHandler = getDefaultInterpretErrorHandler ();

      // Convert the AST to a domain object
      return CSSHandler.readDeclarationListFromNode (eVersion, aNode, aRealInterpretErrorHandler);
    }
    finally
    {
      StreamHelper.close (aReader);
    }
  }
}
