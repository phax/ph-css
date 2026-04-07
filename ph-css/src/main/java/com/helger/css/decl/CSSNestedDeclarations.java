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

import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.state.EChange;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.collection.commons.ICommonsList;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriterSettings;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Represents nested style declarations. When nesting rules, all CSS style declarations after nested rules are wrapped
 * within a nested declarations block, in accordance with the CSS Nesting Module Level 1 specification. A nested
 * declarations instance consists of a number of declarations (the styles to be applied to the selected elements).
 *
 * <p>Example:
 *
 * <pre>div {
  color: red;
  span {
    color: green;
  }
  color: blue;
}</pre>
 *
 * In the above example, <code>color: blue;</code> will be placed inside a nested declarations instances, as a child
 * of a {@link CSSStyleRule}. The resulting object structure will look like this:
 *
 * <ul>
 *     <li>A {@link CSSStyleRule} representing the entire <code>div { ... }</code> block
 *     <ul>
 *         <li>The {@link CSSStyleRule#getAllDeclarations()} with <code>color: red;</code></li>
 *         <li>The {@link CSSStyleRule#getAllRules()} with</li>
 *         <ul>
 *             <li>A nested {@link CSSStyleRule} represent <code>span { color: green; }</code>
 *             <li>A nested {@link CSSNestedDeclarations} representing <code>color: blue;</code></li>
 *         </ul>
 *     </ul>
 * </ul>
 * @author Philip Helger
 * @since 8.2.0
 */
@NotThreadSafe
public class CSSNestedDeclarations implements ICSSNestedRule, IHasCSSDeclarations <CSSNestedDeclarations>, ICSSSourceLocationAware
{
  private final CSSDeclarationContainer m_aDeclarations = new CSSDeclarationContainer ();
  private CSSSourceLocation m_aSourceLocation;

  /**
   * Creates a new, empty instance with no declarations.
   */
  public CSSNestedDeclarations ()
  {}

  @NonNull
  public CSSNestedDeclarations addDeclaration (@NonNull final CSSDeclaration aDeclaration)
  {
    m_aDeclarations.addDeclaration (aDeclaration);
    return this;
  }

  @NonNull
  public CSSNestedDeclarations addDeclaration (@Nonnegative final int nIndex, @NonNull final CSSDeclaration aNewDeclaration)
  {
    m_aDeclarations.addDeclaration (nIndex, aNewDeclaration);
    return this;
  }

  @NonNull
  public EChange removeDeclaration (@NonNull final CSSDeclaration aDeclaration)
  {
    return m_aDeclarations.removeDeclaration (aDeclaration);
  }

  @NonNull
  public EChange removeDeclaration (@Nonnegative final int nDeclarationIndex)
  {
    return m_aDeclarations.removeDeclaration (nDeclarationIndex);
  }

  @NonNull
  public EChange removeAllDeclarations ()
  {
    return m_aDeclarations.removeAllDeclarations ();
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getAllDeclarations ()
  {
    return m_aDeclarations.getAllDeclarations ();
  }

  @Nullable
  public CSSDeclaration getDeclarationAtIndex (@Nonnegative final int nIndex)
  {
    return m_aDeclarations.getDeclarationAtIndex (nIndex);
  }

  @NonNull
  public CSSNestedDeclarations setDeclarationAtIndex (@Nonnegative final int nIndex, @NonNull final CSSDeclaration aNewDeclaration)
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

  @Nullable
  public CSSDeclaration getDeclarationOfPropertyName (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getDeclarationOfPropertyName (sPropertyName);
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSDeclaration> getAllDeclarationsOfPropertyName (@Nullable final String sPropertyName)
  {
    return m_aDeclarations.getAllDeclarationsOfPropertyName (sPropertyName);
  }

  @NonNull
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    // Always ignore nested declarations?
    if (!aSettings.isWriteNestedDeclarations ())
      return "";

    if (aSettings.isRemoveUnnecessaryCode () && !hasDeclarations ())
      return "";

    return m_aDeclarations.getDeclarationsAsCSSString (aSettings, nIndentLevel);
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
    final CSSNestedDeclarations rhs = (CSSNestedDeclarations) o;
    return m_aDeclarations.equals (rhs.m_aDeclarations);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aDeclarations).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("declarations", m_aDeclarations)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
