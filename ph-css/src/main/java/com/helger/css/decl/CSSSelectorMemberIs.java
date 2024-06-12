package com.helger.css.decl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.state.EChange;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSVersionAware;
import com.helger.css.ICSSWriterSettings;

/**
 * Represents complex CSS selector as used for the ":is()" CSS pseudo
 * class function.
 *
 * @author Mike Wiedenauer
 * @author Philip Helger
 * @since 7.0.3
 */
@NotThreadSafe
public class CSSSelectorMemberIs implements ICSSSelectorMember, ICSSVersionAware, ICSSSourceLocationAware
{
  private final ICommonsList <CSSSelector> m_aNestedSelectors;
  private CSSSourceLocation m_aSourceLocation;

  public CSSSelectorMemberIs (@Nonnull final CSSSelector aNestedSelector)
  {
    ValueEnforcer.notNull (aNestedSelector, "NestedSelector");
    m_aNestedSelectors = new CommonsArrayList <> (aNestedSelector);
  }

  public CSSSelectorMemberIs (@Nonnull final CSSSelector... aNestedSelectors)
  {
    ValueEnforcer.notNull (aNestedSelectors, "NestedSelectors");
    m_aNestedSelectors = new CommonsArrayList <> (aNestedSelectors);
  }

  public CSSSelectorMemberIs (@Nonnull final Iterable <CSSSelector> aNestedSelectors)
  {
    ValueEnforcer.notNull (aNestedSelectors, "NestedSelectors");
    m_aNestedSelectors = new CommonsArrayList <> (aNestedSelectors);
  }

  public boolean hasSelectors ()
  {
    return m_aNestedSelectors.isNotEmpty ();
  }

  @Nonnegative
  public int getSelectorCount ()
  {
    return m_aNestedSelectors.size ();
  }

  @Nonnull
  public CSSSelectorMemberIs addSelector (@Nonnull final ICSSSelectorMember aSingleSelectorMember)
  {
    ValueEnforcer.notNull (aSingleSelectorMember, "SingleSelectorMember");

    return addSelector (new CSSSelector ().addMember (aSingleSelectorMember));
  }

  @Nonnull
  public CSSSelectorMemberIs addSelector (@Nonnull final CSSSelector aSelector)
  {
    ValueEnforcer.notNull (aSelector, "Selector");

    m_aNestedSelectors.add (aSelector);
    return this;
  }

  @Nonnull
  public CSSSelectorMemberIs addSelector (@Nonnegative final int nIndex, @Nonnull final ICSSSelectorMember aSingleSelectorMember)
  {
    ValueEnforcer.notNull (aSingleSelectorMember, "SingleSelectorMember");

    return addSelector (nIndex, new CSSSelector ().addMember (aSingleSelectorMember));
  }

  @Nonnull
  public CSSSelectorMemberIs addSelector (@Nonnegative final int nIndex, @Nonnull final CSSSelector aSelector)
  {
    ValueEnforcer.isGE0 (nIndex, "Index");
    ValueEnforcer.notNull (aSelector, "Selector");

    if (nIndex >= getSelectorCount ())
      m_aNestedSelectors.add (aSelector);
    else
      m_aNestedSelectors.add (nIndex, aSelector);
    return this;
  }

  @Nonnull
  public EChange removeSelector (@Nonnull final CSSSelector aSelector)
  {
    return m_aNestedSelectors.removeObject (aSelector);
  }

  @Nonnull
  public EChange removeSelector (@Nonnegative final int nSelectorIndex)
  {
    return m_aNestedSelectors.removeAtIndex (nSelectorIndex);
  }

  /**
   * Remove all selectors.
   *
   * @return {@link EChange#CHANGED} if any selector was removed,
   *         {@link EChange#UNCHANGED} otherwise. Never <code>null</code>.
   */
  @Nonnull
  public EChange removeAllSelectors ()
  {
    return m_aNestedSelectors.removeAll ();
  }

  @Nullable
  public CSSSelector getSelectorAtIndex (@Nonnegative final int nSelectorIndex)
  {
    return m_aNestedSelectors.getAtIndex (nSelectorIndex);
  }

  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSSelector> getAllSelectors ()
  {
    return m_aNestedSelectors.getClone ();
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();
    final StringBuilder aSB = new StringBuilder (":is(");
    boolean bFirst = true;
    for (final CSSSelector aNestedSelector : m_aNestedSelectors)
    {
      if (bFirst)
        bFirst = false;
      else
        aSB.append (bOptimizedOutput ? "," : ", ");
      aSB.append (aNestedSelector.getAsCSSString (aSettings, 0));
    }
    return aSB.append (')').toString ();
  }

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    return ECSSVersion.CSS30;
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
    final CSSSelectorMemberIs rhs = (CSSSelectorMemberIs) o;
    return m_aNestedSelectors.equals (rhs.m_aNestedSelectors);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aNestedSelectors).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (null).append ("nestedSelectors", m_aNestedSelectors)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
