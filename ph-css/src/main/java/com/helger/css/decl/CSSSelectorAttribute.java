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
package com.helger.css.decl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;

/**
 * A single CSS selector attribute.
 *
 * @see ECSSAttributeOperator
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSSelectorAttribute implements ICSSSelectorMember, ICSSSourceLocationAware
{
  private final String m_sNamespacePrefix;
  private final String m_sAttrName;
  private final ECSSAttributeOperator m_eOperator;
  private final String m_sAttrValue;
  private CSSSourceLocation m_aSourceLocation;

  private static boolean _isValidNamespacePrefix (@Nullable final String sNamespacePrefix)
  {
    return StringHelper.hasNoText (sNamespacePrefix) || sNamespacePrefix.endsWith ("|");
  }

  public CSSSelectorAttribute (@Nullable final String sNamespacePrefix, @Nonnull @Nonempty final String sAttrName)
  {
    if (!_isValidNamespacePrefix (sNamespacePrefix))
      throw new IllegalArgumentException ("namespacePrefix is illegal!");
    ValueEnforcer.notEmpty (sAttrName, "AttrName");

    m_sNamespacePrefix = sNamespacePrefix;
    m_sAttrName = sAttrName;
    m_eOperator = null;
    m_sAttrValue = null;
  }

  public CSSSelectorAttribute (@Nullable final String sNamespacePrefix,
                               @Nonnull @Nonempty final String sAttrName,
                               @Nonnull final ECSSAttributeOperator eOperator,
                               @Nonnull final String sAttrValue)
  {
    if (!_isValidNamespacePrefix (sNamespacePrefix))
      throw new IllegalArgumentException ("namespacePrefix is illegal!");
    ValueEnforcer.notEmpty (sAttrName, "AttrName");
    ValueEnforcer.notNull (eOperator, "Operator");
    ValueEnforcer.notNull (sAttrValue, "AttrValue");

    m_sNamespacePrefix = sNamespacePrefix;
    m_sAttrName = sAttrName;
    m_eOperator = eOperator;
    m_sAttrValue = sAttrValue;
  }

  @Nullable
  public String getNamespacePrefix ()
  {
    return m_sNamespacePrefix;
  }

  @Nonnull
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

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final StringBuilder aSB = new StringBuilder ();
    aSB.append ('[');
    if (StringHelper.hasText (m_sNamespacePrefix))
      aSB.append (m_sNamespacePrefix);
    aSB.append (m_sAttrName);
    if (m_eOperator != null)
      aSB.append (m_eOperator.getAsCSSString (aSettings, nIndentLevel)).append (m_sAttrValue);
    return aSB.append (']').toString ();
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
    final CSSSelectorAttribute rhs = (CSSSelectorAttribute) o;
    return EqualsHelper.equals (m_sNamespacePrefix, rhs.m_sNamespacePrefix) &&
           m_sAttrName.equals (rhs.m_sAttrName) &&
           EqualsHelper.equals (m_eOperator, rhs.m_eOperator) &&
           EqualsHelper.equals (m_sAttrValue, rhs.m_sAttrValue);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sNamespacePrefix)
                                       .append (m_sAttrName)
                                       .append (m_eOperator)
                                       .append (m_sAttrValue)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).appendIfNotNull ("namespacePrefix", m_sNamespacePrefix)
                                       .append ("attrName", m_sAttrName)
                                       .appendIfNotNull ("operator", m_eOperator)
                                       .appendIfNotNull ("attrValue", m_sAttrValue)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
