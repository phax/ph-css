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
package com.helger.css;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.base.name.IHasDisplayName;
import com.helger.base.string.StringHelper;

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

  ECSSVendorPrefix (@NonNull @Nonempty final String sPrefix, @NonNull @Nonempty final String sDisplayName)
  {
    m_sPrefix = sPrefix;
    m_sDisplayName = sDisplayName;
  }

  /**
   * @return The prefix used by this CSS vendor. Neither <code>null</code> nor empty.
   */
  @NonNull
  @Nonempty
  public String getPrefix ()
  {
    return m_sPrefix;
  }

  @NonNull
  @Nonempty
  public String getDisplayName ()
  {
    return m_sDisplayName;
  }

  @Nullable
  public static ECSSVendorPrefix getFromPrefixOrNull (@Nullable final String sPrefix)
  {
    if (StringHelper.isNotEmpty (sPrefix))
      for (final ECSSVendorPrefix e : values ())
        if (e.m_sPrefix.equals (sPrefix))
          return e;
    return null;
  }
}
