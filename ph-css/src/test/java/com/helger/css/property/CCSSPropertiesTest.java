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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for class {@link CCSSProperties}.
 *
 * @author Philip Helger
 */
public final class CCSSPropertiesTest
{
  @Test
  public void test ()
  {
    assertTrue (CCSSProperties.BORDER_LEFT_STYLE.isValidValue ("none"));
    assertFalse (CCSSProperties.BORDER_LEFT_STYLE.isValidValue ("any"));
    assertTrue (CCSSProperties.BORDER_STYLE.isValidValue ("solid none"));
    assertTrue (CCSSProperties.BORDER_STYLE.isValidValue ("      solid           none       "));
    assertFalse (CCSSProperties.BORDER_STYLE.isValidValue ("      solid           any       "));
    assertTrue (CCSSProperties.BORDER_STYLE.isValidValue ("solid none none none"));
    assertFalse (CCSSProperties.BORDER_STYLE.isValidValue ("solid none none none solid"));
    assertFalse (CCSSProperties.BORDER_STYLE.isValidValue (""));
    assertTrue (CCSSProperties.COLOR.isValidValue ("rgb(0,0,0)"));
    assertTrue (CCSSProperties.COLOR.isValidValue ("rgb ( 0% , 0% , 50% )"));
    assertTrue (CCSSProperties.COLOR.isValidValue ("rgb(0%,0%,50%)"));
    assertTrue (CCSSProperties.COLOR.isValidValue ("#ffffff"));
    assertTrue (CCSSProperties.COLOR.isValidValue (" #ffffff "));
    assertTrue (CCSSProperties.COLOR.isValidValue ("#fff"));
    assertTrue (CCSSProperties.COLOR.isValidValue (" #fff   "));
    assertTrue (CCSSProperties.COLOR.isValidValue ("#fffe"));
    assertTrue (CCSSProperties.COLOR.isValidValue (" #fffe   "));
    assertFalse (CCSSProperties.COLOR.isValidValue ("#fffffff"));
    assertFalse (CCSSProperties.COLOR.isValidValue ("#aag"));
    assertFalse (CCSSProperties.COLOR.isValidValue ("#ppp"));
    assertTrue (CCSSProperties.CLIP.isValidValue ("auto"));
    assertTrue (CCSSProperties.CLIP.isValidValue ("rect(5px,10in,33px,456em)"));
    assertTrue (CCSSProperties.Z_INDEX.isValidValue ("-500"));
    assertTrue (CCSSProperties.Z_INDEX.isValidValue ("0"));
    assertTrue (CCSSProperties.Z_INDEX.isValidValue ("1000"));
  }
}
