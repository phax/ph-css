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
 * Contains the functions with a single argument that can be used in an @supports condition, as in
 * <code>@supports <b>font-tech(</b>color-COLRv1<b>)</b></code>. The <code>selector()</code>
 * function is handled separately by {@link CSSSupportsConditionSelector}.
 *
 * @author Philip Helger
 * @since 8.4.0
 */
public enum ECSSSupportsFunction implements IHasName
{
  FONT_TECH ("font-tech"),
  FONT_FORMAT ("font-format"),
  AT_RULE ("at-rule"),
  NAMED_FEATURE ("named-feature"),
  ENV ("env");

  private final String m_sName;

  ECSSSupportsFunction (@NonNull @Nonempty final String sName)
  {
    m_sName = sName;
  }

  /**
   * @return The function name without the opening parenthesis. Neither <code>null</code> nor
   *         empty.
   */
  @NonNull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nullable
  public static ECSSSupportsFunction getFromNameCaseInsensitiveOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameCaseInsensitiveOrNull (ECSSSupportsFunction.class, sName);
  }
}
