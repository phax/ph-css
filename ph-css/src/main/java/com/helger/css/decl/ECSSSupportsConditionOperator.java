/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.base.lang.EnumHelper;
import com.helger.base.name.IHasName;
import com.helger.css.ICSSWriterSettings;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Contains the binary operators for the @supports rule as in
 * <code>@supports (transition-property: color) <b>or</b> (animation-name: foo)</code>
 *
 * @author Philip Helger
 */
public enum ECSSSupportsConditionOperator implements ICSSSupportsConditionMember, IHasName
{
  AND ("and"),
  OR ("or");

  private final String m_sName;

  ECSSSupportsConditionOperator (@Nonnull @Nonempty final String sName)
  {
    m_sName = sName;
  }

  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nonnull
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return m_sName;
  }

  @Nullable
  public static ECSSSupportsConditionOperator getFromNameCaseInsensitiveOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameCaseInsensitiveOrNull (ECSSSupportsConditionOperator.class, sName);
  }
}
