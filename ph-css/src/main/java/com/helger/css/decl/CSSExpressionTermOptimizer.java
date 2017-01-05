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
package com.helger.css.decl;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.css.ECSSUnit;
import com.helger.css.propertyvalue.CCSSValue;

/**
 * This class is responsible for expression term optimization
 *
 * @author Philip Helger
 */
@Immutable
public final class CSSExpressionTermOptimizer
{
  private static final ICommonsList <String> s_aUnitValues0 = new CommonsArrayList <> ();

  static
  {
    // Save all "0" formatted unit values
    for (final ECSSUnit eUnit : ECSSUnit.values ())
      s_aUnitValues0.add (eUnit.format (0));
  }

  @PresentForCodeCoverage
  private static final CSSExpressionTermOptimizer s_aInstance = new CSSExpressionTermOptimizer ();

  private CSSExpressionTermOptimizer ()
  {}

  @Nonnull
  @Nonempty
  public static String getOptimizedValue (@Nonnull @Nonempty final String sValue)
  {
    // Replace e.g. "0px" with "0"
    if (s_aUnitValues0.contains (sValue))
      return "0";

    // Check for optimized color values (replace #aabbcc with #abc)
    if (sValue.length () == CCSSValue.HEXVALUE_LENGTH &&
        sValue.charAt (0) == CCSSValue.PREFIX_HEX &&
        sValue.charAt (1) == sValue.charAt (2) &&
        sValue.charAt (3) == sValue.charAt (4) &&
        sValue.charAt (5) == sValue.charAt (6))
    {
      // #112233 => #123
      return Character.toString (CCSSValue.PREFIX_HEX) + sValue.charAt (1) + sValue.charAt (3) + sValue.charAt (5);
    }

    // Don't change - return as is
    return sValue;
  }
}
