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
package com.helger.css.media;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.clone.ICloneable;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.state.EChange;
import com.helger.base.string.StringImplode;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.collection.commons.CommonsLinkedHashSet;
import com.helger.collection.commons.ICommonsOrderedSet;

/**
 * Manages an ordered set of {@link ECSSMedium} objects.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSMediaList implements ICSSMediaList, ICloneable <CSSMediaList>
{
  public static final String DEFAULT_MEDIA_STRING_SEPARATOR = ", ";

  // Ordered but unique
  private final ICommonsOrderedSet <ECSSMedium> m_aMedia = new CommonsLinkedHashSet <> ();

  /**
   * Constructor
   */
  public CSSMediaList ()
  {}

  /**
   * Constructor with a single medium
   *
   * @param eMedium
   *        The medium to be added. May not be <code>null</code>.
   */
  public CSSMediaList (@NonNull final ECSSMedium eMedium)
  {
    addMedium (eMedium);
  }

  /**
   * Constructor with an array of media.
   *
   * @param aMedia
   *        The array of media to be added. The array may be <code>null</code> but it may not
   *        contain <code>null</code> elements.
   */
  public CSSMediaList (@Nullable final ECSSMedium... aMedia)
  {
    if (aMedia != null)
      for (final ECSSMedium eMedium : aMedia)
        addMedium (eMedium);
  }

  /**
   * Constructor with a collection of media.
   *
   * @param aMedia
   *        The collection of media to be added. The collection may be <code>null</code> but it may
   *        not contain <code>null</code> elements.
   */
  public CSSMediaList (@Nullable final Iterable <ECSSMedium> aMedia)
  {
    if (aMedia != null)
      for (final ECSSMedium eMedium : aMedia)
        addMedium (eMedium);
  }

  /**
   * Constructor using another media list.
   *
   * @param aOther
   *        The object to copy from. May not be <code>null</code>.
   * @since 3.8.3
   */
  public CSSMediaList (@NonNull final ICSSMediaList aOther)
  {
    ValueEnforcer.notNull (aOther, "Other");
    m_aMedia.addAll (aOther.getAllMedia ());
  }

  /**
   * Copy constructor.
   *
   * @param aOther
   *        The object to copy from. May not be <code>null</code>.
   */
  public CSSMediaList (@NonNull final CSSMediaList aOther)
  {
    ValueEnforcer.notNull (aOther, "Other");
    m_aMedia.addAll (aOther.m_aMedia);
  }

  /**
   * Add a new medium to the list
   *
   * @param eMedium
   *        The medium to be added. May not be <code>null</code>.
   * @return <code>this</code>
   */
  @NonNull
  public CSSMediaList addMedium (@NonNull final ECSSMedium eMedium)
  {
    ValueEnforcer.notNull (eMedium, "Medium");

    m_aMedia.add (eMedium);
    return this;
  }

  /**
   * Add a media list to the list
   *
   * @param aMediaList
   *        The media list to be added. May not be <code>null</code>.
   * @return <code>this</code>
   * @since 3.8.3
   */
  @NonNull
  public CSSMediaList addMedia (@NonNull final ECSSMedium... aMediaList)
  {
    ValueEnforcer.notNull (aMediaList, "MediaList");

    m_aMedia.addAll (aMediaList);
    return this;
  }

  /**
   * Add a media list to the list
   *
   * @param aMediaList
   *        The media list to be added. May not be <code>null</code>.
   * @return <code>this</code>
   * @since 3.8.3
   */
  @NonNull
  public CSSMediaList addMedia (@NonNull final ICSSMediaList aMediaList)
  {
    ValueEnforcer.notNull (aMediaList, "MediaList");

    m_aMedia.addAll (aMediaList.getAllMedia ());
    return this;
  }

  /**
   * Add a media list to the list
   *
   * @param aMediaList
   *        The media list to be added. May not be <code>null</code>.
   * @return <code>this</code>
   * @since 3.8.3
   */
  @NonNull
  public CSSMediaList addMedia (@NonNull final Iterable <ECSSMedium> aMediaList)
  {
    ValueEnforcer.notNull (aMediaList, "MediaList");

    m_aMedia.addAll (aMediaList);
    return this;
  }

  /**
   * Remove the passed medium
   *
   * @param eMedium
   *        The medium to be removed. May be <code>null</code>.
   * @return {@link EChange#CHANGED} if the medium was removed, {@link EChange#UNCHANGED} otherwise.
   */
  @NonNull
  public EChange removeMedium (@Nullable final ECSSMedium eMedium)
  {
    return m_aMedia.removeObject (eMedium);
  }

  /**
   * Remove all media.
   *
   * @return {@link EChange#CHANGED} if any medium was removed, {@link EChange#UNCHANGED} otherwise.
   *         Never <code>null</code>.
   * @since 3.7.3
   */
  @NonNull
  public EChange removeAllMedia ()
  {
    return m_aMedia.removeAll ();
  }

  @Nonnegative
  public int getMediaCount ()
  {
    return m_aMedia.size ();
  }

  public boolean hasNoMedia ()
  {
    return m_aMedia.isEmpty ();
  }

  public boolean containsMedium (@Nullable final ECSSMedium eMedium)
  {
    return m_aMedia.contains (eMedium);
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsOrderedSet <ECSSMedium> getAllMedia ()
  {
    return m_aMedia.getClone ();
  }

  @NonNull
  public String getMediaString (@NonNull final String sSeparator)
  {
    ValueEnforcer.notNull (sSeparator, "Separator");

    if (m_aMedia.isEmpty ())
      return "";

    return StringImplode.getImplodedMapped (sSeparator, m_aMedia, ECSSMedium::getName);
  }

  @NonNull
  @ReturnsMutableCopy
  public CSSMediaList getClone ()
  {
    return new CSSMediaList (this);
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSMediaList rhs = (CSSMediaList) o;
    return m_aMedia.equals (rhs.m_aMedia);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aMedia).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("media", m_aMedia).getToString ();
  }

  @NonNull
  @ReturnsMutableCopy
  public static CSSMediaList createOnDemand (@Nullable final ICSSMediaList aMediaList)
  {
    return aMediaList == null ? new CSSMediaList () : new CSSMediaList (aMediaList);
  }
}
