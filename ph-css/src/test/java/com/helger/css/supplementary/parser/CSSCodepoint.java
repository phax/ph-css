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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.text.codepoint.Codepoint;

public final class CSSCodepoint extends Codepoint
{
  private final int m_nCharCount;
  // Lazily initialized
  private ECSSTokenStartType m_eTokenStartType;

  /**
   * Private constructor for EOF.
   */
  private CSSCodepoint ()
  {
    // -1 is an invalid codepoint!
    super (-1, true);
    m_nCharCount = 1;
    m_eTokenStartType = ECSSTokenStartType.EOF;
  }

  public CSSCodepoint (@Nonnegative final int nValue)
  {
    super (nValue);
    m_nCharCount = 1;
  }

  public CSSCodepoint (@Nonnegative final char cHigh, @Nonnegative final char cLow)
  {
    super (cHigh, cLow);
    m_nCharCount = 2;
  }

  public boolean isSingleChar ()
  {
    return m_nCharCount == 1;
  }

  public boolean isEOF ()
  {
    return getValue () == -1;
  }

  private static boolean inRange (final int nCodepoint, final int nLow, final int nHigh)
  {
    return nCodepoint >= nLow && nCodepoint <= nHigh;
  }

  @Override
  public boolean isDigit ()
  {
    return inRange (getValue (), '0', '9');
  }

  public boolean isHexDigit ()
  {
    final int nValue = getValue ();
    return isDigit () || inRange (nValue, 'A', 'F') || inRange (nValue, 'a', 'f');
  }

  public boolean isUppercaseLetter ()
  {
    return inRange (getValue (), 'A', 'Z');
  }

  public boolean isLowercaseLetter ()
  {
    return inRange (getValue (), 'a', 'z');
  }

  public boolean isLetter ()
  {
    return isUppercaseLetter () || isLowercaseLetter ();
  }

  public boolean isNonASCII ()
  {
    return getValue () >= 0x80;
  }

  public boolean isNameStart ()
  {
    return isLetter () || isNonASCII () || getValue () == '_';
  }

  public boolean isName ()
  {
    return isNameStart () || isDigit () || getValue () == '-';
  }

  public boolean isNonPrintable ()
  {
    final int nValue = getValue ();
    return inRange (nValue, 0, 8) || nValue == '\t' || inRange (nValue, 0x0e, 0x1f);
  }

  public boolean isNewline ()
  {
    return getValue () == '\n';
  }

  public boolean isWhitespace ()
  {
    final int nValue = getValue ();
    return isNewline () || nValue == '\t' || nValue == ' ';
  }

  @Override
  public void appendTo (@Nonnull final StringBuilder aSB)
  {
    final int nValue = getValue ();
    if (isSingleChar ())
    {
      aSB.append ((char) nValue);
    }
    else
    {
      aSB.append (Character.highSurrogate (nValue));
      aSB.append (Character.lowSurrogate (nValue));
    }
  }

  @Nonnull
  private ECSSTokenStartType _findTokenStartType ()
  {
    final int nValue = getValue ();
    switch (nValue)
    {
      // \r and \f is handled by the CSSInputStream!
      case '\n':
      case '\t':
      case ' ':
        return ECSSTokenStartType.WHITESPACE;
      case '"':
        return ECSSTokenStartType.QUOTATION_MARK;
      case '#':
        return ECSSTokenStartType.NUMBER_SIGN;
      case '$':
        return ECSSTokenStartType.DOLLAR_SIGN;
      case '\'':
        return ECSSTokenStartType.APOSTROPHE;
      case '(':
        return ECSSTokenStartType.LEFT_PARENTHESIS;
      case ')':
        return ECSSTokenStartType.RIGHT_PARENTHESIS;
      case '*':
        return ECSSTokenStartType.ASTERISK;
      case '+':
        return ECSSTokenStartType.PLUS_SIGN;
      case ',':
        return ECSSTokenStartType.COMMA;
      case '-':
        return ECSSTokenStartType.HYPHEN_MINUS;
      case '.':
        return ECSSTokenStartType.FULL_STOP;
      case '/':
        return ECSSTokenStartType.SOLIDUS;
      case ':':
        return ECSSTokenStartType.COLON;
      case ';':
        return ECSSTokenStartType.SEMICOLON;
      case '<':
        return ECSSTokenStartType.LESS_THAN_SIGN;
      case '@':
        return ECSSTokenStartType.COMMERCIAL_AT;
      case '[':
        return ECSSTokenStartType.LEFT_SQUARE_BRACKET;
      case '\\':
        return ECSSTokenStartType.REVERSE_SOLIDUS;
      case ']':
        return ECSSTokenStartType.RIGHT_SQUARE_BRACKET;
      case '^':
        return ECSSTokenStartType.CIRCUMFLEX_ACCENT;
      case '{':
        return ECSSTokenStartType.LEFT_CURLY_BRACKET;
      case '}':
        return ECSSTokenStartType.RIGHT_CURLY_BRACKET;
      case '|':
        return ECSSTokenStartType.VERTICAL_LINE;
      case '~':
        return ECSSTokenStartType.TILDE;
      case -1:
        return ECSSTokenStartType.EOF;
    }
    if (nValue >= '0' && nValue <= '9')
      return ECSSTokenStartType.DIGIT;
    if ((nValue >= 'a' && nValue <= 'z') || (nValue >= 'A' && nValue <= 'Z') || nValue == '_' || nValue > 0x80)
      return ECSSTokenStartType.NAME_START;
    return ECSSTokenStartType.ANYTHING_ELSE;
  }

  @Nonnull
  public ECSSTokenStartType getTokenStartType ()
  {
    ECSSTokenStartType ret = m_eTokenStartType;
    if (ret == null)
    {
      ret = _findTokenStartType ();
      m_eTokenStartType = ret;
    }
    return ret;
  }

  @Nonnull
  public static CSSCodepoint createEOF ()
  {
    return new CSSCodepoint ();
  }
}
