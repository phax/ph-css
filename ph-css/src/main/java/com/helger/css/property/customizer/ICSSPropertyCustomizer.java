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
package com.helger.css.property.customizer;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.css.property.ICSSProperty;
import com.helger.css.propertyvalue.ICSSValue;

/**
 * A special customizer that can be assigned to CSS properties to modify their
 * default behavior. This can be used to add browser-specific values.
 *
 * @author Philip Helger
 */
@FunctionalInterface
public interface ICSSPropertyCustomizer extends Serializable
{
  /**
   * Create a special value for the passed property. For example if the property
   * is "border-radius" than the browser specific values like
   * "-moz-border-radius" and "-webkit-border-radius" should be emitted as well.
   *
   * @param aProperty
   *        The CSS property the fuzz is all about. Never <code>null</code>.
   * @param sValue
   *        The value to be created. Neither <code>null</code> nor empty.
   * @param bIsImportant
   *        <code>true</code> if the property is important, <code>false</code>
   *        if not.
   * @return May be <code>null</code> in which case the default value is
   *         created.
   */
  @Nullable
  ICSSValue createSpecialValue (@Nonnull ICSSProperty aProperty,
                                @Nonnull @Nonempty String sValue,
                                boolean bIsImportant);
}
