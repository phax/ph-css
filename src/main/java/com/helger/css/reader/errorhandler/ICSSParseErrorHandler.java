/**
 * Copyright (C) 2014 Philip Helger (www.helger.com)
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

import com.helger.commons.annotations.Nonempty;
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
   * @param aLastValidToken
   *        The last valid token. May not be <code>null</code>.
   * @param aExpectedTokenSequencesVal
   *        The expected token. May not be <code>null</code>.
   * @param aTokenImageVal
   *        The error token image. May not be <code>null</code>.
   * @param aLastSkippedToken
   *        The token until which was skipped (incl.) May be <code>null</code>.
   * @throws ParseException
   *         In case the error is fatal and should be propagated.
   */
  void onCSSParseError (@Nonnull Token aLastValidToken,
                        @Nonnull int [][] aExpectedTokenSequencesVal,
                        @Nonnull String [] aTokenImageVal,
                        @Nullable Token aLastSkippedToken) throws ParseException;

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
}
