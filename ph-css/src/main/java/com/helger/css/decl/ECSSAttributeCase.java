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
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Enumeration with case sensitivity flags as in <code>[foo="bar" i]</code> or
 * <code>[foo="bar" s]</code>. See
 * https://developer.mozilla.org/en-US/docs/Web/CSS/Attribute_selectors#s for the spec.
 *
 * @author Aditi Singh
 * @since 8.0.1
 */
public enum ECSSAttributeCase implements ICSSWriteable, IHasName
{
  CASE_SENSITIVE ("s"),
  CASE_INSENSITIVE ("i");

  private final String m_sName;

  ECSSAttributeCase (@Nonnull @Nonempty final String sName)
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
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return m_sName;
  }

  @Nullable
  public static ECSSAttributeCase getFromNameOrNull (@Nullable final String sName)
  {
    // The check in the grammar is also case-insensitive
    return EnumHelper.getFromNameCaseInsensitiveOrNull (ECSSAttributeCase.class, sName);
  }
}
