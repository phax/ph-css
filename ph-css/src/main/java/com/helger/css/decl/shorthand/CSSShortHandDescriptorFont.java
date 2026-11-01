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
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpression;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.decl.ECSSExpressionOperator;
import com.helger.css.decl.ICSSExpressionMember;
import com.helger.css.property.CCSSProperties;
import com.helger.css.property.ECSSProperty;
import com.helger.css.property.ICSSProperty;
import com.helger.css.propertyvalue.CCSSValue;
import com.helger.css.writer.CSSWriterSettings;

/**
 * A special {@link CSSShortHandDescriptor} implementation for font. The sequence of the values is
 * relevant: everything before <code>font-size</code> are the optional style, variant, weight and
 * stretch values in any order, followed by the optional <code>/ line-height</code>. All the rest is
 * the comma separated <code>font-family</code> list. If there is no <code>font-size</code> (as in
 * <code>font: caption</code>), the default split is used.
 *
 * @author Philip Helger
 * @since 8.3.0
 */
public class CSSShortHandDescriptorFont extends CSSShortHandDescriptor
{
  public CSSShortHandDescriptorFont (@NonNull final ECSSProperty eProperty,
                                     @NonNull @Nonempty final CSSPropertyWithDefaultValue... aSubProperties)
  {
    super (eProperty, aSubProperties);
  }

  @NonNull
  private static CSSDeclaration _createDeclaration (@NonNull final ICSSProperty aProperty,
                                                    @NonNull final Iterable <ICSSExpressionMember> aMembers)
  {
    final CSSExpression aExpr = new CSSExpression ();
    for (final ICSSExpressionMember aMember : aMembers)
      aExpr.addMember (aMember);
    return new CSSDeclaration (aProperty.getPropertyName (), aExpr);
  }

  private static int _getSizeIndex (@NonNull final ICommonsList <ICSSExpressionMember> aMembers,
                                    @NonNull final CSSWriterSettings aCWS)
  {
    final ICSSProperty [] aPrefixProperties = { CCSSProperties.FONT_STYLE,
                                                CCSSProperties.FONT_VARIANT,
                                                CCSSProperties.FONT_WEIGHT,
                                                CCSSProperties.FONT_STRETCH };
    for (int i = 0; i < aMembers.size (); ++i)
    {
      final String sValue = aMembers.get (i).getAsCSSString (aCWS);
      boolean bIsPrefix = false;
      for (final ICSSProperty aProperty : aPrefixProperties)
        if (!CCSSValue.INHERIT.equals (sValue) && aProperty.isValidValue (sValue))
          bIsPrefix = true;
      // Sizes can be valid for font-stretch, as percentages
      if (CCSSProperties.FONT_SIZE.isValidValue (sValue) && !CCSSProperties.FONT_WEIGHT.isValidValue (sValue))
        return i;
      if (!bIsPrefix)
        return -1;
    }
    return -1;
  }

  @Override
  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getSplitIntoPieces (@NonNull final CSSDeclaration aDeclaration)
  {
    final ICommonsList <ICSSExpressionMember> aMembers = aDeclaration.getExpression ().getAllMembers ();
    final CSSWriterSettings aCWS = new CSSWriterSettings (false);
    final int nSizeIndex = _getSizeIndex (aMembers, aCWS);
    // Without a family, the value is not a valid font shorthand
    if (nSizeIndex < 0 || !aDeclaration.hasProperty (getProperty ()) || nSizeIndex + 1 >= aMembers.size ())
      return super.getSplitIntoPieces (aDeclaration);

    final ICommonsList <CSSDeclaration> ret = new CommonsArrayList <> ();

    // Style, variant, weight and stretch - "normal" is valid for all of them
    final ICommonsList <ICSSProperty> aOpen = new CommonsArrayList <> (CCSSProperties.FONT_STYLE,
                                                                       CCSSProperties.FONT_VARIANT,
                                                                       CCSSProperties.FONT_WEIGHT,
                                                                       CCSSProperties.FONT_STRETCH);
    for (int i = 0; i < nSizeIndex; ++i)
    {
      final ICSSExpressionMember aMember = aMembers.get (i);
      final String sValue = aMember.getAsCSSString (aCWS);
      if (!CCSSValue.NORMAL.equals (sValue))
        for (final ICSSProperty aProperty : aOpen)
          if (aProperty.isValidValue (sValue))
          {
            ret.add (_createDeclaration (aProperty, new CommonsArrayList <> (aMember)));
            aOpen.remove (aProperty);
            break;
          }
    }

    ret.add (_createDeclaration (CCSSProperties.FONT_SIZE, new CommonsArrayList <> (aMembers.get (nSizeIndex))));

    // Optional line height
    int nFamilyIndex = nSizeIndex + 1;
    boolean bHasLineHeight = false;
    if (aMembers.get (nFamilyIndex) == ECSSExpressionOperator.SLASH && nFamilyIndex + 1 < aMembers.size ())
    {
      ret.add (_createDeclaration (CCSSProperties.LINE_HEIGHT, new CommonsArrayList <> (aMembers.get (nFamilyIndex + 1))));
      nFamilyIndex += 2;
      bHasLineHeight = true;
    }

    ret.add (_createDeclaration (CCSSProperties.FONT_FAMILY, aMembers.subList (nFamilyIndex, aMembers.size ())));

    // Assign the default values of all properties that are not present
    for (final CSSPropertyWithDefaultValue aSubProp : getAllSubProperties ())
    {
      final ICSSProperty aProperty = aSubProp.getProperty ();
      if (aOpen.contains (aProperty) || (aProperty == CCSSProperties.LINE_HEIGHT && !bHasLineHeight))
        ret.add (_createDeclaration (aProperty,
                                     new CommonsArrayList <> (new CSSExpressionMemberTermSimple (aSubProp.getDefaultValue ()))));
    }
    return ret;
  }
}
