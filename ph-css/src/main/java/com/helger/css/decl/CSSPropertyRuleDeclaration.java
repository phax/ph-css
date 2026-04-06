package com.helger.css.decl;

import java.util.Locale;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.style.ReturnsMutableObject;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.hashcode.HashCodeGenerator;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.CCSS;
import com.helger.css.CSSSourceLocation;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSWriteable;
import com.helger.css.ICSSWriterSettings;

public class CSSPropertyRuleDeclaration implements ICSSSourceLocationAware, ICSSWriteable
{
  private String m_sDescriptor;
  private CSSExpression m_aExpression;
  private CSSSourceLocation m_aSourceLocation;

  public CSSPropertyRuleDeclaration (@NonNull @Nonempty final String sDescriptor, @NonNull final CSSExpression aExpression)
  {
    setDescriptor(sDescriptor);
    setExpression(aExpression);
  }
  
  @NonNull
  @Nonempty
  public final String getDescriptor ()
  {
    return m_sDescriptor;
  }

  @NonNull
  public final CSSPropertyRuleDeclaration setDescriptor (@NonNull @Nonempty final String sDescriptor)
  {
    ValueEnforcer.notEmpty (sDescriptor, "Descriptor");
    m_sDescriptor = sDescriptor.toLowerCase (Locale.ROOT);
    return this;
  }

  @NonNull
  @ReturnsMutableObject
  public final CSSExpression getExpression ()
  {
    return m_aExpression;
  }

  @NonNull
  public final String getExpressionAsCSSString ()
  {
    return m_aExpression.getAsCSSString ();
  }

  @NonNull
  public final CSSPropertyRuleDeclaration setExpression (@NonNull final CSSExpression aExpression)
  {
    m_aExpression = ValueEnforcer.notNull (aExpression, "Expression");
    return this;
  }

  @NonNull
  @Nonempty
  public String getAsCSSString (@NonNull final ICSSWriterSettings aSettings, @Nonnegative final int nIndentLevel)
  {
    return m_sDescriptor +
           CCSS.SEPARATOR_PROPERTY_VALUE +
           m_aExpression.getAsCSSString (aSettings, nIndentLevel);
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
    final CSSPropertyRuleDeclaration rhs = (CSSPropertyRuleDeclaration) o;
    return m_sDescriptor.equals (rhs.m_sDescriptor) && m_aExpression.equals (rhs.m_aExpression);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_sDescriptor).append (m_aExpression).getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("descriptor", m_sDescriptor)
                                       .append ("expression", m_aExpression)
                                       .appendIfNotNull ("SourceLocation", m_aSourceLocation)
                                       .getToString ();
  }
}
