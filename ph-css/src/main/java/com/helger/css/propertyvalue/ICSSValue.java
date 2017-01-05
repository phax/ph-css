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
package com.helger.css.propertyvalue;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.MustImplementEqualsAndHashcode;
import com.helger.css.ICSSWriteable;
import com.helger.css.property.ECSSProperty;

/**
 * Represents a single CSS value that is used in a CSS declaration.
 *
 * @author Philip Helger
 */
@MustImplementEqualsAndHashcode
public interface ICSSValue extends ICSSWriteable
{
  /**
   * @return The underlying CSS property from an enum. Never <code>null</code>.
   */
  @Nonnull
  ECSSProperty getProp ();
}
