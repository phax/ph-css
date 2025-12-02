/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.annotation.WillClose;
import com.helger.annotation.concurrent.GuardedBy;
import com.helger.annotation.concurrent.Immutable;
import com.helger.annotation.misc.DevelopersNote;
import com.helger.annotation.style.PresentForCodeCoverage;
import com.helger.base.concurrent.SimpleReadWriteLock;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.io.iface.IHasInputStream;
import com.helger.base.io.nonblocking.NonBlockingStringReader;
import com.helger.base.io.stream.StreamHelper;
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
import com.helger.io.resource.FileSystemResource;
import com.helger.io.resource.IReadableResource;

/**
 * This is the central user class for reading and parsing partial CSS from different sources. This
 * class reads CSS style declarations as used in HTML <code>&lt;style&gt;</code> attributes only.
 *
 * @author Philip Helger
 */
@Immutable
public final class CSSReaderDeclarationList
{
  private static final Logger LOGGER = LoggerFactory.getLogger (CSSReaderDeclarationList.class);
  private static final SimpleReadWriteLock RW_LOCK = new SimpleReadWriteLock ();

  // Use the ThrowingCSSParseErrorHandler for maximum backward compatibility
  @GuardedBy ("RW_LOCK")
  private static ICSSParseErrorHandler s_aDefaultParseErrorHandler = new ThrowingCSSParseErrorHandler ();

  // Use the LoggingCSSParseExceptionHandler for maximum backward compatibility
  @GuardedBy ("RW_LOCK")
  private static ICSSParseExceptionCallback s_aDefaultParseExceptionHandler = new LoggingCSSParseExceptionCallback ();

  @GuardedBy ("RW_LOCK")
  private static ICSSInterpretErrorHandler s_aDefaultInterpretErrorHandler = new LoggingCSSInterpretErrorHandler ();

  @PresentForCodeCoverage
  private static final CSSReaderDeclarationList INSTANCE = new CSSReaderDeclarationList ();

  private CSSReaderDeclarationList ()
  {}

  /**
   * @return The default CSS parse error handler. May be <code>null</code>. For backwards
   *         compatibility reasons this is be default an instance of
   *         {@link ThrowingCSSParseErrorHandler}.
   * @since 3.7.4
   */
  @Nullable
  public static ICSSParseErrorHandler getDefaultParseErrorHandler ()
  {
    return RW_LOCK.readLockedGet ( () -> s_aDefaultParseErrorHandler);
  }

  /**
   * Set the default CSS parse error handler (for recoverable errors).
   *
   * @param aDefaultParseErrorHandler
   *        The new default error handler to be used. May be <code>null</code> to indicate that no
   *        special error handler should be used.
   * @since 3.7.4
   */
  public static void setDefaultParseErrorHandler (@Nullable final ICSSParseErrorHandler aDefaultParseErrorHandler)
  {
    RW_LOCK.writeLocked ( () -> s_aDefaultParseErrorHandler = aDefaultParseErrorHandler);
  }

  /**
   * @return The default CSS parse exception handler. May not be <code>null</code>. For backwards
   *         compatibility reasons this is be default an instance of
   *         {@link LoggingCSSParseExceptionCallback}.
   * @since 3.7.4
   */
  @NonNull
  public static ICSSParseExceptionCallback getDefaultParseExceptionHandler ()
  {
    return RW_LOCK.readLockedGet ( () -> s_aDefaultParseExceptionHandler);
  }

  /**
   * Set the default CSS parse exception handler (for unrecoverable errors).
   *
   * @param aDefaultParseExceptionHandler
   *        The new default exception handler to be used. May not be <code>null</code>.
   * @since 3.7.4
   */
  public static void setDefaultParseExceptionHandler (@NonNull final ICSSParseExceptionCallback aDefaultParseExceptionHandler)
  {
    ValueEnforcer.notNull (aDefaultParseExceptionHandler, "DefaultParseExceptionHandler");

    RW_LOCK.writeLocked ( () -> s_aDefaultParseExceptionHandler = aDefaultParseExceptionHandler);
  }

