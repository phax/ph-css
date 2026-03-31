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
package com.helger.css.supplementary.main;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.base.wrapper.Wrapper;
import com.helger.collection.commons.CommonsLinkedHashMap;
import com.helger.collection.commons.ICommonsOrderedMap;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.handler.ICSSParseExceptionCallback;
import com.helger.css.parser.ParseException;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.io.file.FileSystemRecursiveIterator;
import com.helger.io.file.IFileFilter;

/**
 * Crawl the disc for CSS files :)
 *
 * @author Philip Helger
 */
public final class MainReadAllCSSOnDisc
{
  private static final Logger LOGGER = LoggerFactory.getLogger (MainReadAllCSSOnDisc.class);

  public static void main (final String [] args)
  {
    int nFilesOK = 0;
    int nFilesError = 0;
    final ICommonsOrderedMap <File, ParseException> aErrors = new CommonsLinkedHashMap <> ();
    final Wrapper <File> aCurrentFile = new Wrapper <> ();
    final ICSSParseExceptionCallback aHdl = ex -> aErrors.put (aCurrentFile.get (), ex);
    for (final File aFile : new FileSystemRecursiveIterator (new File ("/")).withFilter (IFileFilter.filenameEndsWith (".css")))
    {
      if (false)
        LOGGER.info (aFile.getAbsolutePath ());
      aCurrentFile.set (aFile);
      final CascadingStyleSheet aCSS = CSSReader.readFromFile (aFile,
                                                               new CSSReaderSettings ().setCustomExceptionHandler (aHdl));
      if (aCSS == null)
      {
        nFilesError++;
        LOGGER.warn ("  " + aFile.getAbsolutePath () + " failed!");
      }
      else
        nFilesOK++;
    }

    LOGGER.info ("Done");
    for (final Map.Entry <File, ParseException> aEntry : aErrors.entrySet ())
      LOGGER.info ("  " + aEntry.getKey ().getAbsolutePath () + ":\n" + aEntry.getValue ().getMessage () + "\n");
    LOGGER.info ("OK: " + nFilesOK + "; Error: " + nFilesError);
  }
}
