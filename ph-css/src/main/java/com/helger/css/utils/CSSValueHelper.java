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
package com.helger.css.utils;

import java.util.Locale;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.concurrent.Immutable;
import com.helger.annotation.style.PresentForCodeCoverage;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.string.StringHelper;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;

/**
 * Helper for the textual structure of CSS property values.
 *
 * @author Philip Helger
 * @since 8.3.0
 */
@Immutable
public final class CSSValueHelper
{
  private static final String [] SUBSTITUTION_FUNCTIONS = { "var(", "env(", "attr(" };

  @PresentForCodeCoverage
  private static final CSSValueHelper INSTANCE = new CSSValueHelper ();

  private CSSValueHelper ()
  {}

  /**
   * Check if the value is a call of one of the passed functions.
   *
   * @param sValue
   *        The value to check. May be <code>null</code> and is trimmed inside this method.
   * @param aFunctionNames
   *        The lower case function names without the parenthesis. May not be <code>null</code>.
   * @return <code>true</code> if the value starts with one of the function names plus the opening
   *         parenthesis and ends with the closing parenthesis.
   */
  public static boolean isFunctionCall (@Nullable final String sValue, @NonNull final String... aFunctionNames)
  {
    final String sRealValue = StringHelper.trim (sValue);
    if (StringHelper.isEmpty (sRealValue) || !sRealValue.endsWith (")"))
      return false;

    final String sLowerValue = sRealValue.toLowerCase (Locale.ROOT);
    for (final String sFunctionName : aFunctionNames)
      if (sLowerValue.startsWith (sFunctionName + '('))
        return true;
    return false;
  }

  /**
   * Check if the value contains a function that is substituted at computed value time. Each property
   * value using it is valid when parsing.
   *
   * @param sValue
   *        The value to check. May be <code>null</code>.
   * @return <code>true</code> if the value contains <code>var()</code>, <code>env()</code> or
   *         <code>attr()</code>.
   */
  public static boolean containsSubstitutionFunction (@Nullable final String sValue)
  {
    if (StringHelper.isEmpty (sValue))
      return false;

    final String sLowerValue = sValue.toLowerCase (Locale.ROOT);
    for (final String sFunction : SUBSTITUTION_FUNCTIONS)
      if (sLowerValue.contains (sFunction))
        return true;
    return false;
  }

  @NonNull
  @ReturnsMutableCopy
  private static ICommonsList <String> _getSplit (@NonNull final String sValue, final boolean bComma)
  {
    final ICommonsList <String> ret = new CommonsArrayList <> ();
    final StringBuilder aCurrent = new StringBuilder ();
    int nDepth = 0;
    char cQuote = 0;
    for (final char c : sValue.toCharArray ())
    {
      if (cQuote != 0)
      {
        if (c == cQuote)
          cQuote = 0;
      }
      else
        if (c == '"' || c == '\'')
          cQuote = c;
        else
          if (c == '(')
            nDepth++;
          else
            if (c == ')')
              nDepth--;

      final boolean bSplit = cQuote == 0 && nDepth == 0 && (bComma ? c == ',' : Character.isWhitespace (c));
      if (bSplit)
      {
        if (bComma || aCurrent.length () > 0)
          ret.add (aCurrent.toString ().trim ());
        aCurrent.setLength (0);
      }
      else
        aCurrent.append (c);
    }
    if (bComma || aCurrent.length () > 0)
      ret.add (aCurrent.toString ().trim ());
    return ret;
  }

  /**
   * Split a value by whitespaces. Parenthesis and quoted strings stay together, so
   * <code>calc(1px + 2px) "a b"</code> results in two parts.
   *
   * @param sValue
   *        The value to split. May not be <code>null</code>.
   * @return The non-empty parts. Never <code>null</code> but maybe empty.
   */
  @NonNull
  @ReturnsMutableCopy
  public static ICommonsList <String> getSplitByWhitespace (@NonNull final String sValue)
  {
    return _getSplit (sValue, false);
  }

  /**
   * Split a value by commas. Parenthesis and quoted strings stay together, so
   * <code>rgb(0,0,0), red</code> results in two parts.
   *
   * @param sValue
   *        The value to split. May not be <code>null</code>.
   * @return The trimmed parts, which may be empty. Never <code>null</code> and never empty.
   */
  @NonNull
  @ReturnsMutableCopy
  public static ICommonsList <String> getSplitByComma (@NonNull final String sValue)
  {
    return _getSplit (sValue, true);
  }
}
