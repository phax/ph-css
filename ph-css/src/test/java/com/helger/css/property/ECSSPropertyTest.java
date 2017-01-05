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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link ECSSProperty}.
 *
 * @author Philip Helger
 */
public final class ECSSPropertyTest
{
  @Test
  public void testBasic ()
  {
    for (final ECSSProperty e : ECSSProperty.values ())
    {
      assertTrue (e.getName ().length () > 0);
      assertSame (e, ECSSProperty.getFromNameOrNull (e.getName ()));
      assertSame (e, ECSSProperty.getFromNameOrNullHandlingHacks (e.getName ()));
      assertSame (e, ECSSProperty.getFromNameOrNullHandlingHacks ("*" + e.getName ()));
      assertSame (e, ECSSProperty.getFromNameOrNullHandlingHacks ("$" + e.getName ()));
      assertSame (e, ECSSProperty.getFromNameOrNullHandlingHacks ("_" + e.getName ()));
    }
  }

  @Test
  public void testGetVendorIndependentName ()
  {
    assertEquals ("margin", ECSSProperty.MARGIN.getName ());
    assertEquals ("margin", ECSSProperty.MARGIN.getVendorIndependentName ());

    assertEquals ("-webkit-box-lines", ECSSProperty._WEBKIT_BOX_LINES.getName ());
    assertEquals ("box-lines", ECSSProperty._WEBKIT_BOX_LINES.getVendorIndependentName ());
  }
}
