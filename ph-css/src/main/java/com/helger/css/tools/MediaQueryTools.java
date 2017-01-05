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
package com.helger.css.tools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.string.StringHelper;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSImportRule;
import com.helger.css.decl.CSSMediaQuery;
import com.helger.css.decl.CSSMediaRule;
import com.helger.css.decl.CSSNamespaceRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ICSSTopLevelRule;
import com.helger.css.reader.CSSReader;

/**
 * A small utility class to wrap an existing {@link CascadingStyleSheet} within
 * a specific media, if possible. {@link CascadingStyleSheet} can only be
 * wrapped, if they don't contain a media rule themselves.
 *
 * @author Philip Helger
 */
@Immutable
public final class MediaQueryTools
{
  @PresentForCodeCoverage
  private static final MediaQueryTools s_aInstance = new MediaQueryTools ();

  private MediaQueryTools ()
  {}

  /**
   * Utility method to convert a media query string to a structured list of
   * {@link CSSMediaQuery} objects.
   *
   * @param sMediaQuery
   *        The media query string to parse. May be <code>null</code>.
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @return <code>null</code> if the passed media query is <code>null</code> or
   *         empty or not parsable.
   */
  @Nullable
  public static ICommonsList <CSSMediaQuery> parseToMediaQuery (@Nullable final String sMediaQuery,
                                                                @Nonnull final ECSSVersion eVersion)
  {
    if (StringHelper.hasNoText (sMediaQuery))
      return null;

    final String sCSS = "@media " + sMediaQuery + " {}";
    final CascadingStyleSheet aCSS = CSSReader.readFromString (sCSS, eVersion);
    if (aCSS == null)
      return null;

    final CSSMediaRule aMediaRule = aCSS.getAllMediaRules ().get (0);
    return aMediaRule.getAllMediaQueries ();
  }

  /**
   * Check if the passed CSS can be wrapped in an external media rule.
   *
   * @param aCSS
   *        The CSS to be checked for wrapping. May be <code>null</code>.
   * @param bAllowNestedMediaQueries
   *        if <code>true</code> nested media queries are allowed,
   *        <code>false</code> if they are prohibited.
   * @return <code>true</code> if the CSS can be wrapped, <code>false</code> if
   *         it can't be wrapped.
   */
  public static boolean canWrapInMediaQuery (@Nullable final CascadingStyleSheet aCSS,
                                             final boolean bAllowNestedMediaQueries)
  {
    if (aCSS == null)
      return false;
    if (bAllowNestedMediaQueries)
      return true;
    // Nested media queries are not allowed, therefore wrapping can only take
    // place if no other media queries are present
    return !aCSS.hasMediaRules ();
  }

  /**
   * Get the CSS wrapped in the specified media query. Note: all existing rule
   * objects are reused, so modifying them also modifies the original CSS!
   *
   * @param aCSS
   *        The CSS to be wrapped. May not be <code>null</code>.
   * @param aMediaQuery
   *        The media query to use.
   * @param bAllowNestedMediaQueries
   *        if <code>true</code> nested media queries are allowed,
   *        <code>false</code> if they are prohibited.
   * @return <code>null</code> if out CSS cannot be wrapped, the newly created
   *         {@link CascadingStyleSheet} object otherwise.
   */
  @Nullable
  public static CascadingStyleSheet getWrappedInMediaQuery (@Nonnull final CascadingStyleSheet aCSS,
                                                            @Nonnull final CSSMediaQuery aMediaQuery,
                                                            final boolean bAllowNestedMediaQueries)
  {
    return getWrappedInMediaQuery (aCSS, new CommonsArrayList <> (aMediaQuery), bAllowNestedMediaQueries);
  }

  /**
   * Get the CSS wrapped in the specified media query. Note: all existing rule
   * objects are reused, so modifying them also modifies the original CSS!
   *
   * @param aCSS
   *        The CSS to be wrapped. May not be <code>null</code>.
   * @param aMediaQueries
   *        The media queries to use. May neither be <code>null</code> nor empty
   *        nor may it contain <code>null</code> elements.
   * @param bAllowNestedMediaQueries
   *        if <code>true</code> nested media queries are allowed,
   *        <code>false</code> if they are prohibited.
   * @return <code>null</code> if out CSS cannot be wrapped, the newly created
   *         {@link CascadingStyleSheet} object otherwise.
   */
  @Nullable
  public static CascadingStyleSheet getWrappedInMediaQuery (@Nonnull final CascadingStyleSheet aCSS,
                                                            @Nonnull @Nonempty final Iterable <? extends CSSMediaQuery> aMediaQueries,
                                                            final boolean bAllowNestedMediaQueries)
  {
    ValueEnforcer.notNull (aCSS, "CSS");
    ValueEnforcer.notEmpty (aMediaQueries, "MediaQueries");

    if (!canWrapInMediaQuery (aCSS, bAllowNestedMediaQueries))
      return null;

    final CascadingStyleSheet ret = new CascadingStyleSheet ();

    // Copy all import rules
    for (final CSSImportRule aImportRule : aCSS.getAllImportRules ())
    {
      if (aImportRule.hasMediaQueries ())
      {
        // import rule already has a media query - do not alter
        ret.addImportRule (aImportRule);
      }
      else
      {
        // Create a new rule and add the passed media queries
        final CSSImportRule aNewImportRule = new CSSImportRule (aImportRule.getLocation ());
        for (final CSSMediaQuery aMediaQuery : aMediaQueries)
          aNewImportRule.addMediaQuery (aMediaQuery);
        ret.addImportRule (aNewImportRule);
      }
    }

    // Copy all namespace rules
    for (final CSSNamespaceRule aNamespaceRule : aCSS.getAllNamespaceRules ())
      ret.addNamespaceRule (aNamespaceRule);

    // Create a single top-level media rule ...
    // into this media rule
    final CSSMediaRule aNewMediaRule = new CSSMediaRule ();
    for (final CSSMediaQuery aMediaQuery : aMediaQueries)
      aNewMediaRule.addMediaQuery (aMediaQuery);
    // ... and add the existing top-level rules into this media rule
    for (final ICSSTopLevelRule aRule : aCSS.getAllRules ())
      aNewMediaRule.addRule (aRule);

    // Finally add the resulting media rule into the new CSS
    ret.addRule (aNewMediaRule);

    return ret;
  }
}
