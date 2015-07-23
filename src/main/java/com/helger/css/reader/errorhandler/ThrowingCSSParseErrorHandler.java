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
package com.helger.css.reader.errorhandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.parser.ParseException;
import com.helger.css.parser.Token;

/**
 * An implementation of {@link ICSSParseErrorHandler} that throws a
 * {@link ParseException}. So in case a recoverable error occurs, a new
 * {@link ParseException} is thrown. This is the most strict implementation of
 * {@link ICSSParseErrorHandler}.
 *
 * @author Philip Helger
 */
@Immutable
public class ThrowingCSSParseErrorHandler implements ICSSParseErrorHandler
{
  public ThrowingCSSParseErrorHandler ()
  {}

  public void onCSSParseError (@Nonnull final Token aLastValidToken,
                               @Nonnull final int [] [] aExpectedTokenSequencesVal,
                               @Nonnull final String [] aTokenImageVal,
                               @Nullable final Token aLastSkippedToken) throws ParseException
  {
    throw new ParseException (aLastValidToken, aExpectedTokenSequencesVal, aTokenImageVal);
  }

  public void onCSSUnexpectedRule (@Nonnull final Token aCurrentToken,
                                   @Nonnull @Nonempty final String sRule,
                                   @Nonnull @Nonempty final String sMsg) throws ParseException
  {
    throw new ParseException (LoggingCSSParseErrorHandler.createLoggingStringUnexpectedRule (aCurrentToken,
                                                                                             sRule,
                                                                                             sMsg));
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).toString ();
  }
}
