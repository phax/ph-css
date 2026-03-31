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
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;
import com.helger.css.utils.CSSColorHelper;

/**
 * Represents a color value
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSPropertyColor extends AbstractCSSProperty
{
  public CSSPropertyColor (@NonNull final ECSSProperty eProp)
  {
    this (eProp, (ICSSPropertyCustomizer) null);
  }

  public CSSPropertyColor (@NonNull final ECSSProperty eProp, @Nullable final ICSSPropertyCustomizer aCustomizer)
  {
    this (eProp, (ECSSVendorPrefix) null, aCustomizer);
  }

  public CSSPropertyColor (@NonNull final ECSSProperty eProp,
                           @Nullable final ECSSVendorPrefix eVendorPrefix,
                           @Nullable final ICSSPropertyCustomizer aCustomizer)
  {
    super (eProp, eVendorPrefix, aCustomizer);
  }

  public static boolean isValidPropertyValue (@Nullable final String sValue)
  {
    return AbstractCSSProperty.isValidPropertyValue (sValue) || CSSColorHelper.isColorValue (sValue);
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    return isValidPropertyValue (sValue);
  }

  @NonNull
  public CSSPropertyColor getClone (@NonNull final ECSSProperty eProp)
  {
    return new CSSPropertyColor (eProp, getVendorPrefix (), getCustomizer ());
  }

  @NonNull
  public CSSPropertyColor getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyColor (getProp (), eVendorPrefix, getCustomizer ());
  }
}
