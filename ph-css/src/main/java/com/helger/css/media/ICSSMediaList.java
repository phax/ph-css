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

import java.io.Serializable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.ICommonsOrderedSet;

/**
 * A read only version of a CSS media list
 *
 * @author Philip Helger
 */
public interface ICSSMediaList extends Serializable
{
  /**
   * @return The number of contained media. Always &ge; 0.
   */
  @Nonnegative
  int getMediaCount ();

  /**
   * @return <code>true</code> if any explicit media is defined,
   *         <code>false</code> if not.
   */
  default boolean hasAnyMedia ()
  {
    return !hasNoMedia ();
  }

  /**
   * @return <code>true</code> if no explicit media is defined,
   *         <code>false</code> if a media is defined.
   */
  boolean hasNoMedia ();

  /**
   * @return <code>true</code> if no explicit media is defined or if
   *         {@link ECSSMedium#ALL} is contained.
   */
  default boolean hasNoMediaOrAll ()
  {
    return hasNoMedia () || containsMedium (ECSSMedium.ALL);
  }

  /**
   * Check if the passed medium is explicitly specified
   *
   * @param eMedium
   *        The medium to be checked. May be <code>null</code>.
   * @return <code>true</code> if it is contained, <code>false</code> otherwise
   */
  boolean containsMedium (@Nullable ECSSMedium eMedium);

  /**
   * Check if the passed medium or the {@link ECSSMedium#ALL} is explicitly
   * specified
   *
   * @param eMedium
   *        The medium to be checked. May be <code>null</code>.
   * @return <code>true</code> if the passed medium or the "all" medium is
   *         contained, <code>false</code> otherwise
   */
  default boolean containsMediumOrAll (@Nullable final ECSSMedium eMedium)
  {
    // Either the specific medium is contained, or the "all" medium is contained
    return containsMedium (eMedium) || containsMedium (ECSSMedium.ALL);
  }

  /**
   * Check if the passed medium is usable for the screen. This is the case if
   * either the "screen" medium, the "all" medium or no medium at all is
   * contained.
   *
   * @return <code>true</code> if the media list is usable for screen display
   */
  default boolean isForScreen ()
  {
    // Default is "screen" if none is provided
    return hasNoMedia () || containsMediumOrAll (ECSSMedium.SCREEN);
  }

  /**
   * @return A copy of all specified media in the order they were specified.
   *         Never <code>null</code> but maybe empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  ICommonsOrderedSet <ECSSMedium> getAllMedia ();

  /**
   * @return A non-<code>null</code> but maybe empty String with all media in
   *         the order they where inserted and separated by
   *         {@value CSSMediaList#DEFAULT_MEDIA_STRING_SEPARATOR}
   * @see #getMediaString(String)
   */
  @Nonnull
  String getMediaString ();

  /**
   * @param sSeparator
   *        The separator to be used. May not be <code>null</code>.
   * @return A non-<code>null</code> but maybe empty String with all media in
   *         the order they where inserted and separated by the specified
   *         separator
   */
  @Nonnull
  String getMediaString (@Nonnull String sSeparator);
}
