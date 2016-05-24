/**
 * Copyright (C) 2014-2016 Philip Helger (www.helger.com)
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

  private boolean m_bStrictMode = false;
  private final Charset m_aFallbackEncoding;

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

  private static ECSSTokenType _findTokenType (final int nCP)
  {
    switch (nCP)
    {
      // \r and \f is handled by the CSSInputStream!
      case '\n':
      case '\t':
      case ' ':
        return ECSSTokenType.WHITESPACE;
      case '"':
        return ECSSTokenType.QUOTATION_MARK;
      case '#':
        return ECSSTokenType.NUMBER_SIGN;
      case '$':
        return ECSSTokenType.DOLLAR_SIGN;
      case '\'':
        return ECSSTokenType.APOSTROPHE;
      case '(':
        return ECSSTokenType.LEFT_PARENTHESIS;
      case ')':
        return ECSSTokenType.RIGHT_PARENTHESIS;
      case '*':
        return ECSSTokenType.ASTERISK;
      case '+':
        return ECSSTokenType.PLUS_SIGN;
      case ',':
        return ECSSTokenType.COMMA;
      case '-':
        return ECSSTokenType.HYPHEN_MINUS;
      case '.':
        return ECSSTokenType.FULL_STOP;
      case '/':
        return ECSSTokenType.SOLIDUS;
      case ':':
        return ECSSTokenType.COLON;
      case ';':
        return ECSSTokenType.SEMICOLON;
      case '<':
        return ECSSTokenType.LESS_THAN_SIGN;
      case '@':
        return ECSSTokenType.COMMERCIAL_AT;
      case '[':
        return ECSSTokenType.LEFT_SQUARE_BRACKET;
      case '\\':
        return ECSSTokenType.REVERSE_SOLIDUS;
      case ']':
        return ECSSTokenType.RIGHT_SQUARE_BRACKET;
      case '^':
        return ECSSTokenType.CIRCUMFLEX_ACCENT;
      case '{':
        return ECSSTokenType.LEFT_CURLY_BRACKET;
      case '}':
        return ECSSTokenType.RIGHT_CURLY_BRACKET;
      case '|':
        return ECSSTokenType.VERTICAL_LINE;
      case '~':
        return ECSSTokenType.TILDE;
      case -1:
        return ECSSTokenType.EOF;
    }
    if (nCP >= '0' && nCP <= '9')
      return ECSSTokenType.DIGIT;
    if ((nCP >= 'a' && nCP <= 'z') || (nCP >= 'A' && nCP <= 'Z') || nCP == '_' || nCP > 0x80)
      return ECSSTokenType.NAME_START;
    return ECSSTokenType.ANYTHING_ELSE;
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
          final int nStartLine = aReader.getLineNumber ();
          final int nStartCol = aReader.getColumnNumber ();
          final int nCP = aReader.read ();
          System.out.println ("[" +
                              nStartLine +
                              ":" +
                              nStartCol +
                              "] - " +
                              (nCP >= 0x20 && nCP <= 0x7f ? Character.toString ((char) nCP)
                                                          : "0x" + Integer.toHexString (nCP)));
          final ECSSTokenType eTokenType = _findTokenType (nCP);
          if (eTokenType == ECSSTokenType.EOF)
            break;
        }
      }
    }
  }

  public static void main (final String [] args) throws IOException, CSSTokenizeException
  {
    final File f = new File ("src/test/resources/testfiles/css30/good/pure-min.css");
    try (InputStream aIS = StreamHelper.getBuffered (FileHelper.getInputStream (f)))
    {
      new CSSTokenizer ().tokenize (aIS, t -> {});
    }
  }
}
