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
package com.helger.css.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.annotation.Nonempty;
import com.helger.base.CGlobal;
import com.helger.css.parser.CSSNode;
import com.helger.css.parser.ParserCSS30TreeConstants;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Maps the different parser tokens. This enum is only used internally. It was previously used to
 * map between the 2.1 and the 3.0 parser constants.
 *
 * @author Philip Helger
 */
public enum ECSSNodeType
{
  ROOT (ParserCSS30TreeConstants.JJTROOT),
  // top level
  CHARSET (ParserCSS30TreeConstants.JJTCHARSETRULE),
  UNKNOWNRULE (ParserCSS30TreeConstants.JJTUNKNOWNRULE),
  UNKNOWNRULEPARAMETERLIST (ParserCSS30TreeConstants.JJTUNKNOWNRULEPARAMETERLIST),
  UNKNOWNRULEBODY (ParserCSS30TreeConstants.JJTUNKNOWNRULEBODY),
  STYLERULE (ParserCSS30TreeConstants.JJTSTYLERULE),
  IMPORTRULE (ParserCSS30TreeConstants.JJTIMPORTRULE),
  PAGERULE (ParserCSS30TreeConstants.JJTPAGERULE),
  MEDIARULE (ParserCSS30TreeConstants.JJTMEDIARULE),
  LAYERRULE (ParserCSS30TreeConstants.JJTLAYERRULE),
  FONTFACERULE (ParserCSS30TreeConstants.JJTFONTFACERULE),
  // top level -- style rule
  SELECTOR (ParserCSS30TreeConstants.JJTSELECTOR),
  RELATIVESELECTOR (ParserCSS30TreeConstants.JJTRELATIVESELECTOR),
  STYLEDECLARATIONLIST (ParserCSS30TreeConstants.JJTSTYLEDECLARATIONLIST),
  STYLEDECLARATION (ParserCSS30TreeConstants.JJTSTYLEDECLARATION),
  // style rule -- selector
  NAMESPACEPREFIX (ParserCSS30TreeConstants.JJTNAMESPACEPREFIX),
  ELEMENTNAME (ParserCSS30TreeConstants.JJTELEMENTNAME),
  HASH (ParserCSS30TreeConstants.JJTIDSELECTOR),
  CLASS (ParserCSS30TreeConstants.JJTCLASS),
  PSEUDO (ParserCSS30TreeConstants.JJTPSEUDOCLASSSELECTOR),
  HOST (ParserCSS30TreeConstants.JJTHOST),
  HOSTCONTEXT (ParserCSS30TreeConstants.JJTHOSTCONTEXT),
  SLOTTED (ParserCSS30TreeConstants.JJTSLOTTED),
  NEGATION (ParserCSS30TreeConstants.JJTFUNCNOT),
  PSEUDO_HAS (ParserCSS30TreeConstants.JJTHAS),
  PSEUDO_IS (ParserCSS30TreeConstants.JJTIS),
  PSEUDO_WHERE (ParserCSS30TreeConstants.JJTWHERE),
  ATTRIB (ParserCSS30TreeConstants.JJTATTRIBUTESELECTOR),
  ATTRIBOPERATOR (ParserCSS30TreeConstants.JJTATTRIBOPERATOR),
  ATTRIBVALUE (ParserCSS30TreeConstants.JJTATTRIBVALUE),
  ATTRIBCASE (ParserCSS30TreeConstants.JJTATTRIBCASE),
  // RELATIVE_SELECTOR (ParserCSS30TreeConstants.JJTRELATIVESELECTOR),
  SELECTORCOMBINATOR (ParserCSS30TreeConstants.JJTSELECTORCOMBINATOR),
  NTH (ParserCSS30TreeConstants.JJTNTH),
  // style rule -- declaration
  PROPERTY (ParserCSS30TreeConstants.JJTPROPERTY),
  IMPORTANT (ParserCSS30TreeConstants.JJTIMPORTANT),
  // declaration -- expression
  EXPR (ParserCSS30TreeConstants.JJTEXPR),
  EXPRTERM (ParserCSS30TreeConstants.JJTEXPRTERM),
  EXPROPERATOR (ParserCSS30TreeConstants.JJTEXPROPERATOR),
  URL (ParserCSS30TreeConstants.JJTURL),
  FUNCTION (ParserCSS30TreeConstants.JJTFUNCTION),
  CALC (ParserCSS30TreeConstants.JJTCALC),
  CALCSUMOPERATOR (ParserCSS30TreeConstants.JJTCALCSUMOPERATOR),
  CALCPRODUCT (ParserCSS30TreeConstants.JJTCALCPRODUCT),
  CALCPRODUCTOPERATOR (ParserCSS30TreeConstants.JJTCALCPRODUCTOPERATOR),
  CALCUNIT (ParserCSS30TreeConstants.JJTCALCVALUE),
  LINE_NAMES (ParserCSS30TreeConstants.JJTLINENAMES),
  LINE_NAME (ParserCSS30TreeConstants.JJTLINENAME),
  // media stuff
  MEDIALIST (ParserCSS30TreeConstants.JJTMEDIALIST),
  MEDIUM (ParserCSS30TreeConstants.JJTMEDIUM),
  MEDIAQUERY (ParserCSS30TreeConstants.JJTMEDIAQUERY),
  MEDIAMODIFIER (ParserCSS30TreeConstants.JJTMEDIAMODIFIER),
  MEDIAEXPR (ParserCSS30TreeConstants.JJTMEDIAEXPR),
  MEDIAFEATURE (ParserCSS30TreeConstants.JJTMEDIAFEATURE),
  // layer stuff
  LAYERSELECTOR (ParserCSS30TreeConstants.JJTLAYERSELECTOR),
  LAYERSELECTORLIST (ParserCSS30TreeConstants.JJTLAYERSELECTORLIST),
  LAYERRULEBLOCK (ParserCSS30TreeConstants.JJTLAYERRULEBLOCK),
  // page stuff
  PSEUDOPAGE (CGlobal.ILLEGAL_UINT),
  PAGESELECTOR (ParserCSS30TreeConstants.JJTPAGESELECTOR),
  PAGEMARGINSYMBOL (ParserCSS30TreeConstants.JJTPAGEMARGINSYMBOL),
  PAGERULEBLOCK (ParserCSS30TreeConstants.JJTPAGERULEBLOCK),
  // animations
  KEYFRAMESRULE (ParserCSS30TreeConstants.JJTKEYFRAMESRULE),
  KEYFRAMESIDENTIFIER (ParserCSS30TreeConstants.JJTKEYFRAMESIDENTIFIER),
  KEYFRAMESSELECTOR (ParserCSS30TreeConstants.JJTKEYFRAMESSELECTOR),
  SINGLEKEYFRAMESELECTOR (ParserCSS30TreeConstants.JJTSINGLEKEYFRAMESELECTOR),
  // viewport
  VIEWPORTRULE (ParserCSS30TreeConstants.JJTVIEWPORTRULE),
  // namespace
  NAMESPACERULE (ParserCSS30TreeConstants.JJTNAMESPACERULE),
  NAMESPACERULEPREFIX (ParserCSS30TreeConstants.JJTNAMESPACERULEPREFIX),
  NAMESPACERULEURL (ParserCSS30TreeConstants.JJTNAMESPACERULEURL),
  // supports
  SUPPORTSRULE (ParserCSS30TreeConstants.JJTSUPPORTSRULE),
  SUPPORTSCONDITION (ParserCSS30TreeConstants.JJTSUPPORTSCONDITION),
  SUPPORTSCONDITIONOPERATOR (ParserCSS30TreeConstants.JJTSUPPORTSCONDITIONOPERATOR),
  SUPPORTSNEGATION (ParserCSS30TreeConstants.JJTSUPPORTSNEGATION),
  SUPPORTSCONDITIONINPARENS (ParserCSS30TreeConstants.JJTSUPPORTSCONDITIONINPARENS),
  // rest
  ERROR_SKIPTO (ParserCSS30TreeConstants.JJTERRORSKIPTO);

