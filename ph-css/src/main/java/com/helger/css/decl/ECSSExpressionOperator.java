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
import com.helger.css.ICSSWriterSettings;

/**
 * Enumeration with expression operators. E.g. used in
 * <code>rgb(1<b>,</b>2<b>,</b>3)</code>
 *
 * @author Philip Helger
 */
public enum ECSSExpressionOperator implements ICSSExpressionMember,IHasName
{
  SLASH ("/"),
  COMMA (","),
  EQUALS ("=");

  private final String m_sName;

  private ECSSExpressionOperator (@Nonnull @Nonempty final String sName)
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

  @Nonnull
  public ECSSExpressionOperator getClone ()
  {
    // No possibility to clone :)
    return this;
  }

  @Nullable
  public static ECSSExpressionOperator getFromNameOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameOrNull (ECSSExpressionOperator.class, sName);
  }
}
