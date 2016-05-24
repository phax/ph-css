package com.helger.css.supplementary.parser;

public enum ECSSTokenType
{
  WHITESPACE (false),
  QUOTATION_MARK (false),
  NUMBER_SIGN (false),
  DOLLAR_SIGN (false),
  APOSTROPHE (false),
  LEFT_PARENTHESIS (true),
  RIGHT_PARENTHESIS (true),
  ASTERISK (false),
  PLUS_SIGN (false),
  COMMA (true),
  HYPHEN_MINUS (false),
  FULL_STOP (false),
  SOLIDUS (false),
  COLON (true),
  SEMICOLON (true),
  LESS_THAN_SIGN (false),
  COMMERCIAL_AT (false),
  LEFT_SQUARE_BRACKET (true),
  REVERSE_SOLIDUS (false),
  RIGHT_SQUARE_BRACKET (true),
  CIRCUMFLEX_ACCENT (false),
  LEFT_CURLY_BRACKET (true),
  RIGHT_CURLY_BRACKET (true),
  DIGIT (false),
  NAME_START (false),
  VERTICAL_LINE (false),
  TILDE (false),
  EOF (true),
  ANYTHING_ELSE (true);

  private boolean m_bSelfContained;

  private ECSSTokenType (final boolean bSelfContained)
  {
    m_bSelfContained = bSelfContained;
  }

  public boolean isSelfContained ()
  {
    return m_bSelfContained;
  }
}
