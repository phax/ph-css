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

import org.jspecify.annotations.NonNull;

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
 * Interface for visiting different elements of a CSS domain object.
 *
 * @author Philip Helger
 * @see DefaultCSSVisitor DefaultCSSVisitor for an empty default implementation
 */
public interface ICSSVisitor
{
  /**
   * Before visiting starts.<br>
   * Note: This is only called for complete style sheets, and not when starting
   * e.g. with a declaration list!
   */
  void begin ();

  /**
   * Called on CSS import statement
   *
   * @param aImportRule
   *        Other imported CSS. Never <code>null</code>.
   */
  void onImport (@NonNull CSSImportRule aImportRule);

  /**
   * Called on CSS namespace statement
   *
   * @param aNamespaceRule
   *        The namespace rule. Never <code>null</code>.
   */
  void onNamespace (@NonNull CSSNamespaceRule aNamespaceRule);

  /**
   * Called for each declaration
   *
   * @param aDeclaration
   *        The declaration. Never <code>null</code>.
   */
  void onDeclaration (@NonNull CSSDeclaration aDeclaration);

  // style rules:
  /**
   * Called when a style rule starts.<br>
   * Note: contained declarations are handled by
   * {@link #onDeclaration(CSSDeclaration)}
   *
   * @param aStyleRule
   *        The style rule. Never <code>null</code>.
   */
  void onBeginStyleRule (@NonNull CSSStyleRule aStyleRule);

  /**
   * Called for each selector of a style rule
   *
   * @param aSelector
   *        The style rule selector. Never <code>null</code>.
   */
  void onStyleRuleSelector (@NonNull CSSSelector aSelector);

  /**
   * Called when a style rule ends.
   *
   * @param aStyleRule
   *        The style rule. Never <code>null</code>.
   */
  void onEndStyleRule (@NonNull CSSStyleRule aStyleRule);

  // page rules:
  /**
   * Called when a page rule starts.<br>
   * Note: contained declarations are handled by
   * {@link #onDeclaration(CSSDeclaration)}, contained page margin blocks are
   * handled with {@link #onBeginPageMarginBlock(CSSPageMarginBlock)} and
   * {@link #onEndPageMarginBlock(CSSPageMarginBlock)}.
   *
   * @param aPageRule
   *        The page rule. Never <code>null</code>.
   */
  void onBeginPageRule (@NonNull CSSPageRule aPageRule);

  /**
   * Called when a page margin block starts.<br>
   * Note: contained declarations are handled by
   * {@link #onDeclaration(CSSDeclaration)}
   *
   * @param aPageMarginBlock
   *        The page margin block. Never <code>null</code>.
   */
  void onBeginPageMarginBlock (@NonNull CSSPageMarginBlock aPageMarginBlock);

  /**
   * Called when a page margin block ends.
   *
   * @param aPageMarginBlock
   *        The page margin block. Never <code>null</code>.
   */
  void onEndPageMarginBlock (@NonNull CSSPageMarginBlock aPageMarginBlock);

  /**
   * Called when a page rule ends.
   *
   * @param aPageRule
   *        The page rule. Never <code>null</code>.
   */
  void onEndPageRule (@NonNull CSSPageRule aPageRule);

  // font face rules:
  /**
   * Called when a font-face rule starts.<br>
   * Note: contained declarations are handled by
   * {@link #onDeclaration(CSSDeclaration)}
   *
   * @param aFontFaceRule
   *        The font-face rule. Never <code>null</code>.
   */
  void onBeginFontFaceRule (@NonNull CSSFontFaceRule aFontFaceRule);

  /**
   * Called when a font-face rule ends.
   *
   * @param aFontFaceRule
   *        The font-face rule. Never <code>null</code>.
   */
  void onEndFontFaceRule (@NonNull CSSFontFaceRule aFontFaceRule);

  // media rules:
  /**
   * Called when a media rule starts.<br>
   * Note: contained declarations are handled by
   * {@link #onDeclaration(CSSDeclaration)}
   *
   * @param aMediaRule
   *        The media rule. Never <code>null</code>.
   */
  void onBeginMediaRule (@NonNull CSSMediaRule aMediaRule);

  /**
   * Called when a media rule ends.
   *
   * @param aMediaRule
   *        The media rule. Never <code>null</code>.
   */
  void onEndMediaRule (@NonNull CSSMediaRule aMediaRule);

  // keyframes rules:
  /**
   * Called when a keyframes rule starts.<br>
   * Note: contained declarations are handled by
   * {@link #onDeclaration(CSSDeclaration)}
   *
   * @param aKeyframesRule
   *        The keyframes rule. Never <code>null</code>.
   */
  void onBeginKeyframesRule (@NonNull CSSKeyframesRule aKeyframesRule);

  /**
   * Called when a keyframes block starts.
   *
   * @param aKeyframesBlock
   *        The keyframes rule block. Never <code>null</code>.
   */
  void onBeginKeyframesBlock (@NonNull CSSKeyframesBlock aKeyframesBlock);

  /**
   * Called when a keyframes block ends.
   *
   * @param aKeyframesBlock
   *        The keyframes rule block. Never <code>null</code>.
   */
  void onEndKeyframesBlock (@NonNull CSSKeyframesBlock aKeyframesBlock);

  /**
   * Called when a keyframes rule ends.
   *
   * @param aKeyframesRule
   *        The keyframes rule. Never <code>null</code>.
   */
  void onEndKeyframesRule (@NonNull CSSKeyframesRule aKeyframesRule);

  // viewport rules
  /**
   * Called when a viewport rule starts.
   *
   * @param aViewportRule
   *        The viewport rule. Never <code>null</code>.
   */
  void onBeginViewportRule (@NonNull CSSViewportRule aViewportRule);

  /**
   * Called when a viewport rule ends.
   *
   * @param aViewportRule
   *        The viewport rule. Never <code>null</code>.
   */
  void onEndViewportRule (@NonNull CSSViewportRule aViewportRule);

  // supports rules
  /**
   * Called when a supports rule starts.
   *
   * @param aSupportsRule
   *        The supports rule. Never <code>null</code>.
   */
  void onBeginSupportsRule (@NonNull CSSSupportsRule aSupportsRule);

  /**
   * Called when a supports rule ends.
   *
   * @param aSupportsRule
   *        The supports rule. Never <code>null</code>.
   */
  void onEndSupportsRule (@NonNull CSSSupportsRule aSupportsRule);

  /**
   * Called when a layer rule start.
   *
   * @param aLayerRule
   *        The layer rule. Never <code>null</code>.
   */
  void onBeginLayerRule (@NonNull CSSLayerRule aLayerRule);

  /**
   * Called when a layer rule ends.
   *
   * @param aLayerRule
   *        The layer rule. Never <code>null</code>.
   */
  void onEndLayerRule (@NonNull CSSLayerRule aLayerRule);

  // unknown rules
  /**
   * Called when an unknown rule is encountered.
   *
   * @param aUnknownRule
   *        The unknown rule. Never <code>null</code>.
   */
  void onUnknownRule (@NonNull CSSUnknownRule aUnknownRule);

  /**
   * After visiting is done.<br>
   * Note: This is only called for complete style sheets, and not when starting
   * e.g. with a declaration list!
   */
  void end ();
}
