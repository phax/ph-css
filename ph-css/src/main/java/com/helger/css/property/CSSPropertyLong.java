/*
 * Copyright (C) 2014-2021 Philip Helger (www.helger.com)
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.string.StringParser;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;

/**
 * CSS property that is either an enumeration or a long value without a unit
 * (e.g. flex-grow)
 *
 * @author Philip Helger
 * @since 6.4.0
 */
@NotThreadSafe
public class CSSPropertyLong extends AbstractCSSProperty
{
  public CSSPropertyLong (@Nonnull final ECSSProperty eProp)
  {
    this (eProp, (ICSSPropertyCustomizer) null);
  }

  public CSSPropertyLong (@Nonnull final ECSSProperty eProp, @Nullable final ICSSPropertyCustomizer aCustomizer)
  {
    super (eProp, (ECSSVendorPrefix) null, aCustomizer);
  }

  public CSSPropertyLong (@Nonnull final ECSSProperty eProp,
                          @Nullable final ECSSVendorPrefix eVendorPrefix,
                          @Nullable final ICSSPropertyCustomizer aCustomizer)
  {
    super (eProp, eVendorPrefix, aCustomizer);
  }

  public static boolean isValidPropertyValue (@Nullable final String sValue)
  {
    return AbstractCSSProperty.isValidPropertyValue (sValue) || StringParser.parseLongObj (sValue) != null;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    return isValidPropertyValue (sValue);
  }

  @Nonnull
  public CSSPropertyLong getClone (@Nonnull final ECSSProperty eProp)
  {
    return new CSSPropertyLong (eProp, getVendorPrefix (), getCustomizer ());
  }

  @Nonnull
  public CSSPropertyLong getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyLong (getProp (), eVendorPrefix, getCustomizer ());
  }
}
