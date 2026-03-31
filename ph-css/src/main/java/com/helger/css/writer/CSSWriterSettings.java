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
package com.helger.css.writer;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.clone.ICloneable;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.string.StringHelper;
import com.helger.base.system.ENewLineMode;
import com.helger.base.tostring.ToStringGenerator;
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

  /**
   * Default CSS writer settings to be used for simplified APIs. Must be the last constant - order
   * matters.
   *
   * @since 6.0.0
   */
  public static final ICSSWriterSettings DEFAULT_SETTINGS = new CSSWriterSettings ();

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
    this (DEFAULT_OPTIMIZED_OUTPUT);
  }

  /**
   * @param bOptimizedOutput
   *        if <code>true</code> the output will be optimized for space, else for readability
   */
  public CSSWriterSettings (final boolean bOptimizedOutput)
  {
    setOptimizedOutput (bOptimizedOutput);
  }

  /**
   * Copy constructor.
   *
   * @param aBase
   *        The base settings to copy everything from.
   */
  public CSSWriterSettings (@NonNull final ICSSWriterSettings aBase)
  {
    ValueEnforcer.notNull (aBase, "Base");

    setOptimizedOutput (aBase.isOptimizedOutput ());
    setRemoveUnnecessaryCode (aBase.isRemoveUnnecessaryCode ());
    setNewLineMode (aBase.getNewLineMode ());
    setIndent (aBase.getIndent (1));
    setQuoteURLs (aBase.isQuoteURLs ());
    setWriteNamespaceRules (aBase.isWriteNamespaceRules ());
    setWriteFontFaceRules (aBase.isWriteFontFaceRules ());
    setWriteKeyframesRules (aBase.isWriteKeyframesRules ());
    setWriteMediaRules (aBase.isWriteMediaRules ());
    setWritePageRules (aBase.isWritePageRules ());
    setWriteViewportRules (aBase.isWriteViewportRules ());
    setWriteSupportsRules (aBase.isWriteSupportsRules ());
    setWriteUnknownRules (aBase.isWriteUnknownRules ());
  }

  public final boolean isOptimizedOutput ()
  {
    return m_bOptimizedOutput;
  }

  @NonNull
  public final CSSWriterSettings setOptimizedOutput (final boolean bOptimizedOutput)
  {
    m_bOptimizedOutput = bOptimizedOutput;
    return this;
  }

  public final boolean isRemoveUnnecessaryCode ()
  {
    return m_bRemoveUnnecessaryCode;
  }

  @NonNull
  public final CSSWriterSettings setRemoveUnnecessaryCode (final boolean bRemoveUnnecessaryCode)
  {
    m_bRemoveUnnecessaryCode = bRemoveUnnecessaryCode;
    return this;
  }

  @NonNull
  public final ENewLineMode getNewLineMode ()
  {
    return m_eNewLineMode;
  }

  @NonNull
  @Nonempty
  public final String getNewLineString ()
  {
    return m_eNewLineMode.getText ();
  }

  @NonNull
  public final CSSWriterSettings setNewLineMode (@NonNull final ENewLineMode eNewLineMode)
  {
    m_eNewLineMode = ValueEnforcer.notNull (eNewLineMode, "NewLineMode");
    return this;
  }

  @NonNull
  public final String getIndent (@Nonnegative final int nCount)
  {
    return StringHelper.getRepeated (m_sIndent, nCount);
  }

  @NonNull
  public final CSSWriterSettings setIndent (@NonNull final String sIndent)
  {
    ValueEnforcer.notNull (sIndent, "Indent");

    m_sIndent = sIndent;
    return this;
  }

  public final boolean isQuoteURLs ()
  {
    return m_bQuoteURLs;
  }

  @NonNull
  public final CSSWriterSettings setQuoteURLs (final boolean bQuoteURLs)
  {
    m_bQuoteURLs = bQuoteURLs;
    return this;
  }

  public final boolean isWriteNamespaceRules ()
  {
    return m_bWriteNamespaceRules;
  }

  @NonNull
  public final CSSWriterSettings setWriteNamespaceRules (final boolean bWriteNamespaceRules)
  {
    m_bWriteNamespaceRules = bWriteNamespaceRules;
    return this;
  }

  public final boolean isWriteFontFaceRules ()
  {
    return m_bWriteFontFaceRules;
  }

  @NonNull
  public final CSSWriterSettings setWriteFontFaceRules (final boolean bWriteFontFaceRules)
  {
    m_bWriteFontFaceRules = bWriteFontFaceRules;
    return this;
  }

  public final boolean isWriteKeyframesRules ()
  {
    return m_bWriteKeyframesRules;
  }

  @NonNull
  public final CSSWriterSettings setWriteKeyframesRules (final boolean bWriteKeyframesRules)
  {
    m_bWriteKeyframesRules = bWriteKeyframesRules;
    return this;
  }

  public final boolean isWriteMediaRules ()
  {
    return m_bWriteMediaRules;
  }

  @NonNull
  public final CSSWriterSettings setWriteMediaRules (final boolean bWriteMediaRules)
  {
    m_bWriteMediaRules = bWriteMediaRules;
    return this;
  }

  public final boolean isWritePageRules ()
  {
    return m_bWritePageRules;
  }

  @NonNull
  public final CSSWriterSettings setWritePageRules (final boolean bWritePageRules)
  {
    m_bWritePageRules = bWritePageRules;
    return this;
  }

  public final boolean isWriteViewportRules ()
  {
    return m_bWriteViewportRules;
  }

  @NonNull
  public final CSSWriterSettings setWriteViewportRules (final boolean bWriteViewportRules)
  {
    m_bWriteViewportRules = bWriteViewportRules;
    return this;
  }

  public final boolean isWriteSupportsRules ()
  {
    return m_bWriteSupportsRules;
  }

  @NonNull
  public final CSSWriterSettings setWriteSupportsRules (final boolean bWriteSupportsRules)
  {
    m_bWriteSupportsRules = bWriteSupportsRules;
    return this;
  }

  public final boolean isWriteUnknownRules ()
  {
    return m_bWriteUnknownRules;
  }

  @NonNull
  public final CSSWriterSettings setWriteUnknownRules (final boolean bWriteUnknownRules)
  {
    m_bWriteUnknownRules = bWriteUnknownRules;
    return this;
  }

  @NonNull
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
    return m_bOptimizedOutput == rhs.m_bOptimizedOutput &&
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
    return new HashCodeGenerator (this).append (m_bOptimizedOutput)
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
    return new ToStringGenerator (this).append ("OptimizedOutput", m_bOptimizedOutput)
                                       .append ("RemoveUnnecessaryCode", m_bRemoveUnnecessaryCode)
                                       .append ("NewLineMode", m_eNewLineMode)
                                       .append ("Indent", m_sIndent)
                                       .append ("QuoteURLs", m_bQuoteURLs)
                                       .append ("WriteNamespaceRules", m_bWriteNamespaceRules)
                                       .append ("WriteFontFaceRules", m_bWriteFontFaceRules)
                                       .append ("WriteKeyframesRules", m_bWriteKeyframesRules)
                                       .append ("WriteMediaRules", m_bWriteMediaRules)
                                       .append ("WritePageRules", m_bWritePageRules)
                                       .append ("WriteViewportRules", m_bWriteViewportRules)
                                       .append ("WriteSupportsRules", m_bWriteSupportsRules)
                                       .append ("WriteUnknownRules", m_bWriteUnknownRules)
                                       .getToString ();
  }
}
