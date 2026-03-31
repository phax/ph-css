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
package com.helger.css.reader.errorhandler;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.base.enforce.ValueEnforcer;
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
  void onCSSParseError (@NonNull ParseException aParseEx, @Nullable Token aLastSkippedToken) throws ParseException;

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
  void onCSSUnexpectedRule (@NonNull Token aCurrentToken,
                            @NonNull @Nonempty String sRule,
                            @NonNull @Nonempty String sMsg) throws ParseException;

  /**
   * To be called, if a deprecated old IE 6/7 property is found.
   *
   * @param aPrefixToken
   *        The prefix token found (like '$' or '*'). Never <code>null</code>.
   * @param aIdentifierToken
   *        The identifier token found. Never <code>null</code>.
   * @throws ParseException
   *         In case the error is fatal and should be propagated.
   * @since 6.5.0
   */
  void onCSSDeprecatedProperty (@NonNull Token aPrefixToken, @NonNull Token aIdentifierToken) throws ParseException;

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
   *        The end token until which was skipped (exclusive). Never
   *        <code>null</code>.
   * @throws ParseException
   *         In case the error is fatal and should be propagated.
   * @see com.helger.css.reader.CSSReaderSettings#setBrowserCompliantMode(boolean)
   */
  void onCSSBrowserCompliantSkip (@Nullable ParseException ex, @NonNull Token aFromToken, @NonNull Token aToToken) throws ParseException;

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
  @NonNull
  default ICSSParseErrorHandler and (@NonNull final ICSSParseErrorHandler aOther)
  {
    ValueEnforcer.notNull (aOther, "Other");
    final ICSSParseErrorHandler aThis = this;

    return new ICSSParseErrorHandler ()
    {
      public void onCSSParseError (@NonNull final ParseException aParseEx, @Nullable final Token aLastSkippedToken) throws ParseException
      {
        aThis.onCSSParseError (aParseEx, aLastSkippedToken);
        aOther.onCSSParseError (aParseEx, aLastSkippedToken);
      }

      public void onCSSUnexpectedRule (@NonNull final Token aCurrentToken,
                                       @NonNull @Nonempty final String sRule,
                                       @NonNull @Nonempty final String sMsg) throws ParseException
      {
        aThis.onCSSUnexpectedRule (aCurrentToken, sRule, sMsg);
        aOther.onCSSUnexpectedRule (aCurrentToken, sRule, sMsg);
      }

      public void onCSSDeprecatedProperty (@NonNull final Token aPrefixToken, @NonNull final Token aIdentifierToken) throws ParseException
      {
        aThis.onCSSDeprecatedProperty (aPrefixToken, aIdentifierToken);
        aOther.onCSSDeprecatedProperty (aPrefixToken, aIdentifierToken);
      }

      public void onCSSBrowserCompliantSkip (@Nullable final ParseException ex,
                                             @NonNull final Token aFromToken,
                                             @NonNull final Token aToToken) throws ParseException
      {
        aThis.onCSSBrowserCompliantSkip (ex, aFromToken, aToToken);
        aOther.onCSSBrowserCompliantSkip (ex, aFromToken, aToToken);
      }

      @Override
      public void onIllegalCharacter (final char cIllegalChar)
      {
        aThis.onIllegalCharacter (cIllegalChar);
        aOther.onIllegalCharacter (cIllegalChar);
      }
    };
  }
}
