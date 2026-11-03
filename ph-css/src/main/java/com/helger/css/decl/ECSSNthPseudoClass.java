/*
 * Copyright (C) 2014-2026 Philip Helger (www.helger.com)
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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.base.lang.EnumHelper;
import com.helger.base.name.IHasName;

/**
 * Contains the pseudo classes that take an <code>An+B</code> argument, as in
 * <code><b>:nth-child(</b>2n+1<b>)</b></code>.
 *
 * @author Philip Helger
 * @since 8.4.0
 */
public enum ECSSNthPseudoClass implements IHasName
{
  NTH_CHILD ("nth-child"),
  NTH_LAST_CHILD ("nth-last-child"),
  NTH_OF_TYPE ("nth-of-type"),
  NTH_LAST_OF_TYPE ("nth-last-of-type");

  private final String m_sName;

  ECSSNthPseudoClass (@NonNull @Nonempty final String sName)
  {
    m_sName = sName;
  }

  /**
   * @return The pseudo class name without the leading colon and without the opening parenthesis.
   *         Neither <code>null</code> nor empty.
   */
  @NonNull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nullable
  public static ECSSNthPseudoClass getFromNameCaseInsensitiveOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameCaseInsensitiveOrNull (ECSSNthPseudoClass.class, sName);
  }
}
