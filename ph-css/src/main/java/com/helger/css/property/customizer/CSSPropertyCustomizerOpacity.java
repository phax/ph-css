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
import com.helger.commons.string.StringParser;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.CSSPropertyFree;
import com.helger.css.property.ECSSProperty;
import com.helger.css.property.ICSSProperty;
import com.helger.css.propertyvalue.CSSValueList;
import com.helger.css.propertyvalue.ICSSValue;

/**
 * Special customizer for the "opacity" property.
 *
 * @author Philip Helger
 */
@Immutable
public class CSSPropertyCustomizerOpacity extends AbstractCSSPropertyCustomizer
{
  @Nullable
  public ICSSValue createSpecialValue (@Nonnull final ICSSProperty aProperty,
                                       @Nonnull @Nonempty final String sValue,
                                       final boolean bIsImportant)
  {
    final double dValue = StringParser.parseDouble (sValue, Double.NaN);
    if (!Double.isNaN (dValue))
    {
      final int nPerc = (int) (dValue * 100);
      return new CSSValueList (ECSSProperty.OPACITY,
                               new ICSSProperty [] { new CSSPropertyFree (ECSSProperty.FILTER,
                                                                          ECSSVendorPrefix.MICROSOFT),
                                                     new CSSPropertyFree (ECSSProperty.FILTER),
                                                     aProperty.getClone (ECSSVendorPrefix.MOZILLA),
                                                     aProperty.getClone (ECSSVendorPrefix.WEBKIT),
                                                     aProperty },
                               new String [] { "\"progid:DXImageTransform.Microsoft.Alpha(Opacity=" +
                                               nPerc +
                                               ")\"",
                                               "alpha(opacity=" + nPerc + ")",
                                               sValue,
                                               sValue,
                                               sValue },
                               bIsImportant);
    }
    return null;
  }
}
