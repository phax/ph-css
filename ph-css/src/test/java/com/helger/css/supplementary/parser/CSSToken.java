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

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.string.ToStringGenerator;

public class CSSToken
{
  private final int m_nStartLine;
  private final int m_nStartColumn;
  private final int m_nEndLine;
  private final int m_nEndColumn;
  private final String m_sImage;
  private final ECSSTokenType m_eType;

  public CSSToken (@Nonnegative final int nStartLine,
                   @Nonnegative final int nStartColumn,
                   @Nonnegative final int nEndLine,
                   @Nonnegative final int nEndColumn,
                   @Nonnull @Nonempty final String sImage,
                   @Nonnull final ECSSTokenType eType)
  {
    m_nStartLine = nStartLine;
    m_nStartColumn = nStartColumn;
    m_nEndLine = nEndLine;
    m_nEndColumn = nEndColumn;
    m_sImage = sImage;
    m_eType = eType;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("StartLine", m_nStartLine)
                                       .append ("StartCol", m_nStartColumn)
                                       .append ("EndLine", m_nEndLine)
                                       .append ("EndCol", m_nEndColumn)
                                       .append ("Type", m_eType)
                                       .append ("Image", m_sImage)
                                       .getToString ();
  }
}
