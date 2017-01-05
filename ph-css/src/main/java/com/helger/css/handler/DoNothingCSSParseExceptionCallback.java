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

import com.helger.css.parser.ParseException;

/**
 * An implementation of {@link ICSSParseExceptionCallback} that silently ignores
 * all errors.
 *
 * @author Philip Helger
 */
@Immutable
public class DoNothingCSSParseExceptionCallback implements ICSSParseExceptionCallback
{
  public DoNothingCSSParseExceptionCallback ()
  {}

  public void onException (@Nonnull final ParseException ex)
  {
    // ignore
  }
}
