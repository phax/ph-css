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
package com.helger.css.supplementary.main;

import com.helger.css.property.ECSSProperty;

/**
 * Check all browser specific properties, whether a generic property is present.
 *
 * @author Philip Helger
 */
public class MainCheckVendorSpecificProperties
{
  public static void main (final String [] args) throws Exception
  {
    for (final ECSSProperty e : ECSSProperty.values ())
      if (e.isVendorSpecific ())
      {
        final String sGenericName = e.getVendorIndependentName ();
        if (ECSSProperty.getFromNameOrNull (sGenericName) != null)
          System.out.println (e + " can be replaced!");
      }
    System.out.println ("Done");
  }
}
