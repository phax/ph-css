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

import com.helger.css.property.CCSSProperties;
import com.helger.css.property.ECSSProperty;

/**
 * Helper class to create all constants for {@link CCSSProperties} based on
 * {@link ECSSProperty} that are missing.
 *
 * @author Philip Helger
 */
public class MainAddMissingProperties
{
  public static void main (final String [] args) throws Exception
  {
    boolean bFieldEmitted = false;
    final Class <?> aCClass = CCSSProperties.class;
    for (final ECSSProperty e : ECSSProperty.values ())
      if (!e.isVendorSpecific ())
      {
        boolean bHasField = false;
        try
        {
          bHasField = aCClass.getField (e.name ()) != null;
        }
        catch (final Exception ex)
        {}
        if (!bHasField)
        {
          if (e.getClass ().getField (e.name ()).getAnnotation (Deprecated.class) != null)
            System.out.println ("@Deprecated");
          System.out.println ("public static final ICSSProperty " +
                              e.name () +
                              " = new CSSPropertyFree (ECSSProperty." +
                              e.name () +
                              ");");
          bFieldEmitted = true;
        }
      }
    if (!bFieldEmitted)
      System.out.println ("All fields present");
  }
}
