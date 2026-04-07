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
package com.helger.css.decl;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.state.EChange;
import com.helger.base.string.StringHelper;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.collection.commons.ICommonsList;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single <code>@property</code> rule: a list of style rules only valid when a certain
 * declaration is available. See {@link com.helger.css.ECSSSpecification#CSS3_CONDITIONAL}<br>
 * Example:<br>
 * <code>@supports (transition-property: color) {
  div { color:red; }
}</code>
 *
 * @author Mike Wiedenbauer
 */
public class CSSPropertyRule extends AbstractHasTopLevelRules implements ICSSTopLevelRule, ICSSSourceLocationAware
{ 
  private final String m_sIdentifier;
  private final CSSPropertyRuleDeclarationList m_aDeclarations = new CSSPropertyRuleDeclarationList();
  private CSSSourceLocation m_aSourceLocation;

  public static boolean isValidIdentifier (@NonNull @Nonempty final String sIdentifier)
  {
    return StringHelper.startsWith (sIdentifier, "--");
  }

  public CSSPropertyRule (@NonNull @Nonempty final String sIdentifier)
  {
    ValueEnforcer.isTrue (isValidIdentifier (sIdentifier), "Identifier is invalid");
    m_sIdentifier = sIdentifier;
  }

  @NonNull
  @Nonempty
  public String getIdentifier ()
  {
    return m_sIdentifier;
  }

  @NonNull
  public CSSPropertyRule addDeclaration (@NonNull final CSSPropertyRuleDeclaration aDeclaration)
  {
    ValueEnforcer.notNull (aDeclaration, "PropertyRuleDeclaration");

    m_aDeclarations.addDeclaration (aDeclaration);
    return this;
  }

  @NonNull
  public CSSPropertyRule addDeclaration (@Nonnegative final int nIndex, @NonNull final CSSPropertyRuleDeclaration aDeclaration)
  {
    ValueEnforcer.isGE0(nIndex, "Index");
    ValueEnforcer.notNull (aDeclaration, "PropertyRuleDeclaration");

    m_aDeclarations.addDeclaration (nIndex, aDeclaration);
    return this;
  }

  @NonNull
  public EChange removeDeclaration (@NonNull final CSSPropertyRuleDeclaration aDeclaration)
  {
    return m_aDeclarations.removeDeclaration (aDeclaration);
  }

  @NonNull
  public EChange removeDeclaration (@Nonnegative final int nIndex)
  {
    return m_aDeclarations.removeDeclaration (nIndex);
  }

  @NonNull
  public EChange removeAllDeclarations ()
  {
    return m_aDeclarations.removeAllDeclarations ();
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSPropertyRuleDeclaration> getAllDeclarations ()
  {
    return m_aDeclarations.getAllDeclarations();
  }
  
  @Nullable
  public CSSPropertyRuleDeclaration getDeclarationAtIndex (@Nonnegative final int nIndex)
  {
    return m_aDeclarations.getDeclarationAtIndex (nIndex);
  }

  @NonNull
  public CSSPropertyRule setDeclarationAtIndex (@Nonnegative final int nIndex, @NonNull final CSSPropertyRuleDeclaration aNewDeclaration)
  {
    m_aDeclarations.setDeclarationAtIndex (nIndex, aNewDeclaration);
    return this;
  }

  public boolean hasDeclarations ()
  {
    return m_aDeclarations.hasDeclarations ();
  }

  @Nonnegative
  public int getDeclarationCount ()
  {
    return m_aDeclarations.getDeclarationCount ();
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    // Always ignore Property rules?
    if (!aSettings.isWritePropertyRules ())
      return "";

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();
    final int nDeclCount = m_aDeclarations.getDeclarationCount();

    final StringBuilder aSB = new StringBuilder ("@property ").append (m_sIdentifier);
    if (nDeclCount == 0)
    {
      aSB.append (bOptimizedOutput ? "{}" : " {}" + aSettings.getNewLineString ());
    }
    else
    {
      if (nDeclCount == 1)
      {
        aSB.append (bOptimizedOutput ? "{" : " { ");
        aSB.append (m_aDeclarations.getAsCSSString (aSettings, nIndentLevel));
        aSB.append (bOptimizedOutput ? "}" : " }");
      }
      else
      {
        aSB.append (bOptimizedOutput ? "{" : " {" + aSettings.getNewLineString ());
        aSB.append (m_aDeclarations.getAsCSSString (aSettings, nIndentLevel));
        if (!bOptimizedOutput)
          aSB.append (aSettings.getIndent (nIndentLevel));
        aSB.append ('}');
      }
    }
    return aSB.toString();
  }

  @Nullable
  public final CSSSourceLocation getSourceLocation ()
  {
    return m_aSourceLocation;
  }

  public final void setSourceLocation (@Nullable final CSSSourceLocation aSourceLocation)
  {
    m_aSourceLocation = aSourceLocation;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSPropertyRule rhs = (CSSPropertyRule) o;
    return m_sIdentifier.equals (rhs.m_sIdentifier) && m_aDeclarations.equals (rhs.m_aDeclarations);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sIdentifier).append (m_aDeclarations).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("identifier", m_sIdentifier)
                                       .append ("declaration", m_aDeclarations)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