  /**
   * @return The default interpret error handler to handle interpretation errors in successfully
   *         parsed CSS. Never <code>null</code>.
   * @since 5.0.2
   */
  @NonNull
  public static ICSSInterpretErrorHandler getDefaultInterpretErrorHandler ()
  {
    return RW_LOCK.readLockedGet ( () -> s_aDefaultInterpretErrorHandler);
  }

  /**
   * Set the default interpret error handler to handle interpretation errors in successfully parsed
   * CSS.
   *
   * @param aDefaultErrorHandler
   *        The default error handler to be used. May not be <code>null</code>.
   * @since 5.0.2
   */
  public static void setDefaultInterpretErrorHandler (@NonNull final ICSSInterpretErrorHandler aDefaultErrorHandler)
  {
    ValueEnforcer.notNull (aDefaultErrorHandler, "DefaultErrorHandler");

    RW_LOCK.writeLocked ( () -> s_aDefaultInterpretErrorHandler = aDefaultErrorHandler);
  }

  /**
   * Main reading of the CSS
   *
   * @param aCharStream
   *        The stream to read from. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        A custom handler for recoverable errors. May be <code>null</code>.
   * @param aCustomExceptionHandler
   *        A custom handler for unrecoverable errors. May not be <code>null</code>.
   * @return <code>null</code> if parsing failed with an unrecoverable error (and no throwing
   *         exception handler is used), or <code>null</code> if a recoverable error occurred and no
   *         {@link com.helger.css.reader.errorhandler.ThrowingCSSParseErrorHandler} was used or
   *         non-<code>null</code> if parsing succeeded.
   */
  @Nullable
  private static CSSNode _readStyleDeclaration (@NonNull final CharStream aCharStream,
                                                @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                @NonNull final ICSSParseExceptionCallback aCustomExceptionHandler)
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

  /**
   * Check if the passed CSS file can be parsed without error
   *
   * @param aFile
   *        The file to be parsed. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used for reading the CSS file. May not be <code>null</code>.
   * @return <code>true</code> if the file can be parsed without error, <code>false</code> if not
   */
  public static boolean isValidCSS (@NonNull final File aFile, @NonNull final Charset aCharset)
  {
    return isValidCSS (new FileSystemResource (aFile), aCharset);
  }

  /**
   * Check if the passed CSS resource can be parsed without error
   *
   * @param aRes
   *        The resource to be parsed. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used for reading the CSS file. May not be <code>null</code>.
   * @return <code>true</code> if the file can be parsed without error, <code>false</code> if not
   */
  public static boolean isValidCSS (@NonNull final IReadableResource aRes, @NonNull final Charset aCharset)
  {
    ValueEnforcer.notNull (aRes, "Resource");
    ValueEnforcer.notNull (aCharset, "Charset");

    final Reader aReader = aRes.getReader (aCharset);
    if (aReader == null)
    {
      LOGGER.warn ("Failed to open CSS reader " + aRes);
      return false;
    }
    return isValidCSS (aReader);
  }

  /**
   * Check if the passed input stream can be resembled to valid CSS content. This is accomplished by
   * fully parsing the CSS file each time the method is called. This is similar to calling
   * {@link #readFromStream(IHasInputStream,Charset)} and checking for a non-<code>null</code>
   * result.
   *
   * @param aIS
   *        The input stream to use. Is automatically closed. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @return <code>true</code> if the CSS is valid according to the version, <code>false</code> if
   *         not
   */
  public static boolean isValidCSS (@NonNull @WillClose final InputStream aIS, @NonNull final Charset aCharset)
  {
    ValueEnforcer.notNull (aIS, "InputStream");
    ValueEnforcer.notNull (aCharset, "Charset");

    return isValidCSS (StreamHelper.createReader (aIS, aCharset));
  }

