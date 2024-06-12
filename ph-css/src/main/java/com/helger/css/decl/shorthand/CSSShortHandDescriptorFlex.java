/*
 * Copyright (C) 2014-2024 Philip Helger (www.helger.com)
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

import javax.annotation.Nonnull;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.OverrideOnDemand;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.string.StringParser;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.decl.ICSSExpressionMember;
import com.helger.css.property.ECSSProperty;
import com.helger.css.propertyvalue.CCSSValue;

/**
 * A special {@link CSSShortHandDescriptor} implementation for margin and
 * padding as well as for border-color.
 *
 * @author Philip Helger
 */
public class CSSShortHandDescriptorFlex extends CSSShortHandDescriptor
{
  public CSSShortHandDescriptorFlex (@Nonnull final ECSSProperty eProperty,
                                     @Nonnull @Nonempty final CSSPropertyWithDefaultValue... aSubProperties)
  {
    super (eProperty, aSubProperties);
  }

  @Override
  @OverrideOnDemand
  protected void modifyExpressionMembers (@Nonnull final ICommonsList <ICSSExpressionMember> aExpressionMembers)
  {
    final int nSize = aExpressionMembers.size ();
    if (nSize == 1)
    {
      // * a <number>: In this case it is interpreted as flex: <number> 1 0; the
      // <flex-shrink> value is assumed to be 1 and the <flex-basis> value is
      // assumed to be 0.
      // * one of the keywords: none, auto, or initial.

      final ICSSExpressionMember aMember = aExpressionMembers.getFirstOrNull ();
      final String sCSS = aMember.getAsCSSString ();
      if (CCSSValue.INITIAL.equals (sCSS))
      {
        aExpressionMembers.clear ();
        aExpressionMembers.add (new CSSExpressionMemberTermSimple (0));
        aExpressionMembers.add (new CSSExpressionMemberTermSimple (1));
        aExpressionMembers.add (new CSSExpressionMemberTermSimple (CCSSValue.AUTO));
      }
      else
        if (CCSSValue.AUTO.equals (sCSS))
        {
          aExpressionMembers.clear ();
          aExpressionMembers.add (new CSSExpressionMemberTermSimple (1));
          aExpressionMembers.add (new CSSExpressionMemberTermSimple (1));
          aExpressionMembers.add (new CSSExpressionMemberTermSimple (CCSSValue.AUTO));
        }
        else
          if (CCSSValue.NONE.equals (sCSS))
          {
            aExpressionMembers.clear ();
            aExpressionMembers.add (new CSSExpressionMemberTermSimple (0));
            aExpressionMembers.add (new CSSExpressionMemberTermSimple (0));
            aExpressionMembers.add (new CSSExpressionMemberTermSimple (CCSSValue.AUTO));
          }
          else
          {
            // Assume it is numeric
            aExpressionMembers.add (new CSSExpressionMemberTermSimple (1));
            aExpressionMembers.add (new CSSExpressionMemberTermSimple (0));
          }
    }
    else
      if (nSize == 2)
      {
        // * The first value must be:
        // * * a <number> and it is interpreted as <flex-grow>.
        // * The second value must be one of:
        // * * a <number>: then it is interpreted as <flex-shrink>.
        // * * a valid value for width: then it is interpreted as <flex-basis>.

        final ICSSExpressionMember aMember2 = aExpressionMembers.get (1);
        final String sCSS = aMember2.getAsCSSString ();
        if (StringParser.isLong (sCSS))
        {
          // All in order - nothing do to
        }
        else
        {
          // Assume it is a width - squeeze in an element
          aExpressionMembers.add (1, new CSSExpressionMemberTermSimple (1));
        }
      }
    // else nothing to do
  }
}