  private static final Logger LOGGER = LoggerFactory.getLogger (ECSSNodeType.class);

  private final int m_nParserType30;

  /**
   * Constructor
   *
   * @param nParserType30
   *        The ID of the node in the 3.0 parser or {@link CGlobal#ILLEGAL_UINT} if this node is not
   *        present in CSS 3.0.
   */
  ECSSNodeType (final int nParserType30)
  {
    m_nParserType30 = nParserType30;
  }

  /**
   * Get the internal node type for the specified CSS version
   *
   * @return The internal node type for this node type or {@link CGlobal#ILLEGAL_UINT} if this node
   *         type is not supported by the passed version
   */
  int getParserNodeType ()
  {
    return m_nParserType30;
  }

  /**
   * Check if the passed parser node is of <code>this</code> type.
   *
   * @param aParserNode
   *        The parser node to be checked.
   * @return <code>true</code> if <code>this</code> is the type of the passed parser node in the
   *         given version
   */
  public boolean isNode (@Nonnull final CSSNode aParserNode)
  {
    return aParserNode.getNodeType () == getParserNodeType ();
  }

  @Nonnull
  String getNodeName ()
  {
    return ParserCSS30TreeConstants.jjtNodeName[m_nParserType30];
  }

  @Nullable
  static ECSSNodeType getNodeType (@Nonnull final CSSNode aParserNode)
  {
    for (final ECSSNodeType eNodeType : values ())
      if (eNodeType.isNode (aParserNode))
        return eNodeType;
    return null;
  }

  @Nullable
  static String getNodeName (@Nonnull final CSSNode aParserNode)
  {
    final ECSSNodeType eNodeType = getNodeType (aParserNode);
    if (eNodeType != null)
      return eNodeType.getNodeName ();
    LOGGER.warn ("Unsupported node type " + aParserNode.getNodeType ());
    return null;
  }

  private static void _dumpRecursive (@Nonnull final CSSNode aParserNode,
                                      @Nonnull final StringBuilder aSB,
                                      @Nonnull final String sPrefix)
  {
    aSB.append (sPrefix).append (getNodeName (aParserNode));
    if (aParserNode.isNotEmpty ())
      aSB.append ('[').append (aParserNode.getText ()).append (']');
    aSB.append ('\n');
    for (final CSSNode aChildNode : aParserNode)
      _dumpRecursive (aChildNode, aSB, sPrefix + "  ");
  }

  @Nonnull
  @Nonempty
  public static String getDump (@Nonnull final CSSNode aParserNode)
  {
    final StringBuilder aSB = new StringBuilder ();
    _dumpRecursive (aParserNode, aSB, "");
    return aSB.toString ();
  }

  public static boolean isErrorNode (@Nonnull final CSSNode aParserNode)
  {
    return ERROR_SKIPTO.isNode (aParserNode);
  }
}
