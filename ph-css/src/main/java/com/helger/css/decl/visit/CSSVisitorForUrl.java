/**
 * Copyright (C) 2014-2021 Philip Helger (www.helger.com)
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
package com.helger.css.decl.visit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.NonBlockingStack;
import com.helger.css.decl.*;

/**
 * A special {@link ICSSVisitor} that is used to extract URLs from the available
 * rules and call the {@link ICSSUrlVisitor} visitor. This visitor effectively
 * only visits URLs that are in import rules and those in declaration
 * expressions.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSVisitorForUrl implements ICSSVisitor
{
  private final ICSSUrlVisitor m_aVisitor;
  private final NonBlockingStack <ICSSTopLevelRule> m_aTopLevelRule = new NonBlockingStack <> ();

  /**
   * Constructor
   *
   * @param aVisitor
   *        The URL visitor to be used. May not be <code>null</code>.
   */
  public CSSVisitorForUrl (@Nonnull final ICSSUrlVisitor aVisitor)
  {
    m_aVisitor = ValueEnforcer.notNull (aVisitor, "Visitor");
  }

  /**
   * @return The URL visitor passed in the constructor. Never <code>null</code>.
   */
  @Nonnull
  public ICSSUrlVisitor getVisitor ()
  {
    return m_aVisitor;
  }

  public void begin ()
  {
    m_aVisitor.begin ();
  }

  public void onImport (@Nonnull final CSSImportRule aImportRule)
  {
    m_aVisitor.onImport (aImportRule);
  }

  public void onNamespace (@Nonnull final CSSNamespaceRule aNamespaceRule)
  {
    // No action
  }

  private void _recursiveCheckMathMember (@Nullable final ICSSTopLevelRule aTopLevelRule,
                                          @Nonnull final CSSDeclaration aDeclaration,
                                          @Nonnull final ICSSExpressionMathMember aMathMember)
  {
    if (aMathMember instanceof CSSExpressionMemberFunction)
    {
      final CSSExpressionMemberFunction aRealMathMember = (CSSExpressionMemberFunction) aMathMember;
      _recursiveCheckExpression (aTopLevelRule, aDeclaration, aRealMathMember.getExpression ());
    }
    else
      if (aMathMember instanceof CSSExpressionMemberMath)
      {
        final CSSExpressionMemberMath aRealMathMember = (CSSExpressionMemberMath) aMathMember;
        for (final ICSSExpressionMathMember aChild : aRealMathMember.getAllMembers ())
          _recursiveCheckMathMember (aTopLevelRule, aDeclaration, aChild);
      }
      else
        if (aMathMember instanceof CSSExpressionMemberMathProduct)
        {
          final CSSExpressionMemberMathProduct aRealMathMember = (CSSExpressionMemberMathProduct) aMathMember;
          for (final ICSSExpressionMathMember aChild : aRealMathMember.getAllMembers ())
            _recursiveCheckMathMember (aTopLevelRule, aDeclaration, aChild);
        }
        else
          if (aMathMember instanceof CSSExpressionMemberMathUnitProduct)
          {
            final CSSExpressionMemberMathUnitProduct aRealMathMember = (CSSExpressionMemberMathUnitProduct) aMathMember;
            _recursiveCheckMathMember (aTopLevelRule, aDeclaration, aRealMathMember.getProduct ());
          }
  }

  private void _recursiveCheckExpression (@Nullable final ICSSTopLevelRule aTopLevelRule,
                                          @Nonnull final CSSDeclaration aDeclaration,
                                          @Nullable final CSSExpression aExpr)
  {
    if (aExpr != null)
      for (final ICSSExpressionMember aMember : aExpr.getAllMembers ())
      {
        if (aMember instanceof CSSExpressionMemberFunction)
        {
          // Recursive
          final CSSExpressionMemberFunction aExprMember = (CSSExpressionMemberFunction) aMember;
          _recursiveCheckExpression (aTopLevelRule, aDeclaration, aExprMember.getExpression ());
        }
        else
          if (aMember instanceof CSSExpressionMemberMath)
          {
            // Recursive
            final CSSExpressionMemberMath aExprMember = (CSSExpressionMemberMath) aMember;
            for (final ICSSExpressionMathMember aMathMember : aExprMember.getAllMembers ())
              _recursiveCheckMathMember (aTopLevelRule, aDeclaration, aMathMember);
          }
          else
            if (aMember instanceof CSSExpressionMemberTermURI)
            {
              final CSSExpressionMemberTermURI aExprTerm = (CSSExpressionMemberTermURI) aMember;
              m_aVisitor.onUrlDeclaration (aTopLevelRule, aDeclaration, aExprTerm);
            }
      }
  }

  public void onDeclaration (@Nonnull final CSSDeclaration aDeclaration)
  {
    final ICSSTopLevelRule aTopLevelRule = m_aTopLevelRule.isEmpty () ? null : m_aTopLevelRule.peek ();
    _recursiveCheckExpression (aTopLevelRule, aDeclaration, aDeclaration.getExpression ());
  }

  public void onBeginStyleRule (@Nonnull final CSSStyleRule aStyleRule)
  {
    m_aTopLevelRule.push (aStyleRule);
  }

  public void onStyleRuleSelector (@Nonnull final CSSSelector aSelector)
  {
    // no action
  }

  public void onEndStyleRule (@Nonnull final CSSStyleRule aStyleRule)
  {
    m_aTopLevelRule.pop ();
  }

  public void onBeginPageRule (@Nonnull final CSSPageRule aPageRule)
  {
    m_aTopLevelRule.push (aPageRule);
  }

  public void onBeginPageMarginBlock (@Nonnull final CSSPageMarginBlock aPageMarginBlock)
  {
    // no action
  }

  public void onEndPageMarginBlock (@Nonnull final CSSPageMarginBlock aPageMarginBlock)
  {
    // no action
  }

  public void onEndPageRule (@Nonnull final CSSPageRule aPageRule)
  {
    m_aTopLevelRule.pop ();
  }

  public void onBeginFontFaceRule (@Nonnull final CSSFontFaceRule aFontFaceRule)
  {
    m_aTopLevelRule.push (aFontFaceRule);
  }

  public void onEndFontFaceRule (@Nonnull final CSSFontFaceRule aFontFaceRule)
  {
    m_aTopLevelRule.pop ();
  }

  public void onBeginMediaRule (@Nonnull final CSSMediaRule aMediaRule)
  {
    m_aTopLevelRule.push (aMediaRule);
  }

  public void onEndMediaRule (@Nonnull final CSSMediaRule aMediaRule)
  {
    m_aTopLevelRule.pop ();
  }

  public void onBeginKeyframesRule (@Nonnull final CSSKeyframesRule aKeyframesRule)
  {
    m_aTopLevelRule.push (aKeyframesRule);
  }

  public void onBeginKeyframesBlock (@Nonnull final CSSKeyframesBlock aKeyframesBlock)
  {
    // no action
  }

  public void onEndKeyframesBlock (@Nonnull final CSSKeyframesBlock aKeyframesBlock)
  {
    // no action
  }

  public void onEndKeyframesRule (@Nonnull final CSSKeyframesRule aKeyframesRule)
  {
    m_aTopLevelRule.pop ();
  }

  public void onBeginViewportRule (@Nonnull final CSSViewportRule aViewportRule)
  {
    m_aTopLevelRule.push (aViewportRule);
  }

  public void onEndViewportRule (@Nonnull final CSSViewportRule aViewportRule)
  {
    m_aTopLevelRule.pop ();
  }

  public void onBeginSupportsRule (@Nonnull final CSSSupportsRule aSupportsRule)
  {
    m_aTopLevelRule.push (aSupportsRule);
  }

  public void onEndSupportsRule (@Nonnull final CSSSupportsRule aSupportsRule)
  {
    m_aTopLevelRule.pop ();
  }

  public void onUnknownRule (@Nonnull final CSSUnknownRule aUnknownRule)
  {
    // no action
  }

  public void end ()
  {
    m_aVisitor.end ();
  }
}
