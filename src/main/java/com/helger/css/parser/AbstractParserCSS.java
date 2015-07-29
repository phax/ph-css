/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
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

import com.helger.css.reader.errorhandler.ICSSParseErrorHandler;

/**
 * Abstract base class for CSS parsers.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public abstract class AbstractParserCSS
{
  protected ICSSParseErrorHandler m_aCustomErrorHandler;
  protected boolean m_bBrowserCompliantMode = false;

  public final void setCustomErrorHandler (@Nullable final ICSSParseErrorHandler aCustomErrorHandler)
  {
    m_aCustomErrorHandler = aCustomErrorHandler;
  }

  @Nullable
  public final ICSSParseErrorHandler getCustomErrorHandler ()
  {
    return m_aCustomErrorHandler;
  }

  public final void setBrowserCompliantMode (final boolean bBrowserCompliantMode)
  {
    m_bBrowserCompliantMode = bBrowserCompliantMode;
  }

  public final boolean isBrowserCompliantMode ()
  {
    return m_bBrowserCompliantMode;
  }
}
