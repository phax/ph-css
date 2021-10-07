/*
 * Copyright (C) 2014-2021 Philip Helger (www.helger.com)
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
package com.helger.css;

import java.io.Serializable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.css.writer.CSSWriterSettings;

/**
 * Interface for objects that can be written to CSS.
 *
 * @author Philip Helger
 */
public interface ICSSWriteable extends Serializable
{
  /**
   * Get the contents of this object as a serialized CSS string for writing to
   * an output using the default writer settings.
   *
   * @return The content of this object as CSS string. Never <code>null</code>.
   * @see #getAsCSSString(ICSSWriterSettings, int)
   * @since 6.0.0
   */
  @Nonnull
  default String getAsCSSString ()
  {
    return getAsCSSString (CSSWriterSettings.DEFAULT_SETTINGS);
  }

  /**
   * Get the contents of this object as a serialized CSS string for writing to
   * an output.
   *
   * @param aSettings
   *        The settings to be used to format the output. May not be
   *        <code>null</code>.
   * @return The content of this object as CSS string. Never <code>null</code>.
   * @see #getAsCSSString(ICSSWriterSettings, int)
   * @since 5.0.4
   */
  @Nonnull
  default String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings)
  {
    return getAsCSSString (aSettings, 0);
  }

  /**
   * Get the contents of this object as a serialized CSS string for writing to
   * an output.
   *
   * @param aSettings
   *        The settings to be used to format the output. May not be
   *        <code>null</code>.
   * @param nIndentLevel
   *        The current indentation level
   * @return The content of this object as CSS string. Never <code>null</code>.
   */
  @Nonnull
  String getAsCSSString (@Nonnull ICSSWriterSettings aSettings, @Nonnegative int nIndentLevel);
}
