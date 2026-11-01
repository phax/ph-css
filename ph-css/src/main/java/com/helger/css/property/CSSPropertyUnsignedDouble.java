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
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;
import com.helger.css.utils.CSSNumberHelper;

/**
 * CSS property that is a non-negative decimal value without a unit (e.g. flex-grow)
 *
 * @author Philip Helger
 * @since 8.3.0
 */
@NotThreadSafe
public class CSSPropertyUnsignedDouble extends CSSPropertyDouble
{
  public CSSPropertyUnsignedDouble (@NonNull final ECSSProperty eProp)
  {
    this (eProp, (ICSSPropertyCustomizer) null);
  }

  public CSSPropertyUnsignedDouble (@NonNull final ECSSProperty eProp,
                                   @Nullable final ICSSPropertyCustomizer aCustomizer)
  {
    super (eProp, aCustomizer);
  }

  public CSSPropertyUnsignedDouble (@NonNull final ECSSProperty eProp,
                                   @Nullable final ECSSVendorPrefix eVendorPrefix,
                                   @Nullable final ICSSPropertyCustomizer aCustomizer)
  {
    super (eProp, eVendorPrefix, aCustomizer);
  }


  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    return super.isValidValue (sValue) && !CSSNumberHelper.containsNegativeValue (sValue);
  }

  @Override
  @NonNull
  public CSSPropertyUnsignedDouble getClone (@NonNull final ECSSProperty eProp)
  {
    return new CSSPropertyUnsignedDouble (eProp, getVendorPrefix (), getCustomizer ());
  }

  @Override
  @NonNull
  public CSSPropertyUnsignedDouble getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyUnsignedDouble (getProp (), eVendorPrefix, getCustomizer ());
  }
}
