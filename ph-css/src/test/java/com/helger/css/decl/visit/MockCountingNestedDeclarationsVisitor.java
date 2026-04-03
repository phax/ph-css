package com.helger.css.decl.visit;

import com.helger.css.decl.CSSNestedDeclarations;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

class MockCountingNestedDeclarationsVisitor extends DefaultCSSVisitor {
  private int m_nBeginNestedDeclarations = 0;
  private int m_nEndNestedDeclarations = 0;
  private final List<String> m_sNestedDeclaration = new ArrayList<>();

  @Override
  public void onBeginNestedDeclarations(@NonNull CSSNestedDeclarations aNestedDeclarations) {
    m_nBeginNestedDeclarations++;
  }

  @Override
  public void onEndNestedDeclarations(@NonNull CSSNestedDeclarations aNestedDeclarations) {
    m_nEndNestedDeclarations++;
    m_sNestedDeclaration.add(aNestedDeclarations.getAsCSSString());
  }

  public int getBeginNestedDeclarationsCount() {
    return m_nBeginNestedDeclarations;
  }

  public int getEndNestedDeclarationsCount() {
    return m_nEndNestedDeclarations;
  }

  public List<String> getNestedDeclarations() {
    return m_sNestedDeclaration;
  }
}
