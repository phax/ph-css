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
package com.helger.css.reader.errorhandler;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.string.ToStringGenerator;

/**
 * A logging implementation of {@link ICSSInterpretErrorHandler}. So in case a
 * warning or an error occurs, the details are logged to an SLF4J logger.
 *
 * @author Philip Helger
 * @since 5.0.2
 */
@Immutable
public class LoggingCSSInterpretErrorHandler implements ICSSInterpretErrorHandler
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingCSSInterpretErrorHandler.class);

  /**
   * Default constructor.
   */
  public LoggingCSSInterpretErrorHandler ()
  {}

  public void onCSSInterpretationWarning (@Nonnull @Nonempty final String sMessage)
  {
    s_aLogger.warn (sMessage);
  }

  public void onCSSInterpretationError (@Nonnull @Nonempty final String sMessage)
  {
    s_aLogger.error (sMessage);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
