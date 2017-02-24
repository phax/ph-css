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
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
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
 * Represents a single @keyframes rule.<br>
 * Example:<br>
 * <code>@keyframes identifier {
  0% { top: 0; left: 0; }
  30% { top: 50px; }
 }</code>
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSKeyframesRule implements ICSSTopLevelRule, ICSSVersionAware, ICSSSourceLocationAware
{
  private final String m_sDeclaration;
  private final String m_sAnimationName;
  private final ICommonsList <CSSKeyframesBlock> m_aBlocks = new CommonsArrayList<> ();
  private CSSSourceLocation m_aSourceLocation;

  public static boolean isValidDeclaration (@Nonnull @Nonempty final String sDeclaration)
  {
    return StringHelper.startsWith (sDeclaration, '@') && StringHelper.endsWithIgnoreCase (sDeclaration, "keyframes");
  }

  public CSSKeyframesRule (@Nonnull @Nonempty final String sDeclaration, @Nonnull @Nonempty final String sAnimationName)
  {
    ValueEnforcer.isTrue (isValidDeclaration (sDeclaration), "Declaration is invalid");
    m_sDeclaration = sDeclaration;
    m_sAnimationName = sAnimationName;
  }

  /**
   * @return The rule declaration string used in the CSS. Neither
   *         <code>null</code> nor empty. Always starting with <code>@</code>
   *         and ending with <code>keyframes</code>.
   */
  @Nonnull
  @Nonempty
  public String getDeclaration ()
  {
    return m_sDeclaration;
  }

  @Nonnull
  @Nonempty
  public String getAnimationName ()
  {
    return m_sAnimationName;
  }

  public boolean hasBlocks ()
  {
    return m_aBlocks.isNotEmpty ();
  }

  @Nonnegative
  public int getBlockCount ()
  {
    return m_aBlocks.size ();
  }

  @Nonnull
  public CSSKeyframesRule addBlock (@Nonnull final CSSKeyframesBlock aKeyframesBlock)
  {
    ValueEnforcer.notNull (aKeyframesBlock, "KeyframesBlock");

    m_aBlocks.add (aKeyframesBlock);
    return this;
  }

  @Nonnull
  public CSSKeyframesRule addBlock (@Nonnegative final int nIndex, @Nonnull final CSSKeyframesBlock aKeyframesBlock)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aKeyframesBlock, "KeyframesBlock");

    if (nIndex >= getBlockCount ())
      m_aBlocks.add (aKeyframesBlock);
    else
      m_aBlocks.add (nIndex, aKeyframesBlock);
    return this;
  }

  @Nonnull
  public EChange removeBlock (@Nonnull final CSSKeyframesBlock aKeyframesBlock)
  {
    return EChange.valueOf (m_aBlocks.remove (aKeyframesBlock));
  }

  @Nonnull
  public EChange removeBlock (@Nonnegative final int nBlockIndex)
  {
    return m_aBlocks.removeAtIndex (nBlockIndex);
  }

  /**
   * Remove all blocks.
   *
   * @return {@link EChange#CHANGED} if any block was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   * @since 3.7.3
   */
  @Nonnull
  public EChange removeAllBlocks ()
  {
    return m_aBlocks.removeAll ();
  }

  @Nullable
  public CSSKeyframesBlock getBlockAtIndex (@Nonnegative final int nBlockIndex)
  {
    return m_aBlocks.getAtIndex (nBlockIndex);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSKeyframesBlock> getAllBlocks ()
  {
    return m_aBlocks.getClone ();
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);

    // Always ignore keyframes rules?
    if (!aSettings.isWriteKeyframesRules ())
      return "";

    if (aSettings.isRemoveUnnecessaryCode () && m_aBlocks.isEmpty ())
      return "";

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final StringBuilder aSB = new StringBuilder (m_sDeclaration);
    aSB.append (' ').append (m_sAnimationName).append (bOptimizedOutput ? "{" : " {");
    if (!bOptimizedOutput)
      aSB.append (aSettings.getNewLineString ());

    // Add all blocks
    for (final CSSKeyframesBlock aBlock : m_aBlocks)
    {
      final String sBlockCSS = aBlock.getAsCSSString (aSettings, nIndentLevel + 1);
      if (StringHelper.hasText (sBlockCSS))
      {
        if (!bOptimizedOutput)
          aSB.append (aSettings.getIndent (nIndentLevel + 1));
        aSB.append (sBlockCSS);
        if (!bOptimizedOutput)
          aSB.append (aSettings.getNewLineString ());
      }
    }
    if (!bOptimizedOutput)
      aSB.append (aSettings.getIndent (nIndentLevel));
    aSB.append ('}');
    if (!bOptimizedOutput)
      aSB.append (aSettings.getNewLineString ());
    return aSB.toString ();
  }

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    return ECSSVersion.CSS30;
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
    final CSSKeyframesRule rhs = (CSSKeyframesRule) o;
    return m_sDeclaration.equals (rhs.m_sDeclaration) &&
           m_sAnimationName.equals (rhs.m_sAnimationName) &&
           m_aBlocks.equals (rhs.m_aBlocks);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sDeclaration)
                                       .append (m_sAnimationName)
                                       .append (m_aBlocks)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("declaration", m_sDeclaration)
                                       .append ("animationName", m_sAnimationName)
                                       .append ("blocks", m_aBlocks)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
