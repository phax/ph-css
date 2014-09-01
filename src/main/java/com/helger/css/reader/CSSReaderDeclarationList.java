/**
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
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotations.Nonempty;
import com.helger.commons.annotations.PresentForCodeCoverage;
import com.helger.commons.charset.CharsetManager;
import com.helger.commons.io.IInputStreamProvider;
import com.helger.commons.io.IReadableResource;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.io.streams.NonBlockingStringReader;
import com.helger.commons.io.streams.StreamUtils;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.handler.CSSHandler;
import com.helger.css.handler.DoNothingCSSParseExceptionHandler;
import com.helger.css.handler.ICSSParseExceptionHandler;
import com.helger.css.handler.LoggingCSSParseExceptionHandler;
import com.helger.css.parser.CSSCharStream;
import com.helger.css.parser.CSSNode;
import com.helger.css.parser.CharStream;
import com.helger.css.parser.ParseException;
import com.helger.css.parser.ParserCSS21;
import com.helger.css.parser.ParserCSS21TokenManager;
import com.helger.css.parser.ParserCSS30;
import com.helger.css.parser.ParserCSS30TokenManager;
import com.helger.css.reader.errorhandler.ICSSParseErrorHandler;
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
  private static final ReadWriteLock s_aRWLock = new ReentrantReadWriteLock ();

  // Use the ThrowingCSSParseErrorHandler for maximum backward compatibility
  @GuardedBy ("s_aRWLock")
  private static ICSSParseErrorHandler s_aDefaultParseErrorHandler = ThrowingCSSParseErrorHandler.getInstance ();

  // Use the LoggingCSSParseExceptionHandler for maximum backward compatibility
  @GuardedBy ("s_aRWLock")
  private static ICSSParseExceptionHandler s_aDefaultParseExceptionHandler = new LoggingCSSParseExceptionHandler ();

  @PresentForCodeCoverage
  @SuppressWarnings ("unused")
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
   * @since 3.7.4
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
  private static CSSNode _readStyleDeclaration (@Nonnull final CharStream aStream,
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
          return aParser.styleDeclarationList ();
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
   * @param sCharset
   *        The charset to be used for reading the CSS file. May not be
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
   * @param sCharset
   *        The charset to be used for reading the CSS file. May not be
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
    final Charset aCharset = CharsetManager.getCharsetFromName (sCharset);
    return isValidCSS (aRes, aCharset, eVersion);
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
   * {@link #readFromStream(IInputStreamProvider, String, ECSSVersion)} and
   * checking for a non-<code>null</code> result.
   *
   * @param aIS
   *        The input stream to use. Is automatically closed. May not be
   *        <code>null</code>.
   * @param sCharset
   *        The charset to be used. May not be <code>null</code>.
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

    return isValidCSS (StreamUtils.createReader (aIS, aCharset), eVersion);
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
      final CSSNode aNode = _readStyleDeclaration (aCharStream,
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

  @Nullable
  public static CSSDeclarationList readFromString (@Nonnull final String sCSS, @Nonnull final ECSSVersion eVersion)
  {
    return readFromString (sCSS, eVersion, null, null);
  }

  @Nullable
  public static CSSDeclarationList readFromString (@Nonnull final String sCSS,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromString (sCSS, eVersion, aCustomErrorHandler, null);
  }

  @Nullable
  public static CSSDeclarationList readFromString (@Nonnull final String sCSS,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromString (sCSS, eVersion, null, aCustomExceptionHandler);
  }

  @Nullable
  public static CSSDeclarationList readFromString (@Nonnull final String sCSS,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                   @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromReader (new NonBlockingStringReader (sCSS), eVersion, aCustomErrorHandler, aCustomExceptionHandler);
  }

  @Nullable
  @Deprecated
  public static CSSDeclarationList readFromFile (@Nonnull final File aFile,
                                                 @Nonnull final String sCharset,
                                                 @Nonnull final ECSSVersion eVersion)
  {
    return readFromFile (aFile, sCharset, eVersion, null, null);
  }

  @Nullable
  public static CSSDeclarationList readFromFile (@Nonnull final File aFile,
                                                 @Nonnull final Charset aCharset,
                                                 @Nonnull final ECSSVersion eVersion)
  {
    return readFromFile (aFile, aCharset, eVersion, null, null);
  }

  @Nullable
  @Deprecated
  public static CSSDeclarationList readFromFile (@Nonnull final File aFile,
                                                 @Nonnull final String sCharset,
                                                 @Nonnull final ECSSVersion eVersion,
                                                 @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromFile (aFile, sCharset, eVersion, aCustomErrorHandler, null);
  }

  @Nullable
  public static CSSDeclarationList readFromFile (@Nonnull final File aFile,
                                                 @Nonnull final Charset aCharset,
                                                 @Nonnull final ECSSVersion eVersion,
                                                 @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromFile (aFile, aCharset, eVersion, aCustomErrorHandler, null);
  }

  @Nullable
  @Deprecated
  public static CSSDeclarationList readFromFile (@Nonnull final File aFile,
                                                 @Nonnull final String sCharset,
                                                 @Nonnull final ECSSVersion eVersion,
                                                 @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromFile (aFile, sCharset, eVersion, null, aCustomExceptionHandler);
  }

  @Nullable
  public static CSSDeclarationList readFromFile (@Nonnull final File aFile,
                                                 @Nonnull final Charset aCharset,
                                                 @Nonnull final ECSSVersion eVersion,
                                                 @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromFile (aFile, aCharset, eVersion, null, aCustomExceptionHandler);
  }

  @Nullable
  @Deprecated
  public static CSSDeclarationList readFromFile (@Nonnull final File aFile,
                                                 @Nonnull final String sCharset,
                                                 @Nonnull final ECSSVersion eVersion,
                                                 @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                 @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    final Charset aCharset = CharsetManager.getCharsetFromName (sCharset);
    return readFromFile (aFile, aCharset, eVersion, aCustomErrorHandler, aCustomExceptionHandler);
  }

  @Nullable
  public static CSSDeclarationList readFromFile (@Nonnull final File aFile,
                                                 @Nonnull final Charset aCharset,
                                                 @Nonnull final ECSSVersion eVersion,
                                                 @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                 @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromReader (new FileSystemResource (aFile).getReader (aCharset),
                           eVersion,
                           aCustomErrorHandler,
                           aCustomExceptionHandler);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param sCharset
   *        The charset name to be used. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   */
  @Nullable
  public static CSSDeclarationList readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                   @Nonnull final String sCharset,
                                                   @Nonnull final ECSSVersion eVersion)
  {
    return readFromStream (aISP, sCharset, eVersion, null, null);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}.
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
  public static CSSDeclarationList readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                   @Nonnull final Charset aCharset,
                                                   @Nonnull final ECSSVersion eVersion)
  {
    return readFromStream (aISP, aCharset, eVersion, null, null);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param sCharset
   *        The charset name to be used. May not be <code>null</code>.
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
  public static CSSDeclarationList readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                   @Nonnull final String sCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromStream (aISP, sCharset, eVersion, aCustomErrorHandler, null);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}.
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
  public static CSSDeclarationList readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                   @Nonnull final Charset aCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromStream (aISP, aCharset, eVersion, aCustomErrorHandler, null);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param sCharset
   *        The charset name to be used. May not be <code>null</code>.
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
  public static CSSDeclarationList readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                   @Nonnull final String sCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (aISP, sCharset, eVersion, null, aCustomExceptionHandler);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}.
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
  public static CSSDeclarationList readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                   @Nonnull final Charset aCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (aISP, aCharset, eVersion, null, aCustomExceptionHandler);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param sCharset
   *        The charset name to be used. May not be <code>null</code>.
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
  public static CSSDeclarationList readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                   @Nonnull final String sCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                   @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    final Charset aCharset = CharsetManager.getCharsetFromName (sCharset);
    return readFromStream (aISP, aCharset, eVersion, aCustomErrorHandler, aCustomExceptionHandler);
  }

  /**
   * Read the CSS from the passed {@link IInputStreamProvider}.
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
  public static CSSDeclarationList readFromStream (@Nonnull final IInputStreamProvider aISP,
                                                   @Nonnull final Charset aCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                   @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");

    final InputStream aIS = aISP.getInputStream ();
    if (aIS == null)
      return null;
    return readFromReader (StreamUtils.createReader (aIS, aCharset),
                           eVersion,
                           aCustomErrorHandler,
                           aCustomExceptionHandler);
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading
   *        - independent of success or error. May not be <code>null</code>.
   * @param sCharset
   *        The charset name to be used. May not be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations
   *         otherwise.
   * @since 3.7.4
   */
  @Nullable
  public static CSSDeclarationList readFromStream (@Nonnull @WillClose final InputStream aIS,
                                                   @Nonnull final String sCharset,
                                                   @Nonnull final ECSSVersion eVersion)
  {
    return readFromStream (aIS, sCharset, eVersion, null, null);
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
    return readFromStream (aIS, aCharset, eVersion, null, null);
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading
   *        - independent of success or error. May not be <code>null</code>.
   * @param sCharset
   *        The charset name to be used. May not be <code>null</code>.
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
  @Deprecated
  public static CSSDeclarationList readFromStream (@Nonnull @WillClose final InputStream aIS,
                                                   @Nonnull final String sCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromStream (aIS, sCharset, eVersion, aCustomErrorHandler, null);
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
    return readFromStream (aIS, aCharset, eVersion, aCustomErrorHandler, null);
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading
   *        - independent of success or error. May not be <code>null</code>.
   * @param sCharset
   *        The charset name to be used. May not be <code>null</code>.
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
  @Deprecated
  public static CSSDeclarationList readFromStream (@Nonnull @WillClose final InputStream aIS,
                                                   @Nonnull final String sCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (aIS, sCharset, eVersion, null, aCustomExceptionHandler);
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
                                                   @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromStream (aIS, aCharset, eVersion, null, aCustomExceptionHandler);
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading
   *        - independent of success or error. May not be <code>null</code>.
   * @param sCharset
   *        The charset name to be used. May not be <code>null</code>.
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
  @Deprecated
  public static CSSDeclarationList readFromStream (@Nonnull @WillClose final InputStream aIS,
                                                   @Nonnull final String sCharset,
                                                   @Nonnull final ECSSVersion eVersion,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                   @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    final Charset aCharset = CharsetManager.getCharsetFromName (sCharset);
    return readFromStream (aIS, aCharset, eVersion, aCustomErrorHandler, aCustomExceptionHandler);
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
                                                   @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    ValueEnforcer.notNull (aIS, "InputStream");

    return readFromReader (StreamUtils.createReader (aIS, aCharset),
                           eVersion,
                           aCustomErrorHandler,
                           aCustomExceptionHandler);
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
    return readFromReader (aReader, eVersion, null, null);
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
    return readFromReader (aReader, eVersion, aCustomErrorHandler, null);
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
                                                   @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    return readFromReader (aReader, eVersion, null, aCustomExceptionHandler);
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
                                                   @Nullable final ICSSParseExceptionHandler aCustomExceptionHandler)
  {
    ValueEnforcer.notNull (aReader, "Reader");
    ValueEnforcer.notNull (eVersion, "Version");

    try
    {
      final CSSCharStream aCharStream = new CSSCharStream (aReader);
      // Use the default CSS parse error handler if none is provided
      final ICSSParseErrorHandler aRealErrorHandler = aCustomErrorHandler == null ? getDefaultParseErrorHandler ()
                                                                                 : aCustomErrorHandler;
      // Use the default CSS exception handler if none is provided
      final ICSSParseExceptionHandler aRealExceptionHandler = aCustomExceptionHandler == null ? getDefaultParseExceptionHandler ()
                                                                                             : aCustomExceptionHandler;
      final CSSNode aNode = _readStyleDeclaration (aCharStream, eVersion, aRealErrorHandler, aRealExceptionHandler);

      // Failed to interpret content as CSS?
      if (aNode == null)
        return null;

      // Convert the AST to a domain object
      return CSSHandler.readDeclarationListFromNode (eVersion, aNode);
    }
    finally
    {
      StreamUtils.close (aReader);
    }
  }
}
