/**
 * Copyright (C) 2014-2016 Philip Helger (www.helger.com)
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

import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.charset.CCharset;
import com.helger.commons.lang.ICloneable;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.ECSSVersion;
import com.helger.css.handler.ICSSParseExceptionCallback;
import com.helger.css.reader.errorhandler.ICSSParseErrorHandler;

/**
 * A settings class for usage with {@link CSSReader}.
 *
 * @author Philip Helger
 * @since 3.8.2
 */
public class CSSReaderSettings implements ICloneable <CSSReaderSettings>
{
  public static final ECSSVersion DEFAULT_VERSION = ECSSVersion.CSS30;
  public static final Charset DEFAULT_CHARSET = CCharset.CHARSET_ISO_8859_1_OBJ;
  public static final boolean DEFAULT_BROWSER_COMPLIANT_MODE = false;

  private ECSSVersion m_eCSSVersion = DEFAULT_VERSION;
  private Charset m_aFallbackCharset = DEFAULT_CHARSET;
  private ICSSParseErrorHandler m_aCustomErrorHandler;
  private ICSSParseExceptionCallback m_aCustomExceptionHandler;
  private boolean m_bBrowserCompliantMode = DEFAULT_BROWSER_COMPLIANT_MODE;

  public CSSReaderSettings ()
  {}

  public CSSReaderSettings (@Nonnull final CSSReaderSettings aOther)
  {
    ValueEnforcer.notNull (aOther, "Other");
    m_eCSSVersion = aOther.m_eCSSVersion;
    m_aFallbackCharset = aOther.m_aFallbackCharset;
    m_aCustomErrorHandler = aOther.m_aCustomErrorHandler;
    m_aCustomExceptionHandler = aOther.m_aCustomExceptionHandler;
    m_bBrowserCompliantMode = aOther.m_bBrowserCompliantMode;
  }

  /**
   * @return The CSS version which should be read. Defaults to
   *         {@link #DEFAULT_VERSION}. Never <code>null</code>.
   */
  @Nonnull
  public ECSSVersion getCSSVersion ()
  {
    return m_eCSSVersion;
  }

  /**
   * Set the CSS version to be read.
   *
   * @param eCSSVersion
   *        The version number to use. May not be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CSSReaderSettings setCSSVersion (@Nonnull final ECSSVersion eCSSVersion)
  {
    ValueEnforcer.notNull (eCSSVersion, "CSSVersion");
    m_eCSSVersion = eCSSVersion;
    return this;
  }

  /**
   * @return The charset to be used for reading a CSS file in case neither a
   *         <code>@charset</code> rule nor a BOM is present. Never
   *         <code>null</code>. Defaults to {@link #DEFAULT_CHARSET}.
   */
  @Nonnull
  public Charset getFallbackCharset ()
  {
    return m_aFallbackCharset;
  }

  /**
   * @return The name of the charset to be used for reading a CSS file in case
   *         neither a <code>@charset</code> rule nor a BOM is present. Never
   *         <code>null</code>. Defaults to the name of {@link #DEFAULT_CHARSET}
   *         .
   */
  @Nonnull
  public String getFallbackCharsetName ()
  {
    return m_aFallbackCharset.name ();
  }

  /**
   * @param aFallbackCharset
   *        The charset to be used for reading the CSS file in case neither a
   *        <code>@charset</code> rule nor a BOM is present. May not be
   *        <code>null</code>.
   * @return this
   */
  @Nonnull
  public CSSReaderSettings setFallbackCharset (@Nonnull @Nonempty final Charset aFallbackCharset)
  {
    ValueEnforcer.notNull (aFallbackCharset, "FallbackCharset");
    m_aFallbackCharset = aFallbackCharset;
    return this;
  }

  /**
   * @return An optional custom error handler that can be used to collect the
   *         recoverable parsing errors. May be <code>null</code>.
   */
  @Nullable
  public ICSSParseErrorHandler getCustomErrorHandler ()
  {
    return m_aCustomErrorHandler;
  }

  /**
   * @param aCustomErrorHandler
   *        A custom error handler that can be used to collect the recoverable
   *        parsing errors. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CSSReaderSettings setCustomErrorHandler (@Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    m_aCustomErrorHandler = aCustomErrorHandler;
    return this;
  }

  /**
   * @return An optional custom exception handler that can be used to collect
   *         the unrecoverable parsing errors. May be <code>null</code>.
   */
  @Nullable
  public ICSSParseExceptionCallback getCustomExceptionHandler ()
  {
    return m_aCustomExceptionHandler;
  }

  /**
   * @param aCustomExceptionHandler
   *        A custom exception handler that can be used to collect the
   *        unrecoverable parsing errors. May be <code>null</code>.
   * @return this
   */
  @Nonnull
  public CSSReaderSettings setCustomExceptionHandler (@Nullable final ICSSParseExceptionCallback aCustomExceptionHandler)
  {
    m_aCustomExceptionHandler = aCustomExceptionHandler;
    return this;
  }

  /**
   * @return <code>true</code> if browser compliant parsing is enabled,
   *         <code>false</code> otherwise. The default is
   *         {@link #DEFAULT_BROWSER_COMPLIANT_MODE}.
   */
  public boolean isBrowserCompliantMode ()
  {
    return m_bBrowserCompliantMode;
  }

  /**
   * Change the browser compliant mode to use.
   * 
   * @param bBrowserCompliantMode
   *        <code>true</code> to use enable browser compliant parsing,
   *        <code>false</code> to disable it.
   * @return this
   */
  @Nonnull
  public CSSReaderSettings setBrowserCompliantMode (final boolean bBrowserCompliantMode)
  {
    m_bBrowserCompliantMode = bBrowserCompliantMode;
    return this;
  }

  @Nonnull
  public CSSReaderSettings getClone ()
  {
    return new CSSReaderSettings (this);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("CSSVersion", m_eCSSVersion)
                                       .append ("FallbackCharset", m_aFallbackCharset)
                                       .append ("CustomErrorHandler", m_aCustomErrorHandler)
                                       .append ("CustomExceptionHandler", m_aCustomExceptionHandler)
                                       .append ("BrowserCompliantMode", m_bBrowserCompliantMode)
                                       .toString ();
  }
}
