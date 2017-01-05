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
package com.helger.css.decl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.lang.EnumHelper;
import com.helger.commons.name.IHasName;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents the different math operators available.
 *
 * @author Philip Helger
 */
public enum ECSSMathOperator implements ICSSExpressionMathMember,IHasName
{
  // plus and minus require a whitespace around it when writing!
  PLUS ("+", " + "),
  MINUS ("-", " - "),
  MULTIPLY ("*", "*"),
  DIVIDE ("/", "/");

  private final String m_sName;
  private final String m_sText;

  private ECSSMathOperator (@Nonnull @Nonempty final String sName, @Nonnull @Nonempty final String sText)
  {
    m_sName = sName;
    m_sText = sText;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    return ECSSVersion.CSS30;
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);
    return m_sText;
  }

  @Nullable
  public static ECSSMathOperator getFromNameOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameOrNull (ECSSMathOperator.class, sName);
  }
}
