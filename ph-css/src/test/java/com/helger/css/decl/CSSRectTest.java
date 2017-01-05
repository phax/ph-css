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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.helger.commons.mock.CommonsTestHelper;
import com.helger.css.ECSSVersion;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test class for class {@link CSSRect}.
 *
 * @author Philip Helger
 */
public final class CSSRectTest
{
  @Test
  public void testAPI ()
  {
    final CSSRect aRect = new CSSRect ("1", "2", "3", "4");
    assertEquals ("1", aRect.getTop ());
    assertEquals ("2", aRect.getRight ());
    assertEquals ("3", aRect.getBottom ());
    assertEquals ("4", aRect.getLeft ());
    assertEquals ("rect(1,2,3,4)", aRect.getAsCSSString (new CSSWriterSettings (ECSSVersion.CSS30), 0));

    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aRect, new CSSRect (aRect));
    CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aRect, new CSSRect ("1", "2", "3", "4"));
    CommonsTestHelper.testDefaultImplementationWithDifferentContentObject (aRect, new CSSRect ("0", "2", "3", "4"));
    CommonsTestHelper.testDefaultImplementationWithDifferentContentObject (aRect, new CSSRect ("1", "0", "3", "4"));
    CommonsTestHelper.testDefaultImplementationWithDifferentContentObject (aRect, new CSSRect ("2", "2", "0", "4"));
    CommonsTestHelper.testDefaultImplementationWithDifferentContentObject (aRect, new CSSRect ("3", "2", "3", "0"));

    aRect.setTop ("5");
    assertEquals ("5", aRect.getTop ());
    assertEquals ("2", aRect.getRight ());
    assertEquals ("3", aRect.getBottom ());
    assertEquals ("4", aRect.getLeft ());
    assertEquals ("rect(5,2,3,4)", aRect.getAsCSSString (new CSSWriterSettings (ECSSVersion.CSS30), 0));

    aRect.setRight ("6");
    aRect.setBottom ("7");
    aRect.setLeft ("8");
    assertEquals ("5", aRect.getTop ());
    assertEquals ("6", aRect.getRight ());
    assertEquals ("7", aRect.getBottom ());
    assertEquals ("8", aRect.getLeft ());
    assertEquals ("rect(5,6,7,8)", aRect.getAsCSSString (new CSSWriterSettings (ECSSVersion.CSS30), 0));
  }
}
