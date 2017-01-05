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

public enum ECSSTokenStartType
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

  private ECSSTokenStartType (final boolean bSelfContained)
  {
    m_bSelfContained = bSelfContained;
  }

  public boolean isSelfContained ()
  {
    return m_bSelfContained;
  }
}
