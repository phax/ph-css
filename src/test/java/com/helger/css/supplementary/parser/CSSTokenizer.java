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

  public CSSTokenizer ()
  {
    this (CCharset.CHARSET_UTF_8_OBJ);
  }

  public CSSTokenizer (@Nonnull final Charset aFallbackEncoding)
  {
    m_aFallbackEncoding = ValueEnforcer.notNull (aFallbackEncoding, "FallbackEncoding");
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
      if (CHARSET.equalsIgnoreCase (sPrefix))
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

  private static ECSSTokenType _findTokenType (final char c)
  {
    switch (c)
    {
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
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
        return ECSSTokenType.DIGIT;
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'l':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      case 's':
      case 't':
      case 'u':
      case 'v':
      case 'w':
      case 'x':
      case 'y':
      case 'z':
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'I':
      case 'J':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'S':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case 'Z':
      case '_':
        return ECSSTokenType.NAME_START;
      case '|':
        return ECSSTokenType.VERTICAL_LINE;
      case '~':
        return ECSSTokenType.TILDE;
      case (char) -1:
        return ECSSTokenType.EOF;
      default:
        if (c > 0x80)
          return ECSSTokenType.NAME_START;
        return ECSSTokenType.ANYTHING_ELSE;
    }
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
        ECSSTokenType eTokenType;
        do
        {
          final int nLine = aReader.getLineNumber ();
          final int nCol = aReader.getColumnNumber ();
          final char c = aReader.readChar ();
          System.out.println ("[" +
                              nLine +
                              ":" +
                              nCol +
                              "] - " +
                              (c >= 0x20 && c <= 0x7f ? Character.toString (c) : "0x" + Integer.toHexString (c)));
          eTokenType = _findTokenType (c);
        } while (eTokenType != ECSSTokenType.EOF);
      }
    }
  }

  public static void main (final String [] args) throws IOException, CSSTokenizeException
  {
    final File f = new File ("src/test/resources/testfiles/css30/good/test-charset_utf8.css");
    try (InputStream aIS = StreamHelper.getBuffered (FileHelper.getInputStream (f)))
    {
      new CSSTokenizer ().tokenize (aIS, t -> {});
    }
  }
}
