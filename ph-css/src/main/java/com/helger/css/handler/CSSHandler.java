/*
 * Copyright (C) 2014-2021 Philip Helger (www.helger.com)
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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.parser.CSSNode;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.errorhandler.ICSSInterpretErrorHandler;

/**
 * This class is the entry point for converting AST nodes from the parser to
 * domain objects. This class is only used internally.
 *
 * @author Philip Helger
 */
@Immutable
public final class CSSHandler
{
  public static final boolean DEFAULT_USE_SOURCE_LOCATION = true;

  @PresentForCodeCoverage
  private static final CSSHandler s_aInstance = new CSSHandler ();

  private CSSHandler ()
  {}

  /**
   * Create a {@link CascadingStyleSheet} object from a parsed object.
   *
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aNode
   *        The parsed CSS object to read. May not be <code>null</code>.
   * @return Never <code>null</code>.
   * @deprecated Use
   *             {@link #readCascadingStyleSheetFromNode(ECSSVersion, ICSSInterpretErrorHandler, boolean, CSSNode)}
   *             instead
   */
  @Nonnull
  @Deprecated
  public static CascadingStyleSheet readCascadingStyleSheetFromNode (@Nonnull final ECSSVersion eVersion, @Nonnull final CSSNode aNode)
  {
    return readCascadingStyleSheetFromNode (eVersion, CSSReader.getDefaultInterpretErrorHandler (), DEFAULT_USE_SOURCE_LOCATION, aNode);
  }

  /**
   * Create a {@link CascadingStyleSheet} object from a parsed object.
   *
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aNode
   *        The parsed CSS object to read. May not be <code>null</code>.
   * @param aErrorHandler
   *        The error handler to be used. May not be <code>null</code>.
   * @return Never <code>null</code>.
   * @since 5.0.2
   * @deprecated Use
   *             {@link #readCascadingStyleSheetFromNode(ECSSVersion, ICSSInterpretErrorHandler, boolean, CSSNode)}
   *             instead
   */
  @Nonnull
  @Deprecated
  public static CascadingStyleSheet readCascadingStyleSheetFromNode (@Nonnull final ECSSVersion eVersion,
                                                                     @Nonnull final CSSNode aNode,
                                                                     @Nonnull final ICSSInterpretErrorHandler aErrorHandler)
  {
    return readCascadingStyleSheetFromNode (eVersion, aErrorHandler, DEFAULT_USE_SOURCE_LOCATION, aNode);
  }

  /**
   * Create a {@link CascadingStyleSheet} object from a parsed object.
   *
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aErrorHandler
   *        The error handler to be used. May not be <code>null</code>.
   * @param bUseSourceLocation
   *        <code>true</code> to keep the source location, <code>false</code> to
   *        ignore the source location. Disabling the source location may be a
   *        performance improvement.
   * @param aNode
   *        The parsed CSS object to read. May not be <code>null</code>.
   * @return Never <code>null</code>.
   * @since 6.1.3
   */
  @Nonnull
  public static CascadingStyleSheet readCascadingStyleSheetFromNode (@Nonnull final ECSSVersion eVersion,
                                                                     @Nonnull final ICSSInterpretErrorHandler aErrorHandler,
                                                                     final boolean bUseSourceLocation,
                                                                     @Nonnull final CSSNode aNode)
  {
    ValueEnforcer.notNull (eVersion, "Version");
    ValueEnforcer.notNull (aNode, "Node");
    if (!ECSSNodeType.ROOT.isNode (aNode, eVersion))
      throw new CSSHandlingException (aNode, "Passed node is not a root node!");
    ValueEnforcer.notNull (aErrorHandler, "ErrorHandler");

    return new CSSNodeToDomainObject (eVersion, aErrorHandler, bUseSourceLocation).createCascadingStyleSheetFromNode (aNode);
  }

  /**
   * Create a {@link CSSDeclarationList} object from a parsed object.
   *
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aNode
   *        The parsed CSS object to read. May not be <code>null</code>.
   * @return Never <code>null</code>.
   * @deprecated Use
   *             {@link #readDeclarationListFromNode(ECSSVersion, ICSSInterpretErrorHandler, boolean, CSSNode)}
   *             instead
   */
  @Nonnull
  @Deprecated
  public static CSSDeclarationList readDeclarationListFromNode (@Nonnull final ECSSVersion eVersion, @Nonnull final CSSNode aNode)
  {
    return readDeclarationListFromNode (eVersion, CSSReader.getDefaultInterpretErrorHandler (), DEFAULT_USE_SOURCE_LOCATION, aNode);
  }

  /**
   * Create a {@link CSSDeclarationList} object from a parsed object.
   *
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aNode
   *        The parsed CSS object to read. May not be <code>null</code>.
   * @param aErrorHandler
   *        The error handler to be used. May not be <code>null</code>.
   * @return Never <code>null</code>.
   * @since 5.0.2
   * @deprecated Use
   *             {@link #readDeclarationListFromNode(ECSSVersion, ICSSInterpretErrorHandler, boolean, CSSNode)}
   *             instead
   */
  @Deprecated
  @Nonnull
  public static CSSDeclarationList readDeclarationListFromNode (@Nonnull final ECSSVersion eVersion,
                                                                @Nonnull final CSSNode aNode,
                                                                @Nonnull final ICSSInterpretErrorHandler aErrorHandler)
  {
    return readDeclarationListFromNode (eVersion, aErrorHandler, DEFAULT_USE_SOURCE_LOCATION, aNode);
  }

  /**
   * Create a {@link CSSDeclarationList} object from a parsed object.
   *
   * @param eVersion
   *        The CSS version to use. May not be <code>null</code>.
   * @param aErrorHandler
   *        The error handler to be used. May not be <code>null</code>.
   * @param bUseSourceLocation
   *        <code>true</code> to keep the source location, <code>false</code> to
   *        ignore the source location. Disabling the source location may be a
   *        performance improvement.
   * @param aNode
   *        The parsed CSS object to read. May not be <code>null</code>.
   * @return Never <code>null</code>.
   * @since 6.1.3
   */
  @Nonnull
  public static CSSDeclarationList readDeclarationListFromNode (@Nonnull final ECSSVersion eVersion,
                                                                @Nonnull final ICSSInterpretErrorHandler aErrorHandler,
                                                                final boolean bUseSourceLocation,
                                                                @Nonnull final CSSNode aNode)
  {
    ValueEnforcer.notNull (eVersion, "Version");
    ValueEnforcer.notNull (aNode, "Node");
    if (!ECSSNodeType.STYLEDECLARATIONLIST.isNode (aNode, eVersion))
      throw new CSSHandlingException (aNode, "Passed node is not a style declaration node!");
    ValueEnforcer.notNull (aErrorHandler, "ErrorHandler");

    return new CSSNodeToDomainObject (eVersion, aErrorHandler, bUseSourceLocation).createDeclarationListFromNode (aNode);
  }
}
