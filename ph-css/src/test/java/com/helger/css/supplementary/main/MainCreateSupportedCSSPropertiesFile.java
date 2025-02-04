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
package com.helger.css.supplementary.main;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.commons.collection.CollectionHelper;
import com.helger.commons.compare.IComparator;
import com.helger.commons.io.file.SimpleFileIO;
import com.helger.commons.lang.ClassHelper;
import com.helger.commons.name.IHasName;
import com.helger.commons.string.StringHelper;
import com.helger.commons.version.Version;
import com.helger.css.ECSSSpecification;
import com.helger.css.ECSSVendorPrefix;
import com.helger.css.ECSSVersion;
import com.helger.css.property.ECSSProperty;
import com.helger.xml.microdom.IMicroElement;
import com.helger.xml.microdom.MicroElement;
import com.helger.xml.microdom.serialize.MicroWriter;
import com.helger.xml.serialize.write.EXMLSerializeIndent;
import com.helger.xml.serialize.write.EXMLSerializeVersion;
import com.helger.xml.serialize.write.XMLWriterSettings;

/**
 * Create the src/main/resources/supported-css-properties.html file with all
 * supported properties.
 *
 * @author Philip Helger
 */
public class MainCreateSupportedCSSPropertiesFile
{
  private static void _boolean (@Nonnull final IMicroElement td, final boolean bSet, @Nullable final String sTitle)
  {
    if (bSet)
    {
      td.setAttribute ("class", "center").appendText ("X");
      if (StringHelper.hasText (sTitle))
        td.setAttribute ("title", sTitle);
    }
    else
      td.appendText ("");
  }

