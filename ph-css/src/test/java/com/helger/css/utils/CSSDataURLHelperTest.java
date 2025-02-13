/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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
package com.helger.css.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.commons.mock.CommonsTestHelper;

/**
 * Test class for class {@link CSSDataURLHelper}.<br>
 * Test data from:
 * http://www-archive.mozilla.org/quality/networking/testing/datatests.html
 *
 * @author Philip Helger
 */
public final class CSSDataURLHelperTest
{
  private static final String [] VALID_PLAIN = new String [] { "data:",
                                                               "data:,",
                                                               "data:text/plain,",
                                                               "data:,;test",
                                                               "data:text/plain,test",
                                                               "data:text/plain;charset=US-ASCII,test",
                                                               "data:,a,b",
                                                               "data:,foo bar",
                                                               "data:;charset=UTF-8,Hello",
                                                               "data:,A%20brief%20note",
                                                               "data:application/vnd-xxx-query,select_vcount,fcol_from_fieldtable/local",
                                                               "data:text/xml,<root>code</root>",
                                                               "data:text/xml;base64=true,<root>code</root>",
                                                               "data:text/xml;param1=abc,<root>code</root>",
                                                               "data:text/xml;param1=abc;base64=true,<root>code</root>",
                                                               "data:text/xml;param1=abc;param2=ab%20cd,<root>code</root>",
                                                               "data:text/xml;param1=abc;param2=ab%20cd;base64=true,<root>code</root>",
                                                               "data: text / xml ; param1 = abc ; param2 = ab%20cd ,<root>code</root>",
                                                               "data: text / xml ; param1 = abc ; param2 = ab%20cd ;base64=true ,<root>code</root>",
                                                               "data:text/html;charset=utf-8,%3C%21DOCTYPE%20html%3E%0D%0A%3Cht'+\r\n" +
                                                                                                                                                     "  'ml%20lang%3D%22en%22%3E%0D%0A%3Chead%3E%3Ctitle%3EEmbedded%20Window%3C%2F'+\r\n" +
                                                                                                                                                     "  'title%3E%3C%2Fhead%3E%0D%0A%3Cbody%3E%3Ch1%3E42%3C%2Fh1%3E%3C%2Fbody%3E%0'+\r\n" +
                                                                                                                                                     "  'A%3C%2Fhtml%3E%0A%0D%0A",
                                                               "data:application/vnd.mozilla.xul+xml,%3C?xml%20version=%221.0%22?%3E%3Cwindow%20xmlns=%22http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul%22%3E%3C?xml-stylesheet%20href=%22data:text/css,#a%7B-moz-box-flex:1;%7D%22?%3E%3Cbox%20id=%22a%22%3E%3Clabel%20value=%22This%20works%21%22/%3E%3C/box%3E%3Cbox/%3E%3C/window%3E",
                                                               "data:image/svg+xml,%3C?xml%20version=\"1.0\"?%3E%3Csvg%20xmlns=\"http://www.w3.org/2000/svg\"%3E%20%3Ccircle%20id=\"circ\"%20r=\"1cm\"%20cx=\"5cm\"%20cy=\"3cm\"%20style=\"fill:red;%20stroke:blue;%20stroke-width:3;\"/%3E%3C/svg%3E" };

