/*
 * Copyright (C) 2014-2023 Philip Helger (www.helger.com)
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

import static org.junit.Assert.assertNotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpressionMemberTermURI;
import com.helger.css.decl.CSSImportRule;
import com.helger.css.decl.ICSSTopLevelRule;

final class MockCountingUrlVisitor extends DefaultCSSUrlVisitor
{
  private int m_nCount = 0;

  public MockCountingUrlVisitor ()
  {}

  @Override
  public void onImport (final CSSImportRule aImportRule)
  {
    assertNotNull (aImportRule.getLocation ());
    m_nCount++;
  }

  @Override
  public void onUrlDeclaration (@Nullable final ICSSTopLevelRule aTopLevelRule,
                                @Nonnull final CSSDeclaration aDeclaration,
                                @Nonnull final CSSExpressionMemberTermURI aURITerm)
  {
    assertNotNull (aURITerm.getURI ());
    m_nCount++;
  }

  public int getCount ()
  {
    return m_nCount;
  }
}
