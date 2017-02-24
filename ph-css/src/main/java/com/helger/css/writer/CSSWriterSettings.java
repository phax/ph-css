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
package com.helger.css.writer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.lang.ICloneable;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.commons.system.ENewLineMode;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSVersionAware;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.utils.CSSURLHelper;

/**
 * This class represents the options required for writing
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSWriterSettings implements ICSSWriterSettings, ICloneable <CSSWriterSettings>
{
  /** By default no optimized output */
  public static final boolean DEFAULT_OPTIMIZED_OUTPUT = false;
  /** By default unnecessary code is not removed */
  public static final boolean DEFAULT_REMOVE_UNNECESSARY_CODE = false;
  /**
   * By default unix line endings are used - for backwards compatibility reasons
   */
  public static final ENewLineMode DEFAULT_NEW_LINE_MODE = ENewLineMode.UNIX;
  /** By default indentation is done by 2 spaces */
  public static final String DEFAULT_INDENT = "  ";
  /** By default URLs are not quoted */
  public static final boolean DEFAULT_QUOTE_URLS = CSSURLHelper.DEFAULT_QUOTE_URLS;
  /** By default namespace rules are written */
  public static final boolean DEFAULT_WRITE_NAMESPACE_RULES = true;
  /** By default font-face rules are written */
  public static final boolean DEFAULT_WRITE_FONT_FACE_RULES = true;
  /** By default keyframes rules are written */
  public static final boolean DEFAULT_WRITE_KEYFRAMES_RULES = true;
  /** By default media rules are written */
  public static final boolean DEFAULT_WRITE_MEDIA_RULES = true;
  /** By default page rules are written */
  public static final boolean DEFAULT_WRITE_PAGE_RULES = true;
  /** By default viewport rules are written */
  public static final boolean DEFAULT_WRITE_VIEWPORT_RULES = true;
  /** By default supports rules are written */
  public static final boolean DEFAULT_WRITE_SUPPORTS_RULES = true;
  /** By default unknown rules are written */
  public static final boolean DEFAULT_WRITE_UNKNOWN_RULES = true;

  private final ECSSVersion m_eVersion;
  private boolean m_bOptimizedOutput;
  private boolean m_bRemoveUnnecessaryCode = DEFAULT_REMOVE_UNNECESSARY_CODE;
  private ENewLineMode m_eNewLineMode = DEFAULT_NEW_LINE_MODE;
  private String m_sIndent = DEFAULT_INDENT;
  private boolean m_bQuoteURLs = DEFAULT_QUOTE_URLS;
  private boolean m_bWriteNamespaceRules = DEFAULT_WRITE_NAMESPACE_RULES;
  private boolean m_bWriteFontFaceRules = DEFAULT_WRITE_FONT_FACE_RULES;
  private boolean m_bWriteKeyframesRules = DEFAULT_WRITE_KEYFRAMES_RULES;
  private boolean m_bWriteMediaRules = DEFAULT_WRITE_MEDIA_RULES;
  private boolean m_bWritePageRules = DEFAULT_WRITE_PAGE_RULES;
  private boolean m_bWriteViewportRules = DEFAULT_WRITE_VIEWPORT_RULES;
  private boolean m_bWriteSupportsRules = DEFAULT_WRITE_SUPPORTS_RULES;
  private boolean m_bWriteUnknownRules = DEFAULT_WRITE_UNKNOWN_RULES;

  /**
   * Default constructor using the latest CSS version and none-optimized output.
   */
  public CSSWriterSettings ()
  {
    this (ECSSVersion.LATEST, DEFAULT_OPTIMIZED_OUTPUT);
  }

  /**
   * @param eVersion
   *        CSS version to emit
   */
  public CSSWriterSettings (@Nonnull final ECSSVersion eVersion)
  {
    this (eVersion, DEFAULT_OPTIMIZED_OUTPUT);
  }

  /**
   * @param eVersion
   *        CSS version to emit
   * @param bOptimizedOutput
   *        if <code>true</code> the output will be optimized for space, else
   *        for readability
   */
  public CSSWriterSettings (@Nonnull final ECSSVersion eVersion, final boolean bOptimizedOutput)
  {
    ValueEnforcer.notNull (eVersion, "Version");

    m_eVersion = eVersion;
    m_bOptimizedOutput = bOptimizedOutput;
  }

  /**
   * Copy constructor.
   *
   * @param aBase
   *        The base settings to copy everything from.
   */
  public CSSWriterSettings (@Nonnull final ICSSWriterSettings aBase)
  {
    ValueEnforcer.notNull (aBase, "Base");

    m_eVersion = aBase.getVersion ();
    m_bOptimizedOutput = aBase.isOptimizedOutput ();
    m_bRemoveUnnecessaryCode = aBase.isRemoveUnnecessaryCode ();
    m_eNewLineMode = aBase.getNewLineMode ();
    m_sIndent = aBase.getIndent (1);
    m_bQuoteURLs = aBase.isQuoteURLs ();
    m_bWriteNamespaceRules = aBase.isWriteNamespaceRules ();
    m_bWriteFontFaceRules = aBase.isWriteFontFaceRules ();
    m_bWriteKeyframesRules = aBase.isWriteKeyframesRules ();
    m_bWriteMediaRules = aBase.isWriteMediaRules ();
    m_bWritePageRules = aBase.isWritePageRules ();
    m_bWriteViewportRules = aBase.isWriteViewportRules ();
    m_bWriteSupportsRules = aBase.isWriteSupportsRules ();
    m_bWriteUnknownRules = aBase.isWriteUnknownRules ();
  }

  @Nonnull
  public final ECSSVersion getVersion ()
  {
    return m_eVersion;
  }

  public final boolean isOptimizedOutput ()
  {
    return m_bOptimizedOutput;
  }

  @Nonnull
  public final CSSWriterSettings setOptimizedOutput (final boolean bOptimizedOutput)
  {
    m_bOptimizedOutput = bOptimizedOutput;
    return this;
  }

  public final boolean isRemoveUnnecessaryCode ()
  {
    return m_bRemoveUnnecessaryCode;
  }

  @Nonnull
  public final CSSWriterSettings setRemoveUnnecessaryCode (final boolean bRemoveUnnecessaryCode)
  {
    m_bRemoveUnnecessaryCode = bRemoveUnnecessaryCode;
    return this;
  }

  @Nonnull
  public final ENewLineMode getNewLineMode ()
  {
    return m_eNewLineMode;
  }

  @Nonnull
  @Nonempty
  public final String getNewLineString ()
  {
    return m_eNewLineMode.getText ();
  }

  @Nonnull
  public final CSSWriterSettings setNewLineMode (@Nonnull final ENewLineMode eNewLineMode)
  {
    m_eNewLineMode = ValueEnforcer.notNull (eNewLineMode, "NewLineMode");
    return this;
  }

  @Nonnull
  public final String getIndent (@Nonnegative final int nCount)
  {
    return StringHelper.getRepeated (m_sIndent, nCount);
  }

  @Nonnull
  public final CSSWriterSettings setIndent (@Nonnull final String sIndent)
  {
    ValueEnforcer.notNull (sIndent, "Indent");

    m_sIndent = sIndent;
    return this;
  }

  public final boolean isQuoteURLs ()
  {
    return m_bQuoteURLs;
  }

  @Nonnull
  public final CSSWriterSettings setQuoteURLs (final boolean bQuoteURLs)
  {
    m_bQuoteURLs = bQuoteURLs;
    return this;
  }

  public final boolean isWriteNamespaceRules ()
  {
    return m_bWriteNamespaceRules;
  }

  @Nonnull
  public final CSSWriterSettings setWriteNamespaceRules (final boolean bWriteNamespaceRules)
  {
    m_bWriteNamespaceRules = bWriteNamespaceRules;
    return this;
  }

  public final boolean isWriteFontFaceRules ()
  {
    return m_bWriteFontFaceRules;
  }

  @Nonnull
  public final CSSWriterSettings setWriteFontFaceRules (final boolean bWriteFontFaceRules)
  {
    m_bWriteFontFaceRules = bWriteFontFaceRules;
    return this;
  }

  public final boolean isWriteKeyframesRules ()
  {
    return m_bWriteKeyframesRules;
  }

  @Nonnull
  public final CSSWriterSettings setWriteKeyframesRules (final boolean bWriteKeyframesRules)
  {
    m_bWriteKeyframesRules = bWriteKeyframesRules;
    return this;
  }

  public final boolean isWriteMediaRules ()
  {
    return m_bWriteMediaRules;
  }

  @Nonnull
  public final CSSWriterSettings setWriteMediaRules (final boolean bWriteMediaRules)
  {
    m_bWriteMediaRules = bWriteMediaRules;
    return this;
  }

  public final boolean isWritePageRules ()
  {
    return m_bWritePageRules;
  }

  @Nonnull
  public final CSSWriterSettings setWritePageRules (final boolean bWritePageRules)
  {
    m_bWritePageRules = bWritePageRules;
    return this;
  }

  public final boolean isWriteViewportRules ()
  {
    return m_bWriteViewportRules;
  }

  @Nonnull
  public final CSSWriterSettings setWriteViewportRules (final boolean bWriteViewportRules)
  {
    m_bWriteViewportRules = bWriteViewportRules;
    return this;
  }

  public final boolean isWriteSupportsRules ()
  {
    return m_bWriteSupportsRules;
  }

  @Nonnull
  public final CSSWriterSettings setWriteSupportsRules (final boolean bWriteSupportsRules)
  {
    m_bWriteSupportsRules = bWriteSupportsRules;
    return this;
  }

  public final boolean isWriteUnknownRules ()
  {
    return m_bWriteUnknownRules;
  }

  @Nonnull
  public final CSSWriterSettings setWriteUnknownRules (final boolean bWriteUnknownRules)
  {
    m_bWriteUnknownRules = bWriteUnknownRules;
    return this;
  }

  public void checkVersionRequirements (@Nonnull final ICSSVersionAware aCSSObject)
  {
    final ECSSVersion eMinCSSVersion = aCSSObject.getMinimumCSSVersion ();
    if (m_eVersion.compareTo (eMinCSSVersion) < 0)
      throw new IllegalStateException ("This object cannot be serialized to CSS version " +
                                       m_eVersion.getVersion ().getAsString () +
                                       " but requires at least " +
                                       eMinCSSVersion.getVersion ().getAsString ());
  }

  @Nonnull
  @ReturnsMutableCopy
  public CSSWriterSettings getClone ()
  {
    return new CSSWriterSettings (this);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSWriterSettings rhs = (CSSWriterSettings) o;
    return m_eVersion.equals (rhs.m_eVersion) &&
           m_bOptimizedOutput == rhs.m_bOptimizedOutput &&
           m_bRemoveUnnecessaryCode == rhs.m_bRemoveUnnecessaryCode &&
           m_eNewLineMode.equals (rhs.m_eNewLineMode) &&
           m_sIndent.equals (rhs.m_sIndent) &&
           m_bQuoteURLs == rhs.m_bQuoteURLs &&
           m_bWriteNamespaceRules == rhs.m_bWriteNamespaceRules &&
           m_bWriteFontFaceRules == rhs.m_bWriteFontFaceRules &&
           m_bWriteKeyframesRules == rhs.m_bWriteKeyframesRules &&
           m_bWriteMediaRules == rhs.m_bWriteMediaRules &&
           m_bWritePageRules == rhs.m_bWritePageRules &&
           m_bWriteViewportRules == rhs.m_bWriteViewportRules &&
           m_bWriteSupportsRules == rhs.m_bWriteSupportsRules &&
           m_bWriteUnknownRules == rhs.m_bWriteUnknownRules;
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_eVersion)
                                       .append (m_bOptimizedOutput)
                                       .append (m_bRemoveUnnecessaryCode)
                                       .append (m_eNewLineMode)
                                       .append (m_sIndent)
                                       .append (m_bQuoteURLs)
                                       .append (m_bWriteNamespaceRules)
                                       .append (m_bWriteFontFaceRules)
                                       .append (m_bWriteKeyframesRules)
                                       .append (m_bWriteMediaRules)
                                       .append (m_bWritePageRules)
                                       .append (m_bWriteViewportRules)
                                       .append (m_bWriteSupportsRules)
                                       .append (m_bWriteUnknownRules)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("version", m_eVersion)
                                       .append ("optimizedOutput", m_bOptimizedOutput)
                                       .append ("removeUnnecessaryCode", m_bRemoveUnnecessaryCode)
                                       .append ("newLineMode", m_eNewLineMode)
                                       .append ("indent", m_sIndent)
                                       .append ("quoteURLs", m_bQuoteURLs)
                                       .append ("writeNamespaceRules", m_bWriteNamespaceRules)
                                       .append ("writeFontFaceRules", m_bWriteFontFaceRules)
                                       .append ("writeKeyframesRules", m_bWriteKeyframesRules)
                                       .append ("writeMediaRules", m_bWriteMediaRules)
                                       .append ("writePageRules", m_bWritePageRules)
                                       .append ("writeViewportRules", m_bWriteViewportRules)
                                       .append ("writeSupportsRules", m_bWriteSupportsRules)
                                       .append ("writeUnknownRules", m_bWriteUnknownRules)
                                       .getToString ();
  }
}
