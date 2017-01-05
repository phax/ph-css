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
package com.helger.css.supplementary.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.WillClose;
import javax.annotation.WillNotClose;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.charset.CCharset;
import com.helger.commons.charset.CharsetManager;
import com.helger.commons.io.file.FileHelper;
import com.helger.commons.io.stream.StreamHelper;

public class CSSTokenizer
{
  private static final String CHARSET = "@charset \"";

  private final Charset m_aFallbackEncoding;
  private boolean m_bStrictMode = false;
  private boolean m_bDebugMode = false;

  public CSSTokenizer ()
  {
    this (CCharset.CHARSET_UTF_8_OBJ);
  }

  public CSSTokenizer (@Nonnull final Charset aFallbackEncoding)
  {
    m_aFallbackEncoding = ValueEnforcer.notNull (aFallbackEncoding, "FallbackEncoding");
  }

  @Nonnull
  public CSSTokenizer setStrictMode (final boolean bStrictMode)
  {
    m_bStrictMode = bStrictMode;
    return this;
  }

  @Nonnull
  public CSSTokenizer setDebugMode (final boolean bDebugMode)
  {
    m_bDebugMode = bDebugMode;
    return this;
  }

  @Nonnull
  private Charset _determineCharset (@Nonnull @WillNotClose final CSSInputStream aIS) throws IOException,
                                                                                      CSSTokenizeException
  {
    // Determine charset
    // https://www.w3.org/TR/css-syntax-3/#input-byte-stream
    final int nMaxHeader = Math.min (1024, aIS.available ());
    if (nMaxHeader > 11)
    {
      final byte [] aBuffer = new byte [nMaxHeader];
      aIS.read (aBuffer);
      aIS.unread (aBuffer);

      final String sPrefix = new String (aBuffer, 0, CHARSET.length (), CCharset.CHARSET_US_ASCII_OBJ);
      if (m_bStrictMode ? CHARSET.equals (sPrefix) : CHARSET.equalsIgnoreCase (sPrefix))
      {
        int nEnd = CHARSET.length ();
        while (nEnd < nMaxHeader && aBuffer[nEnd] != '"')
          nEnd++;
        if (nEnd == nMaxHeader)
          throw new CSSTokenizeException ("Unexpected end of @charset declaration");
        String sCharset = new String (aBuffer,
                                      CHARSET.length (),
                                      nEnd - CHARSET.length (),
                                      CCharset.CHARSET_US_ASCII_OBJ);
        if ("utf-16be".equalsIgnoreCase (sCharset) || "utf-16le".equalsIgnoreCase (sCharset))
          sCharset = "utf-8";
        final Charset aCharset = CharsetManager.getCharsetFromName (sCharset);
        if (aCharset == null)
          throw new CSSTokenizeException ("Unsupported charset '" + sCharset + "' provided!");
        return aCharset;
      }
    }
    return m_aFallbackEncoding;
  }

  public void tokenize (@Nonnull @WillClose final InputStream aIS,
                        @Nonnull final Consumer <CSSToken> aConsumer) throws IOException, CSSTokenizeException
  {
    ValueEnforcer.notNull (aIS, "InputStream");
    ValueEnforcer.notNull (aConsumer, "Consumer");

    try (final CSSInputStream aCSSIS = new CSSInputStream (aIS))
    {
      final Charset aCharset = _determineCharset (aCSSIS);

      try (final CSSCodepointReader aReader = new CSSCodepointReader (aCSSIS, aCharset))
      {
        while (true)
        {
          // https://www.w3.org/TR/css-syntax-3/#consume-a-token0
          CSSCodepoint aCP = aReader.startToken ();
          final ECSSTokenStartType eTokenStartType = aCP.getTokenStartType ();

          if (m_bDebugMode)
          {
            final int nValue = aCP.getValue ();
            System.out.println ("[" +
                                aReader.getTokenStartLineNumber () +
                                ":" +
                                aReader.getTokenStartColumnNumber () +
                                "] - " +
                                (eTokenStartType == ECSSTokenStartType.EOF ? "EOF"
                                                                           : "read CP " +
                                                                             (nValue >= 0x20 &&
                                                                              nValue <= 0x7f ? Character.toString ((char) nValue)
                                                                                             : "0x" +
                                                                                               Integer.toHexString (nValue)) +
                                                                             " as " +
                                                                             eTokenStartType));
          }

          if (eTokenStartType == ECSSTokenStartType.EOF)
          {
            // EOF
            aConsumer.accept (aReader.createToken (ECSSTokenType.EOF));
            break;
          }

          CSSToken aToken = null;
          switch (eTokenStartType)
          {
            case WHITESPACE:
              while (true)
              {
                aCP = aReader.read ();
                if (aCP.getTokenStartType () != ECSSTokenStartType.WHITESPACE)
                  break;
              }
              aReader.unread (aCP);
              aToken = aReader.createToken (ECSSTokenType.WHITESPACE);
              break;
            case SOLIDUS:
              // Maybe a comment?
              if (aReader.peek ().getTokenStartType () == ECSSTokenStartType.ASTERISK)
              {
                // It's a comment
                aReader.read ();
                while (true)
                {
                  aCP = aReader.read ();
                  if (aCP.getTokenStartType () == ECSSTokenStartType.ASTERISK)
                    if (aReader.peek ().getTokenStartType () == ECSSTokenStartType.SOLIDUS)
                    {
                      aReader.read ();
                      break;
                    }
                }
                aToken = aReader.createToken (ECSSTokenType.COMMENT);
              }
              else
                aToken = aReader.createToken (ECSSTokenType.DELIM);
              break;
            default:
              if (false)
                throw new IllegalStateException ("Unsupported token start type " + eTokenStartType);
              System.err.println ("Unsupported token start type " + eTokenStartType);
          }

          if (aToken != null)
            aConsumer.accept (aToken);
        }
      }
    }
  }

  public static void main (final String [] args) throws IOException, CSSTokenizeException
  {
    final File f = new File ("src/test/resources/testfiles/css30/good/pure-min.css");
    try (InputStream aIS = StreamHelper.getBuffered (FileHelper.getInputStream (f)))
    {
      new CSSTokenizer ().setDebugMode (false).tokenize (aIS, t -> {
        System.out.println (t);
      });
    }
  }
}