  private static final String [] VALID_BASE64 = new String [] { "data:;base64,",
                                                                "data:;base64",
                                                                "data:text/html;base64,VGhpcyBpcyBhIHRlc3QK",
                                                                "data:text/plain;charset=thing;base64;test",
                                                                "data:image/gif;base64,R0lGODdhMAAwAPAAAAAAAP///ywAAAAAMAAw\r\n" +
                                                                                                             "AAAC8IyPqcvt3wCcDkiLc7C0qwyGHhSWpjQu5yqmCYsapyuvUUlvONmOZtfzgFz\r\n" +
                                                                                                             "ByTB10QgxOR0TqBQejhRNzOfkVJ+5YiUqrXF5Y5lKh/DeuNcP5yLWGsEbtLiOSp\r\n" +
                                                                                                             "a/TPg7JpJHxyendzWTBfX0cxOnKPjgBzi4diinWGdkF8kjdfnycQZXZeYGejmJl\r\n" +
                                                                                                             "ZeGl9i2icVqaNVailT6F5iJ90m6mvuTS4OK05M0vDk0Q4XUtwvKOzrcd3iq9uis\r\n" +
                                                                                                             "F81M1OIcR7lEewwcLp7tuNNkM3uNna3F2JQFo97Vriy/Xl4/f1cf5VWzXyym7PH\r\n" +
                                                                                                             "hhx4dbgYKAAA7",
                                                                "data:image/gif;base64,R0lGODlhEAAQAMQAAORHHOVSKudfOulrSOp3WOyDZu6QdvCchPGolfO0o/XBs/fNwfjZ0frl3/zy7////wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAkAABAALAAAAAAQABAAAAVVICSOZGlCQAosJ6mu7fiyZeKqNKToQGDsM8hBADgUXoGAiqhSvp5QAnQKGIgUhwFUYLCVDFCrKUE1lBavAViFIDlTImbKC5Gm2hB0SlBCBMQiB0UjIQA7",
                                                                "data:text/xml;base64,VGhpcyBpcyBhIHRlc3QK",
                                                                "data:text/xml;base64=true;base64,VGhpcyBpcyBhIHRlc3QK",
                                                                "data:text/xml;param1=abc;base64,VGhpcyBpcyBhIHRlc3QK",
                                                                "data:text/xml;param1=abc;base64=true;base64,VGhpcyBpcyBhIHRlc3QK",
                                                                "data:text/xml;param1=abc;param2=ab%20cd;base64,VGhpcyBpcyBhIHRlc3QK",
                                                                "data:text/xml;param1=abc;param2=ab%20cd;base64=true;base64,VGhpcyBpcyBhIHRlc3QK",
                                                                "data: text / xml ; param1 = abc ; param2 = ab%2ccd ;base64 ,VGhpcyBpcyBhIHRlc3QK",
                                                                "data: text / xml ; param1 = abc ; param2 = ab%2ccd ;base64=true;base64 ,VGhpcyBpcyBhIHRlc3QK",
                                                                "data:image/png;base64,\r\n" +
                                                                                                                                                                "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQAQMAAAAlPW0iAAAABlBMVEUAAAD/\r\n" +
                                                                                                                                                                "//+l2Z/dAAAAM0lEQVR4nGP4/5/h/1+G/58ZDrAz3D/McH8yw83NDDeNGe4U\r\n" +
                                                                                                                                                                "g9C9zwz3gVLMDA/A6P9/AFGGFyjOXZtQAAAAAElFTkSuQmCC",
                                                                "data:text/html;base64,PGh0bWw+PGhlYWQ+PHRpdGxlPlRlc3Q8L3RpdGxlPjwvaGVhZD48Ym9keT48cD5UaGlzIGlzIGEgdGVzdDwvYm9keT48L2h0bWw+Cg==",
                                                                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAIAAAACUFjqAAAABGdBTUEAALGPC/xhBQAAAAlwSFlzAAALEgAACxIB0t1+/AAAAAd0SU1FB9EFBAoYMhVvMQIAAAAtSURBVHicY/z//z8DHoBH+v///yy4FDEyMjIwMDDhM3lgpaEuh7gTEzDiDxYA9HEPDF90e5YAAAAASUVORK5CYII=",
                                                                "data:audio/mp3;base64,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8vaF9XjfQxrNcEcK8r6Q/QOSe9NXe/s+T9Fuymqr83Kdaz7XFXNEnz342p9f0+dr+vAD8bccmLw+VaqcNaeYI6FokNIqRUHSeoXbTeKOzCJl7HqzFIWzUudQ+lctVYFfI2UURy9t/uXZJ2Z6njB0cLh8nRXMIC1mWVS1irnykaIIAAAqqANMtpKzQQBPDv/7QsTPAAplC13njEuhP4/p8PSNIGN6FDjWeIsNhAEtUFuGqqcesdvPUWRZs3mdlYzHJLJkq0E2a/jykWbdpPATyy5YoeCbDAwyKQyRqldL0h70xGt96SFxUMpCpsKgiDYGwlKmVta1xpcuSeZNBgESr0luSjMyxxJJQdBXEoKhosdLA0eEwNCUFfyoaq9R7ES3Q7EQNPlgaPYKuTywdaCoKgr/qadni0SIMCljpQugNkwpEQyHhv/7QsTOgAnIRU+HrMbJPo4qNPGNIcQCsSCUVCEaD4wRkBOaCQYSBCxBMgqpZ2dndnZ2uFpETjSizLQRJGnFlHmJpInGlFmWICaaQNb////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7QsTQgAms3USnmGtBRpfosPGNaP/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7QsTSAAmgWzIEsMcBKBZSQGSZaf///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////1RBR0hhdHMgSWRlYSAgICAgICAgICAgICAgICAgICAgIFNhbGVtICAgICAgICAgICAgICAgICAgICAgICAgIFNhYnJpbmEgVGhlIFRlZW5hZ2UgV2l0Y2ggICAgIDE5OTcgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA5" };

