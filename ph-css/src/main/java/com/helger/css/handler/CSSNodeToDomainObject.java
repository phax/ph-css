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

import java.util.function.Consumer;

import com.helger.annotation.Nonempty;
import com.helger.annotation.concurrent.NotThreadSafe;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.string.StringImplode;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
import com.helger.css.CSSSourceLocation;
import com.helger.css.decl.*;
import com.helger.css.decl.CSSMediaQuery.EModifier;
import com.helger.css.media.ECSSMediaExpressionFeature;
import com.helger.css.media.ECSSMedium;
import com.helger.css.parser.CSSNode;
import com.helger.css.parser.CSSParseHelper;
import com.helger.css.reader.errorhandler.ICSSInterpretErrorHandler;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * This class converts the jjtree node to a domain object. This is where the hard work happens.
 *
 * @author Philip Helger
 */
@NotThreadSafe
final class CSSNodeToDomainObject
{
  private final ICSSInterpretErrorHandler m_aErrorHandler;
  private final boolean m_bUseSourceLocation;

  /**
   * Constructor
   *
   * @param aErrorHandler
   *        Error handler to use. May not be <code>null</code>.
   * @param bUseSourceLocation
   *        <code>true</code> to keep the source location, <code>false</code> to ignore the source
   *        location. Disabling the source location may be a performance improvement.
   */
  public CSSNodeToDomainObject (@Nonnull final ICSSInterpretErrorHandler aErrorHandler,
                                final boolean bUseSourceLocation)
  {
    m_aErrorHandler = ValueEnforcer.notNull (aErrorHandler, "ErrorHandler");
    m_bUseSourceLocation = bUseSourceLocation;
  }

  private void _expectNodeType (@Nonnull final CSSNode aNode, @Nonnull final ECSSNodeType eExpected)
  {
    if (!eExpected.isNode (aNode))
      throw new CSSHandlingException (aNode,
                                      "Expected a '" +
                                             eExpected.getNodeName () +
                                             "' node but received a '" +
                                             ECSSNodeType.getNodeName (aNode) +
                                             "'");
  }

  private void _throwUnexpectedChildrenCount (@Nonnull final CSSNode aNode, @Nonnull @Nonempty final String sMsg)
  {
    m_aErrorHandler.onCSSInterpretationError (sMsg);
    for (int i = 0; i < aNode.jjtGetNumChildren (); ++i)
      m_aErrorHandler.onCSSInterpretationError ("  " + aNode.jjtGetChild (i));
    throw new CSSHandlingException (aNode, sMsg);
  }

  @Nonnull
  private CSSImportRule _createImportRule (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.IMPORTRULE);
    final int nChildCount = aNode.jjtGetNumChildren ();
    if (nChildCount > 2)
      _throwUnexpectedChildrenCount (aNode, "Expected at last 2 children but got " + nChildCount + "!");

    CSSURI aImportURI = null;
    int nCurrentIndex = 0;
    if (nChildCount > 0)
    {
      final CSSNode aURINode = aNode.jjtGetChild (0);
      if (ECSSNodeType.URL.isNode (aURINode))
      {
        aImportURI = new CSSURI (aURINode.getText ());
        if (m_bUseSourceLocation)
          aImportURI.setSourceLocation (aURINode.getSourceLocation ());
        ++nCurrentIndex;
      }
      else
        if (!ECSSNodeType.MEDIALIST.isNode (aURINode))
          throw new IllegalStateException ("Expected an URI or MEDIALIST node but got " +
                                           ECSSNodeType.getNodeName (aURINode));
    }

    if (aImportURI == null)
    {
      // No URI child node present, so the location is printed directly
      // E.g. @import "abc.css"
      aImportURI = new CSSURI (CSSParseHelper.extractStringValue (aNode.getText ()));
      if (m_bUseSourceLocation)
        aImportURI.setSourceLocation (aNode.getSourceLocation ());
    }

    // Import rule
    final CSSImportRule ret = new CSSImportRule (aImportURI);
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    if (nChildCount > nCurrentIndex)
    {
      // We have a media query present!
      final CSSNode aMediaListNode = aNode.jjtGetChild (nCurrentIndex);
      if (ECSSNodeType.MEDIALIST.isNode (aMediaListNode))
      {
        for (final CSSNode aMediaQueryNode : aMediaListNode)
        {
          ret.addMediaQuery (_createMediaQuery (aMediaQueryNode));
        }
        ++nCurrentIndex;
      }
      else
        m_aErrorHandler.onCSSInterpretationError ("Expected an MEDIALIST node but got " +
                                                  ECSSNodeType.getNodeName (aMediaListNode));
    }

