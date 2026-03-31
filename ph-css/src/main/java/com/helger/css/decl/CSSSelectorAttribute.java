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
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.equals.EqualsHelper;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.string.StringHelper;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * A single CSS selector attribute.
 *
 * @see ECSSAttributeOperator
 * @see ECSSAttributeCase
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSSelectorAttribute implements ICSSSelectorMember, ICSSSourceLocationAware
{
  private final String m_sNamespacePrefix;
  private final String m_sAttrName;
  private final ECSSAttributeOperator m_eOperator;
  private final String m_sAttrValue;
  private final ECSSAttributeCase m_eAttrCase;
  private CSSSourceLocation m_aSourceLocation;

  private static boolean _isValidNamespacePrefix (@Nullable final String sNamespacePrefix)
  {
    // A namespace prefix may indeed be only the pipe char. Valid values are e.g. "|", "prefix|" or
    // "*|"
    return StringHelper.isEmpty (sNamespacePrefix) || sNamespacePrefix.endsWith ("|");
  }

  public CSSSelectorAttribute (@Nullable final String sNamespacePrefix, @NonNull @Nonempty final String sAttrName)
  {
    if (!_isValidNamespacePrefix (sNamespacePrefix))
      throw new IllegalArgumentException ("NamespacePrefix is illegal!");
    ValueEnforcer.notEmpty (sAttrName, "AttrName");

    m_sNamespacePrefix = sNamespacePrefix;
    m_sAttrName = sAttrName;
    m_eOperator = null;
    m_sAttrValue = null;
    m_eAttrCase = null;
  }

  /**
   * Constructor
   *
   * @param sNamespacePrefix
   *        CSS namespace prefix. May be <code>null</code>.
   * @param sAttrName
   *        The attribute name. May neither be <code>null</code> nor empty
   * @param eOperator
   *        The operator to use. May not be <code>null</code>.
   * @param sAttrValue
   *        The attribute value to select. May not be <code>null</code> but maybe empty.
   * @deprecated Use the constructor with the additional {@link ECSSAttributeCase} instead.
   */
  @Deprecated (forRemoval = true, since = "8.0.1")
  public CSSSelectorAttribute (@Nullable final String sNamespacePrefix,
                               @NonNull @Nonempty final String sAttrName,
                               @NonNull final ECSSAttributeOperator eOperator,
                               @NonNull final String sAttrValue)
  {
    this (sNamespacePrefix, sAttrName, eOperator, sAttrValue, null);
  }

  /**
   * Constructor
   *
   * @param sNamespacePrefix
   *        CSS namespace prefix. May be <code>null</code>.
   * @param sAttrName
   *        The attribute name. May neither be <code>null</code> nor empty
   * @param eOperator
   *        The operator to use. May not be <code>null</code>.
   * @param sAttrValue
   *        The attribute value to select. May not be <code>null</code> but maybe empty.
   * @param eCaseFlag
   *        The case flag to be used specifically for the matching operators.
   */
  public CSSSelectorAttribute (@Nullable final String sNamespacePrefix,
                               @NonNull @Nonempty final String sAttrName,
                               @NonNull final ECSSAttributeOperator eOperator,
                               @NonNull final String sAttrValue,
                               @Nullable final ECSSAttributeCase eCaseFlag)
  {
    if (!_isValidNamespacePrefix (sNamespacePrefix))
      throw new IllegalArgumentException ("NamespacePrefix is illegal!");
    ValueEnforcer.notEmpty (sAttrName, "AttrName");
    ValueEnforcer.notNull (eOperator, "Operator");
    ValueEnforcer.notNull (sAttrValue, "AttrValue");

    m_sNamespacePrefix = sNamespacePrefix;
    m_sAttrName = sAttrName;
    m_eOperator = eOperator;
    m_sAttrValue = sAttrValue;
    m_eAttrCase = eCaseFlag;
  }

  @Nullable
  public String getNamespacePrefix ()
  {
    return m_sNamespacePrefix;
  }

  @NonNull
  @Nonempty
  public String getAttrName ()
  {
    return m_sAttrName;
  }

  @Nullable
  public ECSSAttributeOperator getOperator ()
  {
    return m_eOperator;
  }

  @Nullable
  public String getAttrValue ()
  {
    return m_sAttrValue;
  }

  @Nullable
  public ECSSAttributeCase getCaseSensitivityFlag ()
  {
    return m_eAttrCase;
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final StringBuilder aSB = new StringBuilder ();
    aSB.append ('[');
    if (StringHelper.isNotEmpty (m_sNamespacePrefix))
      aSB.append (m_sNamespacePrefix);
    aSB.append (m_sAttrName);
    if (m_eOperator != null)
    {
      aSB.append (m_eOperator.getAsCSSString (aSettings, nIndentLevel)).append (m_sAttrValue);
      if (m_eAttrCase != null)
        aSB.append (' ').append (m_eAttrCase.getName ());
    }
    return aSB.append (']').toString ();
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
    final CSSSelectorAttribute rhs = (CSSSelectorAttribute) o;
    return EqualsHelper.equals (m_sNamespacePrefix, rhs.m_sNamespacePrefix) &&
           m_sAttrName.equals (rhs.m_sAttrName) &&
           EqualsHelper.equals (m_eOperator, rhs.m_eOperator) &&
           EqualsHelper.equals (m_sAttrValue, rhs.m_sAttrValue) &&
           EqualsHelper.equals (m_eAttrCase, rhs.m_eAttrCase);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sNamespacePrefix)
                                       .append (m_sAttrName)
                                       .append (m_eOperator)
                                       .append (m_sAttrValue)
                                       .append (m_eAttrCase)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).appendIfNotNull ("namespacePrefix", m_sNamespacePrefix)
                                       .append ("attrName", m_sAttrName)
                                       .appendIfNotNull ("operator", m_eOperator)
                                       .appendIfNotNull ("attrValue", m_sAttrValue)
                                       .appendIfNotNull ("caseFlag", m_eAttrCase)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
