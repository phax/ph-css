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
package com.helger.css.propertyvalue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CCSS;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.property.ECSSProperty;
import com.helger.css.property.ICSSProperty;

/**
 * Represents the combination of a single CSS property ({@link ICSSProperty})
 * and it's according value plus the important state (<code>!important</code> or
 * not). The main purpose of this class to make building a CSS from scratch
 * simpler. When an existing CSS is read the information resides in a
 * {@link com.helger.css.decl.CSSDeclaration} because it contains the
 * declaration value in a more structured form.<br>
 * Instances of this class are mutable since 3.7.3.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSValue implements ICSSValue
{
  public static final boolean DEFAULT_CONSISTENCY_CHECKS_ENABLED = true;

  private static final Logger s_aLogger = LoggerFactory.getLogger (CSSValue.class);
  private static boolean s_bConsistencyChecksEnabled = DEFAULT_CONSISTENCY_CHECKS_ENABLED;

  /**
   * @return <code>true</code> if consistency checks are enabled (by default),
   *         <code>false</code> if not.
   * @since 5.0.2
   */
  public static boolean areConsistencyChecksEnabled ()
  {
    return s_bConsistencyChecksEnabled;
  }

  /**
   * Enable or disable consistency checks. By default the consistency checks are
   * enabled (for backwards compatibility) but if performance is a real matter,
   * you may want to disable them globally.
   * 
   * @param bEnabled
   *        <code>true</code> to enable them, <code>false</code> to disable
   *        them.
   * @since 5.0.2
   */
  public static void setConsistencyChecksEnabled (final boolean bEnabled)
  {
    s_bConsistencyChecksEnabled = bEnabled;
  }

  private ICSSProperty m_aProperty;
  private String m_sValue;
  private boolean m_bIsImportant;

  /**
   * Constructor
   *
   * @param aProperty
   *        The CSS property. May not be <code>null</code>.
   * @param sValue
   *        The String value to use. May be <code>null</code>. The value may
   *        <strong>NOT</strong> contain the <code>!important</code> flag! The
   *        value is internally trimmed to avoid leading and trailing.
   * @param bIsImportant
   *        <code>true</code> if the value should be important,
   *        <code>false</code> otherwise
   */
  public CSSValue (@Nonnull final ICSSProperty aProperty, @Nonnull final String sValue, final boolean bIsImportant)
  {
    setProperty (aProperty);
    setValue (sValue);
    setImportant (bIsImportant);
  }

  /**
   * @return The CSS property used. Never <code>null</code>.
   */
  @Nonnull
  public ICSSProperty getProperty ()
  {
    return m_aProperty;
  }

  /**
   * @return The CSS base property used. Never <code>null</code>.
   */
  @Nonnull
  public ECSSProperty getProp ()
  {
    return m_aProperty.getProp ();
  }

  /**
   * @return The property name including an eventually contained vendor prefix.
   *         Neither <code>null</code> nor empty.
   * @since 3.9.0
   */
  @Nonnull
  @Nonempty
  public String getPropertyName ()
  {
    return m_aProperty.getPropertyName ();
  }

  /**
   * Set the property of this CSS value (e.g. <code>background-color</code>).
   *
   * @param aProperty
   *        The CSS property to set. May not be <code>null</code>.
   * @return this
   * @since 3.7.3
   */
  @Nonnull
  public CSSValue setProperty (@Nonnull final ICSSProperty aProperty)
  {
    m_aProperty = ValueEnforcer.notNull (aProperty, "Property");
    return this;
  }

  /**
   * @return The CSS value used. May not be <code>null</code> but maybe empty.
   */
  @Nonnull
  public String getValue ()
  {
    return m_sValue;
  }

  /**
   * Set the value of this CSS value (e.g. <code>red</code> in case the property
   * is <code>background-color</code>).
   *
   * @param sValue
   *        The value to be set. May not be <code>null</code>. The value may
   *        <strong>NOT</strong> contain the <code>!important</code> flag! The
   *        value is internally trimmed to avoid leading and trailing.
   * @return this
   * @since 3.7.3
   */
  @Nonnull
  public CSSValue setValue (@Nonnull final String sValue)
  {
    ValueEnforcer.notNull (sValue, "Value");

    if (areConsistencyChecksEnabled ())
    {
      if (!m_aProperty.isValidValue (sValue))
        s_aLogger.warn ("CSS: the value '" +
                        sValue +
                        "' is not valid for property '" +
                        m_aProperty.getPropertyName () +
                        "'");
      if (sValue.contains (CCSS.IMPORTANT_SUFFIX))
        s_aLogger.warn ("CSS: the value '" +
                        sValue +
                        "' should not contain the '" +
                        CCSS.IMPORTANT_SUFFIX +
                        "' string! Use 'setImportant' method instead.");
    }
    m_sValue = sValue.trim ();
    return this;
  }

  /**
   * @return <code>true</code> if it is important, <code>false</code> if not
   */
  public boolean isImportant ()
  {
    return m_bIsImportant;
  }

  /**
   * Set the important flag of this value.
   *
   * @param bIsImportant
   *        <code>true</code> to mark it important, <code>false</code> to remove
   *        it.
   * @return this
   * @since 3.7.3
   */
  @Nonnull
  public CSSValue setImportant (final boolean bIsImportant)
  {
    m_bIsImportant = bIsImportant;
    return this;
  }

  @Nonnull
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (m_aProperty);
    return m_aProperty.getPropertyName () +
           CCSS.SEPARATOR_PROPERTY_VALUE +
           m_sValue +
           (StringHelper.hasText (m_sValue) && m_bIsImportant ? CCSS.IMPORTANT_SUFFIX : "") +
           CCSS.DEFINITION_END;
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSValue rhs = (CSSValue) o;
    return m_aProperty.getProp ().equals (rhs.m_aProperty.getProp ()) &&
           m_sValue.equals (rhs.m_sValue) &&
           m_bIsImportant == rhs.m_bIsImportant;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aProperty.getProp ())
                                       .append (m_sValue)
                                       .append (m_bIsImportant)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("property", m_aProperty)
                                       .append ("value", m_sValue)
                                       .append ("important", m_bIsImportant)
                                       .toString ();
  }
}
