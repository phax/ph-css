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

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.OverridingMethodsMustInvokeSuper;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.collection.commons.CommonsHashSet;
import com.helger.collection.commons.ICommonsList;
import com.helger.collection.commons.ICommonsSet;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.property.customizer.ICSSPropertyCustomizer;
import com.helger.css.utils.CSSValueHelper;

/**
 * CSS property that is either a single enumeration value or a combination of different values. Some
 * values may only stand alone - as <code>none</code> in <code>display</code>, where
 * <code>inline flex</code> is valid but <code>none flex</code> and <code>flex flex</code> are not.
 *
 * @author Philip Helger
 * @since 8.3.0
 */
@NotThreadSafe
public class CSSPropertyEnumCombination extends CSSPropertyEnums
{
  private final ICommonsSet <String> m_aCombinableValues;

  public CSSPropertyEnumCombination (@NonNull final ECSSProperty eProp,
                                     @Nonnegative final int nMaxArgCount,
                                     final String @NonNull [] aStandaloneValues,
                                     @NonNull @Nonempty final String... aCombinableValues)
  {
    this (eProp,
          (ECSSVendorPrefix) null,
          (ICSSPropertyCustomizer) null,
          nMaxArgCount,
          _getAll (aStandaloneValues, aCombinableValues),
          new CommonsHashSet <> (aCombinableValues));
  }

  public CSSPropertyEnumCombination (@NonNull final ECSSProperty eProp,
                                     @Nullable final ECSSVendorPrefix eVendorPrefix,
                                     @Nullable final ICSSPropertyCustomizer aCustomizer,
                                     @Nonnegative final int nMaxArgCount,
                                     @NonNull @Nonempty final Iterable <String> aEnumValues,
                                     @NonNull @Nonempty final Set <String> aCombinableValues)
  {
    super (eProp, eVendorPrefix, aCustomizer, 1, nMaxArgCount, aEnumValues);
    m_aCombinableValues = new CommonsHashSet <> (aCombinableValues);
  }

  @NonNull
  private static ICommonsSet <String> _getAll (final String @NonNull [] aStandaloneValues,
                                               @NonNull final String [] aCombinableValues)
  {
    final ICommonsSet <String> ret = new CommonsHashSet <> (aCombinableValues);
    ret.addAll (Arrays.asList (aStandaloneValues));
    return ret;
  }

  @Override
  @OverridingMethodsMustInvokeSuper
  public boolean isValidValue (@Nullable final String sValue)
  {
    if (!super.isValidValue (sValue))
      return false;

    if (CSSValueHelper.containsSubstitutionFunction (sValue))
      return true;

    final ICommonsList <String> aParts = CSSValueHelper.getSplitByWhitespace (sValue.trim ());
    if (aParts.size () == 1)
      return true;

    final ICommonsSet <String> aUsed = new CommonsHashSet <> ();
    for (final String sPart : aParts)
    {
      final String sLowerPart = sPart.toLowerCase (Locale.ROOT);
      if (!m_aCombinableValues.contains (sLowerPart) || !aUsed.add (sLowerPart))
        return false;
    }
    return true;
  }

  @Override
  @NonNull
  public CSSPropertyEnumCombination getClone (@NonNull final ECSSProperty eProp)
  {
    return new CSSPropertyEnumCombination (eProp,
                                           getVendorPrefix (),
                                           getCustomizer (),
                                           getMaximumArgumentCount (),
                                           directGetEnumValues (),
                                           m_aCombinableValues);
  }

  @Override
  @NonNull
  public CSSPropertyEnumCombination getClone (@Nullable final ECSSVendorPrefix eVendorPrefix)
  {
    return new CSSPropertyEnumCombination (getProp (),
                                           eVendorPrefix,
                                           getCustomizer (),
                                           getMaximumArgumentCount (),
                                           directGetEnumValues (),
                                           m_aCombinableValues);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (!super.equals (o))
      return false;
    final CSSPropertyEnumCombination rhs = (CSSPropertyEnumCombination) o;
    return m_aCombinableValues.equals (rhs.m_aCombinableValues);
  }

  @Override
  public int hashCode ()
  {
    return HashCodeGenerator.getDerived (super.hashCode ()).append (m_aCombinableValues).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ())
                            .append ("combinableValues", m_aCombinableValues)
                            .getToString ();
  }
}
