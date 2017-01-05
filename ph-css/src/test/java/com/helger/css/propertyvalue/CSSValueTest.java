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
package com.helger.css.propertyvalue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.css.property.CCSSProperties;
import com.helger.css.utils.ECSSColor;

/**
 * Test class for class {@link CSSValue}.
 *
 * @author Philip Helger
 */
public final class CSSValueTest
{
  @Test
  public void testBasic ()
  {
    final ICSSValue v1 = CCSSProperties.DISPLAY.newValue (CCSSValue.BLOCK);
    final ICSSValue v21 = CCSSProperties.DISPLAY.newValue (CCSSValue.BLOCK);
    final ICSSValue v22 = CCSSProperties.DISPLAY.newValue (CCSSValue.INLINE);
    final ICSSValue v3 = CCSSProperties.DISPLAY.newValue (CCSSValue.INLINE_BLOCK);
    final ICSSValue v4 = CCSSProperties.OPACITY.newValue (Double.toString (0.39));
    final ICSSValue v5 = CCSSProperties.COLOR.newValue (ECSSColor.ALICEBLUE);
    assertNotNull (v1);
    assertNotNull (v21);
    assertNotNull (v22);
    assertTrue (v1 instanceof CSSValue);
    assertTrue (v3 instanceof CSSValueMultiValue);
    assertTrue (v4 instanceof CSSValueList);
    assertTrue (v5 instanceof CSSValue);
    assertEquals (v1, v1);
    assertEquals (v1, v21);
    assertEquals (v21, v1);
    assertFalse (v1.equals (v22));
    assertFalse (v22.equals (v1));
    assertEquals (v1.hashCode (), v21.hashCode ());
  }
}
