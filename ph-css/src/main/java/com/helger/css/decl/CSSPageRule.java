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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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
import com.helger.css.ICSSSourceLocationAware;
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
public class CSSPageRule implements ICSSTopLevelRule, ICSSSourceLocationAware
{
  private final ICommonsList <String> m_aSelectors;
  private final CSSWritableList <ICSSPageRuleMember> m_aMembers = new CSSWritableList <> ();
  private CSSSourceLocation m_aSourceLocation;

  public CSSPageRule (@Nullable final String sPseudoPage)
  {
    m_aSelectors = StringHelper.isNotEmpty (sPseudoPage) ? new CommonsArrayList <> (sPseudoPage)
                                                         : new CommonsArrayList <> ();
  }

  public CSSPageRule (@NonNull final Iterable <String> aSelectors)
  {
    ValueEnforcer.notNullNoNullValue (aSelectors, "Selectors");
    m_aSelectors = new CommonsArrayList <> (aSelectors);
  }

  @NonNull
  public ICommonsList <String> getAllSelectors ()
  {
    return m_aSelectors.getClone ();
  }

  @NonNull
  public CSSPageRule addMember (@NonNull final ICSSPageRuleMember aMember)
  {
    m_aMembers.add (aMember);
    return this;
  }

  @NonNull
  public CSSPageRule addMember (@Nonnegative final int nIndex, @NonNull final ICSSPageRuleMember aMember)
  {
    m_aMembers.add (nIndex, aMember);
    return this;
  }

  @NonNull
  public EChange removeMember (@NonNull final ICSSPageRuleMember aMember)
  {
    return m_aMembers.removeObject (aMember);
  }

  @NonNull
  public EChange removeMember (@Nonnegative final int nIndex)
  {
    return m_aMembers.removeAtIndex (nIndex);
  }

  @NonNull
  public EChange removeAllMembers ()
  {
    return m_aMembers.removeAll ();
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <ICSSPageRuleMember> getAllMembers ()
  {
    return m_aMembers.getClone ();
  }

  @Nullable
  public ICSSPageRuleMember getMemberAtIndex (@Nonnegative final int nIndex)
  {
    return m_aMembers.getAtIndex (nIndex);
  }

  @NonNull
  public CSSPageRule setMemberAtIndex (@Nonnegative final int nIndex, @NonNull final ICSSPageRuleMember aNewDeclaration)
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
    return m_aMembers.size ();
  }

  @NonNull
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    // Always ignore page rules?
    if (!aSettings.isWritePageRules ())
      return "";

    if (aSettings.isRemoveUnnecessaryCode () && m_aMembers.isEmpty ())
      return "";

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final StringBuilder aSB = new StringBuilder ("@page");

    if (m_aSelectors.isNotEmpty ())
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

    final int nDeclCount = m_aMembers.size ();
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
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
