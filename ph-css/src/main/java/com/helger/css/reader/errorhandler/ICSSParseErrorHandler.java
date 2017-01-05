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
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.css.parser.ParseException;
import com.helger.css.parser.Token;

/**
 * Special CSS handler that is invoked during reading in case of a recoverable
 * errors. In case of unrecoverable errors, a
 * {@link com.helger.css.parser.ParseException} is thrown!
 *
 * @author Philip Helger
 */
public interface ICSSParseErrorHandler
{
  /**
   * Called upon a recoverable error. The parameter list is similar to the one
   * of the {@link com.helger.css.parser.ParseException}.
   *
   * @param aParseEx
   *        The original parse exception. May not be <code>null</code>.
   * @param aLastSkippedToken
   *        The token until which was skipped (incl.) May be <code>null</code>.
   * @throws ParseException
   *         In case the error is fatal and should be propagated.
   */
  void onCSSParseError (@Nonnull ParseException aParseEx, @Nullable Token aLastSkippedToken) throws ParseException;

  /**
   * Called upon an unexpected rule. This happens e.g. when <code>@import</code>
   * is used in the middle of the file.
   *
   * @param aCurrentToken
   *        The token that could not be interpreted. Never <code>null</code>.
   * @param sRule
   *        The name of the rule. Always starts with a '@'. Neither
   *        <code>null</code> nor empty.
   * @param sMsg
   *        The custom error message. Neither <code>null</code> nor empty.
   * @throws ParseException
   *         In case the error is fatal and should be propagated.
   */
  void onCSSUnexpectedRule (@Nonnull Token aCurrentToken,
                            @Nonnull @Nonempty String sRule,
                            @Nonnull @Nonempty String sMsg) throws ParseException;

  /**
   * This method is only called in browser compliant mode if a certain part of
   * the CSS is skipped.
   *
   * @param ex
   *        The original {@link ParseException} that causes the parser to skip.
   *        May be <code>null</code>.
   * @param aFromToken
   *        Original token that caused the error and was skipped (inclusive).
   *        Never <code>null</code>.
   * @param aToToken
   *        The end token until which was skipped(exclusive). Never
   *        <code>null</code>.
   * @throws ParseException
   *         In case the error is fatal and should be propagated.
   * @see com.helger.css.reader.CSSReaderSettings#setBrowserCompliantMode(boolean)
   */
  void onCSSBrowserCompliantSkip (@Nullable ParseException ex,
                                  @Nonnull Token aFromToken,
                                  @Nonnull Token aToToken) throws ParseException;

  /**
   * This method is invoked, when an illegal character is encountered (in
   * TokenManager), and the respective rule is part of the JavaCC grammar.
   *
   * @param cIllegalChar
   *        The illegal char
   * @since 5.0.3
   */
  default void onIllegalCharacter (final char cIllegalChar)
  {}

  /**
   * Create a new {@link ICSSParseErrorHandler} that invokes both
   * <code>this</code> and the other error handler in a serial way.
   *
   * @param aOther
   *        The other handler to also be invoked.
   * @return A new instance. Never <code>null</code>.
   */
  @Nonnull
  default ICSSParseErrorHandler and (@Nonnull final ICSSParseErrorHandler aOther)
  {
    ValueEnforcer.notNull (aOther, "Other");
    final ICSSParseErrorHandler aThis = this;

    return new ICSSParseErrorHandler ()
    {
      public void onCSSParseError (@Nonnull final ParseException aParseEx,
                                   @Nullable final Token aLastSkippedToken) throws ParseException
      {
        aThis.onCSSParseError (aParseEx, aLastSkippedToken);
        aOther.onCSSParseError (aParseEx, aLastSkippedToken);
      }

      public void onCSSUnexpectedRule (@Nonnull final Token aCurrentToken,
                                       @Nonnull @Nonempty final String sRule,
                                       @Nonnull @Nonempty final String sMsg) throws ParseException
      {
        aThis.onCSSUnexpectedRule (aCurrentToken, sRule, sMsg);
        aOther.onCSSUnexpectedRule (aCurrentToken, sRule, sMsg);
      }

      public void onCSSBrowserCompliantSkip (@Nullable final ParseException ex,
                                             @Nonnull final Token aFromToken,
                                             @Nonnull final Token aToToken) throws ParseException
      {
        aThis.onCSSBrowserCompliantSkip (ex, aFromToken, aToToken);
        aOther.onCSSBrowserCompliantSkip (ex, aFromToken, aToToken);
      }

      public void onIllegalCharacter (final char cIllegalChar)
      {
        aThis.onIllegalCharacter (cIllegalChar);
        aOther.onIllegalCharacter (cIllegalChar);
      }
    };
  }
}
