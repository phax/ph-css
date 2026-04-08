package com.helger.css.decl.visit;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NonNull;

import com.helger.css.decl.CSSDeclaration;

class MockCountingDeclarationsVisitor extends DefaultCSSVisitor
{
  private int m_nDeclaration = 0;
  private final List <String> m_sDeclarations = new ArrayList <> ();

  @Override
  public void onDeclaration (@NonNull CSSDeclaration aDeclaration)
  {
    m_nDeclaration++;
    m_sDeclarations.add (aDeclaration.getAsCSSString ());
  }

  public int getDeclarationCount ()
  {
    return m_nDeclaration;
  }

  public List <String> getDeclarations ()
  {
    return m_sDeclarations;
  }
}
