/**
 * Copyright (C) 2006-2014 phloc systems (www.phloc.com)
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
package com.helger.css.decl.shorthand;

import java.util.List;

import javax.annotation.Nonnull;

import com.helger.commons.annotations.Nonempty;
import com.helger.commons.annotations.OverrideOnDemand;
import com.helger.css.decl.ICSSExpressionMember;
import com.helger.css.property.ECSSProperty;

/**
 * A special {@link CSSShortHandDescriptor} implementation for margin and
 * padding
 *
 * @author Philip Helger
 */
public class CSSShortHandDescriptorMarginPadding extends CSSShortHandDescriptor
{
  public CSSShortHandDescriptorMarginPadding (@Nonnull final ECSSProperty eProperty,
                                              @Nonnull @Nonempty final CSSPropertyWithDefaultValue... aSubProperties)
  {
    super (eProperty, aSubProperties);
  }

  @Override
  @OverrideOnDemand
  protected void modifyExpressionMembers (@Nonnull final List <ICSSExpressionMember> aExpressionMembers)
  {
    final int nSize = aExpressionMembers.size ();
    if (nSize == 1)
    {
      // 4px -> 4px 4px 4px 4px
      final ICSSExpressionMember aMember = aExpressionMembers.get (0);
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
}