  /**
   * Check if the passed String can be resembled to valid CSS content. This is accomplished by fully
   * parsing the CSS file each time the method is called. This is similar to calling
   * {@link #readFromString(String)} and checking for a non-<code>null</code> result.
   *
   * @param sCSS
   *        The CSS string to scan. May not be <code>null</code>.
   * @return <code>true</code> if the CSS is valid according to the version, <code>false</code> if
   *         not
   */
  public static boolean isValidCSS (@NonNull final String sCSS)
  {
    ValueEnforcer.notNull (sCSS, "CSS");

    return isValidCSS (new NonBlockingStringReader (sCSS));
  }

  /**
   * Check if the passed reader can be resembled to valid CSS content. This is accomplished by fully
   * parsing the CSS each time the method is called. This is similar to calling
   * {@link #readFromStream(IHasInputStream, Charset)} and checking for a non-<code>null</code>
   * result.
   *
   * @param aReader
   *        The reader to use. May not be <code>null</code>.
   * @return <code>true</code> if the CSS is valid according to the version, <code>false</code> if
   *         not
   */
  public static boolean isValidCSS (@NonNull @WillClose final Reader aReader)
  {
    ValueEnforcer.notNull (aReader, "Reader");

    try
    {
      final CSSCharStream aCharStream = new CSSCharStream (aReader);
      final CSSNode aNode = _readStyleDeclaration (aCharStream,
                                                   getDefaultParseErrorHandler (),
                                                   new DoNothingCSSParseExceptionCallback ());
      return aNode != null;
    }
    finally
    {
      StreamHelper.close (aReader);
    }
  }

  /**
   * Read the CSS from the passed String.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   */
  @Nullable
  public static CSSDeclarationList readFromString (@NonNull final String sCSS)
  {
    return readFromReader (new NonBlockingStringReader (sCSS), new CSSReaderSettings ());
  }

