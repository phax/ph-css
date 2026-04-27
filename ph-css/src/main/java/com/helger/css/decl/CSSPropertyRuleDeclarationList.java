package com.helger.css.decl;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonnegative;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.state.EChange;
import com.helger.collection.commons.ICommonsList;

public class CSSPropertyRuleDeclarationList extends CSSWritableList <CSSPropertyRuleDeclaration>
{
  public CSSPropertyRuleDeclarationList ()
  {}

  @NonNull
  public CSSPropertyRuleDeclarationList addDeclaration (@NonNull final CSSPropertyRuleDeclaration aDeclaration)
  {
    add (aDeclaration);
    return this;
  }

  @NonNull
  public CSSPropertyRuleDeclarationList addDeclaration (@Nonnegative final int nIndex, @NonNull final CSSPropertyRuleDeclaration aDeclaration)
  {
    add (nIndex, aDeclaration);
    return this;
  }

  @NonNull
  public EChange removeDeclaration (@NonNull final CSSPropertyRuleDeclaration aDeclaration)
  {
    return removeObject (aDeclaration);
  }

  @NonNull
  public EChange removeDeclaration (@Nonnegative final int nIndex)
  {
    return removeAtIndex (nIndex);
  }

  @NonNull
  public EChange removeAllDeclarations ()
  {
    return removeAll ();
  }

  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSPropertyRuleDeclaration> getAllDeclarations ()
  {
    return getClone();
  }
  
  @Nullable
  public CSSPropertyRuleDeclaration getDeclarationAtIndex (@Nonnegative final int nIndex)
  {
    return getAtIndex (nIndex);
  }

  @NonNull
  public CSSPropertyRuleDeclarationList setDeclarationAtIndex (@Nonnegative final int nIndex, @NonNull final CSSPropertyRuleDeclaration aNewDeclaration)
  {
    set (nIndex, aNewDeclaration);
    return this;
  }

  public boolean hasDeclarations ()
  {
    return isNotEmpty ();
  }

  @Nonnegative
  public int getDeclarationCount ()
  {
    return size ();
  }
}
