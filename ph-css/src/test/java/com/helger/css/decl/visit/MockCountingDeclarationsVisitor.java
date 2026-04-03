package com.helger.css.decl.visit;

import com.helger.css.decl.CSSDeclaration;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

class MockCountingDeclarationsVisitor extends DefaultCSSVisitor {
    private int m_nDeclaration = 0;
    private final List<String> declarations = new ArrayList<>();

    @Override
    public void onDeclaration(@NonNull CSSDeclaration aDeclaration) {
        m_nDeclaration++;
        declarations.add(aDeclaration.getAsCSSString());
    }

    public int getDeclarationCount() {
        return m_nDeclaration;
    }

    public List<String> getDeclarations() {
        return declarations;
    }
}
