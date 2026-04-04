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

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.string.StringHelper;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a single <code>@viewport</code> rule.
 *
 * <p>Example:
 *
 * <pre>@property --rotation {
  syntax: '&lt;angle&gt;';
  inherits: false;
  initial-value: 45deg;
}</pre>
 *
 * @author Philip Helger
 * @since 8.2.0
 */
@NotThreadSafe
public class CSSPropertyRule implements ICSSTopLevelRule, ICSSSourceLocationAware
{
  private String m_sName;
  private String m_sInitialValue;
  private Boolean m_bInherits;
  private String m_sSyntax;
  private CSSSourceLocation m_aSourceLocation;

  /**
   * Checks if the passed property name is a valid custom property name. A valid custom property name starts with
   * <code>--</code>.
   * @param sPropertyName The property name to check. May not be <code>null</code> or empty.
   * @return <code>true</code> if the passed property name is valid, <code>false</code> otherwise.
   */
  public static boolean isValidPropertyName (@NonNull @Nonempty final String sPropertyName)
  {
    return StringHelper.startsWith (sPropertyName, "--");
  }

  public CSSPropertyRule(@NonNull @Nonempty final String sPropertyName)
  {
    ValueEnforcer.isTrue (isValidPropertyName (sPropertyName), "Property name is invalid");
    m_sName = sPropertyName;
  }

  /**
   * @return The property name. Neither <code>null</code> nor empty. Always starts with <code>--</code>.
   */
  @NonNull
  @Nonempty
  public String getName ()
  {
    return m_sName;
  }

  public void setName(String name) {
    ValueEnforcer.isTrue (isValidPropertyName (name), "Property name is invalid");
    this.m_sName = name;
  }


  @NonNull
  public String getInitialValue ()
  {
      return m_sInitialValue != null ? m_sInitialValue : "";
  }

  public void setInitialValue(String initialValue) {
      this.m_sInitialValue = initialValue;
  }

  public boolean isInherits ()
  {
      return m_bInherits  != null ? m_bInherits : false;
  }

  public void setInherits(Boolean inherits) {
      this.m_bInherits = inherits;
  }

  @NonNull
  public String getSyntax ()
  {
      return m_sSyntax != null ? m_sSyntax : "";
  }

  public void setSyntax(String syntax) {
      this.m_sSyntax = syntax;
  }

  @Override
  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    // Always ignore property rules?
    if (!aSettings.isWritePropertyRules ())
      return "";

    final boolean bOptimizeOutput = aSettings.isOptimizedOutput ();
    final List<Map.Entry<String, String>> aDeclarations = _buildDeclarations();
    final int nCount = aDeclarations.size ();

    final StringBuilder aSB = new StringBuilder ("@property ");
    aSB.append(m_sName);

    aSB.append (bOptimizeOutput ? "{" : " {");
    if (!bOptimizeOutput && nCount == 1)
      aSB.append (" ");

    int nIndex = 0;
    for (final Map.Entry<String, String> aDeclaration : aDeclarations)
    {
      if (!bOptimizeOutput && nCount > 1)
        aSB.append (aSettings.getNewLineString()).append (aSettings.getIndent (nIndentLevel + 1));
      aSB.append (aDeclaration.getKey ());
      aSB.append (":");
      aSB.append (aDeclaration.getValue ());
      if (!bOptimizeOutput || nIndex != aDeclarations.size() - 1)
        aSB.append (";");
      ++nIndex;
    }

    if (!bOptimizeOutput && nCount > 1)
      aSB.append (aSettings.getNewLineString()).append (aSettings.getIndent (nIndentLevel));
    if (!bOptimizeOutput && nCount == 1)
      aSB.append (" ");
    aSB.append("}");

    return aSB.toString ();
  }

  @Override
  @Nullable
  public final CSSSourceLocation getSourceLocation ()
  {
    return m_aSourceLocation;
  }

  @Override
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
    return m_sName.equals (rhs.m_sName) && m_sInitialValue.equals (rhs.m_sInitialValue) && m_bInherits == rhs.m_bInherits && m_sSyntax.equals (rhs.m_sSyntax);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sName).append (m_bInherits).append(m_sSyntax).append(m_sInitialValue).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("propertyName", m_sName)
                                       .append ("syntax", m_sSyntax)
                                       .append ("inherits", m_bInherits)
                                       .append ("initialValue", m_sInitialValue)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }

  private List<Map.Entry<String, String>> _buildDeclarations()
  {
    final List<Map.Entry<String, String>> ret = new ArrayList<> ();

    if (StringHelper.isNotEmpty(m_sSyntax))
    {
      ret.add (Map.entry ("syntax", m_sSyntax));
    }

    if (m_bInherits != null)
    {
      ret.add (Map.entry ("inherits", Boolean.toString(m_bInherits)));
    }

    if (StringHelper.isNotEmpty(m_sInitialValue))
    {
      ret.add (Map.entry ("initial-value", m_sInitialValue));
    }

    return ret;
  }
}
