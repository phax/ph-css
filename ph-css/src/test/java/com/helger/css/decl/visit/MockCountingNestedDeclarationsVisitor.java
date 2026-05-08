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
package com.helger.css.decl.visit;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NonNull;

import com.helger.css.decl.CSSNestedDeclarations;

class MockCountingNestedDeclarationsVisitor extends DefaultCSSVisitor
{
  private int m_nBeginNestedDeclarations = 0;
  private int m_nEndNestedDeclarations = 0;
  private final List <String> m_sNestedDeclaration = new ArrayList <> ();

  @Override
  public void onBeginNestedDeclarations (@NonNull CSSNestedDeclarations aNestedDeclarations)
  {
    m_nBeginNestedDeclarations++;
  }

  @Override
  public void onEndNestedDeclarations (@NonNull CSSNestedDeclarations aNestedDeclarations)
  {
    m_nEndNestedDeclarations++;
    m_sNestedDeclaration.add (aNestedDeclarations.getAsCSSString ());
  }

  public int getBeginNestedDeclarationsCount ()
  {
    return m_nBeginNestedDeclarations;
  }

  public int getEndNestedDeclarationsCount ()
  {
    return m_nEndNestedDeclarations;
  }

  public List <String> getNestedDeclarations ()
  {
    return m_sNestedDeclaration;
  }
}
