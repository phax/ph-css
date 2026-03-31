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
import com.helger.annotation.Nonnegative;
import com.helger.base.lang.EnumHelper;
import com.helger.base.name.IHasName;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;

/**
 * Enumeration with selector attribute operations as in <code>[foo<b>^=</b>"bar"]</code>
 *
 * @author Philip Helger
 */
public enum ECSSAttributeOperator implements ICSSWriteable, IHasName
{
  EQUALS ("="),
  INCLUDES ("~="),
  DASHMATCH ("|="),
  BEGINMATCH ("^="),
  ENDMATCH ("$="),
  CONTAINSMATCH ("*=");

  private final String m_sName;

  ECSSAttributeOperator (@NonNull @Nonempty final String sName)
  {
    m_sName = sName;
  }

  @NonNull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return m_sName;
  }

  @Nullable
  public static ECSSAttributeOperator getFromNameOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameOrNull (ECSSAttributeOperator.class, sName);
  }
}
