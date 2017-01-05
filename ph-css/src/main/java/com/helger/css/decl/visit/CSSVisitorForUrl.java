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
package com.helger.css.decl.visit;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.collection.impl.NonBlockingStack;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpression;
import com.helger.css.decl.CSSExpressionMemberTermURI;
import com.helger.css.decl.CSSFontFaceRule;
import com.helger.css.decl.CSSImportRule;
import com.helger.css.decl.CSSKeyframesBlock;
import com.helger.css.decl.CSSKeyframesRule;
import com.helger.css.decl.CSSMediaRule;
import com.helger.css.decl.CSSNamespaceRule;
import com.helger.css.decl.CSSPageMarginBlock;
import com.helger.css.decl.CSSPageRule;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CSSSupportsRule;
import com.helger.css.decl.CSSUnknownRule;
import com.helger.css.decl.CSSViewportRule;
import com.helger.css.decl.ICSSExpressionMember;
import com.helger.css.decl.ICSSTopLevelRule;

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

  public void onDeclaration (@Nonnull final CSSDeclaration aDeclaration)
  {
    final ICSSTopLevelRule aTopLevelRule = m_aTopLevelRule.isEmpty () ? null : m_aTopLevelRule.peek ();
    final CSSExpression aExpr = aDeclaration.getExpression ();
    for (final ICSSExpressionMember aMember : aExpr.getAllMembers ())
      if (aMember instanceof CSSExpressionMemberTermURI)
      {
        final CSSExpressionMemberTermURI aExprTerm = (CSSExpressionMemberTermURI) aMember;
        m_aVisitor.onUrlDeclaration (aTopLevelRule, aDeclaration, aExprTerm);
      }
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
