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
import javax.annotation.Nullable;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.name.IHasDisplayName;
import com.helger.commons.string.StringHelper;

/**
 * Enumeration containing all known CSS vendor prefixes
 *
 * @author Philip Helger
 * @since 3.9.0
 */
public enum ECSSVendorPrefix implements IHasDisplayName
{
  /** Safari alternative prefix */
  APPLE ("-apple-", "Safari"),
  /** Advanced Television Standards Committee */
  ATSC ("-atsc-", "Advanced Television Standards Committee"),
  /** EPUB */
  EPUB ("-epub-", "EPUB"),
  /** Konqueror browser */
  KHTML ("-khtml-", "Konqueror browser"),
  /** Microsoft */
  MICROSOFT ("-ms-", "Microsoft"),
  /** Microsoft Office - no leading dash! */
  MICROSOFT_OFFICE ("mso-", "Microsoft Office"),
  /** Mozilla */
  MOZILLA ("-moz-", "Mozilla"),
  /** Opera */
  OPERA ("-o-", "Opera"),
  /** Microsoft - scrollbars - no leading dash! */
  SCROLLBAR ("scrollbar-", "Scrollbars"),
  /** The WAP forum */
  WAP ("-wap-", "The WAP forum"),
  /** Safari and other WebKit-based browsers */
  WEBKIT ("-webkit-", "WebKit-based browsers");

  private final String m_sPrefix;
  private final String m_sDisplayName;

  private ECSSVendorPrefix (@Nonnull @Nonempty final String sPrefix, @Nonnull @Nonempty final String sDisplayName)
  {
    m_sPrefix = sPrefix;
    m_sDisplayName = sDisplayName;
  }

  /**
   * @return The prefix used by this CSS vendor. Neither <code>null</code> nor
   *         empty.
   */
  @Nonnull
  @Nonempty
  public String getPrefix ()
  {
    return m_sPrefix;
  }

  @Nonnull
  @Nonempty
  public String getDisplayName ()
  {
    return m_sDisplayName;
  }

  @Nullable
  public static ECSSVendorPrefix getFromPrefixOrNull (@Nullable final String sPrefix)
  {
    if (StringHelper.hasText (sPrefix))
      for (final ECSSVendorPrefix e : values ())
        if (e.m_sPrefix.equals (sPrefix))
          return e;
    return null;
  }
}
