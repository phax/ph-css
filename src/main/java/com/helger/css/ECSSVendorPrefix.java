/**
 * Copyright (C) 2014 Philip Helger (www.helger.com)
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

import com.helger.commons.annotations.Nonempty;

/**
 * Enumeration containing all known CSS vendor prefixes
 *
 * @author Philip Helger
 */
public enum ECSSVendorPrefix
{
  KHTML ("-khtml-"),
  MICROSOFT ("-ms-"),
  MOZILLA ("-moz-"),
  OPERA ("-o-"),
  EPUB ("-epub-"),
  WEBKIT ("-webkit-");

  private final String m_sPrefix;

  private ECSSVendorPrefix (@Nonnull @Nonempty final String sPrefix)
  {
    m_sPrefix = sPrefix;
  }

  @Nonnull
  @Nonempty
  public String getPrefix ()
  {
    return m_sPrefix;
  }
}
