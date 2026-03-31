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

import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.css.CCSS;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents a list of {@link ICSSWriteable} objects. This class emits all
 * contained elements with a semicolon as separator but without any surrounding
 * block elements.
 *
 * @author Philip Helger
 * @param <DATATYPE>
 *        The type of object to be handled.
 * @since 5.0.0
 */
@NotThreadSafe
public class CSSWritableList <DATATYPE extends ICSSWriteable> extends CommonsArrayList <DATATYPE> implements
                             ICSSSourceLocationAware,
                             ICSSWriteable
{
  private transient CSSSourceLocation m_aSourceLocation;

  public CSSWritableList ()
  {}

  @NonNull
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final int nDeclCount = size ();
    if (nDeclCount == 0)
      return "";
    if (nDeclCount == 1)
    {
      // A single element
      final StringBuilder aSB = new StringBuilder ();
      aSB.append (get (0).getAsCSSString (aSettings, nIndentLevel));
      // No ';' at the last entry
      if (!bOptimizedOutput)
        aSB.append (CCSS.DEFINITION_END);
      return aSB.toString ();
    }

    // More than one element
    final StringBuilder aSB = new StringBuilder ();
    int nIndex = 0;
    for (final DATATYPE aElement : this)
    {
      // Indentation
      if (!bOptimizedOutput)
        aSB.append (aSettings.getIndent (nIndentLevel + 1));
      // Emit the main element plus the semicolon
      aSB.append (aElement.getAsCSSString (aSettings, nIndentLevel + 1));
      // No ';' at the last decl
      if (!bOptimizedOutput || nIndex < nDeclCount - 1)
        aSB.append (CCSS.DEFINITION_END);
      if (!bOptimizedOutput)
        aSB.append (aSettings.getNewLineString ());
      ++nIndex;
    }
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
    return super.equals (o);
  }

  @Override
  public int hashCode ()
  {
    return super.hashCode ();
  }

  @Override
  public String toString ()
  {
    return ToStringGenerator.getDerived (super.toString ()).appendIfNotNull ("SourceLocation", m_aSourceLocation).getToString ();
  }
}
