/*
 * Copyright (C) 2014-2026 Philip Helger (www.helger.com)
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
import java.util.regex.Pattern;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.RegEx;
import com.helger.annotation.concurrent.Immutable;
import com.helger.annotation.style.PresentForCodeCoverage;
import com.helger.base.string.StringHelper;
import com.helger.cache.regex.RegExCache;
import com.helger.css.CCSS;
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

  /**
   * Order of the rules in brackets is important!<br>
   * The parts "([eE][+-]?[0-9]+)?" were added for #79, #82
   */
  @RegEx
  private static final String SPLIT_NUMBER_REGEX = "^([0-9]*\\.[0-9]+([eE][+-]?[0-9]+)?|[0-9]+([eE][+-]?[0-9]+)?).*$";
  private static final Pattern SPLIT_NUMBER_PATTERN = RegExCache.getPattern (SPLIT_NUMBER_REGEX);

  private static final char [] HEXA_CHARS_UPPER = "0123456789ABCDEF".toCharArray ();
  private static final char [] HEXA_CHARS_LOWER = "0123456789abcdef".toCharArray ();

  @PresentForCodeCoverage
  private static final CSSParseHelper INSTANCE = new CSSParseHelper ();

  private CSSParseHelper ()
  {}

  @NonNull
  private static String _trimBy (@NonNull final CharSequence s, final int nLeftSkip, final int nRightSkip)
  {
    return s.toString ().substring (nLeftSkip, s.length () - nRightSkip);
  }

  @NonNull
  private static int _parseIntFromReference (@NonNull final String text,
                                             final int start,
                                             final int end,
                                             final int radix)
  {
    int result = 0;
    for (int i = start; i < end; i++)
    {
      final char c = text.charAt (i);
      int n = -1;
      for (int j = 0; j < HEXA_CHARS_UPPER.length; j++)
      {
        if (c == HEXA_CHARS_UPPER[j] || c == HEXA_CHARS_LOWER[j])
        {
          n = j;
          break;
        }
      }
      result = (radix * result) + n;
    }
    return result;
  }

  /**
   * Remove surrounding quotes (single or double) of a string (if present). If the start and the end
   * quote are not equal, nothing happens.
   *
   * @param sStr
   *        The string where the quotes should be removed
   * @return The string without quotes.
   */
  @Nullable
  public static String extractStringValue (@Nullable final String sStr)
  {
    if (sStr == null || sStr.length () < 2)
      return sStr;

    final char cFirst = sStr.charAt (0);
    if ((cFirst == CCSS.DOUBLE_QUOTE || cFirst == CCSS.SINGLE_QUOTE) && StringHelper.getLastChar (sStr) == cFirst)
    {
      // Remove quotes around the string
      return _trimBy (sStr, 1, 1);
    }
    return sStr;
  }

  /**
   * Unescape all escaped characters in a CSS URL. All characters masked with a '\\' character
   * replaced. Implementation taken from: https://github.com/unbescape/unbescape
   *
   * @param sEscapedURL
   *        The escaped URL. May not be <code>null</code>!
   * @return The unescaped URL or the original string, if not a single escape sequence is found.
   */
  @NonNull
  public static String unescapeURL (@NonNull final String sEscapedURL)
  {
    int nIndex = sEscapedURL.indexOf (URL_ESCAPE_CHAR);
    if (nIndex < 0)
    {
      // No escape sequence found
      return sEscapedURL;
    }

    final StringBuilder aSB = new StringBuilder (sEscapedURL.length ());
    int nPrevIndex = 0;
    int nReferenceOffset = 0;
    while (nIndex >= 0)
    {
      int nCodePoint = -1;

      final char c1 = sEscapedURL.charAt (nIndex + 1);
      switch (c1)
      {
        case '\n':
          nCodePoint = -2;
          nReferenceOffset = nIndex + 1;
          break;
        case ' ':
        case '!':
        case '"':
        case '#':
        case '$':
        case '%':
        case '&':
        case '\'':
        case '(':
        case ')':
        case '*':
        case '+':
        case ',':
          // hyphen: will only be escaped when identifer starts with '--' or '-{digit}'
        case '-':
        case '.':
        case '/':
          // colon: will not be used for escaping: not recognized by IE < 8
        case ':':
        case ';':
        case '<':
        case '=':
        case '>':
        case '?':
        case '@':
        case '[':
        case '\\':
        case ']':
        case '^':
          // underscore: will only be escaped at the beginning of an identifier (in order to avoid
          // issues in IE6)
        case '_':
        case '`':
        case '{':
        case '|':
        case '}':
        case '~':
          nCodePoint = c1;
          nReferenceOffset = nIndex + 1;
          break;
        default:
          break;
      }

      if (nCodePoint == -1)
      {
        if ((c1 >= '0' && c1 <= '9') || (c1 >= 'A' && c1 <= 'F') || (c1 >= 'a' && c1 <= 'f'))
        {
          // This is a hexa escape

          int f = nIndex + 2;
          while (f < (nIndex + 7) && f < sEscapedURL.length ())
          {
            final char cf = sEscapedURL.charAt (f);
            if (!((cf >= '0' && cf <= '9') || (cf >= 'A' && cf <= 'F') || (cf >= 'a' && cf <= 'f')))
            {
              break;
            }
            f++;
          }

          nCodePoint = _parseIntFromReference (sEscapedURL, nIndex + 1, f, 16);

          // Fast-forward to the first char after the parsed codepoint
          nReferenceOffset = f - 1;

          // If there is a whitespace after the escape, just ignore it.
          if (f < sEscapedURL.length () && sEscapedURL.charAt (f) == ' ')
          {
            nReferenceOffset++;
          }

          // Don't continue here, just let the unescape code below do its job
        }
        else
          if (c1 == '\r' || c1 == '\f')
          {
            // The only characters that cannot be escaped by means of a backslash are
            // carriage return and form feed (besides hexadecimal digits).
            nIndex = sEscapedURL.indexOf (URL_ESCAPE_CHAR, nIndex + 1);
            continue;
          }
          else
          {
            // We weren't able to consume any valid escape chars, just consider it a normal char,
            // which is allowed by the CSS escape syntax.
            nCodePoint = c1;
            nReferenceOffset = nIndex + 1;
          }
      }

      // Append everything before the first quote char
      aSB.append (sEscapedURL, nPrevIndex, nIndex);

      nIndex = nReferenceOffset;
      nPrevIndex = nIndex + 1;

      // Append the unescaped char itself
      if (nCodePoint > '\uFFFF')
      {
        aSB.append (Character.toChars (nCodePoint));
      }
      else
        if (nCodePoint != -2)
        {
          aSB.append ((char) nCodePoint);
        }

      // Search the next escaped char
      nIndex = sEscapedURL.indexOf (URL_ESCAPE_CHAR, nPrevIndex);
    }
    while (nIndex >= 0)
      ;
    // Append the rest
    aSB.append (sEscapedURL.substring (nPrevIndex));
    return aSB.toString ();
  }

  /**
   * Remove the leading "url(" and the trailing ")" from an URL CSS value. No check is performed for
   * the existence of a leading "url("! This method should only be called from within the parser.
   *
   * @param s
   *        The value to remove the string from.
   * @return The trimmed value. Never <code>null</code>.
   */
  @NonNull
  public static String trimUrl (@NonNull final CharSequence s)
  {
    // Extract from "url(...)"
    final String sTrimmed = _trimBy (s, CCSSValue.PREFIX_URL_OPEN.length (), CCSSValue.SUFFIX_URL_CLOSE.length ())
                                                                                                                  .trim ();
    // Remove the trailing quotes (if any)
    final String sUnquoted = extractStringValue (sTrimmed);
    // Unescape all escaped chars
    return unescapeURL (sUnquoted);
  }

  @NonNull
  public static String splitNumber (@NonNull final StringBuilder aPattern)
  {
    // Find the longest matching number within the pattern
    final Matcher m = SPLIT_NUMBER_PATTERN.matcher (aPattern);
    if (m.matches ())
      return m.group (1);
    return "";
  }

  /**
   * In CSS, identifiers (including element names, classes, and IDs in selectors) can contain only
   * the characters [a-zA-Z0-9] and ISO 10646 characters U+00A0 and higher, plus the hyphen (-) and
   * the underscore (_); they cannot start with a digit, two hyphens, or a hyphen followed by a
   * digit. Identifiers can also contain escaped characters and any ISO 10646 character as a numeric
   * code (see next item). For instance, the identifier "B&amp;W?" may be written as "B\&amp;W\?" or
   * "B\26 W\3F".<br>
   * CSS Variables on the other hand allow for double dashes:
   * https://www.w3.org/TR/css-variables-1/#defining-variables<br>
   * Source: <a href=
   * "http://stackoverflow.com/questions/30819462/can-css-identifiers-begin-with-two-hyphens">
   * http://stackoverflow.com/questions/30819462/can-css-identifiers-begin-with- two-hyphens</a>
   *
   * @param aPattern
   *        pattern to check
   * @return The input string
   */
  @NonNull
  public static String validateIdentifier (@NonNull final StringBuilder aPattern)
  {
    final int nLength = aPattern.length ();
    final char c1 = aPattern.charAt (0);
    final char c2 = nLength <= 1 ? 0 : aPattern.charAt (1);

    // Starts with a hack?
    if (c1 == '-' || c1 == '$' || c1 == '*')
    {
      if (nLength > 1 && Character.isDigit (c2))
        throw new IllegalArgumentException ("Identifier may not start with a hyphen/dollar/star and a digit: " +
                                            aPattern);
    }
    else
    {
      if (Character.isDigit (c1))
        throw new IllegalArgumentException ("Identifier may not start with a digit: " + aPattern);
    }

    if (false)
    {
      // This is a CSS variable, so okay
      if (nLength > 1 && c1 == '-' && c2 == '-')
        throw new IllegalArgumentException ("Identifier may not start with two hyphens: " + aPattern);
    }

    return aPattern.toString ();
  }

  /**
   * Unescape e.g. <code>\26</code> or <code>\000026</code> to <code>&amp;</code>.
   *
   * @param aImage
   *        Source string
   * @return Unmasked string
   */
  @NonNull
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
  @NonNull
  public static String unescapeOther (final StringBuilder aImage)
  {
    // FIXME
    return aImage.toString ();
  }
}
