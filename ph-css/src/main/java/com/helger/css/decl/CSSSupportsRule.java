/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.state.EChange;
import com.helger.base.string.StringHelper;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSVersionAware;
import com.helger.css.ICSSWriterSettings;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Represents a single <code>@supports</code> rule: a list of style rules only valid when a certain
 * declaration is available. See {@link com.helger.css.ECSSSpecification#CSS3_CONDITIONAL}<br>
 * Example:<br>
 * <code>@supports (transition-property: color) {
  div { color:red; }
}</code>
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSSupportsRule extends AbstractHasTopLevelRules implements
                             ICSSTopLevelRule,
                             ICSSSourceLocationAware,
                             ICSSVersionAware
{
  private final ICommonsList <ICSSSupportsConditionMember> m_aConditionMembers = new CommonsArrayList <> ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSSupportsRule ()
  {}

  public boolean hasSupportConditionMembers ()
  {
    return m_aConditionMembers.isNotEmpty ();
  }

  @Nonnegative
  public int getSupportsConditionMemberCount ()
  {
    return m_aConditionMembers.size ();
  }

  @Nonnull
  public CSSSupportsRule addSupportConditionMember (@Nonnull final ICSSSupportsConditionMember aMember)
  {
    ValueEnforcer.notNull (aMember, "SupportsConditionMember");

    m_aConditionMembers.add (aMember);
    return this;
  }

  @Nonnull
  public CSSSupportsRule addSupportConditionMember (@Nonnegative final int nIndex,
                                                    @Nonnull final ICSSSupportsConditionMember aMember)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aMember, "SupportsConditionMember");

    if (nIndex >= getSupportsConditionMemberCount ())
      m_aConditionMembers.add (aMember);
    else
      m_aConditionMembers.add (nIndex, aMember);
    return this;
  }

  @Nonnull
  public EChange removeSupportsConditionMember (@Nonnull final ICSSSupportsConditionMember aMember)
  {
    return m_aConditionMembers.removeObject (aMember);
  }

  @Nonnull
  public EChange removeSupportsConditionMember (@Nonnegative final int nIndex)
  {
    return m_aConditionMembers.removeAtIndex (nIndex);
  }

  /**
   * Remove all supports condition members.
   *
   * @return {@link EChange#CHANGED} if any supports condition was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  public EChange removeAllSupportsConditionMembers ()
  {
    return m_aConditionMembers.removeAll ();
  }

  @Nullable
  public ICSSSupportsConditionMember getSupportsConditionMemberAtIndex (@Nonnegative final int nIndex)
  {
    return m_aConditionMembers.getAtIndex (nIndex);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <ICSSSupportsConditionMember> getAllSupportConditionMembers ()
  {
    return m_aConditionMembers.getClone ();
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);

    // Always ignore SupportsCondition rules?
    if (!aSettings.isWriteSupportsRules ())
      return "";

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();
    final int nRuleCount = m_aRules.size ();

    if (aSettings.isRemoveUnnecessaryCode () && nRuleCount == 0)
      return "";

    final StringBuilder aSB = new StringBuilder ("@supports ");
    boolean bFirst = true;
    for (final ICSSSupportsConditionMember aCondition : m_aConditionMembers)
    {
      if (bFirst)
        bFirst = false;
      else
        aSB.append (' ');
      aSB.append (aCondition.getAsCSSString (aSettings, nIndentLevel));
    }

    if (nRuleCount == 0)
    {
      aSB.append (bOptimizedOutput ? "{}" : " {}" + aSettings.getNewLineString ());
    }
    else
    {
      // At least one rule present
      aSB.append (bOptimizedOutput ? "{" : " {" + aSettings.getNewLineString ());
      bFirst = true;
      for (final ICSSTopLevelRule aRule : m_aRules)
      {
        final String sRuleCSS = aRule.getAsCSSString (aSettings, nIndentLevel + 1);
        if (StringHelper.isNotEmpty (sRuleCSS))
        {
          if (bFirst)
            bFirst = false;
          else
            if (!bOptimizedOutput)
              aSB.append (aSettings.getNewLineString ());

          if (!bOptimizedOutput)
            aSB.append (aSettings.getIndent (nIndentLevel + 1));
          aSB.append (sRuleCSS);
        }
      }
      if (!bOptimizedOutput)
        aSB.append (aSettings.getIndent (nIndentLevel));
      aSB.append ('}');
      if (!bOptimizedOutput)
        aSB.append (aSettings.getNewLineString ());
    }
    return aSB.toString ();
  }

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    return ECSSVersion.CSS30;
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
    final CSSSupportsRule rhs = (CSSSupportsRule) o;
    return m_aConditionMembers.equals (rhs.m_aConditionMembers) && m_aRules.equals (rhs.m_aRules);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aConditionMembers).append (m_aRules).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("conditionMembers", m_aConditionMembers)
                                       .append ("rules", m_aRules)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
