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
package com.helger.css.decl.shorthand;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.helger.collection.commons.ICommonsList;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.property.ECSSProperty;
import com.helger.css.reader.CSSReaderDeclarationList;
import com.helger.css.writer.CSSWriterSettings;

/**
 * Test class for class {@link CSSShortHandDescriptor}.
 *
 * @author Philip Helger
 */
public final class CSSShortHandDescriptorTest
{
  private static final CSSWriterSettings CWS = new CSSWriterSettings (false);

  @Test
  public void testBasic ()
  {
    assertTrue (CSSShortHandRegistry.isShortHandProperty (ECSSProperty.MARGIN));
    assertFalse (CSSShortHandRegistry.isShortHandProperty (ECSSProperty.MARGIN_TOP));
  }

  @Test
  public void testBorder1 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BORDER);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("border:1px").getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (3, aSplittedDecls.size ());
    assertEquals ("border-width:1px", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("border-style:solid", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("border-color:black", aSplittedDecls.get (2).getAsCSSString (CWS));
  }

  @Test
  public void testBorder2 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BORDER);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("border:1px dashed")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (3, aSplittedDecls.size ());
    assertEquals ("border-width:1px", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("border-style:dashed", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("border-color:black", aSplittedDecls.get (2).getAsCSSString (CWS));
  }

  @Test
  public void testBorder3a ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BORDER);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("border:dashed 1px red")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (3, aSplittedDecls.size ());
    assertEquals ("border-style:dashed", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("border-width:1px", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("border-color:red", aSplittedDecls.get (2).getAsCSSString (CWS));
  }

  @Test
  public void testBorder3b ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BORDER);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("border:red 1px dashed")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (3, aSplittedDecls.size ());
    assertEquals ("border-color:red", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("border-width:1px", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("border-style:dashed", aSplittedDecls.get (2).getAsCSSString (CWS));
  }

  @Test
  public void testMargin1 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.MARGIN);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("margin:1px").getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());

    assertEquals ("margin-top:1px", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("margin-right:1px", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("margin-bottom:1px", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("margin-left:1px", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testMargin2 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.MARGIN);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("margin:1px 3px").getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());

    assertEquals ("margin-top:1px", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("margin-right:3px", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("margin-bottom:1px", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("margin-left:3px", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testMargin3 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.MARGIN);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("margin:1px 3px 5px")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());

    assertEquals ("margin-top:1px", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("margin-right:3px", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("margin-bottom:5px", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("margin-left:3px", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testMargin4 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.MARGIN);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("margin:1px 3px 5px 7px")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());

    assertEquals ("margin-top:1px", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("margin-right:3px", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("margin-bottom:5px", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("margin-left:7px", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testPadding1 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.PADDING);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("padding:1px").getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());

    assertEquals ("padding-top:1px", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("padding-right:1px", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("padding-bottom:1px", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("padding-left:1px", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testPadding2 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.PADDING);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("padding:1px 3px").getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());

    assertEquals ("padding-top:1px", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("padding-right:3px", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("padding-bottom:1px", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("padding-left:3px", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testPadding3 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.PADDING);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("padding:1px 3px 5px")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());

    assertEquals ("padding-top:1px", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("padding-right:3px", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("padding-bottom:5px", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("padding-left:3px", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testPadding4 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.PADDING);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("padding:1px 3px 5px 7px")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());

    assertEquals ("padding-top:1px", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("padding-right:3px", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("padding-bottom:5px", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("padding-left:7px", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testBackground1 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BACKGROUND);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("background: #ff0000 url('grafik.png') left top / 180px 100px no-repeat")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (8, aSplittedDecls.size ());

    assertEquals ("background-color:#ff0000", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("background-image:url(grafik.png)", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("background-position:left top", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("background-size:180px 100px", aSplittedDecls.get (3).getAsCSSString (CWS));
    assertEquals ("background-repeat:no-repeat", aSplittedDecls.get (4).getAsCSSString (CWS));
    assertEquals ("background-attachment:scroll", aSplittedDecls.get (5).getAsCSSString (CWS));
    assertEquals ("background-clip:border-box", aSplittedDecls.get (6).getAsCSSString (CWS));
    assertEquals ("background-origin:padding-box", aSplittedDecls.get (7).getAsCSSString (CWS));
  }

  @Test
  public void testBorderColor1 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BORDER_COLOR);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("border-color: red")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());
    assertEquals ("border-top-color:red", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("border-right-color:red", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("border-bottom-color:red", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("border-left-color:red", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testBorderColor2 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BORDER_COLOR);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("border-color: red blue")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());
    assertEquals ("border-top-color:red", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("border-right-color:blue", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("border-bottom-color:red", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("border-left-color:blue", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testBorderWidth1 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BORDER_WIDTH);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("border-width: thick")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());
    assertEquals ("border-top-width:thick", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("border-right-width:thick", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("border-bottom-width:thick", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("border-left-width:thick", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testBorderWidth2 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BORDER_WIDTH);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("border-width: 1px 3rem")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());
    assertEquals ("border-top-width:1px", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("border-right-width:3rem", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("border-bottom-width:1px", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("border-left-width:3rem", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testBorderWidth3 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BORDER_WIDTH);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("border-width: 1px 3rem 4px")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());
    assertEquals ("border-top-width:1px", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("border-right-width:3rem", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("border-bottom-width:4px", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("border-left-width:3rem", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testBorderWidth4 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.BORDER_WIDTH);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("border-width: 1px 3rem 4px thick")
                                                         .getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (4, aSplittedDecls.size ());
    assertEquals ("border-top-width:1px", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("border-right-width:3rem", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("border-bottom-width:4px", aSplittedDecls.get (2).getAsCSSString (CWS));
    assertEquals ("border-left-width:thick", aSplittedDecls.get (3).getAsCSSString (CWS));
  }

  @Test
  public void testFlexInitial ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.FLEX);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("flex:initial").getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (3, aSplittedDecls.size ());
    assertEquals ("flex-grow:0", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("flex-shrink:1", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("flex-basis:auto", aSplittedDecls.get (2).getAsCSSString (CWS));
  }

  @Test
  public void testFlexAuto ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.FLEX);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("flex:auto").getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (3, aSplittedDecls.size ());
    assertEquals ("flex-grow:1", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("flex-shrink:1", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("flex-basis:auto", aSplittedDecls.get (2).getAsCSSString (CWS));
  }

  @Test
  public void testFlexNone ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.FLEX);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("flex:none").getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (3, aSplittedDecls.size ());
    assertEquals ("flex-grow:0", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("flex-shrink:0", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("flex-basis:auto", aSplittedDecls.get (2).getAsCSSString (CWS));
  }

  @Test
  public void testFlex1Num ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.FLEX);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("flex:2").getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (3, aSplittedDecls.size ());
    assertEquals ("flex-grow:2", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("flex-shrink:1", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("flex-basis:0", aSplittedDecls.get (2).getAsCSSString (CWS));
  }

  @Test
  public void testFlex2Num ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.FLEX);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("flex:2 2").getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (3, aSplittedDecls.size ());
    assertEquals ("flex-grow:2", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("flex-shrink:2", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("flex-basis:auto", aSplittedDecls.get (2).getAsCSSString (CWS));
  }

  @Test
  public void testFlex2Width ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.FLEX);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("flex:2 2em").getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (3, aSplittedDecls.size ());
    assertEquals ("flex-grow:2", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("flex-shrink:1", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("flex-basis:2em", aSplittedDecls.get (2).getAsCSSString (CWS));
  }

  @Test
  public void testFlex3 ()
  {
    final CSSShortHandDescriptor aSHD = CSSShortHandRegistry.getShortHandDescriptor (ECSSProperty.FLEX);
    assertNotNull (aSHD);

    final CSSDeclaration aDecl = CSSReaderDeclarationList.readFromString ("flex:2 3 4em").getDeclarationAtIndex (0);
    assertNotNull (aDecl);

    final ICommonsList <CSSDeclaration> aSplittedDecls = aSHD.getSplitIntoPieces (aDecl);
    assertNotNull (aSplittedDecls);
    assertEquals (3, aSplittedDecls.size ());
    assertEquals ("flex-grow:2", aSplittedDecls.get (0).getAsCSSString (CWS));
    assertEquals ("flex-shrink:3", aSplittedDecls.get (1).getAsCSSString (CWS));
    assertEquals ("flex-basis:4em", aSplittedDecls.get (2).getAsCSSString (CWS));
  }
}
