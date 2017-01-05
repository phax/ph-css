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
package com.helger.css.decl.shorthand;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.property.ICSSProperty;

/**
 * A descriptor for a property and a default value
 *
 * @author Philip Helger
 * @since 3.7.4
 */
public final class CSSPropertyWithDefaultValue
{
  private final ICSSProperty m_aProperty;
  private final String m_sDefaultValue;

  public CSSPropertyWithDefaultValue (@Nonnull final ICSSProperty aProperty, @Nonnull final String sDefaultValue)
  {
    m_aProperty = ValueEnforcer.notNull (aProperty, "Property");
    m_sDefaultValue = ValueEnforcer.notNull (sDefaultValue, "DefaultValue");

    // Check that default value is valid for property
    if (!aProperty.isValidValue (sDefaultValue))
      throw new IllegalArgumentException ("Default value '" + sDefaultValue + "' does not match property " + aProperty);
  }

  @Nonnull
  public ICSSProperty getProperty ()
  {
    return m_aProperty;
  }

  @Nonnull
  public String getDefaultValue ()
  {
    return m_sDefaultValue;
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("property", m_aProperty)
                                       .append ("defaultValue", m_sDefaultValue)
                                       .toString ();
  }
}
