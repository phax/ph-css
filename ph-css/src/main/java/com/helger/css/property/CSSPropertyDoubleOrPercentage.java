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
import com.helger.annotation.OverridingMethodsMustInvokeSuper;
import com.helger.css.ECSSUnit;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;
import com.helger.css.propertyvalue.CSSSimpleValueWithUnit;
import com.helger.css.utils.CSSNumberHelper;

/**
 * CSS property that is either a decimal value without a unit or a percentage (e.g. opacity)
 *
 * @author Philip Helger
 * @since 8.3.0
 */
@NotThreadSafe
public class CSSPropertyDoubleOrPercentage extends CSSPropertyDouble
{
  public CSSPropertyDoubleOrPercentage (@NonNull final ECSSProperty eProp)
  {
    this (eProp, (ICSSPropertyCustomizer) null);
  }

  public CSSPropertyDoubleOrPercentage (@NonNull final ECSSProperty eProp,
                                        @Nullable final ICSSPropertyCustomizer aCustomizer)
  {
    super (eProp, aCustomizer);
  }

  public CSSPropertyDoubleOrPercentage (@NonNull final ECSSProperty eProp,
                                        @Nullable final ECSSVendorPrefix eVendorPrefix,
                                        @Nullable final ICSSPropertyCustomizer aCustomizer)
  {
    super (eProp, eVendorPrefix, aCustomizer);
  }

  public static boolean isValidPropertyValue (@Nullable final String sValue)
  {
    if (CSSPropertyDouble.isValidPropertyValue (sValue))
      return true;
    final CSSSimpleValueWithUnit aValue = CSSNumberHelper.getValueWithUnit (sValue, true);
    return aValue != null && aValue.getUnit () == ECSSUnit.PERCENTAGE;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    return isValidPropertyValue (sValue);
  }

  @Override
  @NonNull
  public CSSPropertyDoubleOrPercentage getClone (@NonNull final ECSSProperty eProp)
  {
    return new CSSPropertyDoubleOrPercentage (eProp, getVendorPrefix (), getCustomizer ());
  }

  @Override
  @NonNull
  public CSSPropertyDoubleOrPercentage getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyDoubleOrPercentage (getProp (), eVendorPrefix, getCustomizer ());
  }
}
