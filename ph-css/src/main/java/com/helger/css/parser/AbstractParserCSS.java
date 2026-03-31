/*
 * Copyright (C) 2014-2026 Philip Helger (www.helger.com)
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
package com.helger.css.parser;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.reader.errorhandler.ICSSParseErrorHandler;

/**
 * Abstract base class for CSS parsers.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public abstract class AbstractParserCSS
{
  private static final Logger LOGGER = LoggerFactory.getLogger (AbstractParserCSS.class);

  protected ICSSParseErrorHandler m_aCustomErrorHandler;
  protected boolean m_bBrowserCompliantMode = CSSReaderSettings.DEFAULT_BROWSER_COMPLIANT_MODE;
  protected boolean m_bKeepDeprecatedProperties = CSSReaderSettings.DEFAULT_KEEP_DEPRECATED_PROPERTIES;

  /**
   * @return The custom error handler to be used for this parser. May be <code>null</code>.
   */
  @Nullable
  public final ICSSParseErrorHandler getCustomErrorHandler ()
  {
    return m_aCustomErrorHandler;
  }

  /**
   * Set a custom error handler to use.
   *
   * @param aCustomErrorHandler
   *        The custom error handler to use. May be <code>null</code>.
   */
  public final void setCustomErrorHandler (@Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    m_aCustomErrorHandler = aCustomErrorHandler;
  }

  /**
   * @return <code>true</code> if browser compliant mode is active, <code>false</code> if not. By
   *         default browser compliant mode is disabled.
   */
  public final boolean isBrowserCompliantMode ()
  {
    return m_bBrowserCompliantMode;
  }

  /**
   * Enable or disable browser compliant mode.
   *
   * @param bBrowserCompliantMode
   *        <code>true</code> to enable browser compliant mode, <code>false</code> to disable it.
   */
  public final void setBrowserCompliantMode (final boolean bBrowserCompliantMode)
  {
    m_bBrowserCompliantMode = bBrowserCompliantMode;
  }

  /**
   * @return <code>true</code> if deprecated properties (e.g. <code>*zoom</code>) should be kept
   *         while reading, <code>false</code> if they should be discarded. The default is
   *         {@link CSSReaderSettings#DEFAULT_KEEP_DEPRECATED_PROPERTIES}.
   * @since 7.0.4
   */
  public final boolean isKeepDeprecatedProperties ()
  {
    return m_bKeepDeprecatedProperties;
  }

  /**
   * Define, whether deprecated properties (e.g. <code>*zoom</code>) should be kept or not.
   *
   * @param bKeepDeprecatedProperties
   *        <code>true</code> to keep them, <code>false</code> to discard them on reading.
   * @since 7.0.4
   */
  public final void setKeepDeprecatedProperties (final boolean bKeepDeprecatedProperties)
  {
    m_bKeepDeprecatedProperties = bKeepDeprecatedProperties;
  }

  // Used when NODE_SCOPE_HOOK is true - for debugging only
  public void jjtreeOpenNodeScope (final Node aNode)
  {
    if (LOGGER.isDebugEnabled ())
      LOGGER.debug ("Opening scope for " + aNode.toString ());
  }

  // Used when NODE_SCOPE_HOOK is true - for debugging only
  public void jjtreeCloseNodeScope (final Node aNode)
  {
    if (LOGGER.isDebugEnabled ())
      LOGGER.debug ("Closing scope for " + aNode.toString ());
  }
}
