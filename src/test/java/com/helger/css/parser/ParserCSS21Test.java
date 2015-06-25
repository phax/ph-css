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
package com.helger.css.parser;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.helger.commons.io.stream.NonBlockingStringReader;
import com.helger.css.AbstractCSS21TestCase;

/**
 * Test class for class {@link ParserCSS21}.
 *
 * @author Philip Helger
 */
public final class ParserCSS21Test extends AbstractCSS21TestCase
{
  @Test
  public void test1 () throws ParseException
  {
    final ParserCSS21TokenManager aTokenHdl = new ParserCSS21TokenManager (new CSSCharStream (new NonBlockingStringReader (CSS1)));
    aTokenHdl.setDebugStream (System.out);
    final ParserCSS21 aParser = new ParserCSS21 (aTokenHdl);
    aParser.disable_tracing ();
    final CSSNode aNode = aParser.styleSheet ();
    assertNotNull (aNode);
  }

  @Test
  public void test2 () throws ParseException
  {
    final ParserCSS21TokenManager aTokenHdl = new ParserCSS21TokenManager (new CSSCharStream (new NonBlockingStringReader (CSS2)));
    aTokenHdl.setDebugStream (System.out);
    final ParserCSS21 aParser = new ParserCSS21 (aTokenHdl);
    aParser.disable_tracing ();
    final CSSNode aNode = aParser.styleSheet ();
    assertNotNull (aNode);
  }
}
