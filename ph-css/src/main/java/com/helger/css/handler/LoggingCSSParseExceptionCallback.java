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
package com.helger.css.handler;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.css.parser.ParseException;
import com.helger.css.reader.errorhandler.LoggingCSSParseErrorHandler;

/**
 * An implementation of {@link ICSSParseExceptionCallback} that logs all
 * {@link ParseException}s to a Logger.
 *
 * @author Philip Helger
 * @since 3.7.4
 */
@Immutable
public class LoggingCSSParseExceptionCallback implements ICSSParseExceptionCallback
{
  private static final Logger s_aLogger = LoggerFactory.getLogger (LoggingCSSParseExceptionCallback.class);

  public LoggingCSSParseExceptionCallback ()
  {}

  public void onException (@Nonnull final ParseException ex)
  {
    s_aLogger.error ("Failed to parse CSS: " + LoggingCSSParseErrorHandler.createLoggingStringParseError (ex));
  }
}
