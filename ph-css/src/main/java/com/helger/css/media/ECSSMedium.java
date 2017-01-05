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
package com.helger.css.media;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.lang.EnumHelper;
import com.helger.commons.name.IHasName;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSVersionAware;
import com.helger.css.annotation.DeprecatedInCSS21;

/**
 * Defines all default CSS media types.
 *
 * @author Philip Helger
 */
public enum ECSSMedium implements ICSSVersionAware,IHasName
{
  /** for all media types */
  ALL ("all", ECSSVersion.CSS21),

  /**
   * For computer synthesized voice. Deprecated in CSS 2.1. Is "speech" in CSS
   * 3.
   */
  @DeprecatedInCSS21 AURAL ("aural", ECSSVersion.CSS21),

  /** for blind people */
  BRAILLE ("braille", ECSSVersion.CSS21),

  /** for blind people */
  EMBOSSED ("embossed", ECSSVersion.CSS21),

  /** for PDAs */
  HANDHELD ("handheld", ECSSVersion.CSS21),

  /** for printing */
  PRINT ("print", ECSSVersion.CSS10),

  /** for projection */
  PROJECTION ("projection", ECSSVersion.CSS21),

  /** for normal screen display */
  SCREEN ("screen", ECSSVersion.CSS10),

  /** For computer synthesized voice. */
  SPEECH ("speech", ECSSVersion.CSS21),

  /** for text oriented devices */
  TTY ("tty", ECSSVersion.CSS21),

  /** for televisions */
  TV ("tv", ECSSVersion.CSS21);

  private final String m_sName;
  private final ECSSVersion m_eVersion;

  private ECSSMedium (@Nonnull @Nonempty final String sName, @Nonnull final ECSSVersion eVersion)
  {
    m_sName = sName;
    m_eVersion = eVersion;
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

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    return m_eVersion;
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
