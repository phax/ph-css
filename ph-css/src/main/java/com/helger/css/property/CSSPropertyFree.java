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

import com.helger.annotation.OverridingMethodsMustInvokeSuper;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.string.StringHelper;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;

/**
 * CSS property without any value constraints, except that it may not be empty.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSPropertyFree extends AbstractCSSProperty
{
  public CSSPropertyFree (@NonNull final ECSSProperty eProp)
  {
    this (eProp, (ICSSPropertyCustomizer) null);
  }

  public CSSPropertyFree (@NonNull final ECSSProperty eProp, @Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    this (eProp, eVendorPrefix, (ICSSPropertyCustomizer) null);
  }

  public CSSPropertyFree (@NonNull final ECSSProperty eProp, @Nullable final ICSSPropertyCustomizer aCustomizer)
  {
    this (eProp, (ECSSVendorPrefix) null, aCustomizer);
  }

  public CSSPropertyFree (@NonNull final ECSSProperty eProp,
                          @Nullable final ECSSVendorPrefix eVendorPrefix,
                          @Nullable final ICSSPropertyCustomizer aCustomizer)
  {
    super (eProp, eVendorPrefix, aCustomizer);
  }

  public static boolean isValidPropertyValue (@Nullable final String sValue)
  {
    return AbstractCSSProperty.isValidPropertyValue (sValue) || StringHelper.isNotEmpty (sValue);
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    return isValidPropertyValue (sValue);
  }

  @NonNull
  public CSSPropertyFree getClone (@NonNull final ECSSProperty eProp)
  {
    return new CSSPropertyFree (eProp, getVendorPrefix (), getCustomizer ());
  }

  @NonNull
  public CSSPropertyFree getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyFree (getProp (), eVendorPrefix, getCustomizer ());
  }
}
