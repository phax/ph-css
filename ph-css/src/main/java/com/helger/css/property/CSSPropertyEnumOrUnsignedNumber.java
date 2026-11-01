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
package com.helger.css.property;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.Nonempty;
import com.helger.annotation.OverridingMethodsMustInvokeSuper;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;
import com.helger.css.utils.CSSNumberHelper;

/**
 * CSS property that is either an enumeration or a non-negative numeric value (e.g. width)
 *
 * @author Philip Helger
 * @since 8.3.0
 */
@NotThreadSafe
public class CSSPropertyEnumOrUnsignedNumber extends CSSPropertyEnumOrNumber
{
  public CSSPropertyEnumOrUnsignedNumber (@NonNull final ECSSProperty eProp,
                                         final boolean bWithPercentage,
                                         @NonNull @Nonempty final String... aEnumValues)
  {
    this (eProp, (ECSSVendorPrefix) null, (ICSSPropertyCustomizer) null, bWithPercentage, aEnumValues);
  }

  public CSSPropertyEnumOrUnsignedNumber (@NonNull final ECSSProperty eProp,
                                         @Nullable final ECSSVendorPrefix eVendorPrefix,
                                         @Nullable final ICSSPropertyCustomizer aCustomizer,
                                         final boolean bWithPercentage,
                                         @NonNull @Nonempty final String... aEnumValues)
  {
    super (eProp, eVendorPrefix, aCustomizer, bWithPercentage, aEnumValues);
  }

  public CSSPropertyEnumOrUnsignedNumber (@NonNull final ECSSProperty eProp,
                                         @Nullable final ECSSVendorPrefix eVendorPrefix,
                                         @Nullable final ICSSPropertyCustomizer aCustomizer,
                                         final boolean bWithPercentage,
                                         @NonNull @Nonempty final Iterable <String> aEnumValues)
  {
    super (eProp, eVendorPrefix, aCustomizer, bWithPercentage, aEnumValues);
  }


  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    return super.isValidValue (sValue) && !CSSNumberHelper.containsNegativeValue (sValue);
  }

  @Override
  @NonNull
  public CSSPropertyEnumOrUnsignedNumber getClone (@NonNull final ECSSProperty eProp)
  {
    return new CSSPropertyEnumOrUnsignedNumber (eProp,
                                               getVendorPrefix (),
                                               getCustomizer (),
                                               isWithPercentage (),
                                               directGetEnumValues ());
  }

  @Override
  @NonNull
  public CSSPropertyEnumOrUnsignedNumber getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyEnumOrUnsignedNumber (getProp (),
                                               eVendorPrefix,
                                               getCustomizer (),
                                               isWithPercentage (),
                                               directGetEnumValues ());
  }
}
