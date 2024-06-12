package com.helger.css.supplementary.issues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.io.resource.IReadableResource;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.reader.errorhandler.LoggingCSSParseErrorHandler;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test for issue 88: https://github.com/phax/ph-css/issues/88
 *
 * @author Mike Wiedenbauer 
 */
public final class Issue88Test
{
  @Test
  public void testIssue ()
  {
    final String sCSS = ":where(.some-tile:not(.preserve-color))>*{color:#161616}";
    final CascadingStyleSheet aCSS = CSSReader.readFromStringReader (sCSS,
                                                                     new CSSReaderSettings ().setCSSVersion (ECSSVersion.LATEST));
    assertNotNull (aCSS);
    assertEquals (":where(.some-tile:not(.preserve-color))>*{color:#161616}",
                  new CSSWriter (new CSSWriterSettings ().setOptimizedOutput (true)).setWriteHeaderText (false)
                                                                                    .getCSSAsString (aCSS));
  }
}
