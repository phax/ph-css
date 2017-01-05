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
package com.helger.css.parser;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.css.reader.errorhandler.ICSSParseErrorHandler;

/**
 * Abstract base class for CSS parsers.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public abstract class AbstractParserCSS
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (AbstractParserCSS.class);

  protected ICSSParseErrorHandler m_aCustomErrorHandler;
  protected boolean m_bBrowserCompliantMode = false;

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
   * @return The custom error handler to be used for this parser. May be
   *         <code>null</code>.
   */
  @Nullable
  public final ICSSParseErrorHandler getCustomErrorHandler ()
  {
    return m_aCustomErrorHandler;
  }

  /**
   * Enable or disable browser compliant mode.
   *
   * @param bBrowserCompliantMode
   *        <code>true</code> to enable browser compliant mode,
   *        <code>false</code> to disable it.
   */
  public final void setBrowserCompliantMode (final boolean bBrowserCompliantMode)
  {
    m_bBrowserCompliantMode = bBrowserCompliantMode;
  }

  /**
   * @return <code>true</code> if browser compliant mode is active,
   *         <code>false</code> if not. By default browser compliant mode is
   *         disabled.
   */
  public final boolean isBrowserCompliantMode ()
  {
    return m_bBrowserCompliantMode;
  }

  // Used when NODE_SCOPE_HOOK is true - for debugging only
  public void jjtreeOpenNodeScope (final Node aNode)
  {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Opening scope for " + aNode.toString ());
  }

  // Used when NODE_SCOPE_HOOK is true - for debugging only
  public void jjtreeCloseNodeScope (final Node aNode)
  {
    if (s_aLogger.isDebugEnabled ())
      s_aLogger.debug ("Closing scope for " + aNode.toString ());
  }
}
