package com.helger.css.decl;

import java.util.List;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSWriterSettings;

public class CSSPageSelectorContainer implements ICSSPageSelectorContainer
{
  private final List <String> m_aSelectors;
  private CSSSourceLocation m_aSourceLocation;

  public CSSPageSelectorContainer (@Nonnull final List <String> aSelectors)
  {
    ValueEnforcer.notNullNoNullValue (aSelectors, "Selectors");
    m_aSelectors = CollectionHelper.newList (aSelectors);
  }

  public boolean isNotEmpty ()
  {
    return !m_aSelectors.isEmpty ();
  }

  @Nonnull
  @ReturnsMutableCopy
  public List <String> getAllSelectors ()
  {
    return CollectionHelper.newList (m_aSelectors);
  }

  @Nonnull
  @Nonempty
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);

    final boolean bOptimizedOutput = aSettings.isOptimizedOutput ();

    final StringBuilder aSB = new StringBuilder ();

    // Emit all selectors
    for (final String sSelector : m_aSelectors)
    {
      if (aSB.length () > 0)
        aSB.append (bOptimizedOutput ? "," : ", ");
      aSB.append (sSelector);
    }

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
    final CSSPageSelectorContainer rhs = (CSSPageSelectorContainer) o;
    return m_aSelectors.equals (rhs.m_aSelectors);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aSelectors).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Selectors", m_aSelectors)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .toString ();
  }
}
