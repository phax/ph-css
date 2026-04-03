package com.helger.css.decl.visit;

import com.helger.css.decl.CSSPropertyRule;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

class MockCountingPageRuleVisitor extends DefaultCSSVisitor {
  private int m_nBeginPropertyRules = 0;
  private int m_nEndPropertyRules = 0;
  private final List<String> m_sPropertyRules = new ArrayList<>();

  @Override
  public void onBeginPropertyRule(@NonNull CSSPropertyRule aPropertyRule)
  {
    m_nBeginPropertyRules++;
    m_sPropertyRules.add(aPropertyRule.getAsCSSString());
  }

  @Override
  public void onEndPropertyRule(@NonNull CSSPropertyRule aPropertyRule)
  {
    m_nEndPropertyRules++;
  }

  public int getBeginPropertyRuleCount() {
    return m_nBeginPropertyRules;
  }

  public int getEndPropertyRuleCount() {
    return m_nEndPropertyRules;
  }

  public List<String> getPropertyRules() {
    return m_sPropertyRules;
  }
}