  /**
   * Read the CSS from the passed String.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the recoverable parsing
   *        errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromString (@NonNull final String sCSS,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromString (sCSS, new CSSReaderSettings ().setCustomErrorHandler (aCustomErrorHandler));
  }

  /**
   * Read the CSS from the passed String.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the unrecoverable
   *        parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromString (@NonNull final String sCSS,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromString (sCSS, new CSSReaderSettings ().setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed String.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the recoverable parsing
   *        errors. May be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the unrecoverable
   *        parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromString (@NonNull final String sCSS,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromString (sCSS,
                           new CSSReaderSettings ().setCustomErrorHandler (aCustomErrorHandler)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed String.
   *
   * @param sCSS
   *        The source string containing the CSS to be parsed. May not be <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   * @since 6.4.3
   */
  @Nullable
  public static CSSDeclarationList readFromString (@NonNull final String sCSS,
                                                   @NonNull final CSSReaderSettings aSettings)
  {
    return readFromReader (new NonBlockingStringReader (sCSS), aSettings);
  }

  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromFile (@NonNull final File aFile, @NonNull final Charset aCharset)
  {
    return readFromFile (aFile, aCharset, new CSSReaderSettings ());
  }

  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromFile (@NonNull final File aFile,
                                                 @NonNull final Charset aCharset,
                                                 @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromFile (aFile, aCharset, new CSSReaderSettings ().setCustomErrorHandler (aCustomErrorHandler));
  }

  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromFile (@NonNull final File aFile,
                                                 @NonNull final Charset aCharset,
                                                 @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromFile (aFile, aCharset, new CSSReaderSettings ().setCustomExceptionHandler (aCustomExceptionHandler));
  }

  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromFile (@NonNull final File aFile,
                                                 @NonNull final Charset aCharset,
                                                 @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                 @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromFile (aFile,
                         aCharset,
                         new CSSReaderSettings ().setCustomErrorHandler (aCustomErrorHandler)
                                                 .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed File.
   *
   * @param aFile
   *        The file containing the CSS to be parsed. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   * @since 6.4.3
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromFile (@NonNull final File aFile,
                                                 @NonNull final Charset aCharset,
                                                 @NonNull final CSSReaderSettings aSettings)
  {
    return readFromReader (new FileSystemResource (aFile).getReader (aCharset), aSettings);
  }

  /**
   * Read the CSS from the passed File.
   *
   * @param aFile
   *        The file containing the CSS to be parsed. May not be <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   * @since 8.0.0
   */
  @Nullable
  public static CSSDeclarationList readFromFile (@NonNull final File aFile, @NonNull final CSSReaderSettings aSettings)
  {
    return readFromReader (new FileSystemResource (aFile).getReader (aSettings.getFallbackCharset ()), aSettings);
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromStream (@NonNull final IHasInputStream aISP, @NonNull final Charset aCharset)
  {
    return readFromStream (aISP, aCharset, new CSSReaderSettings ());
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the recoverable parsing
   *        errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromStream (@NonNull final IHasInputStream aISP,
                                                   @NonNull final Charset aCharset,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromStream (aISP, aCharset, new CSSReaderSettings ().setCustomErrorHandler (aCustomErrorHandler));
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the unrecoverable
   *        parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromStream (@NonNull final IHasInputStream aISP,
                                                   @NonNull final Charset aCharset,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromStream (aISP,
                           aCharset,
                           new CSSReaderSettings ().setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the recoverable parsing
   *        errors. May be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the unrecoverable
   *        parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromStream (@NonNull final IHasInputStream aISP,
                                                   @NonNull final Charset aCharset,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromStream (aISP,
                           aCharset,
                           new CSSReaderSettings ().setCustomErrorHandler (aCustomErrorHandler)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   * @since 6.4.3
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromStream (@NonNull final IHasInputStream aISP,
                                                   @NonNull final Charset aCharset,
                                                   @NonNull final CSSReaderSettings aSettings)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");

    final InputStream aIS = aISP.getInputStream ();
    if (aIS == null)
      return null;
    return readFromStream (aIS, aCharset, aSettings);
  }

  /**
   * Read the CSS from the passed {@link IHasInputStream}.
   *
   * @param aISP
   *        The input stream provider to use. May not be <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   * @since 8.0.0
   */
  @Nullable
  public static CSSDeclarationList readFromStream (@NonNull final IHasInputStream aISP,
                                                   @NonNull final CSSReaderSettings aSettings)
  {
    ValueEnforcer.notNull (aISP, "InputStreamProvider");

    final InputStream aIS = aISP.getInputStream ();
    if (aIS == null)
      return null;
    return readFromStream (aIS, aSettings);
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading - independent of
   *        success or error. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   * @since 3.7.4
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromStream (@NonNull @WillClose final InputStream aIS,
                                                   @NonNull final Charset aCharset)
  {
    return readFromStream (aIS, aCharset, new CSSReaderSettings ());
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading - independent of
   *        success or error. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the recoverable parsing
   *        errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   * @since 3.7.4
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromStream (@NonNull @WillClose final InputStream aIS,
                                                   @NonNull final Charset aCharset,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromStream (aIS, aCharset, new CSSReaderSettings ().setCustomErrorHandler (aCustomErrorHandler));
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading - independent of
   *        success or error. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the unrecoverable
   *        parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   * @since 3.7.4
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromStream (@NonNull @WillClose final InputStream aIS,
                                                   @NonNull final Charset aCharset,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromStream (aIS, aCharset, new CSSReaderSettings ().setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading - independent of
   *        success or error. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the recoverable parsing
   *        errors. May be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the unrecoverable
   *        parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   * @since 3.7.4
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromStream (@NonNull @WillClose final InputStream aIS,
                                                   @NonNull final Charset aCharset,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    ValueEnforcer.notNull (aIS, "InputStream");

    return readFromStream (aIS,
                           aCharset,
                           new CSSReaderSettings ().setCustomErrorHandler (aCustomErrorHandler)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading - independent of
   *        success or error. May not be <code>null</code>.
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   * @since 6.4.3
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromStream (@NonNull @WillClose final InputStream aIS,
                                                   @NonNull final Charset aCharset,
                                                   @NonNull final CSSReaderSettings aSettings)
  {
    ValueEnforcer.notNull (aIS, "InputStream");

    return readFromReader (StreamHelper.createReader (aIS, aCharset), aSettings);
  }

  /**
   * Read the CSS from the passed {@link InputStream}.
   *
   * @param aIS
   *        The input stream to use. Will be closed automatically after reading - independent of
   *        success or error. May not be <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   * @since 8.0.0
   */
  @Nullable
  public static CSSDeclarationList readFromStream (@NonNull @WillClose final InputStream aIS,
                                                   @NonNull final CSSReaderSettings aSettings)
  {
    ValueEnforcer.notNull (aIS, "InputStream");

    return readFromReader (StreamHelper.createReader (aIS, aSettings.getFallbackCharset ()), aSettings);
  }

  /**
   * Read the CSS from the passed {@link Reader}.
   *
   * @param aReader
   *        The reader to use. Will be closed automatically after reading - independent of success
   *        or error. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   */
  @Nullable
  public static CSSDeclarationList readFromReader (@NonNull @WillClose final Reader aReader)
  {
    return readFromReader (aReader, new CSSReaderSettings ());
  }

  /**
   * Read the CSS from the passed {@link Reader}.
   *
   * @param aReader
   *        The reader to use. Will be closed automatically after reading - independent of success
   *        or error. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the recoverable parsing
   *        errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromReader (@NonNull @WillClose final Reader aReader,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    return readFromReader (aReader, new CSSReaderSettings ().setCustomErrorHandler (aCustomErrorHandler));
  }

  /**
   * Read the CSS from the passed {@link Reader}.
   *
   * @param aReader
   *        The reader to use. Will be closed automatically after reading - independent of success
   *        or error. May not be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the unrecoverable
   *        parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromReader (@NonNull @WillClose final Reader aReader,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromReader (aReader, new CSSReaderSettings ().setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link Reader}.
   *
   * @param aReader
   *        The reader to use. Will be closed automatically after reading - independent of success
   *        or error. May not be <code>null</code>.
   * @param aCustomErrorHandler
   *        An optional custom error handler that can be used to collect the recoverable parsing
   *        errors. May be <code>null</code>.
   * @param aCustomExceptionHandler
   *        An optional custom exception handler that can be used to collect the unrecoverable
   *        parsing errors. May be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   */
  @Nullable
  @Deprecated (forRemoval = true, since = "8.0.0")
  @DevelopersNote ("Use the version with CSSReaderSettings instead")
  public static CSSDeclarationList readFromReader (@NonNull @WillClose final Reader aReader,
                                                   @Nullable final ICSSParseErrorHandler aCustomErrorHandler,
                                                   @Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    return readFromReader (aReader,
                           new CSSReaderSettings ().setCustomErrorHandler (aCustomErrorHandler)
                                                   .setCustomExceptionHandler (aCustomExceptionHandler));
  }

  /**
   * Read the CSS from the passed {@link Reader}.
   *
   * @param aReader
   *        The reader to use. Will be closed automatically after reading - independent of success
   *        or error. May not be <code>null</code>.
   * @param aSettings
   *        The settings to be used for reading the CSS. May not be <code>null</code>.
   * @return <code>null</code> if reading failed, the CSS declarations otherwise.
   * @since 3.8.2
   */
  @Nullable
  public static CSSDeclarationList readFromReader (@NonNull @WillClose final Reader aReader,
                                                   @NonNull final CSSReaderSettings aSettings)
  {
    ValueEnforcer.notNull (aReader, "Reader");
    ValueEnforcer.notNull (aSettings, "Settings");

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

      final CSSNode aNode = _readStyleDeclaration (aCharStream, aRealParseErrorHandler, aRealParseExceptionHandler);

      // Failed to parse content as CSS?
      if (aNode == null)
        return null;

      // Get the interpret error handler
      ICSSInterpretErrorHandler aRealInterpretErrorHandler = aSettings.getInterpretErrorHandler ();
      if (aRealInterpretErrorHandler == null)
        aRealInterpretErrorHandler = getDefaultInterpretErrorHandler ();

      final boolean bUseSourceLocation = aSettings.isUseSourceLocation ();

      // Convert the AST to a domain object
      return CSSHandler.readDeclarationListFromNode (aRealInterpretErrorHandler, bUseSourceLocation, aNode);
    }
    finally
    {
      StreamHelper.close (aReader);
    }
  }
}
