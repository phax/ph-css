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
package com.helger.css.parser;

import java.util.regex.Matcher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.RegEx;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.regex.RegExHelper;
import com.helger.commons.string.StringHelper;
import com.helger.css.propertyvalue.CCSSValue;

/**
 * This class is used by the generated parsers to do some common stuff.
 *
 * @author Philip Helger
 */
@Immutable
public final class CSSParseHelper
{
  /** The character used to quote elements in CSS URLs */
  public static final char URL_ESCAPE_CHAR = '\\';

  // Order of the rules in brackets is important!
  @RegEx
  private static final String SPLIT_NUMBER_REGEX = "^([0-9]*\\.[0-9]+|[0-9]+).*$";

  @PresentForCodeCoverage
  private static final CSSParseHelper s_aInstance = new CSSParseHelper ();

  private CSSParseHelper ()
  {}

  @Nonnull
  private static String _trimBy (@Nonnull final CharSequence s, final int nLeftSkip, final int nRightSkip)
  {
    return s.toString ().substring (nLeftSkip, s.length () - nRightSkip);
  }

  /**
   * Remove surrounding quotes (single or double) of a string (if present). If
   * the start and the end quote are not equal, nothing happens.
   *
   * @param sStr
   *        The string where the quotes should be removed
   * @return The string without quotes.
   */
  @Nullable
  public static String extractStringValue (@Nullable final String sStr)
  {
    if (StringHelper.hasNoText (sStr) || sStr.length () < 2)
      return sStr;

    final char cFirst = sStr.charAt (0);
    if ((cFirst == '"' || cFirst == '\'') && StringHelper.getLastChar (sStr) == cFirst)
    {
      // Remove quotes around the string
      return _trimBy (sStr, 1, 1);
    }
    return sStr;
  }

  /**
   * Unescape all escaped characters in a CSS URL. All characters masked with a
   * '\\' character replaced.
   *
   * @param sEscapedURL
   *        The escaped URL. May not be <code>null</code>!
   * @return The unescaped URL or the original string, if not a single escape
   *         sequence is found.
   */
  @Nonnull
  public static String unescapeURL (@Nonnull final String sEscapedURL)
  {
    int nIndex = sEscapedURL.indexOf (URL_ESCAPE_CHAR);
    if (nIndex < 0)
    {
      // No escape sequence found
      return sEscapedURL;
    }

    final StringBuilder aSB = new StringBuilder (sEscapedURL.length ());
    int nPrevIndex = 0;
    do
    {
      // Append everything before the first quote char
      aSB.append (sEscapedURL, nPrevIndex, nIndex);
      // Append the quoted char itself
      aSB.append (sEscapedURL, nIndex + 1, nIndex + 2);
      // The new position to start searching
      nPrevIndex = nIndex + 2;
      // Search the next escaped char
      nIndex = sEscapedURL.indexOf (URL_ESCAPE_CHAR, nPrevIndex);
    } while (nIndex >= 0);
    // Append the rest
    aSB.append (sEscapedURL.substring (nPrevIndex));
    return aSB.toString ();
  }

  /**
   * Remove the leading "url(" and the trailing ")" from an URL CSS value. No
   * check is performed for the existence of a leading "url("! This method
   * should only be called from within the parser.
   *
   * @param s
   *        The value to remove the string from.
   * @return The trimmed value. Never <code>null</code>.
   */
  @Nonnull
  public static String trimUrl (@Nonnull final CharSequence s)
  {
    // Extract from "url(...)"
    final String sTrimmed = _trimBy (s,
                                     CCSSValue.PREFIX_URL_OPEN.length (),
                                     CCSSValue.SUFFIX_URL_CLOSE.length ()).trim ();
    // Remove the trailing quotes (if any)
    final String sUnquoted = extractStringValue (sTrimmed);
    // Unescape all escaped chars
    return unescapeURL (sUnquoted);
  }

  @Nonnull
  public static String splitNumber (@Nonnull final StringBuilder aPattern)
  {
    // Find the longest matching number within the pattern
    final Matcher m = RegExHelper.getMatcher (SPLIT_NUMBER_REGEX, aPattern.toString ());
    if (m.matches ())
      return m.group (1);
    return "";
  }

  /**
   * In CSS, identifiers (including element names, classes, and IDs in
   * selectors) can contain only the characters [a-zA-Z0-9] and ISO 10646
   * characters U+00A0 and higher, plus the hyphen (-) and the underscore (_);
   * they cannot start with a digit, two hyphens, or a hyphen followed by a
   * digit. Identifiers can also contain escaped characters and any ISO 10646
   * character as a numeric code (see next item). For instance, the identifier
   * "B&amp;W?" may be written as "B\&amp;W\?" or "B\26 W\3F".<br>
   * CSS Variables on the other hand allow for double dashes:
   * https://www.w3.org/TR/css-variables-1/#defining-variables<br>
   * Source: <a href=
   * "http://stackoverflow.com/questions/30819462/can-css-identifiers-begin-with-two-hyphens">
   * http://stackoverflow.com/questions/30819462/can-css-identifiers-begin-with-
   * two-hyphens</a>
   *
   * @param aPattern
   *        pattern to check
   * @return The input string
   */
  @Nonnull
  public static String validateIdentifier (@Nonnull final StringBuilder aPattern)
  {
    final int nLength = aPattern.length ();
    final char c1 = aPattern.charAt (0);
    final char c2 = nLength <= 1 ? 0 : aPattern.charAt (1);

    // Starts with a hack?
    if (c1 == '-' || c1 == '$' || c1 == '*')
    {
      if (nLength > 1 && Character.isDigit (c2))
        throw new IllegalArgumentException ("Identifier may not start with a hyphen and a digit: " + aPattern);
    }
    else
    {
      if (Character.isDigit (c1))
        throw new IllegalArgumentException ("Identifier may not start with a digit: " + aPattern);
    }

    if (false)
      if (nLength > 1 && c1 == '-' && c2 == '-')
        throw new IllegalArgumentException ("Identifier may not start with two hyphens: " + aPattern);

    return aPattern.toString ();
  }

  /**
   * Unescape e.g. <code>\26</code> or <code>\000026</code> to
   * <code>&amp;</code>.
   *
   * @param aImage
   *        Source string
   * @return Unmasked string
   */
  @Nonnull
  public static String unescapeUnicode (final StringBuilder aImage)
  {
    // FIXME
    return aImage.toString ();
  }

  /**
   * Unescape e.g. <code>\x</code> to x.
   *
   * @param aImage
   *        Source string
   * @return Unmasked string
   */
  @Nonnull
  public static String unescapeOther (final StringBuilder aImage)
  {
    // FIXME
    return aImage.toString ();
  }
}
