/**
 * Copyright (C) 2014-2017 Philip Helger (www.helger.com)
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
package com.helger.css.supplementary.main;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.hierarchy.visit.DefaultHierarchyVisitorCallback;
import com.helger.commons.hierarchy.visit.EHierarchyVisitorReturn;
import com.helger.commons.io.file.SimpleFileIO;
import com.helger.commons.io.resource.URLResource;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.commons.string.StringHelper;
import com.helger.xml.microdom.IMicroDocument;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.IMicroNode;
import com.helger.xml.microdom.serialize.MicroReader;
import com.helger.xml.microdom.util.MicroVisitor;

public class MainFetchW3C_CSSTests
{
  public static void main (final String [] args) throws MalformedURLException
  {
    _fetch ("http://www.w3.org/Style/CSS/Test/CSS3/Selectors/current/xml/full/flat/",
            "src\\test\\resources\\testfiles\\css30\\w3c\\selectors");
  }

  private static void _fetch (final String sURL, final String sDestDir) throws MalformedURLException
  {
    final ICommonsList <String> aCSSFilenames = new CommonsArrayList<> ();
    System.out.println ("Fetching from " + sURL);
    final ICommonsList <String> aIndex = StreamHelper.readStreamLines (new URLResource (sURL + "index.html"),
                                                                       StandardCharsets.UTF_8);
    {
      // Remove doctype
      aIndex.remove (0);

      // Fix HTML to be XML
      for (int i = 0; i < aIndex.size (); ++i)
      {
        final String sLine = aIndex.get (i);
        if (sLine.contains ("<link"))
          aIndex.set (i, sLine + "</link>");
      }
    }
    final IMicroDocument aDoc = MicroReader.readMicroXML (StringHelper.getImploded ('\n', aIndex));
    MicroVisitor.visit (aDoc, new DefaultHierarchyVisitorCallback <IMicroNode> ()
    {
      @Override
      public EHierarchyVisitorReturn onItemBeforeChildren (final IMicroNode aItem)
      {
        if (aItem.isElement ())
        {
          final IMicroElement e = (IMicroElement) aItem;
          if (e.getTagName ().equals ("a"))
          {
            final String sHref = e.getAttributeValue ("href");
            if (sHref.endsWith (".xml"))
              aCSSFilenames.add (StringHelper.replaceAll (sHref, ".xml", ".css"));
          }
        }
        return EHierarchyVisitorReturn.CONTINUE;
      }
    });

    System.out.println ("Fetching a total of " + aCSSFilenames.size () + " files");
    int i = 0;
    for (final String sCSSFilename : aCSSFilenames)
    {
      System.out.println ("  " + (++i) + ".: " + sCSSFilename);
      final String sContent = StreamHelper.getAllBytesAsString (new URLResource (sURL + sCSSFilename),
                                                                StandardCharsets.UTF_8);
      SimpleFileIO.writeFile (new File (sDestDir, sCSSFilename), sContent, StandardCharsets.UTF_8);
    }
  }
}