  private static final String [] INVALID = new String [] { "data:;base64,äöü" };

  @Test
  public void testIsDataURL ()
  {
    assertFalse (CSSDataURLHelper.isDataURL (null));
    assertFalse (CSSDataURLHelper.isDataURL (""));
    assertFalse (CSSDataURLHelper.isDataURL ("data"));
    assertFalse (CSSDataURLHelper.isDataURL (" data"));
    // Even though they are false positives
    assertTrue (CSSDataURLHelper.isDataURL ("data:"));
    assertTrue (CSSDataURLHelper.isDataURL (" data:"));
    assertTrue (CSSDataURLHelper.isDataURL (" data: "));
    assertTrue (CSSDataURLHelper.isDataURL (" data:,"));
    assertTrue (CSSDataURLHelper.isDataURL ("data:any"));
    assertTrue (CSSDataURLHelper.isDataURL (" data:any"));
    assertTrue (CSSDataURLHelper.isDataURL ("daTA:"));
    assertTrue (CSSDataURLHelper.isDataURL (" daTA:"));
    assertTrue (CSSDataURLHelper.isDataURL (" daTA: "));
    assertTrue (CSSDataURLHelper.isDataURL (" daTA:,"));
    assertTrue (CSSDataURLHelper.isDataURL ("daTA:any"));
    assertTrue (CSSDataURLHelper.isDataURL (" daTA:any"));

    for (final String sValid : VALID_PLAIN)
      assertTrue (CSSDataURLHelper.isDataURL (sValid));

    for (final String sValid : VALID_BASE64)
      assertTrue (CSSDataURLHelper.isDataURL (sValid));
  }

  @Test
  public void testParseDataURL ()
  {
    for (final String sValid : VALID_PLAIN)
    {
      final CSSDataURL aURL = CSSDataURLHelper.parseDataURL (sValid);
      assertNotNull ("Failed to parse: " + sValid, aURL);
      assertFalse ("Should not be Base64: " + sValid, aURL.isBase64Encoded ());

      // Convert to string and parse again
      String sAsString = aURL.getAsString ();
      assertNotNull (sAsString);
      CSSDataURL aURL2 = CSSDataURLHelper.parseDataURL (sAsString);
      assertNotNull ("Failed to parse: " + sAsString, aURL2);

      assertEquals (aURL, aURL2);
      CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aURL, aURL2);

      // Convert to optimized string and parse again
      sAsString = aURL.getAsString (true);
      assertNotNull (sAsString);
      aURL2 = CSSDataURLHelper.parseDataURL (sAsString);
      assertNotNull ("Failed to parse: " + sAsString, aURL2);
      // -> not necessarily equals because of optional Base64 marker
    }

    for (final String sValid : VALID_BASE64)
    {
      final CSSDataURL aURL = CSSDataURLHelper.parseDataURL (sValid);
      assertNotNull ("Failed to parse: " + sValid, aURL);
      assertTrue ("Should be Base64: " + sValid, aURL.isBase64Encoded ());

      // Convert to string and parse again
      String sAsString = aURL.getAsString ();
      assertNotNull (sAsString);
      CSSDataURL aURL2 = CSSDataURLHelper.parseDataURL (sAsString);
      assertNotNull ("Failed to parse: " + sAsString, aURL2);

      assertEquals (aURL, aURL2);
      CommonsTestHelper.testDefaultImplementationWithEqualContentObject (aURL, aURL2);

      // Convert to optimized string and parse again
      sAsString = aURL.getAsString (true);
      assertNotNull (sAsString);
      aURL2 = CSSDataURLHelper.parseDataURL (sAsString);
      assertNotNull ("Failed to parse: " + sAsString, aURL2);
      // -> not necessarily equals because of optional Base64 marker
    }

    for (final String sInvalid : INVALID)
    {
      final CSSDataURL aURL = CSSDataURLHelper.parseDataURL (sInvalid);
      assertNull ("Should not parse: " + sInvalid, aURL);
    }
  }
}
