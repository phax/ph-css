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
package com.helger.css.decl.shorthand;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonempty;
import com.helger.annotation.style.OverrideOnDemand;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.decl.CSSExpression;
import com.helger.css.decl.ECSSExpressionOperator;
import com.helger.css.decl.ICSSExpressionMember;
import com.helger.css.property.ECSSProperty;

/**
 * A special {@link CSSShortHandDescriptor} implementation for margin and padding as well as for
 * border-color.
 *
 * @author Philip Helger
 */
public class CSSShortHandDescriptorWithAlignment extends CSSShortHandDescriptor
{
  public CSSShortHandDescriptorWithAlignment (@NonNull final ECSSProperty eProperty,
                                              @NonNull @Nonempty final CSSPropertyWithDefaultValue... aSubProperties)
  {
    super (eProperty, aSubProperties);
  }

  @Override
  @OverrideOnDemand
  protected void modifyExpressionMembers (@NonNull final ICommonsList <ICSSExpressionMember> aExpressionMembers)
  {
    final int nSize = aExpressionMembers.size ();
    if (nSize == 1)
    {
      // 4px -> 4px 4px 4px 4px
      final ICSSExpressionMember aMember = aExpressionMembers.getFirstOrNull ();
      for (int i = 0; i < 3; ++i)
        aExpressionMembers.add (aMember.getClone ());
    }
    else
      if (nSize == 2)
      {
        // 4px 10px -> 4px 10px 4px 10px
        final ICSSExpressionMember aMemberY = aExpressionMembers.get (0);
        final ICSSExpressionMember aMemberX = aExpressionMembers.get (1);
        aExpressionMembers.add (aMemberY.getClone ());
        aExpressionMembers.add (aMemberX.getClone ());
      }
      else
        if (nSize == 3)
        {
          // 4px 10px 6px -> 4px 10px 6px 10px
          final ICSSExpressionMember aMemberX = aExpressionMembers.get (1);
          aExpressionMembers.add (aMemberX.getClone ());
        }
    // else nothing to do
  }

  /**
   * Compact a 1/2/3/4-value box-shorthand expression member list according to the CSS box model
   * rules:
   * <ul>
   * <li>4 values (T R B L): if T==R==B==L &rarr; 1; if T==B and R==L &rarr; 2; if R==L &rarr; 3;
   * else 4</li>
   * <li>3 values (T R/L B): if all equal &rarr; 1; if T==B &rarr; 2; else 3</li>
   * <li>2 values (T/B R/L): if equal &rarr; 1; else 2</li>
   * <li>1 value: unchanged</li>
   * </ul>
   * If any member is an operator (e.g. <code>/</code> or <code>,</code>) the list is returned
   * unchanged. The comparison uses the rendered CSS string of each member with the provided
   * settings.
   *
   * @param aMembers
   *        The original expression members. Never <code>null</code>.
   * @param aSettings
   *        Writer settings used for rendering the equality comparison. Never <code>null</code>.
   * @return A new list containing the compacted members or the original list reference if no
   *         compaction was possible. Never <code>null</code>.
   * @since 8.2.1
   */
  @NonNull
  public static ICommonsList <ICSSExpressionMember> getCompactedExpressionMembers (@NonNull final ICommonsList <ICSSExpressionMember> aMembers,
                                                                                   @NonNull final ICSSWriterSettings aSettings)
  {
    final int nSize = aMembers.size ();
    if (nSize < 2 || nSize > 4)
      return aMembers;

    // Don't compact if any member is an operator (the box shorthands don't use operators, but be
    // defensive)
    for (final ICSSExpressionMember aMember : aMembers)
      if (aMember instanceof ECSSExpressionOperator)
        return aMembers;

    // Pre-compute rendered strings
    final String [] aRendered = new String [nSize];
    for (int i = 0; i < nSize; ++i)
      aRendered[i] = aMembers.get (i).getAsCSSString (aSettings);

    int nKeep;
    switch (nSize)
    {
      case 2:
        // T/B R/L
        nKeep = aRendered[0].equals (aRendered[1]) ? 1 : 2;
        break;
      case 3:
        // T R/L B
        if (aRendered[0].equals (aRendered[1]) && aRendered[1].equals (aRendered[2]))
          nKeep = 1;
        else
          if (aRendered[0].equals (aRendered[2]))
            nKeep = 2;
          else
            nKeep = 3;
        break;
      case 4:
        // T R B L
        if (aRendered[0].equals (aRendered[1]) &&
            aRendered[1].equals (aRendered[2]) &&
            aRendered[2].equals (aRendered[3]))
          nKeep = 1;
        else
          if (aRendered[0].equals (aRendered[2]) && aRendered[1].equals (aRendered[3]))
            nKeep = 2;
          else
            if (aRendered[1].equals (aRendered[3]))
              nKeep = 3;
            else
              nKeep = 4;
        break;
      default:
        // unreachable due to size check above
        return aMembers;
    }

    if (nKeep == nSize)
      return aMembers;

    final ICommonsList <ICSSExpressionMember> ret = new CommonsArrayList <> (nKeep);
    for (int i = 0; i < nKeep; ++i)
      ret.add (aMembers.get (i));
    return ret;
  }

  @Override
  @NonNull
  public CSSExpression getOptimizedExpression (@NonNull final CSSExpression aExpression,
                                               @NonNull final ICSSWriterSettings aSettings)
  {
    final ICommonsList <ICSSExpressionMember> aOriginal = aExpression.getAllMembers ();
    final ICommonsList <ICSSExpressionMember> aCompact = getCompactedExpressionMembers (aOriginal, aSettings);
    if (aCompact == aOriginal || aCompact.size () == aOriginal.size ())
      return aExpression;

    final CSSExpression aResult = new CSSExpression ();
    aResult.setSourceLocation (aExpression.getSourceLocation ());
    for (final ICSSExpressionMember aMember : aCompact)
      aResult.addMember (aMember);
    return aResult;
  }
}
