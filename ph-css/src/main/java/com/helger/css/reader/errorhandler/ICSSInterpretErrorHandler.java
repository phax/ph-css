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

import com.helger.commons.annotation.Nonempty;

/**
 * Special CSS error handler that is invoked during interpretation of a parsed
 * CSS.
 *
 * @author Philip Helger
 * @since 5.0.2
 */
public interface ICSSInterpretErrorHandler
{
  /**
   * Called when an interpretation error or warning occurs.
   *
   * @param sMessage
   *        The message text of the error.
   */
  void onCSSInterpretationWarning (@Nonnull @Nonempty final String sMessage);

  /**
   * Called when an interpretation error occurs.
   *
   * @param sMessage
   *        The message text of the error.
   */
  void onCSSInterpretationError (@Nonnull @Nonempty final String sMessage);
}
