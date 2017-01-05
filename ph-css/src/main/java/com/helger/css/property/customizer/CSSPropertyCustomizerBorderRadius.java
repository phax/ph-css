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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.Nonempty;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.ICSSProperty;
import com.helger.css.propertyvalue.CSSValueMultiProperty;
import com.helger.css.propertyvalue.ICSSValue;

/**
 * Special customizer for the "border-radius" property.
 *
 * @author Philip Helger
 */
@Immutable
public class CSSPropertyCustomizerBorderRadius extends AbstractCSSPropertyCustomizer
{
  @Nullable
  public ICSSValue createSpecialValue (@Nonnull final ICSSProperty aProperty,
                                       @Nonnull @Nonempty final String sValue,
                                       final boolean bIsImportant)
  {
    return new CSSValueMultiProperty (aProperty.getProp (),
                                      new ICSSProperty [] { aProperty,
                                                            aProperty.getClone (ECSSVendorPrefix.MOZILLA),
                                                            aProperty.getClone (ECSSVendorPrefix.WEBKIT),
                                                            aProperty.getClone (ECSSVendorPrefix.KHTML) },
                                      sValue,
                                      bIsImportant);
  }
}
