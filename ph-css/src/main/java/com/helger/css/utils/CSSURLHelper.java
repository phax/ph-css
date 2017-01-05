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
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.string.StringHelper;
import com.helger.commons.url.ISimpleURL;
import com.helger.css.parser.CSSParseHelper;
import com.helger.css.propertyvalue.CCSSValue;

/**
 * Provides URL handling sanity methods.
 *
 * @author Philip Helger
 */
@Immutable
public final class CSSURLHelper
{
  /** For compatibility reasons, this is set to false */
  public static final boolean DEFAULT_QUOTE_URLS = false;

  @PresentForCodeCoverage
  private static final CSSURLHelper s_aInstance = new CSSURLHelper ();

  private CSSURLHelper ()
  {}

  /**
   * Check if the passed CSS value is an URL value. This is either a URL
   * starting with "url(" or it is the string "none".
   *
   * @param sValue
   *        The value to be checked.
   * @return <code>true</code> if the passed value starts with "url(" and ends
   *         with ")" - <code>false</code> otherwise.
   */
  public static boolean isURLValue (@Nullable final String sValue)
  {
    final String sRealValue = StringHelper.trim (sValue);
    if (StringHelper.hasNoText (sRealValue))
      return false;

    if (sRealValue.equals (CCSSValue.NONE))
      return true;

    // 5 = "url(".length () + ")".length
    return sRealValue.length () > 5 &&
           sRealValue.startsWith (CCSSValue.PREFIX_URL_OPEN) &&
           sRealValue.endsWith (CCSSValue.SUFFIX_URL_CLOSE);
  }

  /**
   * Extract the real URL contained in a CSS URL value.
   *
   * @param sValue
   *        The value containing the CSS value
   * @return <code>null</code> if the passed value is not an URL value
   * @see #isURLValue
   */
  @Nullable
  public static String getURLValue (@Nullable final String sValue)
  {
    if (isURLValue (sValue))
    {
      return CSSParseHelper.trimUrl (sValue);
    }
    return null;
  }

  /**
   * Surround the passed URL with the CSS "url(...)"
   *
   * @param aURL
   *        URL to be wrapped. May not be <code>null</code>.
   * @param bQuoteURL
   *        if <code>true</code> single quotes are added around the URL
   * @return <code>url(<i>sURL</i>)</code> or <code>url('<i>sURL</i>')</code>
   */
  @Nonnull
  @Nonempty
  public static String getAsCSSURL (@Nonnull final ISimpleURL aURL, final boolean bQuoteURL)
  {
    ValueEnforcer.notNull (aURL, "URL");

    return getAsCSSURL (aURL.getAsStringWithEncodedParameters (), bQuoteURL);
  }

  /**
   * Check if the passed character is a valid character inside a URL. Characters
   * for which this method returns <code>false</code> must be escaped!
   *
   * @param c
   *        The character to be checked.
   * @return <code>true</code> if the passed character can be directly contained
   *         inside a URL, <code>false</code> otherwise if the character needs
   *         to be escaped.
   */
  public static boolean isValidCSSURLChar (final char c)
  {
    return c == '!' ||
           c == '#' ||
           c == '$' ||
           c == '%' ||
           c == '&' ||
           (c >= '*' && c <= '[') ||
           (c >= ']' && c <= '~') ||
           (c >= '\u0080' && c <= '\uffff');
  }

  /**
   * Check if any character inside the passed URL needs escaping.
   *
   * @param sURL
   *        The URL to be checked. May not be <code>null</code>.
   * @return <code>true</code> if any of the contained characters needs
   *         escaping, <code>false</code> if the URL can be used as is.
   */
  public static boolean isCSSURLRequiringQuotes (@Nonnull final String sURL)
  {
    ValueEnforcer.notNull (sURL, "URL");

    for (final char c : sURL.toCharArray ())
      if (!isValidCSSURLChar (c))
        return true;
    return false;
  }

  /**
   * Internal method to escape a CSS URL. Because this method is only called for
   * quoted URLs, only the quote character itself needs to be quoted.
   *
   * @param sURL
   *        The URL to be escaped. May not be <code>null</code>.
   * @param cQuoteChar
   *        The quote char that is used. Either '\'' or '"'
   * @return The escaped string. Never <code>null</code>.
   */
  @Nonnull
  @Nonempty
  public static String getEscapedCSSURL (@Nonnull final String sURL, final char cQuoteChar)
  {
    ValueEnforcer.notNull (sURL, "URL");

    if (sURL.indexOf (cQuoteChar) < 0 && sURL.indexOf (CSSParseHelper.URL_ESCAPE_CHAR) < 0)
    {
      // Found nothing to quote
      return sURL;
    }

    final StringBuilder aSB = new StringBuilder (sURL.length () * 2);
    for (final char c : sURL.toCharArray ())
    {
      // Escape the quote char and the escape char itself
      if (c == cQuoteChar || c == CSSParseHelper.URL_ESCAPE_CHAR)
        aSB.append (CSSParseHelper.URL_ESCAPE_CHAR);
      aSB.append (c);
    }
    return aSB.toString ();
  }

  /**
   * Surround the passed URL with the CSS "url(...)". When the passed URL
   * contains characters that require quoting, quotes are automatically added!
   *
   * @param sURL
   *        URL to be wrapped. May not be <code>null</code> but maybe empty.
   * @param bForceQuoteURL
   *        if <code>true</code> single quotes are added around the URL
   * @return <code>url(<i>sURL</i>)</code> or <code>url('<i>sURL</i>')</code>
   */
  @Nonnull
  @Nonempty
  public static String getAsCSSURL (@Nonnull final String sURL, final boolean bForceQuoteURL)
  {
    ValueEnforcer.notNull (sURL, "URL");

    final StringBuilder aSB = new StringBuilder (CCSSValue.PREFIX_URL_OPEN);
    final boolean bAreQuotesRequired = bForceQuoteURL || isCSSURLRequiringQuotes (sURL);
    if (bAreQuotesRequired)
    {
      // Determine the best quote char to use - default to '\'' for backwards
      // compatibility
      final int nIndexSingleQuote = sURL.indexOf ('\'');
      final int nIndexDoubleQuote = sURL.indexOf ('"');
      final char cQuote = nIndexSingleQuote >= 0 && nIndexDoubleQuote < 0 ? '"' : '\'';
      // Append the quoted and escaped URL
      aSB.append (cQuote).append (getEscapedCSSURL (sURL, cQuote)).append (cQuote);
    }
    else
    {
      // No quotes needed
      aSB.append (sURL);
    }
    return aSB.append (CCSSValue.SUFFIX_URL_CLOSE).toString ();
  }
}