    if (nCurrentIndex < nChildCount)
      m_aErrorHandler.onCSSInterpretationError ("Import statement has children which are unhandled.");
    return ret;
  }

  @Nonnull
  private CSSSelectorAttribute _createSelectorAttribute (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.ATTRIB);
    final int nChildren = aNode.jjtGetNumChildren ();

    // Check if an optional namespace prefix is present
    String sNamespacePrefix = null;
    int nOperatorIndex = 0;
    if (nChildren > 0 && ECSSNodeType.NAMESPACEPREFIX.isNode (aNode.jjtGetChild (0)))
    {
      sNamespacePrefix = aNode.jjtGetChild (0).getText ();
      nOperatorIndex = 1;
    }
    final String sAttrName = aNode.getText ();

    CSSSelectorAttribute ret;
    if (nChildren == nOperatorIndex)
    {
      // Just check for existence of the attribute
      ret = new CSSSelectorAttribute (sNamespacePrefix, sAttrName);
    }
    else
    {
      // With operator, value and case sensitivity flag
      final int nExpectedChildCount = nOperatorIndex + 3;
      if (nChildren != nExpectedChildCount)
        _throwUnexpectedChildrenCount (aNode,
                                       "Illegal number of children present (" +
                                              nChildren +
                                              ") - expected " +
                                              nExpectedChildCount);

      // With operator...
      final CSSNode aOperator = aNode.jjtGetChild (nOperatorIndex);
      _expectNodeType (aOperator, ECSSNodeType.ATTRIBOPERATOR);

      // ...and value
      final CSSNode aAttrValue = aNode.jjtGetChild (nOperatorIndex + 1);
      _expectNodeType (aAttrValue, ECSSNodeType.ATTRIBVALUE);

      // Case sensitivity flag
      // The node is always present, but the text may be null
      ECSSAttributeCase eCaseFlag = null;
      final CSSNode aFlag = aNode.jjtGetChild (nOperatorIndex + 2);
      _expectNodeType (aFlag, ECSSNodeType.ATTRIBCASE);
      if (aFlag.getText () != null)
      {
        eCaseFlag = ECSSAttributeCase.getFromNameOrNull (aFlag.getText ());
        if (eCaseFlag == null)
          throw new CSSHandlingException (aNode,
                                          "The attribute selector uses the unknown flag '" +
                                                 aFlag.getText () +
                                                 "'. Allowed values are only: " +
                                                 StringImplode.imploder ()
                                                              .source (ECSSAttributeCase.values (),
                                                                       x -> '"' + x.getName () + '"')
                                                              .separator (", ")
                                                              .build ());
      }

      ret = new CSSSelectorAttribute (sNamespacePrefix,
                                      sAttrName,
                                      ECSSAttributeOperator.getFromNameOrNull (aOperator.getText ()),
                                      aAttrValue.getText (),
                                      eCaseFlag);
    }
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    return ret;
  }

  @Nullable
  private ECSSSelectorCombinator _createSelectorCombinator (final String sText)
  {
    final ECSSSelectorCombinator eCombinator = ECSSSelectorCombinator.getFromNameOrNull (sText);
    if (eCombinator == null)
      m_aErrorHandler.onCSSInterpretationError ("Failed to parse CSS selector combinator '" + sText + "'");
    return eCombinator;
  }

  @Nullable
  private ICSSSelectorMember _createSelectorMember (final CSSNode aNode)
  {
    final int nChildCount = aNode.jjtGetNumChildren ();

    if (ECSSNodeType.NAMESPACEPREFIX.isNode (aNode) ||
        ECSSNodeType.ELEMENTNAME.isNode (aNode) ||
        ECSSNodeType.HASH.isNode (aNode) ||
        ECSSNodeType.CLASS.isNode (aNode))
    {
      if (nChildCount != 0)
        _throwUnexpectedChildrenCount (aNode, "CSS simple selector member expected 0 children and got " + nChildCount);
      final CSSSelectorSimpleMember ret = new CSSSelectorSimpleMember (aNode.getText ());
      if (m_bUseSourceLocation)
        ret.setSourceLocation (aNode.getSourceLocation ());
      return ret;
    }

    if (ECSSNodeType.ATTRIB.isNode (aNode))
      return _createSelectorAttribute (aNode);

    if (ECSSNodeType.SELECTORCOMBINATOR.isNode (aNode))
      return _createSelectorCombinator (aNode.getText ());

    if (ECSSNodeType.NEGATION.isNode (aNode))
    {
      // Note: no children don't make sense but are syntactically allowed!
      final ICommonsList <CSSSelector> aNestedSelectors = new CommonsArrayList <> ();
      for (int i = 0; i < nChildCount; ++i)
      {
        final CSSNode aChildNode = aNode.jjtGetChild (i);
        final CSSSelector aSelector = _createSelector (aChildNode);
        aNestedSelectors.add (aSelector);
      }

      final CSSSelectorMemberNot ret = new CSSSelectorMemberNot (aNestedSelectors);
      if (m_bUseSourceLocation)
        ret.setSourceLocation (aNode.getSourceLocation ());
      return ret;
    }

    if (ECSSNodeType.PSEUDO.isNode (aNode))
    {
      if (nChildCount == 0)
      {
        // E.g. ":focus" or ":hover"
        String sText = aNode.getText ();
        if (sText.endsWith ("("))
        {
          // Or bogus functions like ":lang()" - see #72
          sText += ')';
        }
        final CSSSelectorSimpleMember ret = new CSSSelectorSimpleMember (sText);
        if (m_bUseSourceLocation)
          ret.setSourceLocation (aNode.getSourceLocation ());
        return ret;
      }

      if (nChildCount == 1)
      {
        final CSSNode aChildNode = aNode.jjtGetChild (0);
        if (ECSSNodeType.NTH.isNode (aChildNode))
        {
          // Handle nth. E.g. ":nth-child(even)" or ":nth-child(3n+1)"
          final CSSSelectorSimpleMember ret = new CSSSelectorSimpleMember (aNode.getText () +
                                                                           aChildNode.getText () +
                                                                           ")");
          if (m_bUseSourceLocation)
            ret.setSourceLocation (aNode.getSourceLocation ());
          return ret;
        }

        if (ECSSNodeType.HOST.isNode (aChildNode))
        {
          final CSSSelector aSelector = new CSSSelector ();
          final int nChildChildCount = aChildNode.jjtGetNumChildren ();
          for (int j = 0; j < nChildChildCount; ++j)
            aSelector.addMember (_createSelectorMember (aChildNode.jjtGetChild (j)));
          final CSSSelectorMemberHost ret = new CSSSelectorMemberHost (aSelector);
          if (m_bUseSourceLocation)
            ret.setSourceLocation (aNode.getSourceLocation ());
          return ret;
        }

        if (ECSSNodeType.HOSTCONTEXT.isNode (aChildNode))
        {
          final CSSSelector aSelector = new CSSSelector ();
          final int nChildChildCount = aChildNode.jjtGetNumChildren ();
          for (int j = 0; j < nChildChildCount; ++j)
            aSelector.addMember (_createSelectorMember (aChildNode.jjtGetChild (j)));
          final CSSSelectorMemberHostContext ret = new CSSSelectorMemberHostContext (aSelector);
          if (m_bUseSourceLocation)
            ret.setSourceLocation (aNode.getSourceLocation ());
          return ret;
        }

        if (ECSSNodeType.SLOTTED.isNode (aChildNode))
        {
          final CSSSelector aSelector = new CSSSelector ();
          final int nChildChildCount = aChildNode.jjtGetNumChildren ();
          for (int j = 0; j < nChildChildCount; ++j)
            aSelector.addMember (_createSelectorMember (aChildNode.jjtGetChild (j)));
          final CSSSelectorMemberSlotted ret = new CSSSelectorMemberSlotted (aSelector);
          if (m_bUseSourceLocation)
            ret.setSourceLocation (aNode.getSourceLocation ());
          return ret;
        }

        if (ECSSNodeType.PSEUDO_HAS.isNode (aChildNode))
        {
          final int nChildChildCount = aChildNode.jjtGetNumChildren ();
          final ICommonsList <CSSSelector> aNestedSelectors = new CommonsArrayList <> ();
          for (int j = 0; j < nChildChildCount; ++j)
            aNestedSelectors.add (_createRelativeSelector (aChildNode.jjtGetChild (j)));

          final CSSSelectorMemberPseudoHas ret = new CSSSelectorMemberPseudoHas (aNestedSelectors);
          if (m_bUseSourceLocation)
            ret.setSourceLocation (aNode.getSourceLocation ());
          return ret;
        }

        if (ECSSNodeType.PSEUDO_WHERE.isNode (aChildNode))
        {
          final int nChildChildCount = aChildNode.jjtGetNumChildren ();
          final ICommonsList <CSSSelector> aNestedSelectors = new CommonsArrayList <> ();
          for (int j = 0; j < nChildChildCount; ++j)
            aNestedSelectors.add (_createSelector (aChildNode.jjtGetChild (j)));
          final CSSSelectorMemberPseudoWhere ret = new CSSSelectorMemberPseudoWhere (aNestedSelectors);
          if (m_bUseSourceLocation)
            ret.setSourceLocation (aNode.getSourceLocation ());
          return ret;
        }

        if (ECSSNodeType.PSEUDO_IS.isNode (aChildNode))
        {
          final int nChildChildCount = aChildNode.jjtGetNumChildren ();
          final ICommonsList <CSSSelector> aNestedSelectors = new CommonsArrayList <> ();
          for (int j = 0; j < nChildChildCount; ++j)
            aNestedSelectors.add (_createSelector (aChildNode.jjtGetChild (j)));
          final CSSSelectorMemberPseudoIs ret = new CSSSelectorMemberPseudoIs (aNestedSelectors);
          if (m_bUseSourceLocation)
            ret.setSourceLocation (aNode.getSourceLocation ());
          return ret;
        }

        // It's a function (e.g. ":lang(fr)")
        final CSSExpression aExpr = _createExpression (aChildNode);
        final CSSSelectorMemberFunctionLike ret = new CSSSelectorMemberFunctionLike (aNode.getText (), aExpr);
        if (m_bUseSourceLocation)
          ret.setSourceLocation (aNode.getSourceLocation ());
        return ret;
      }

      throw new UnsupportedOperationException ("Not supporting pseudo-selectors with functions and " +
                                               nChildCount +
                                               " args: " +
                                               aNode.toString ());
    }

    if (ECSSNodeType.SELECTOR.isNode (aNode))
    {
      return _createSelector (aNode);
    }

    m_aErrorHandler.onCSSInterpretationError ("Unsupported selector child: " + ECSSNodeType.getNodeName (aNode));
    return null;
  }

  @Nonnull
  private CSSSelector _createSelector (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.SELECTOR);

    final CSSSelector ret = new CSSSelector ();
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    for (final CSSNode aChildNode : aNode)
    {
      final ICSSSelectorMember aMember = _createSelectorMember (aChildNode);
      if (aMember != null)
        ret.addMember (aMember);
    }
    return ret;
  }

  @Nonnull
  private CSSSelector _createRelativeSelector (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.RELATIVESELECTOR);

    final CSSSelector ret = new CSSSelector ();
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    for (final CSSNode aChildNode : aNode)
    {
      final ICSSSelectorMember aMember = _createSelectorMember (aChildNode);
      if (aMember != null)
        ret.addMember (aMember);
    }
    return ret;
  }

  @Nonnull
  private CSSExpressionMemberMathProduct _createExpressionCalcProduct (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.CALCPRODUCT);

    final CSSExpressionMemberMathProduct ret = new CSSExpressionMemberMathProduct ();
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());

    // read all sums
    for (final CSSNode aChildNode : aNode)
    {
      if (ECSSNodeType.CALCUNIT.isNode (aChildNode))
      {
        final int nChildCount = aChildNode.jjtGetNumChildren ();
        if (nChildCount == 0)
        {
          final CSSExpressionMemberMathUnitSimple aMember = new CSSExpressionMemberMathUnitSimple (aChildNode.getText ());
          if (m_bUseSourceLocation)
            aMember.setSourceLocation (aChildNode.getSourceLocation ());
          ret.addMember (aMember);
        }
        else
          if (nChildCount == 1 && ECSSNodeType.FUNCTION.isNode (aChildNode.jjtGetChild (0)))
          {
            // Source location is taken from aNestedProduct
            ret.addMember (_createExpressionFunction (aChildNode.jjtGetChild (0)));
          }
          else
            if (nChildCount == 1 && ECSSNodeType.CALC.isNode (aChildNode.jjtGetChild (0)))
            {
              // Source location is taken from aNestedProduct
              ret.addMember (_createExpressionCalc (aChildNode.jjtGetChild (0)));
            }
            else
            {
              // Must be even child count
              if ((nChildCount % 2) == 0)
                _throwUnexpectedChildrenCount (aChildNode,
                                               "CSS math unit expected odd child count and got " + nChildCount);

              final CSSExpressionMemberMathProduct aNestedProduct = new CSSExpressionMemberMathProduct ();
              for (int i = 0; i < nChildCount; ++i)
              {
                final CSSNode aChildChildNode = aChildNode.jjtGetChild (i);
                if (ECSSNodeType.CALCPRODUCT.isNode (aChildChildNode))
                {
                  // Source location is taken from aNestedProduct
                  aNestedProduct.addMember (_createExpressionCalcProduct (aChildChildNode));
                }
                else
                  if (ECSSNodeType.CALCSUMOPERATOR.isNode (aChildChildNode))
                  {
                    final String sText = aChildChildNode.getText ();
                    final ECSSMathOperator eMathOp = ECSSMathOperator.getFromNameOrNull (sText);
                    if (eMathOp == null)
                      m_aErrorHandler.onCSSInterpretationError ("Failed to parse math operator '" + sText + "'");
                    else
                      aNestedProduct.addMember (eMathOp);
                  }
                  else
                  {
                    m_aErrorHandler.onCSSInterpretationError ("Unsupported child of " +
                                                              ECSSNodeType.getNodeName (aChildNode) +
                                                              ": " +
                                                              ECSSNodeType.getNodeName (aChildChildNode));
                  }
              }
              if (m_bUseSourceLocation)
                aNestedProduct.setSourceLocation (aChildNode.getSourceLocation ());
              ret.addMember (new CSSExpressionMemberMathUnitProduct (aNestedProduct));
            }
      }
      else
        if (ECSSNodeType.CALCPRODUCTOPERATOR.isNode (aChildNode))
        {
          final String sText = aChildNode.getText ();
          final ECSSMathOperator eMathOp = ECSSMathOperator.getFromNameOrNull (sText);
          if (eMathOp == null)
            m_aErrorHandler.onCSSInterpretationError ("Failed to parse math product operator '" + sText + "'");
          else
            ret.addMember (eMathOp);
        }
        else
          m_aErrorHandler.onCSSInterpretationError ("Unsupported child of " +
                                                    ECSSNodeType.getNodeName (aNode) +
                                                    ": " +
                                                    ECSSNodeType.getNodeName (aChildNode));
    }

    return ret;
  }

  @Nonnull
  private CSSExpressionMemberTermURI _createExpressionURL (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.URL);

    final int nChildCount = aNode.jjtGetNumChildren ();
    if (nChildCount > 0)
      _throwUnexpectedChildrenCount (aNode, "Expected 0 children but got " + nChildCount + "!");

    final CSSURI aURI = new CSSURI (aNode.getText ());
    final CSSExpressionMemberTermURI ret = new CSSExpressionMemberTermURI (aURI);
    if (m_bUseSourceLocation)
    {
      final CSSSourceLocation aSrcLoc = aNode.getSourceLocation ();
      aURI.setSourceLocation (aSrcLoc);
      ret.setSourceLocation (aSrcLoc);
    }
    return ret;
  }

  @Nonnull
  private CSSExpressionMemberFunction _createExpressionFunction (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.FUNCTION);

    final int nChildCount = aNode.jjtGetNumChildren ();
    if (nChildCount > 1)
      _throwUnexpectedChildrenCount (aNode, "Expected 0 or 1 children but got " + nChildCount + "!");

    final String sFunctionName = aNode.getText ();
    CSSExpressionMemberFunction aFunc;
    if (nChildCount == 1)
    {
      // Parameters present
      final CSSNode aFirstChild = aNode.jjtGetChild (0);
      final CSSExpression aFuncExpr = _createExpression (aFirstChild);
      aFunc = new CSSExpressionMemberFunction (sFunctionName, aFuncExpr);
    }
    else
    {
      // No parameters
      aFunc = new CSSExpressionMemberFunction (sFunctionName);
    }
    if (m_bUseSourceLocation)
      aFunc.setSourceLocation (aNode.getSourceLocation ());
    return aFunc;
  }

  @Nonnull
  private CSSExpressionMemberMath _createExpressionCalc (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.CALC);

    final CSSExpressionMemberMath ret = new CSSExpressionMemberMath ();
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());

    // read all sums
    for (final CSSNode aChildNode : aNode)
    {
      if (ECSSNodeType.CALCPRODUCT.isNode (aChildNode))
      {
        ret.addMember (_createExpressionCalcProduct (aChildNode));
      }
      else
        if (ECSSNodeType.CALCSUMOPERATOR.isNode (aChildNode))
        {
          final String sText = aChildNode.getText ();
          final ECSSMathOperator eMathOp = ECSSMathOperator.getFromNameOrNull (sText);
          if (eMathOp == null)
            m_aErrorHandler.onCSSInterpretationError ("Failed to parse math operator '" + sText + "'");
          else
            ret.addMember (eMathOp);
        }
        else
          m_aErrorHandler.onCSSInterpretationError ("Unsupported child of " +
                                                    ECSSNodeType.getNodeName (aNode) +
                                                    ": " +
                                                    ECSSNodeType.getNodeName (aChildNode));
    }

    return ret;
  }

  @Nonnull
  private CSSExpressionMemberLineNames _createExpressionLineNamesTerm (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.LINE_NAMES);

    final CSSExpressionMemberLineNames ret = new CSSExpressionMemberLineNames ();
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());

    // read all sums
    for (final CSSNode aChildNode : aNode)
    {
      if (ECSSNodeType.LINE_NAME.isNode (aChildNode))
      {
        ret.addMember (aChildNode.getText ());
      }
      else
        m_aErrorHandler.onCSSInterpretationError ("Unsupported child of " +
                                                  ECSSNodeType.getNodeName (aNode) +
                                                  ": " +
                                                  ECSSNodeType.getNodeName (aChildNode));
    }

    return ret;
  }

  @Nonnull
  private ICSSExpressionMember _createExpressionTerm (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.EXPRTERM);
    final int nChildCount = aNode.jjtGetNumChildren ();
    if (nChildCount > 1)
      _throwUnexpectedChildrenCount (aNode, "Expected 0 or 1 children but got " + nChildCount + "!");

    // Simple value
    if (nChildCount == 0)
    {
      final CSSExpressionMemberTermSimple ret = new CSSExpressionMemberTermSimple (aNode.getText ());
      if (m_bUseSourceLocation)
        ret.setSourceLocation (aNode.getSourceLocation ());
      return ret;
    }

    final CSSNode aChildNode = aNode.jjtGetChild (0);

    if (ECSSNodeType.URL.isNode (aChildNode))
    {
      // URI value
      return _createExpressionURL (aChildNode);
    }
    else
      if (ECSSNodeType.FUNCTION.isNode (aChildNode))
      {
        // function value
        return _createExpressionFunction (aChildNode);
      }
      else
        if (ECSSNodeType.CALC.isNode (aChildNode))
        {
          // Math value
          return _createExpressionCalc (aChildNode);
        }
        else
          if (ECSSNodeType.LINE_NAMES.isNode (aChildNode))
          {
            // Math value
            return _createExpressionLineNamesTerm (aChildNode);
          }
          else
            throw new IllegalStateException ("Expected an expression term but got " +
                                             ECSSNodeType.getNodeName (aChildNode));
  }

  @Nonnull
  private CSSExpression _createExpression (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.EXPR);
    final CSSExpression ret = new CSSExpression ();
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    for (final CSSNode aChildNode : aNode)
    {
      if (ECSSNodeType.EXPRTERM.isNode (aChildNode))
        ret.addMember (_createExpressionTerm (aChildNode));
      else
        if (ECSSNodeType.EXPROPERATOR.isNode (aChildNode))
        {
          final String sText = aChildNode.getText ();
          final ECSSExpressionOperator eOp = ECSSExpressionOperator.getFromNameOrNull (sText);
          if (eOp == null)
            m_aErrorHandler.onCSSInterpretationError ("Failed to parse expression operator '" + sText + "'");
          else
            ret.addMember (eOp);
        }
        else
        {
          m_aErrorHandler.onCSSInterpretationError ("Unsupported child of " +
                                                    ECSSNodeType.getNodeName (aNode) +
                                                    ": " +
                                                    ECSSNodeType.getNodeName (aChildNode));
        }
    }
    return ret;
  }

  @Nullable
  private CSSDeclaration _createDeclaration (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.STYLEDECLARATION);
    final int nChildCount = aNode.jjtGetNumChildren ();
    if (nChildCount < 1 || nChildCount > 3)
      _throwUnexpectedChildrenCount (aNode, "Expected 1-3 children but got " + nChildCount + "!");

    if (nChildCount == 1)
    {
      // Syntax error. E.g. "color:;"
      return null;
    }

    if (!ECSSNodeType.EXPR.isNode (aNode.jjtGetChild (1)))
    {
      // Syntax error. E.g. "color: !important;"
      return null;
    }

    final String sProperty = aNode.jjtGetChild (0).getText ();
    if (sProperty == null)
    {
      // Syntax error with deprecated property name (see #84)
      return null;
    }

    final CSSExpression aExpression = _createExpression (aNode.jjtGetChild (1));
    boolean bImportant = false;
    if (nChildCount == 3)
    {
      // Must be an "!important" node
      final CSSNode aChildNode = aNode.jjtGetChild (2);
      if (ECSSNodeType.IMPORTANT.isNode (aChildNode))
        bImportant = true;
      else
        m_aErrorHandler.onCSSInterpretationError ("Expected an " +
                                                  ECSSNodeType.IMPORTANT.getNodeName () +
                                                  " token but got a " +
                                                  ECSSNodeType.getNodeName (aChildNode));
    }

    final CSSDeclaration ret = new CSSDeclaration (sProperty, aExpression, bImportant);
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    return ret;
  }

  private void _readStyleDeclarationList (@Nonnull final CSSNode aNode,
                                          @Nonnull final Consumer <CSSDeclaration> aConsumer)
  {
    _expectNodeType (aNode, ECSSNodeType.STYLEDECLARATIONLIST);
    // Read all contained declarations
    final int nDecls = aNode.jjtGetNumChildren ();
    for (int nDecl = 0; nDecl < nDecls; ++nDecl)
    {
      final CSSNode aChildNode = aNode.jjtGetChild (nDecl);
      if (ECSSNodeType.STYLEDECLARATION.isNode (aChildNode))
      {
        final CSSDeclaration aDeclaration = _createDeclaration (aChildNode);
        if (aDeclaration != null)
          aConsumer.accept (aDeclaration);
      }
      // else
      // ignore ERROR_SKIP to and all "@" things
    }
  }

  @Nullable
  private CSSStyleRule _createStyleRule (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.STYLERULE);
    final CSSStyleRule ret = new CSSStyleRule ();
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    boolean bSelectors = true;
    for (final CSSNode aChildNode : aNode)
    {
      if (ECSSNodeType.SELECTOR.isNode (aChildNode))
      {
        if (!bSelectors)
          m_aErrorHandler.onCSSInterpretationError ("Found a selector after a declaration!");

        ret.addSelector (_createSelector (aChildNode));
      }
      else
      {
        // OK, we're after the selectors
        bSelectors = false;
        if (ECSSNodeType.STYLEDECLARATIONLIST.isNode (aChildNode))
        {
          // Read all contained declarations
          _readStyleDeclarationList (aChildNode, ret::addDeclaration);
        }
        else
          if (!ECSSNodeType.isErrorNode (aChildNode))
            m_aErrorHandler.onCSSInterpretationError ("Unsupported child of " +
                                                      ECSSNodeType.getNodeName (aNode) +
                                                      ": " +
                                                      ECSSNodeType.getNodeName (aChildNode));
      }
    }

    if (ret.getSelectorCount () == 0)
    {
      // Error in selector parsing in browser compliant mode
      return null;
    }

    return ret;
  }

  @Nonnull
  private CSSPageRule _createPageRule (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.PAGERULE);

    final int nChildCount = aNode.jjtGetNumChildren ();
    if (nChildCount < 1)
      _throwUnexpectedChildrenCount (aNode, "Expected at least 1 child but got " + nChildCount + "!");

    // Read page selectors (0-n)
    final ICommonsList <String> aSelectors = new CommonsArrayList <> ();
    for (int nIndex = 0; nIndex < nChildCount - 1; ++nIndex)
    {
      final CSSNode aChildNode = aNode.jjtGetChild (nIndex);
      _expectNodeType (aChildNode, ECSSNodeType.PAGESELECTOR);
      aSelectors.add (aChildNode.getText ());
    }

    final CSSPageRule ret = new CSSPageRule (aSelectors);
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());

    // Read page body
    final CSSNode aBodyNode = aNode.jjtGetChild (nChildCount - 1);
    _expectNodeType (aBodyNode, ECSSNodeType.PAGERULEBLOCK);

    final int nBodyChildren = aBodyNode.jjtGetNumChildren ();
    for (int nIndex = 0; nIndex < nBodyChildren; ++nIndex)
    {
      final CSSNode aBodyChildNode = aBodyNode.jjtGetChild (nIndex);
      if (ECSSNodeType.STYLEDECLARATION.isNode (aBodyChildNode))
      {
        final CSSDeclaration aDeclaration = _createDeclaration (aBodyChildNode);
        if (aDeclaration != null)
          ret.addMember (aDeclaration);
      }
      else
        if (ECSSNodeType.PAGEMARGINSYMBOL.isNode (aBodyChildNode))
        {
          final CSSPageMarginBlock aBlock = new CSSPageMarginBlock (aBodyChildNode.getText ());
          if (m_bUseSourceLocation)
            aBlock.setSourceLocation (aBodyChildNode.getSourceLocation ());

          final CSSNode aBodyNextChildNode = aBodyNode.jjtGetChild (nIndex + 1);
          _readStyleDeclarationList (aBodyNextChildNode, aBlock::addDeclaration);

          ret.addMember (aBlock);

          // Skip style declaration list
          ++nIndex;
        }
        else
          if (!ECSSNodeType.isErrorNode (aBodyChildNode))
            m_aErrorHandler.onCSSInterpretationError ("Unsupported page rule body child: " +
                                                      ECSSNodeType.getNodeName (aBodyChildNode));
    }

    return ret;
  }

  @Nonnull
  private CSSMediaRule _createMediaRule (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.MEDIARULE);
    final CSSMediaRule ret = new CSSMediaRule ();
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    for (final CSSNode aChildNode : aNode)
    {
      if (ECSSNodeType.MEDIALIST.isNode (aChildNode))
      {
        for (final CSSNode aMediaListChildNode : aChildNode)
          ret.addMediaQuery (_createMediaQuery (aMediaListChildNode));
      }
      else
        if (ECSSNodeType.STYLERULE.isNode (aChildNode))
        {
          final CSSStyleRule aStyleRule = _createStyleRule (aChildNode);
          if (aStyleRule != null)
            ret.addRule (aStyleRule);
        }
        else
          if (ECSSNodeType.MEDIARULE.isNode (aChildNode))
          {
            // Nested media rules are OK!
            ret.addRule (_createMediaRule (aChildNode));
          }
          else
            if (ECSSNodeType.PAGERULE.isNode (aChildNode))
              ret.addRule (_createPageRule (aChildNode));
            else
              if (ECSSNodeType.FONTFACERULE.isNode (aChildNode))
                ret.addRule (_createFontFaceRule (aChildNode));
              else
                if (ECSSNodeType.KEYFRAMESRULE.isNode (aChildNode))
                  ret.addRule (_createKeyframesRule (aChildNode));
                else
                  if (ECSSNodeType.VIEWPORTRULE.isNode (aChildNode))
                    ret.addRule (_createViewportRule (aChildNode));
                  else
                    if (ECSSNodeType.SUPPORTSRULE.isNode (aChildNode))
                      ret.addRule (_createSupportsRule (aChildNode));
                    else
                      if (ECSSNodeType.LAYERRULE.isNode (aChildNode))
                        ret.addRule (_createLayerRule (aChildNode));
                      else
                        if (ECSSNodeType.UNKNOWNRULE.isNode (aChildNode))
                        {
                          // Unknown rule indicates either
                          // 1. a parsing error
                          // 2. a non-standard rule
                          ret.addRule (_createUnknownRule (aChildNode));
                        }
                        else
                          if (!ECSSNodeType.isErrorNode (aChildNode))
                            m_aErrorHandler.onCSSInterpretationError ("Unsupported media-rule child: " +
                                                                      ECSSNodeType.getNodeName (aChildNode));
    }
    return ret;
  }

  @Nonnull
  private CSSMediaQuery _createMediaQuery (@Nonnull final CSSNode aNode)
  {
    if (ECSSNodeType.MEDIUM.isNode (aNode))
    {
      // CSS 2.1 compatibility
      final String sMedium = aNode.getText ();
      if (ECSSMedium.getFromNameOrNull (sMedium) == null)
        m_aErrorHandler.onCSSInterpretationWarning ("CSS Media query uses unknown medium '" + sMedium + "'");
      final CSSMediaQuery ret = new CSSMediaQuery (EModifier.NONE, sMedium);
      if (m_bUseSourceLocation)
        ret.setSourceLocation (aNode.getSourceLocation ());
      return ret;
    }

    // CSS 3.0 media query
    _expectNodeType (aNode, ECSSNodeType.MEDIAQUERY);
    final int nChildCount = aNode.jjtGetNumChildren ();

    int nStartIndex = 0;
    EModifier eModifier = EModifier.NONE;

    // Check if a media modifier is present
    if (nChildCount > 0)
    {
      final CSSNode aFirstChildNode = aNode.jjtGetChild (0);
      if (ECSSNodeType.MEDIAMODIFIER.isNode (aFirstChildNode))
      {
        final String sMediaModifier = aFirstChildNode.getText ();
        // The "mediaModifier" token might be present, but without text!!!
        if (sMediaModifier != null)
        {
          if ("not".equalsIgnoreCase (sMediaModifier))
            eModifier = EModifier.NOT;
          else
            if ("only".equalsIgnoreCase (sMediaModifier))
              eModifier = EModifier.ONLY;
            else
              m_aErrorHandler.onCSSInterpretationError ("Unsupported media modifier '" + sMediaModifier + "' found!");
        }
        ++nStartIndex;
      }
    }

    // Next check if a medium is present
    String sMedium = null;
    if (nChildCount > nStartIndex)
    {
      final CSSNode aNextChild = aNode.jjtGetChild (nStartIndex);
      if (ECSSNodeType.MEDIUM.isNode (aNextChild))
      {
        sMedium = aNextChild.getText ();
        if (ECSSMedium.getFromNameOrNull (sMedium) == null)
          m_aErrorHandler.onCSSInterpretationWarning ("CSS media query uses unknown medium '" + sMedium + "'");
        ++nStartIndex;
      }
    }

    final CSSMediaQuery ret = new CSSMediaQuery (eModifier, sMedium);
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    for (int i = nStartIndex; i < nChildCount; ++i)
    {
      final CSSNode aChildNode = aNode.jjtGetChild (i);
      if (ECSSNodeType.MEDIAEXPR.isNode (aChildNode))
        ret.addMediaExpression (_createMediaExpr (aChildNode));
      else
        if (!ECSSNodeType.isErrorNode (aChildNode))
          m_aErrorHandler.onCSSInterpretationError ("Unsupported media query child: " +
                                                    ECSSNodeType.getNodeName (aChildNode));
    }
    return ret;
  }

  @Nonnull
  private CSSMediaExpression _createMediaExpr (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.MEDIAEXPR);
    final int nChildCount = aNode.jjtGetNumChildren ();
    if (nChildCount != 1 && nChildCount != 2)
      _throwUnexpectedChildrenCount (aNode, "Expected 1 or 2 children but got " + nChildCount + "!");

    final CSSNode aFeatureNode = aNode.jjtGetChild (0);
    if (!ECSSNodeType.MEDIAFEATURE.isNode (aFeatureNode))
      throw new IllegalStateException ("Expected a media feature but got " + ECSSNodeType.getNodeName (aFeatureNode));
    final String sFeature = aFeatureNode.getText ();
    if (ECSSMediaExpressionFeature.getFromNameOrNull (sFeature) == null)
      m_aErrorHandler.onCSSInterpretationWarning ("Media expression uses unknown feature '" + sFeature + "'");

    CSSMediaExpression ret;
    if (nChildCount == 1)
    {
      // Feature only
      ret = new CSSMediaExpression (sFeature);
    }
    else
    {
      // Feature + value
      final CSSNode aValueNode = aNode.jjtGetChild (1);
      ret = new CSSMediaExpression (sFeature, _createExpression (aValueNode));
    }
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    return ret;
  }

  @Nonnull
  private CSSFontFaceRule _createFontFaceRule (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.FONTFACERULE);

    final String sFontFaceDeclaration = aNode.getText ();
    final CSSFontFaceRule ret = new CSSFontFaceRule (sFontFaceDeclaration);
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    for (final CSSNode aChildNode : aNode)
    {
      if (ECSSNodeType.STYLEDECLARATIONLIST.isNode (aChildNode))
      {
        // Read all contained declarations
        _readStyleDeclarationList (aChildNode, ret::addDeclaration);
      }
      else
        if (!ECSSNodeType.isErrorNode (aChildNode))
          m_aErrorHandler.onCSSInterpretationError ("Unsupported font-face rule child: " +
                                                    ECSSNodeType.getNodeName (aChildNode));
    }
    return ret;
  }

  @Nonnull
  private CSSLayerRule _createLayerRule (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.LAYERRULE);
    final int nChildCount = aNode.jjtGetNumChildren ();
    if (nChildCount < 1 || nChildCount > 2)
      _throwUnexpectedChildrenCount (aNode,
                                     "Expected at least 1 child and at last 2 children but got " + nChildCount + "!");

    final CSSLayerRule ret;
    final ICommonsList <String> aLayerSelectors = new CommonsArrayList <> ();
    if (ECSSNodeType.LAYERSELECTORLIST.isNode (aNode.jjtGetChild (0)))
    {
      for (final CSSNode aSelectorChild : aNode.jjtGetChild (0))
      {
        _expectNodeType (aSelectorChild, ECSSNodeType.LAYERSELECTOR);
        aLayerSelectors.add (aSelectorChild.getText ());
      }

      ret = new CSSLayerRule (aLayerSelectors);
    }
    else
    {
      if (ECSSNodeType.LAYERSELECTOR.isNode (aNode.jjtGetChild (0)))
      {
        aLayerSelectors.add (aNode.jjtGetChild (0).getText ());
      }

      ret = new CSSLayerRule (aLayerSelectors);

      final CSSNode aBodyNode = aNode.jjtGetChild (nChildCount - 1);
      _expectNodeType (aBodyNode, ECSSNodeType.LAYERRULEBLOCK);

      final int nBodyChildren = aBodyNode.jjtGetNumChildren ();
      for (int nIndex = 0; nIndex < nBodyChildren; ++nIndex)
      {
        final CSSNode aBodyChildNode = aBodyNode.jjtGetChild (nIndex);
        if (ECSSNodeType.STYLERULE.isNode (aBodyChildNode))
        {
          final CSSStyleRule aStyleRule = _createStyleRule (aBodyChildNode);
          if (aStyleRule != null)
            ret.addRule (aStyleRule);
        }
        else
          if (ECSSNodeType.LAYERRULE.isNode (aBodyChildNode))
            ret.addRule (_createLayerRule (aBodyChildNode));
          else
            if (ECSSNodeType.MEDIARULE.isNode (aBodyChildNode))
              ret.addRule (_createMediaRule (aBodyChildNode));
            else
              if (ECSSNodeType.SUPPORTSRULE.isNode (aBodyChildNode))
                ret.addRule (_createSupportsRule (aBodyChildNode));
              else
                if (ECSSNodeType.KEYFRAMESRULE.isNode (aBodyChildNode))
                  ret.addRule (_createKeyframesRule (aBodyChildNode));
                else
                  if (ECSSNodeType.FONTFACERULE.isNode (aBodyChildNode))
                    ret.addRule (_createFontFaceRule (aBodyChildNode));
                  else
                    if (!ECSSNodeType.isErrorNode (aBodyChildNode))
                      m_aErrorHandler.onCSSInterpretationError ("Unsupported layer-rule child: " +
                                                                ECSSNodeType.getNodeName (aBodyChildNode));
      }
    }

    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    return ret;
  }

  @Nonnull
  private CSSKeyframesRule _createKeyframesRule (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.KEYFRAMESRULE);
    final int nChildCount = aNode.jjtGetNumChildren ();
    if (nChildCount == 0)
      _throwUnexpectedChildrenCount (aNode, "Expected at least 1 child but got " + nChildCount + "!");

    // Get the identifier (e.g. the default "@keyframes" or the non-standard
    // "@-webkit-keyframes")
    final String sKeyframesDeclaration = aNode.getText ();

    // get the name of the animation
    final CSSNode aAnimationNameNode = aNode.jjtGetChild (0);
    _expectNodeType (aAnimationNameNode, ECSSNodeType.KEYFRAMESIDENTIFIER);
    final String sAnimationName = aAnimationNameNode.getText ();

    final CSSKeyframesRule ret = new CSSKeyframesRule (sKeyframesDeclaration, sAnimationName);
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());

    // Get the key frame blocks
    int nIndex = 1;
    CSSKeyframesBlock aBlock = null;
    while (nIndex < nChildCount)
    {
      final CSSNode aChildNode = aNode.jjtGetChild (nIndex);
      if (ECSSNodeType.KEYFRAMESSELECTOR.isNode (aChildNode))
      {
        // Read all single selectors
        final ICommonsList <String> aKeyframesSelectors = new CommonsArrayList <> ();
        for (final CSSNode aSelectorChild : aChildNode)
        {
          _expectNodeType (aSelectorChild, ECSSNodeType.SINGLEKEYFRAMESELECTOR);
          aKeyframesSelectors.add (aSelectorChild.getText ());
        }
        aBlock = new CSSKeyframesBlock (aKeyframesSelectors);
        if (m_bUseSourceLocation)
          aBlock.setSourceLocation (aChildNode.getSourceLocation ());
        ret.addBlock (aBlock);
      }
      else
        if (ECSSNodeType.STYLEDECLARATIONLIST.isNode (aChildNode))
        {
          if (aBlock == null)
            throw new IllegalStateException ("No keyframes block present!");

          // Read all contained declarations
          final CSSKeyframesBlock aFinalBlock = aBlock;
          _readStyleDeclarationList (aChildNode, aFinalBlock::addDeclaration);
        }
        else
          if (!ECSSNodeType.isErrorNode (aChildNode))
            m_aErrorHandler.onCSSInterpretationError ("Unsupported keyframes rule child: " +
                                                      ECSSNodeType.getNodeName (aChildNode));

      ++nIndex;
    }
    return ret;
  }

  @Nonnull
  private CSSViewportRule _createViewportRule (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.VIEWPORTRULE);

    // Get the identifier (e.g. the default "@viewport" or the non-standard
    // "@-ms-viewport")
    final String sViewportDeclaration = aNode.getText ();

    final CSSViewportRule ret = new CSSViewportRule (sViewportDeclaration);
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    for (final CSSNode aChildNode : aNode)
    {
      if (ECSSNodeType.STYLEDECLARATIONLIST.isNode (aChildNode))
      {
        // Read all contained declarations
        _readStyleDeclarationList (aChildNode, ret::addDeclaration);
      }
      else
        if (!ECSSNodeType.isErrorNode (aChildNode))
          m_aErrorHandler.onCSSInterpretationError ("Unsupported viewport rule child: " +
                                                    ECSSNodeType.getNodeName (aChildNode));
    }
    return ret;
  }

  @Nonnull
  private CSSNamespaceRule _createNamespaceRule (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.NAMESPACERULE);
    final int nChildCount = aNode.jjtGetNumChildren ();
    if (nChildCount < 1 || nChildCount > 2)
      _throwUnexpectedChildrenCount (aNode,
                                     "Expected at least 1 child and at last 2 children but got " + nChildCount + "!");

    String sPrefix = null;
    int nURLIndex = 0;
    if (ECSSNodeType.NAMESPACERULEPREFIX.isNode (aNode.jjtGetChild (0)))
    {
      sPrefix = aNode.jjtGetChild (0).getText ();
      nURLIndex++;
    }

    final CSSNode aURLNode = aNode.jjtGetChild (nURLIndex);
    _expectNodeType (aURLNode, ECSSNodeType.NAMESPACERULEURL);
    final String sURL = CSSParseHelper.extractStringValue (aURLNode.getText ());

    final CSSNamespaceRule ret = new CSSNamespaceRule (sPrefix, sURL);
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    return ret;
  }

  @Nullable
  private ICSSSupportsConditionMember _createSupportsConditionMemberRecursive (@Nonnull final CSSNode aNode)
  {
    final int nChildCount = aNode.jjtGetNumChildren ();

    if (ECSSNodeType.SUPPORTSCONDITIONOPERATOR.isNode (aNode))
    {
      if (nChildCount != 0)
        _throwUnexpectedChildrenCount (aNode, "Expected no children but got " + nChildCount + "!");

      return ECSSSupportsConditionOperator.getFromNameCaseInsensitiveOrNull (aNode.getText ());
    }

    if (ECSSNodeType.SUPPORTSNEGATION.isNode (aNode))
    {
      if (nChildCount != 1)
        _throwUnexpectedChildrenCount (aNode, "Expected at exactly 1 child but got " + nChildCount + "!");

      final ICSSSupportsConditionMember aNestedMember = _createSupportsConditionMemberRecursive (aNode.jjtGetChild (0));
      if (aNestedMember == null)
        return null;

      final CSSSupportsConditionNegation ret = new CSSSupportsConditionNegation (aNestedMember);
      if (m_bUseSourceLocation)
        ret.setSourceLocation (aNode.getSourceLocation ());
      return ret;
    }

    if (ECSSNodeType.SUPPORTSCONDITIONINPARENS.isNode (aNode))
    {
      if (nChildCount != 1)
        _throwUnexpectedChildrenCount (aNode, "Expected at exactly 1 child but got " + nChildCount + "!");

      final CSSNode aChildNode = aNode.jjtGetChild (0);

      if (ECSSNodeType.STYLEDECLARATION.isNode (aChildNode))
      {
        final CSSDeclaration aDeclaration = _createDeclaration (aChildNode);
        if (aDeclaration == null)
          throw new CSSHandlingException (aChildNode, "The style declaration in the @supports rule is invalid!");
        final CSSSupportsConditionDeclaration ret = new CSSSupportsConditionDeclaration (aDeclaration);
        if (m_bUseSourceLocation)
          ret.setSourceLocation (aChildNode.getSourceLocation ());
        return ret;
      }

      if (ECSSNodeType.SUPPORTSCONDITION.isNode (aChildNode))
      {
        final CSSSupportsConditionNested ret = new CSSSupportsConditionNested ();
        for (final CSSNode aChildChildNode : aChildNode)
        {
          final ICSSSupportsConditionMember aMember = _createSupportsConditionMemberRecursive (aChildChildNode);
          if (aMember != null)
            ret.addMember (aMember);
        }
        if (m_bUseSourceLocation)
          ret.setSourceLocation (aChildNode.getSourceLocation ());
        return ret;
      }

      m_aErrorHandler.onCSSInterpretationError ("Unsupported supportsConditionInParents child: " +
                                                ECSSNodeType.getNodeName (aChildNode));
      return null;
    }

    if (!ECSSNodeType.isErrorNode (aNode))
      m_aErrorHandler.onCSSInterpretationError ("Unsupported supports-condition child: " +
                                                ECSSNodeType.getNodeName (aNode));

    return null;
  }

  @Nonnull
  private CSSSupportsRule _createSupportsRule (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.SUPPORTSRULE);
    final CSSSupportsRule ret = new CSSSupportsRule ();
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    for (final CSSNode aChildNode : aNode)
    {
      if (ECSSNodeType.SUPPORTSCONDITION.isNode (aChildNode))
      {
        for (final CSSNode aChildChildNode : aChildNode)
        {
          final ICSSSupportsConditionMember aMember = _createSupportsConditionMemberRecursive (aChildChildNode);
          if (aMember != null)
            ret.addSupportConditionMember (aMember);
        }
      }
      else
        if (ECSSNodeType.STYLERULE.isNode (aChildNode))
        {
          final CSSStyleRule aStyleRule = _createStyleRule (aChildNode);
          if (aStyleRule != null)
            ret.addRule (aStyleRule);
        }
        else
          if (ECSSNodeType.MEDIARULE.isNode (aChildNode))
            ret.addRule (_createMediaRule (aChildNode));
          else
            if (ECSSNodeType.PAGERULE.isNode (aChildNode))
              ret.addRule (_createPageRule (aChildNode));
            else
              if (ECSSNodeType.FONTFACERULE.isNode (aChildNode))
                ret.addRule (_createFontFaceRule (aChildNode));
              else
                if (ECSSNodeType.KEYFRAMESRULE.isNode (aChildNode))
                  ret.addRule (_createKeyframesRule (aChildNode));
                else
                  if (ECSSNodeType.VIEWPORTRULE.isNode (aChildNode))
                    ret.addRule (_createViewportRule (aChildNode));
                  else
                    if (ECSSNodeType.SUPPORTSRULE.isNode (aChildNode))
                      ret.addRule (_createSupportsRule (aChildNode));
                    else
                      if (ECSSNodeType.LAYERRULE.isNode (aChildNode))
                        ret.addRule (_createLayerRule (aChildNode));
                      else
                        if (!ECSSNodeType.isErrorNode (aChildNode))
                          m_aErrorHandler.onCSSInterpretationError ("Unsupported supports-rule child: " +
                                                                    ECSSNodeType.getNodeName (aChildNode));
    }
    return ret;
  }

  @Nonnull
  private CSSUnknownRule _createUnknownRule (@Nonnull final CSSNode aNode)
  {
    _expectNodeType (aNode, ECSSNodeType.UNKNOWNRULE);

    final int nChildCount = aNode.jjtGetNumChildren ();
    if (nChildCount != 2)
      _throwUnexpectedChildrenCount (aNode, "Expected 2 children but got " + nChildCount + "!");

    final CSSNode aParameterList = aNode.jjtGetChild (0);
    _expectNodeType (aParameterList, ECSSNodeType.UNKNOWNRULEPARAMETERLIST);

    final CSSNode aBody = aNode.jjtGetChild (1);
    _expectNodeType (aBody, ECSSNodeType.UNKNOWNRULEBODY);

    // Get the name of the rule
    final String sRuleDeclaration = aNode.getText ();

    final CSSUnknownRule ret = new CSSUnknownRule (sRuleDeclaration);
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    ret.setParameterList (aParameterList.getText ());
    ret.setBody (aBody.getText ());
    return ret;
  }

  private void _recursiveFillCascadingStyleSheetFromNode (@Nonnull final CSSNode aNode,
                                                          @Nonnull final CascadingStyleSheet ret)
  {
    _expectNodeType (aNode, ECSSNodeType.ROOT);
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    for (final CSSNode aChildNode : aNode)
    {
      if (ECSSNodeType.CHARSET.isNode (aChildNode))
      {
        // Ignore because this was handled when reading!
      }
      else
        if (ECSSNodeType.IMPORTRULE.isNode (aChildNode))
          ret.addImportRule (_createImportRule (aChildNode));
        else
          if (ECSSNodeType.NAMESPACERULE.isNode (aChildNode))
            ret.addNamespaceRule (_createNamespaceRule (aChildNode));
          else
            if (ECSSNodeType.STYLERULE.isNode (aChildNode))
            {
              final CSSStyleRule aStyleRule = _createStyleRule (aChildNode);
              if (aStyleRule != null)
                ret.addRule (aStyleRule);
            }
            else
              if (ECSSNodeType.PAGERULE.isNode (aChildNode))
                ret.addRule (_createPageRule (aChildNode));
              else
                if (ECSSNodeType.MEDIARULE.isNode (aChildNode))
                  ret.addRule (_createMediaRule (aChildNode));
                else
                  if (ECSSNodeType.FONTFACERULE.isNode (aChildNode))
                    ret.addRule (_createFontFaceRule (aChildNode));
                  else
                    if (ECSSNodeType.LAYERRULE.isNode (aChildNode))
                      ret.addRule (_createLayerRule (aChildNode));
                    else
                      if (ECSSNodeType.KEYFRAMESRULE.isNode (aChildNode))
                        ret.addRule (_createKeyframesRule (aChildNode));
                      else
                        if (ECSSNodeType.VIEWPORTRULE.isNode (aChildNode))
                          ret.addRule (_createViewportRule (aChildNode));
                        else
                          if (ECSSNodeType.SUPPORTSRULE.isNode (aChildNode))
                            ret.addRule (_createSupportsRule (aChildNode));
                          else
                            if (ECSSNodeType.UNKNOWNRULE.isNode (aChildNode))
                            {
                              // Unknown rule indicates either
                              // 1. a parsing error
                              // 2. a non-standard rule
                              ret.addRule (_createUnknownRule (aChildNode));
                            }
                            else
                              if (ECSSNodeType.ROOT.isNode (aChildNode))
                              {
                                /*
                                 * In case a parsing error occurs (as e.g. happening in issue #41)
                                 * and browser compliant mode is enabled, some CSS code is skipped
                                 * and a retry happens. This retry will be a recursive stylesheet
                                 * object that is a child of the previous stylesheet but "flattened"
                                 * for the result object.
                                 */
                                _recursiveFillCascadingStyleSheetFromNode (aChildNode, ret);
                              }
                              else
                                m_aErrorHandler.onCSSInterpretationError ("Unsupported child of " +
                                                                          ECSSNodeType.getNodeName (aNode) +
                                                                          ": " +
                                                                          ECSSNodeType.getNodeName (aChildNode));
    }
  }

  @Nonnull
  public CascadingStyleSheet createCascadingStyleSheetFromNode (@Nonnull final CSSNode aNode)
  {
    final CascadingStyleSheet ret = new CascadingStyleSheet ();
    _recursiveFillCascadingStyleSheetFromNode (aNode, ret);
    return ret;
  }

  @Nonnull
  public CSSDeclarationList createDeclarationListFromNode (@Nonnull final CSSNode aNode)
  {
    final CSSDeclarationList ret = new CSSDeclarationList ();
    if (m_bUseSourceLocation)
      ret.setSourceLocation (aNode.getSourceLocation ());
    _readStyleDeclarationList (aNode, ret::addDeclaration);
    return ret;
  }
}
