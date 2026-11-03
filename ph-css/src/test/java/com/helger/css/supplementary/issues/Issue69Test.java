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
package com.helger.css.supplementary.issues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jspecify.annotations.NonNull;
import org.junit.Test;

import com.helger.base.system.ENewLineMode;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSSelectorSimpleMember;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ECSSSelectorCombinator;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test for issue 69: https://github.com/phax/ph-css/issues/69<br>
 * "u+b" is a unicode range token, but in a selector it is "u", "+" and "b".
 *
 * @author Philip Helger
 */
public final class Issue69Test
{
  @NonNull
  private static String _readAndWrite (@NonNull final String sCSS)
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS,
                                                                     new CSSReaderSettings ().setBrowserCompliantMode (false));
    assertNotNull (sCSS, aCSS);
    return new CSSWriter (new CSSWriterSettings ().setOptimizedOutput (true)).setWriteHeaderText (false)
                                                                             .getCSSAsString (aCSS);
  }

  @Test
  public void testIssue69_v1 ()
  {
    final String sCSS = "div+p .foo{width:100vw}";
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS,
                                                                     new CSSReaderSettings ().setBrowserCompliantMode (true));
    assertNotNull (aCSS);
    assertEquals ("div+p .foo { width:100vw; }\n",
                  new CSSWriter (new CSSWriterSettings ().setOptimizedOutput (false).setNewLineMode (ENewLineMode.UNIX))
                                                                                                                        .setWriteHeaderText (false)
                                                                                                                        .getCSSAsString (aCSS));
    assertEquals ("div+p .foo{width:100vw}",
                  new CSSWriter (new CSSWriterSettings ().setOptimizedOutput (true)).setWriteHeaderText (false)
                                                                                    .getCSSAsString (aCSS));
  }

  @Test
  public void testIssue69_v2 ()
  {
    final String sCSS = "div + p .foo{width:100vw}";
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS,
                                                                     new CSSReaderSettings ().setBrowserCompliantMode (true));
    assertNotNull (aCSS);
    assertEquals ("div+p .foo { width:100vw; }\n",
                  new CSSWriter (new CSSWriterSettings ().setOptimizedOutput (false).setNewLineMode (ENewLineMode.UNIX))
                                                                                                                        .setWriteHeaderText (false)
                                                                                                                        .getCSSAsString (aCSS));
    assertEquals ("div+p .foo{width:100vw}",
                  new CSSWriter (new CSSWriterSettings ().setOptimizedOutput (true)).setWriteHeaderText (false)
                                                                                    .getCSSAsString (aCSS));
  }

  @Test
  public void testSelector ()
  {
    assertEquals ("u+b .foo{color:red}", _readAndWrite ("u+b .foo{color:red}"));
    assertEquals ("u+b .foo{color:red}", _readAndWrite ("u + b .foo{color:red}"));
    assertEquals ("U+B{color:red}", _readAndWrite ("U+B{color:red}"));
    assertEquals ("u+bold{color:red}", _readAndWrite ("u+bold{color:red}"));
    assertEquals ("u+abbr.x{color:red}", _readAndWrite ("u+abbr.x{color:red}"));
    assertEquals ("u+a-b{color:red}", _readAndWrite ("u+a-b{color:red}"));
    assertEquals ("u+a-bold{color:red}", _readAndWrite ("u+a-bold{color:red}"));
    assertEquals ("u+b,u+dl{color:red}", _readAndWrite ("u+b, u+dl{color:red}"));
    assertEquals (".u+b{color:red}", _readAndWrite (".u+b{color:red}"));
    assertEquals ("p.u+b>i{color:red}", _readAndWrite ("p.u+b>i{color:red}"));
    assertEquals ("ns|u+b{color:red}", _readAndWrite ("ns|u+b{color:red}"));
    assertEquals ("div{color:red;u+b{color:blue}}",_readAndWrite ("div{color:red; u+b{color:blue}}"));

    // Separate tokens are not merged
    assertEquals ("u+b old{color:red}", _readAndWrite ("u+b old{color:red}"));
  }

  @Test
  public void testStructure ()
  {
    final CascadingStyleSheet aCSS = CSSReader.readFromString ("u+bold{}");
    assertNotNull (aCSS);
    final CSSSelector aSelector = aCSS.getStyleRuleAtIndex (0).getSelectorAtIndex (0);
    assertEquals (3, aSelector.getMemberCount ());
    assertEquals (new CSSSelectorSimpleMember ("u"), aSelector.getMemberAtIndex (0));
    assertEquals (ECSSSelectorCombinator.PLUS, aSelector.getMemberAtIndex (1));
    assertEquals (new CSSSelectorSimpleMember ("bold"), aSelector.getMemberAtIndex (2));
  }

  @Test
  public void testNoSelector ()
  {
    // Not a valid selector
    assertNull (CSSReader.readFromStringReader ("u+0{color:red}",
                                                new CSSReaderSettings ().setBrowserCompliantMode (false)));
    assertNull (CSSReader.readFromStringReader ("u+b?{color:red}",
                                                new CSSReaderSettings ().setBrowserCompliantMode (false)));
  }

  @Test
  public void testUnicodeRangeUnchanged ()
  {
    assertEquals ("@font-face{unicode-range:U+0025-00FF,u+4??,u+b}",
                  _readAndWrite ("@font-face { unicode-range: U+0025-00FF, u+4??, u+b; }"));
  }
}
