/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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

import org.jspecify.annotations.NonNull;

import com.helger.annotation.concurrent.Immutable;
import com.helger.annotation.style.OverrideOnDemand;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSFontFaceRule;
import com.helger.css.decl.CSSImportRule;
import com.helger.css.decl.CSSKeyframesBlock;
import com.helger.css.decl.CSSKeyframesRule;
import com.helger.css.decl.CSSLayerRule;
import com.helger.css.decl.CSSMediaRule;
import com.helger.css.decl.CSSNamespaceRule;
import com.helger.css.decl.CSSPageMarginBlock;
import com.helger.css.decl.CSSPageRule;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CSSSupportsRule;
import com.helger.css.decl.CSSUnknownRule;
import com.helger.css.decl.CSSViewportRule;

/**
 * Default implementation of the {@link ICSSVisitor} interface. Use as base
 * class for your own implementations since this class does nothing on its own.
 *
 * @author Philip Helger
 */
@Immutable
public class DefaultCSSVisitor implements ICSSVisitor
{
  public DefaultCSSVisitor ()
  {}

  @OverrideOnDemand
  public void begin ()
  {}

  @OverrideOnDemand
  public void onImport (@NonNull final CSSImportRule aImportRule)
  {}

  @OverrideOnDemand
  public void onNamespace (@NonNull final CSSNamespaceRule aNamespaceRule)
  {}

  @OverrideOnDemand
  public void onDeclaration (@NonNull final CSSDeclaration aDeclaration)
  {}

  @OverrideOnDemand
  public void onBeginStyleRule (@NonNull final CSSStyleRule aStyleRule)
  {}

  @OverrideOnDemand
  public void onStyleRuleSelector (@NonNull final CSSSelector aSelector)
  {}

  @OverrideOnDemand
  public void onEndStyleRule (@NonNull final CSSStyleRule aStyleRule)
  {}

  @OverrideOnDemand
  public void onBeginPageRule (@NonNull final CSSPageRule aPageRule)
  {}

  @OverrideOnDemand
  public void onBeginPageMarginBlock (@NonNull final CSSPageMarginBlock aPageMarginBlock)
  {}

  @OverrideOnDemand
  public void onEndPageMarginBlock (@NonNull final CSSPageMarginBlock aPageMarginBlock)
  {}

  @OverrideOnDemand
  public void onEndPageRule (@NonNull final CSSPageRule aPageRule)
  {}

  @OverrideOnDemand
  public void onBeginFontFaceRule (@NonNull final CSSFontFaceRule aFontFaceRule)
  {}

  @OverrideOnDemand
  public void onEndFontFaceRule (@NonNull final CSSFontFaceRule aFontFaceRule)
  {}

  @OverrideOnDemand
  public void onBeginMediaRule (@NonNull final CSSMediaRule aMediaRule)
  {}

  @OverrideOnDemand
  public void onEndMediaRule (@NonNull final CSSMediaRule aMediaRule)
  {}

  @OverrideOnDemand
  public void onBeginKeyframesRule (@NonNull final CSSKeyframesRule aKeyframesRule)
  {}

  @OverrideOnDemand
  public void onBeginKeyframesBlock (@NonNull final CSSKeyframesBlock aKeyframesBlock)
  {}

  @OverrideOnDemand
  public void onEndKeyframesBlock (@NonNull final CSSKeyframesBlock aKeyframesBlock)
  {}

  @OverrideOnDemand
  public void onEndKeyframesRule (@NonNull final CSSKeyframesRule aKeyframesRule)
  {}

  @OverrideOnDemand
  public void onBeginViewportRule (@NonNull final CSSViewportRule aViewportRule)
  {}

  @OverrideOnDemand
  public void onEndViewportRule (@NonNull final CSSViewportRule aViewportRule)
  {}

  @OverrideOnDemand
  public void onBeginSupportsRule (@NonNull final CSSSupportsRule aSupportsRule)
  {}

  @OverrideOnDemand
  public void onEndSupportsRule (@NonNull final CSSSupportsRule aSupportsRule)
  {}

  @OverrideOnDemand
  public void onBeginLayerRule (@NonNull final CSSLayerRule aLayerRule)
  {}

  @OverrideOnDemand
  public void onEndLayerRule (@NonNull final CSSLayerRule aLayerRule)
  {}
  
  @OverrideOnDemand
  public void onUnknownRule (@NonNull final CSSUnknownRule aUnknownRule)
  {}

  @OverrideOnDemand
  public void end ()
  {}
}
