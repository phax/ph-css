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
package com.helger.css.property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.annotation.Nonempty;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;
import com.helger.css.utils.CSSRectHelper;

/**
 * CSS property that is either an enumeration or a rectangle (e.g. clip)
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSPropertyEnumOrRect extends CSSPropertyEnum
{
  public CSSPropertyEnumOrRect (@Nonnull final ECSSProperty eProp, @Nonnull @Nonempty final String... aEnumValues)
  {
    super (eProp, aEnumValues);
  }

  public CSSPropertyEnumOrRect (@Nonnull final ECSSProperty eProp,
                                @Nullable final ICSSPropertyCustomizer aCustomizer,
                                @Nonnull @Nonempty final String... aEnumValues)
  {
    super (eProp, aCustomizer, aEnumValues);
  }

  public CSSPropertyEnumOrRect (@Nonnull final ECSSProperty eProp,
                                @Nullable final ECSSVendorPrefix eVendorPrefix,
                                @Nullable final ICSSPropertyCustomizer aCustomizer,
                                @Nonnull @Nonempty final String... aEnumValues)
  {
    super (eProp, eVendorPrefix, aCustomizer, aEnumValues);
  }

  public CSSPropertyEnumOrRect (@Nonnull final ECSSProperty eProp,
                                @Nonnull @Nonempty final Iterable <String> aEnumValues)
  {
    super (eProp, aEnumValues);
  }

  public CSSPropertyEnumOrRect (@Nonnull final ECSSProperty eProp,
                                @Nullable final ICSSPropertyCustomizer aCustomizer,
                                @Nonnull @Nonempty final Iterable <String> aEnumValues)
  {
    super (eProp, aCustomizer, aEnumValues);
  }

  public CSSPropertyEnumOrRect (@Nonnull final ECSSProperty eProp,
                                @Nullable final ECSSVendorPrefix eVendorPrefix,
                                @Nullable final ICSSPropertyCustomizer aCustomizer,
                                @Nonnull @Nonempty final Iterable <String> aEnumValues)
  {
    super (eProp, eVendorPrefix, aCustomizer, aEnumValues);
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    return super.isValidValue (sValue) || CSSRectHelper.isRectValue (sValue);
  }

  @Override
  @Nonnull
  public CSSPropertyEnumOrRect getClone (@Nonnull final ECSSProperty eProp)
  {
    return new CSSPropertyEnumOrRect (eProp, getVendorPrefix (), getCustomizer (), directGetEnumValues ());
  }

  @Override
  @Nonnull
  public CSSPropertyEnumOrRect getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyEnumOrRect (getProp (), eVendorPrefix, getCustomizer (), directGetEnumValues ());
  }
}
