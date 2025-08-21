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
package com.helger.css.media;

import com.helger.annotation.Nonempty;
import com.helger.base.lang.EnumHelper;
import com.helger.base.name.IHasName;
import com.helger.css.annotation.DeprecatedInCSS21;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Defines all default CSS media types.
 *
 * @author Philip Helger
 */
public enum ECSSMedium implements IHasName
{
  /** for all media types */
  ALL ("all"),

  /**
   * For computer synthesized voice. Deprecated in CSS 2.1. Is "speech" in CSS 3.
   */
  @DeprecatedInCSS21
  AURAL ("aural"),

  /** for blind people */
  BRAILLE ("braille"),

  /** for blind people */
  EMBOSSED ("embossed"),

  /** for PDAs */
  HANDHELD ("handheld"),

  /** for printing */
  PRINT ("print"),

  /** for projection */
  PROJECTION ("projection"),

  /** for normal screen display */
  SCREEN ("screen"),

  /** For computer synthesized voice. */
  SPEECH ("speech"),

  /** for text oriented devices */
  TTY ("tty"),

  /** for televisions */
  TV ("tv");

  private final String m_sName;

  ECSSMedium (@Nonnull @Nonempty final String sName)
  {
    m_sName = sName;
  }

  /**
   * @return the CSS medium name. Never <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nullable
  public static ECSSMedium getFromNameOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameOrNull (ECSSMedium.class, sName);
  }

  @Nullable
  public static ECSSMedium getFromNameOrDefault (@Nullable final String sName, @Nullable final ECSSMedium eDefault)
  {
    return EnumHelper.getFromNameOrDefault (ECSSMedium.class, sName, eDefault);
  }
}
