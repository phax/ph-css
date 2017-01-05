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
package com.helger.css;

import javax.annotation.Nonnull;

import com.helger.commons.annotation.DevelopersNote;
import com.helger.commons.version.IHasVersion;
import com.helger.commons.version.Version;

/**
 * Contains the different CSS versions that may be of relevance.
 *
 * @author Philip Helger
 */
public enum ECSSVersion implements IHasVersion
{
  // Sort fields according to the version!
  @DevelopersNote ("No CSS parser is available for 1.0!")
  CSS10(new Version (1, 0)),
  @DevelopersNote ("Up to version 4.x of ph-css a special CSS 2.1 parser was available. Now it is the same as CSS 3.0")
  CSS21(new Version (2, 1)),
  CSS30 (new Version (3, 0));

  /** Latest version is CSS 3.0 */
  @Nonnull
  public static final ECSSVersion LATEST = CSS30;

  private final Version m_aVersion;

  private ECSSVersion (@Nonnull final Version aVersion)
  {
    m_aVersion = aVersion;
  }

  @Nonnull
  public Version getVersion ()
  {
    return m_aVersion;
  }

  @Nonnull
  public String getVersionString ()
  {
    return m_aVersion.getMajor () + "." + m_aVersion.getMinor ();
  }
}
