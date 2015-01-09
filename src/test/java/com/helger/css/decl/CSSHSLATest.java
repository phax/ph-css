/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
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

import com.helger.commons.mock.PHTestUtils;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSHSLA;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test class for class {@link CSSHSLA}.
 *
 * @author Philip Helger
 */
public final class CSSHSLATest
{
  @Test
  public void testBasic ()
  {
    final CSSWriterSettings aSettings = new CSSWriterSettings (ECSSVersion.CSS30, false);
    final CSSHSLA aColor = new CSSHSLA (1, 2, 3, 0.5f);
    assertEquals ("hsla(1,2%,3%,0.5)", aColor.getAsCSSString (aSettings, 0));

    PHTestUtils.testDefaultImplementationWithEqualContentObject (aColor, new CSSHSLA (aColor));
    PHTestUtils.testDefaultImplementationWithEqualContentObject (aColor, new CSSHSLA (1, 2, 3, 0.5f));
    PHTestUtils.testDefaultImplementationWithDifferentContentObject (aColor, new CSSHSLA (0, 2, 3, 0.5f));
    PHTestUtils.testDefaultImplementationWithDifferentContentObject (aColor, new CSSHSLA (1, 0, 3, 0.5f));
    PHTestUtils.testDefaultImplementationWithDifferentContentObject (aColor, new CSSHSLA (1, 2, 0, 0.5f));
    PHTestUtils.testDefaultImplementationWithDifferentContentObject (aColor, new CSSHSLA (1, 2, 3, 0f));
  }
}
