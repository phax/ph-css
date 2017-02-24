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
package com.helger.css.parser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.ToStringGenerator;

/**
 * This class has the same layout as the normal {@link Token} class, but its
 * contents are not modifiable!
 *
 * @author Philip Helger
 */
@Immutable
public class ReadOnlyToken
{
  private final int m_nKind;
  private final int m_nBeginLine;
  private final int m_nBeginColumn;
  private final int m_nEndLine;
  private final int m_nEndColumn;
  private final String m_sImage;

  public ReadOnlyToken (@Nonnull final Token aToken)
  {
    ValueEnforcer.notNull (aToken, "Token");

    m_nKind = aToken.kind;
    m_nBeginLine = aToken.beginLine;
    m_nBeginColumn = aToken.beginColumn;
    m_nEndLine = aToken.endLine;
    m_nEndColumn = aToken.endColumn;
    m_sImage = aToken.image;
  }

  /**
   * @return The internal index identifying this token.
   */
  public int getKind ()
  {
    return m_nKind;
  }

  /**
   * @return Line number where the token starts
   */
  public int getBeginLine ()
  {
    return m_nBeginLine;
  }

  /**
   * @return Column number where the token starts
   */
  public int getBeginColumn ()
  {
    return m_nBeginColumn;
  }

  /**
   * @return Line number where the token ends
   */
  public int getEndLine ()
  {
    return m_nEndLine;
  }

  /**
   * @return Column number where the token ends
   */
  public int getEndColumn ()
  {
    return m_nEndColumn;
  }

  /**
   * @return The string representation of the token. May be <code>null</code>
   *         but should not.
   */
  @Nullable
  public String getImage ()
  {
    return m_sImage;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("kind", m_nKind)
                                       .append ("beginLine", m_nBeginLine)
                                       .append ("beginColumn", m_nBeginColumn)
                                       .append ("endLine", m_nEndLine)
                                       .append ("endColumn", m_nEndColumn)
                                       .append ("image", m_sImage)
                                       .getToString ();
  }
}
