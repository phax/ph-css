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

/**
 * This enum represents the W3C standard media query expression features.
 *
 * @author Philip Helger
 */
public enum ECSSMediaExpressionFeature implements IHasName
{
  WIDTH ("width"),
  MIN_WIDTH ("min-width"),
  MAX_WIDTH ("max-width"),
  HEIGHT ("height"),
  MIN_HEIGHT ("min-height"),
  MAX_HEIGHT ("max-height"),
  DEVICE_WIDTH ("device-width"),
  MIN_DEVICE_WIDTH ("min-device-width"),
  MAX_DEVICE_WIDTH ("max-device-width"),
  DEVICE_HEIGHT ("device-height"),
  MIN_DEVICE_HEIGHT ("min-device-height"),
  MAX_DEVICE_HEIGHT ("max-device-height"),
  ORIENTATION ("orientation"),
  ASPECT_RATIO ("aspect-ratio"),
  MIN_ASPECT_RATIO ("min-aspect-ratio"),
  MAX_ASPECT_RATIO ("max-aspect-ratio"),
  DEVICE_ASPECT_RATIO ("device-aspect-ratio"),
  MIN_DEVICE_ASPECT_RATIO ("min-device-aspect-ratio"),
  MAX_DEVICE_ASPECT_RATIO ("max-device-aspect-ratio"),
  COLOR ("color"),
  MIN_COLOR ("min-color"),
  MAX_COLOR ("max-color"),
  COLOR_INDEX ("color-index"),
  MIN_COLOR_INDEX ("min-color-index"),
  MAX_COLOR_INDEX ("max-color-index"),
  MONOCHROME ("monochrome"),
  MIN_MONOCHROME ("min-monochrome"),
  MAX_MONOCHROME ("max-monochrome"),
  RESOLUTION ("resolution"),
  MIN_RESOLUTION ("min-resolution"),
  MAX_RESOLUTION ("max-resolution"),
  SCAN ("scan"),
  GRID ("grid"),
  MAX_DEVICE_PIXEL_RATIO ("max-device-pixel-ratio"),
  MIN_DEVICE_PIXEL_RATIO ("min-device-pixel-ratio"),
  TRANSFORM_3D ("transform-3d"),
  _WEBKIT_DEVICE_PIXEL_RATIO ("-webkit-device-pixel-ratio"),
  _WEBKIT_MAX_DEVICE_PIXEL_RATIO ("-webkit-max-device-pixel-ratio"),
  _WEBKIT_MIN_DEVICE_PIXEL_RATIO ("-webkit-min-device-pixel-ratio"),
  _WEBKIT_TRANSFORM_3D ("-webkit-transform-3d"),
  _MS_HIGH_CONTRAST ("-ms-high-contrast"),
  _MS_VIEW_STATE ("-ms-view-state"),
  _MOZ_MAX_DEVICE_PIXEL_RATIO ("-moz-max-device-pixel-ratio"),
  _MOZ_MIN_DEVICE_PIXEL_RATIO ("-moz-min-device-pixel-ratio"),
  MAX_MOZ_DEVICE_PIXEL_RATION ("max--moz-device-pixel-ratio"),
  MIN_MOZ_DEVICE_PIXEL_RATION ("min--moz-device-pixel-ratio"),
  _MOZ_IMAGES_IN_MENUS ("-moz-images-in-menus"),
  _MOZ_MAC_GRAPHITE_THEME ("-moz-mac-graphite-theme"),
  _MOZ_MAEMO_CLASSIC ("-moz-maemo-classic"),
  _MOZ_DEVICE_PIXEL_RATIO ("-moz-device-pixel-ratio"),
  _MOZ_OS_VERSION ("-moz-os-version"),
  _MOZ_SCROLLBAR_END_BACKWARD ("-moz-scrollbar-end-backward"),
  _MOZ_SCROLLBAR_END_FORWARD ("-moz-scrollbar-end-forward"),
  _MOZ_SCROLLBAR_START_BACKWARD ("-moz-scrollbar-start-backward"),
  _MOZ_SCROLLBAR_START_FORWARD ("-moz-scrollbar-start-forward"),
  _MOZ_SCROLLBAR_THUMB_PROPORTIONAL ("-moz-scrollbar-thumb-proportional"),
  _MOZ_TOUCH_ENABLED ("-moz-touch-enabled"),
  _MOZ_WINDOWS_CLASSIC ("-moz-windows-classic"),
  _MOZ_WINDOWS_COMPOSITOR ("-moz-windows-compositor"),
  _MOZ_WINDOWS_DEFAULT_THEME ("-moz-windows-default-theme"),
  _MOZ_WINDOWS_GLASS ("-moz-windows-glass"),
  _MOZ_WINDOWS_THEME ("-moz-windows-theme");

  private final String m_sName;

  private ECSSMediaExpressionFeature (@Nonnull @Nonempty final String sName)
  {
    m_sName = sName;
  }

  /**
   * @return the CSS media expression name. Never <code>null</code> nor empty.
   */
  @Nonnull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  /**
   * @return <code>true</code> if this media expression feature is WebKit
   *         specific, <code>false</code> if not.
   */
  public boolean isWebkitSpecific ()
  {
    return m_sName.startsWith ("-webkit-");
  }

  /**
   * @return <code>true</code> if this media expression feature is Mozilla
   *         specific, <code>false</code> if not.
   */
  public boolean isMozillaSpecific ()
  {
    return m_sName.contains ("-moz-");
  }

  /**
   * @return <code>true</code> if this media expression feature is Microsoft
   *         specific, <code>false</code> if not.
   */
  public boolean isMicrosoftSpecific ()
  {
    return m_sName.startsWith ("-ms-");
  }

  /**
   * @return <code>true</code> if this media expression feature is browser
   *         specific, <code>false</code> if not.
   */
  public boolean isBrowserSpecific ()
  {
    return m_sName.startsWith ("-") || m_sName.contains ("--");
  }

  @Nullable
  public static ECSSMediaExpressionFeature getFromNameOrNull (@Nullable final String sName)
  {
    return EnumHelper.getFromNameOrNull (ECSSMediaExpressionFeature.class, sName);
  }
}
