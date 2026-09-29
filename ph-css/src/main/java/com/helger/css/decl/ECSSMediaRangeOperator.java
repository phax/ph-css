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
 * Enumeration with the comparison operators of the Media Queries Level 4 range context as in
 * <code>(width <b>&gt;=</b> 600px)</code> or <code>(400px <b>&lt;=</b> width <b>&lt;=</b> 600px)</code>.
 *
 * @see <a href="https://www.w3.org/TR/mediaqueries-4/#mq-range-context">Media Queries Level 4 -
 *      Range context</a>
 * @since 8.1.2-unblu-4
 */
public enum ECSSMediaRangeOperator implements ICSSWriteable, IHasName
{
  LESS ("<"),
  LESS_EQUALS ("<="),
  GREATER (">"),
  GREATER_EQUALS (">="),
  EQUALS ("=");

  private final String m_sName;

  ECSSMediaRangeOperator (@NonNull @Nonempty final String sName)
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
  public static ECSSMediaRangeOperator getFromNameOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameOrNull (ECSSMediaRangeOperator.class, sName);
  }
}
