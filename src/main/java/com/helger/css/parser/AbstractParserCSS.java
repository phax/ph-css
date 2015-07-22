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
