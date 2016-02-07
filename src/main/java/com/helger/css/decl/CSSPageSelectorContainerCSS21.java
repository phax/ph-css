package com.helger.css.decl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.string.StringHelper;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSWriterSettings;

public class CSSPageSelectorContainerCSS21 implements ICSSPageSelectorContainer
{
  private final String m_sPseudoPage;
  private CSSSourceLocation m_aSourceLocation;

  public CSSPageSelectorContainerCSS21 (@Nullable final String sPseudoPage)
  {
    m_sPseudoPage = sPseudoPage;
  }

  public boolean isNotEmpty ()
  {
    return StringHelper.hasText (m_sPseudoPage);
  }

  @Nullable
  public String getPseudoPage ()
  {
    return m_sPseudoPage;
  }

  @Nonnull
  public String getAsCSSString (@Nonnull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    aSettings.checkVersionRequirements (this);

    return StringHelper.getNotNull (m_sPseudoPage);
  }

  @Nonnull
  public ECSSVersion getMinimumCSSVersion ()
  {
    return ECSSVersion.CSS21;
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
    final CSSPageSelectorContainerCSS21 rhs = (CSSPageSelectorContainerCSS21) o;
    return EqualsHelper.equals (m_sPseudoPage, rhs.m_sPseudoPage);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sPseudoPage).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).appendIfNotNull ("PseudoPage", m_sPseudoPage)
                                       .appendIfNotNull ("sourceLocation", m_aSourceLocation)
                                       .toString ();
  }
}
