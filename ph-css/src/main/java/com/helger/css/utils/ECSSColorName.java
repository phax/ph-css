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
package com.helger.css.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.lang.EnumHelper;
import com.helger.css.annotation.DeprecatedInCSS30;

/**
 * Contains a list of predefined color values in CSS 2.1.<br>
 * These names are deprecated in CSS 3.0 in favour of the appearance property.
 * <br>
 * Source: http://www.w3.org/TR/css3-color/ chapter 4.5.1
 *
 * @author Philip Helger
 */
@DeprecatedInCSS30
public enum ECSSColorName implements ICSSNamedColor
{
  ACTIVEBORDER ("ActiveBorder"),
  ACTIVECAPTION ("ActiveCaption"),
  APPWORKSPACE ("AppWorkspace"),
  BACKGROUND ("Background"),
  BUTTONFACE ("ButtonFace"),
  BUTTONHIGHLIGHT ("ButtonHighlight"),
  BUTTONSHADOW ("ButtonShadow"),
  BUTTONTEXT ("ButtonText"),
  CAPTIONTEXT ("CaptionText"),
  GRAYTEXT ("GrayText"),
  HIGHLIGHT ("Highlight"),
  HIGHLIGHTTEXT ("HighlightText"),
  INACTIVEBORDER ("InactiveBorder"),
  INACTIVECAPTION ("InactiveCaption"),
  INACTIVECAPTIONTEXT ("InactiveCaptionText"),
  INFOBACKGROUND ("InfoBackground"),
  INFOTEXT ("InfoText"),
  MENU ("Menu"),
  MENUTEXT ("MenuText"),
  SCROLLBAR ("Scrollbar"),
  THREEDDARKSHADOW ("ThreeDDarkShadow"),
  THREEDFACE ("ThreeDFace"),
  THREEDHIGHLIGHT ("ThreeDHighlight"),
  THREEDLIGHTSHADOW ("ThreeDLightShadow"),
  THREEDSHADOW ("ThreeDShadow"),
  WINDOW ("Window"),
  WINDOWFRAME ("WindowFrame"),
  WINDOWTEXT ("WindowText");

  private final String m_sName;

  private ECSSColorName (@Nonnull @Nonempty final String sName)
  {
    m_sName = sName;
  }

  /**
   * @return The name of the color as to be used in CSS. Neither
   *         <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  @Nullable
  public static ECSSColorName getFromNameCaseInsensitiveOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameCaseInsensitiveOrNull (ECSSColorName.class, sName);
  }

  /**
   * Check if the passed color name is a default color name.
   *
   * @param sName
   *        The color name to check.
   * @return <code>true</code> if the passed color name is a default color name,
   *         <code>false</code> if not.
   */
  public static boolean isDefaultColorName (@Nullable final String sName)
  {
    return getFromNameCaseInsensitiveOrNull (sName) != null;
  }
}
