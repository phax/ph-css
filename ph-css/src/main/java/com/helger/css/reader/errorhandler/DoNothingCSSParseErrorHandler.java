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
package com.helger.css.reader.errorhandler;

import com.helger.annotation.Nonempty;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.parser.ParseException;
import com.helger.css.parser.Token;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * An implementation of {@link ICSSParseErrorHandler} that does nothing. So in
 * case a recoverable error occurs it is silently ignored.
 *
 * @author Philip Helger
 */
public class DoNothingCSSParseErrorHandler implements ICSSParseErrorHandler
{
  public DoNothingCSSParseErrorHandler ()
  {}

  public void onCSSParseError (@Nonnull final ParseException aParseEx, @Nullable final Token aLastSkippedToken)
  {
    /* really do nothing :) */
  }

  public void onCSSUnexpectedRule (@Nonnull final Token aCurrentToken,
                                   @Nonnull @Nonempty final String sRule,
                                   @Nonnull @Nonempty final String sMsg)
  {
    /* really do nothing :) */
  }

  public void onCSSDeprecatedProperty (@Nonnull final Token aPrefixToken, @Nonnull final Token aIdentifierToken)
  {
    /* really do nothing :) */
  }

  public void onCSSBrowserCompliantSkip (@Nullable final ParseException ex, @Nonnull final Token aFromToken, @Nonnull final Token aToToken)
  {
    /* really do nothing :) */
  }

  @Override
  public void onIllegalCharacter (final char cIllegalChar)
  {
    /* really do nothing :) */
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).getToString ();
  }
}
