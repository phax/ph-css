/**
 * Copyright (C) 2014-2016 Philip Helger (www.helger.com)
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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSVersionAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a single <code>@page</code> rule.<br>
 * Example:<br>
 * <code>@page {
  size: auto;
  margin: 10%;
}</code>
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSPageRule implements ICSSTopLevelRule, ICSSVersionAware, ICSSSourceLocationAware
{
  private final List <String> m_aSelectors = new ArrayList <String> ();
  private final CSSWritableList <ICSSPageRuleMember> m_aMembers = new CSSWritableList <ICSSPageRuleMember> ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSPageRule (@Nullable final String sPseudoPage)
  {
    if (StringHelper.hasText (sPseudoPage))
      m_aSelectors.add (sPseudoPage);
  }

  public CSSPageRule (@Nonnull final Iterable <String> aSelectors)
  {
    ValueEnforcer.notNull (aSelectors, "Selectors");
    for (final String sSelector : aSelectors)
      m_aSelectors.add (sSelector);
  }

  @Nonnull
  public List <String> getAllSelectors ()
  {
    return CollectionHelper.newList (m_aSelectors);
  }

  @Nonnull
  public CSSPageRule addMember (@Nonnull final ICSSPageRuleMember aMember)
  {
    m_aMembers.add (aMember);
    return this;
  }

  @Nonnull
  public CSSPageRule addMember (@Nonnegative final int nIndex, @Nonnull final ICSSPageRuleMember aMember)
  {
    m_aMembers.add (nIndex, aMember);
    return this;
  }

  @Nonnull
  public EChange removeMember (@Nonnull final ICSSPageRuleMember aMember)
  {
    return m_aMembers.remove (aMember);
  }

  @Nonnull
  public EChange removeMember (@Nonnegative final int nIndex)
  {
    return m_aMembers.remove (nIndex);
  }

  @Nonnull
  public EChange removeAllMembers ()
  {
    return m_aMembers.removeAll ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <ICSSPageRuleMember> getAllMembers ()
  {
    return m_aMembers.getAll ();
  }

  @Nullable
  public ICSSPageRuleMember getMemberAtIndex (@Nonnegative final int nIndex)
  {
    return m_aMembers.getAtIndex (nIndex);
  }

  @Nonnull
  public CSSPageRule setMemberAtIndex (@Nonnegative final int nIndex, @Nonnull final ICSSPageRuleMember aNewDeclaration)
  {
    m_aMembers.set (nIndex, aNewDeclaration);
    return this;
  }

  public boolean hashMembers ()
  {
    return m_aMembers.isNotEmpty ();
  }

  @Nonnegative
  public int getMemberCount ()
  {
    return m_aMembers.getCount ();
  }

  @Nonnull
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);

    // Always ignore page rules?
    if (!aSettings.isWritePageRules ())
      return "";

    if (aSettings.isRemoveUnnecessaryCode () && m_aMembers.isEmpty ())
      return "";

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final StringBuilder aSB = new StringBuilder ("@page");

    if (!m_aSelectors.isEmpty ())
    {
      aSB.append (' ');
      boolean bFirst = true;
      for (final String sSelector : m_aSelectors)
      {
        if (bFirst)
          bFirst = false;
        else
          aSB.append (bOptimizedOutput ? "," : ", ");
        aSB.append (sSelector);
      }
    }

    final int nDeclCount = m_aMembers.getCount ();
    if (nDeclCount == 0)
    {
      aSB.append (bOptimizedOutput ? "{}" : " {}");
    }
    else
    {
      if (nDeclCount == 1)
      {
        // A single declaration
        aSB.append (bOptimizedOutput ? "{" : " { ");
        aSB.append (m_aMembers.getAsCSSString (aSettings, nIndentLevel));
        aSB.append (bOptimizedOutput ? "}" : " }");
      }
      else
      {
        // More than one declaration
        aSB.append (bOptimizedOutput ? "{" : " {" + aSettings.getNewLineString ());
        aSB.append (m_aMembers.getAsCSSString (aSettings, nIndentLevel));
        if (!bOptimizedOutput)
          aSB.append (aSettings.getIndent (nIndentLevel));
        aSB.append ('}');
      }
    }

    if (!bOptimizedOutput)
      aSB.append (aSettings.getNewLineString ());

    return aSB.toString ();
  }

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    for (final ICSSPageRuleMember aMember : m_aMembers.directGetAll ())
      if (aMember instanceof CSSPageMarginBlock)
        return ECSSVersion.CSS30;
    return ECSSVersion.CSS21;
  }

  public void setSourceLocation (@Nullable final CSSSourceLocation aSourceLocation)
  {
    m_aSourceLocation = aSourceLocation;
  }

  @Nullable
  public CSSSourceLocation getSourceLocation ()
  {
    return m_aSourceLocation;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSPageRule rhs = (CSSPageRule) o;
    return m_aMembers.equals (rhs.m_aMembers);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aMembers).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("declarations", m_aMembers)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .toString ();
  }
}