  public static void main (final String [] args)
  {
    final Locale aLocale = Locale.US;

    final IMicroElement html = new MicroElement ("html");
    final IMicroElement head = html.appendElement ("head");
    head.appendElement ("title").appendText ("Supported CSS properties in ph-css");
    head.appendElement ("style")
        .appendText ("* {font-family:Arial,Helvetica;}" +
                     " table{border-collapse:collapse;}" +
                     " td,th {border:solid 1px black;padding:3px;vertical-align:top; }" +
                     " .odd{background-color:#ddd;}" +
                     " .center{text-align:center;}" +
                     " .nowrap{white-space:nowrap;}" +
                     " a, a:link, a:visited, a:hover, a:active{color:blue;}");

    final IMicroElement body = html.appendElement ("body");

    body.appendElement ("div")
        .appendText ("Automatically generated by " +
                     ClassHelper.getClassLocalName (MainCreateSupportedCSSPropertiesFile.class) +
                     " on " +
                     new Date ().toString ());

    body.appendElement ("div").appendElement ("a").setAttribute ("href", "#generic").appendText ("Generic properties");
    body.appendElement ("div").appendElement ("a").setAttribute ("href", "#vendor").appendText ("Vendor specific properties");

    body.appendElement ("a").setAttribute ("name", "generic").appendText ("");
    body.appendElement ("h1").appendText ("Generic properties");
    IMicroElement table = body.appendElement ("table");
    IMicroElement thead = table.appendElement ("thead");
    IMicroElement tr = thead.appendElement ("tr");
    tr.appendElement ("th").appendText ("Name");
    tr.appendElement ("th").appendText ("CSS 1.0");
    tr.appendElement ("th").appendText ("CSS 2.1");
    tr.appendElement ("th").appendText ("CSS 3.0");
    tr.appendElement ("th").appendText ("Links");

    IMicroElement tbody = table.appendElement ("tbody");
    int nIndex = 0;
    for (final ECSSProperty eProperty : CollectionHelper.getSorted (ECSSProperty.values (), IHasName.getComparatorName ()))
      if (!eProperty.isVendorSpecific ())
      {
        final Version eMinVersion = eProperty.getMinimumCSSVersion ().getVersion ();
        final boolean bCSS10 = eMinVersion.isLE (ECSSVersion.CSS10.getVersion ());
        final boolean bCSS21 = eMinVersion.isLE (ECSSVersion.CSS21.getVersion ());
        final boolean bCSS30 = eMinVersion.isLE (ECSSVersion.CSS30.getVersion ());

        tr = tbody.appendElement ("tr");
        if ((nIndex & 1) == 1)
          tr.setAttribute ("class", "odd");
        tr.appendElement ("td").setAttribute ("class", "nowrap").appendText (eProperty.getName ());
        _boolean (tr.appendElement ("td"), bCSS10, null);
        _boolean (tr.appendElement ("td"), bCSS21, null);
        _boolean (tr.appendElement ("td"), bCSS30, null);

        final IMicroElement td = tr.appendElement ("td");
        for (final ECSSSpecification eSpecs : eProperty.getAllSpecifications ())
          if (eSpecs.hasHandledURL ())
            td.appendElement ("div")
              .appendElement ("a")
              .setAttribute ("href", eSpecs.getHandledURL ())
              .setAttribute ("target", "_blank")
              .appendText (eSpecs.getID ());
          else
            td.appendElement ("div").appendText (eSpecs.getID ());

        ++nIndex;
      }

    // Determine all used vendor prefixes
    final EnumSet <ECSSVendorPrefix> aRequiredPrefixes = EnumSet.noneOf (ECSSVendorPrefix.class);
    for (final ECSSVendorPrefix eVendorPrefix : ECSSVendorPrefix.values ())
    {
      for (final ECSSProperty eProperty : ECSSProperty.values ())
        if (eProperty.isVendorSpecific (eVendorPrefix))
        {
          aRequiredPrefixes.add (eVendorPrefix);
          break;
        }
    }

    body.appendElement ("a").setAttribute ("name", "vendor").appendText ("");
    body.appendElement ("h1").appendText ("Vendor specific properties");
    table = body.appendElement ("table");
    thead = table.appendElement ("thead");
    tr = thead.appendElement ("tr");
    tr.appendElement ("th").appendText ("Name");
    for (final ECSSVendorPrefix e : aRequiredPrefixes)
    {
      final IMicroElement th = tr.appendElement ("th");
      th.appendText (e.getDisplayName ());
      th.appendElement ("span").setAttribute ("class", "nowrap").appendText (" (" + e.getPrefix () + ")");
    }

    tbody = table.appendElement ("tbody");
    nIndex = 0;

    for (final ECSSProperty eProperty : CollectionHelper.getSorted (ECSSProperty.values (),
                                                                    IComparator.getComparatorCollating (ECSSProperty::getVendorIndependentName,
                                                                                                        aLocale)))
      if (eProperty.isVendorSpecific ())
      {
        tr = tbody.appendElement ("tr");
        if ((nIndex & 1) == 1)
          tr.setAttribute ("class", "odd");
        tr.appendElement ("td").setAttribute ("class", "nowrap").appendText (eProperty.getVendorIndependentName ());

        for (final ECSSVendorPrefix e : aRequiredPrefixes)
          _boolean (tr.appendElement ("td"), eProperty.isVendorSpecific (e), eProperty.getName ());

        ++nIndex;
      }

    body.appendElement ("div").setAttribute ("style", "margin:2em 0").appendText ("That's it.");

    String sHTML = "<!--\r\n" +
                   "\r\n" +
                   "    Copyright (C) 2014 Philip Helger (www.helger.com)\r\n" +
                   "    philip[at]helger[dot]com\r\n" +
                   "\r\n" +
                   "    Licensed under the Apache License, Version 2.0 (the \"License\");\r\n" +
                   "    you may not use this file except in compliance with the License.\r\n" +
                   "    You may obtain a copy of the License at\r\n" +
                   "\r\n" +
                   "            http://www.apache.org/licenses/LICENSE-2.0\r\n" +
                   "\r\n" +
                   "    Unless required by applicable law or agreed to in writing, software\r\n" +
                   "    distributed under the License is distributed on an \"AS IS\" BASIS,\r\n" +
                   "    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\r\n" +
                   "    See the License for the specific language governing permissions and\r\n" +
                   "    limitations under the License.\r\n" +
                   "\r\n" +
                   "-->\r\n";
    sHTML += MicroWriter.getNodeAsString (html,
                                          new XMLWriterSettings ().setIndent (EXMLSerializeIndent.ALIGN_ONLY)
                                                                  .setSerializeVersion (EXMLSerializeVersion.HTML));

    SimpleFileIO.writeFile (new File ("src/main/resources/supported-css-properties.html"), sHTML, StandardCharsets.UTF_8);
    System.out.println ("Done");
  }
}
