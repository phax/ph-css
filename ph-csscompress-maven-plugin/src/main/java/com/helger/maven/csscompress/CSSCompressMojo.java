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
package com.helger.maven.csscompress;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import com.helger.commons.charset.CharsetHelper;
import com.helger.commons.io.EAppend;
import com.helger.commons.io.file.FileHelper;
import com.helger.commons.io.file.FileOperationManager;
import com.helger.commons.io.file.FilenameHelper;
import com.helger.commons.io.resource.FileSystemResource;
import com.helger.commons.system.ENewLineMode;
import com.helger.commons.system.EOperatingSystem;
import com.helger.css.CCSS;
import com.helger.css.CSSFilenameHelper;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.handler.ICSSParseExceptionCallback;
import com.helger.css.reader.CSSReader;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @goal csscompress
 * @phase generate-resources
 * @description Compress existing CSS file using ph-css compressor.
 */
@SuppressFBWarnings (value = { "UWF_UNWRITTEN_FIELD", "NP_UNWRITTEN_FIELD" }, justification = "set via maven property")
public final class CSSCompressMojo extends AbstractMojo
{
  private static final String [] EXTENSIONS_CSS_COMPRESSED = { CCSS.FILE_EXTENSION_MIN_CSS,
                                                               "-min.css",
                                                               ".minified.css",
                                                               "-minified.css" };

  /**
   * The Maven Project.
   *
   * @parameter property="project"
   * @required
   * @readonly
   */
  private MavenProject project;

  /**
   * The directory where the source CSS files reside. It must be an existing
   * directory.
   *
   * @required
   * @parameter property="sourceDirectory"
   *            default-value="${basedir}/src/main/resources"
   */
  private File sourceDirectory;

  /**
   * The directory where the target CSS files reside. If the directory is not
   * existing, it is created. If no target directory is provided, the source
   * directory will be used.
   *
   * @parameter property="targetDirectory"
   */
  private File targetDirectory;

  /**
   * Should all directories be scanned recursively for CSS files to compress?
   *
   * @parameter property="recursive" default-value="true"
   */
  private boolean recursive = true;

  /**
   * Should unnecessary code be removed (e.g. rules without declarations)?
   *
   * @parameter property="removeUnnecessaryCode" default-value="false"
   */
  private boolean removeUnnecessaryCode = false;

  /**
   * Should URLs always be quoted? If false they are only quoted when absolutely
   * necessary.
   *
   * @parameter property="quoteURLs" default-value="false"
   */
  private boolean quoteURLs = false;

  /**
   * Should <code>@namespace</code> rules be written?
   *
   * @parameter property="writeNamespaceRules" default-value="true"
   * @since 1.0.18
   */
  private boolean writeNamespaceRules = true;

  /**
   * Should <code>@font-face</code> rules be written?
   *
   * @parameter property="writeFontFaceRules" default-value="true"
   */
  private boolean writeFontFaceRules = true;

  /**
   * Should <code>@keyframes</code> rules be written?
   *
   * @parameter property="writeKeyframesRules" default-value="true"
   */
  private boolean writeKeyframesRules = true;

  /**
   * Should <code>@media</code> rules be written?
   *
   * @parameter property="writeMediaRules" default-value="true"
   */
  private boolean writeMediaRules = true;

  /**
   * Should <code>@page</code> rules be written?
   *
   * @parameter property="writePageRules" default-value="true"
   */
  private boolean writePageRules = true;

  /**
   * Should <code>@viewport</code> rules be written?
   *
   * @parameter property="writeViewportRules" default-value="true"
   * @since 1.0.18
   */
  private boolean writeViewportRules = true;

  /**
   * Should <code>@supports</code> rules be written?
   *
   * @parameter property="writeSupportsRules" default-value="true"
   * @since 1.0.18
   */
  private boolean writeSupportsRules = true;

  /**
   * Should unknown <code>@</code> rules be written?
   *
   * @parameter property="writeUnknownRules" default-value="true"
   * @since 1.1.0
   */
  private boolean writeUnknownRules = true;

  /**
   * Should the CSS files be compressed, even if the timestamp of the compressed
   * file is newer than the timestamp of the original CSS file?
   *
   * @parameter property="forceCompress" default-value="false"
   */
  private boolean forceCompress = false;

  /**
   * If true some more output is emitted.
   *
   * @parameter property="verbose" default-value="false"
   */
  private boolean verbose = false;

  /**
   * If true the "browser compliant mode" for parsing is selected.
   *
   * @parameter property="browserCompliantMode" default-value="false"
   * @since 1.4.0
   */
  private boolean browserCompliantMode = CSSReaderSettings.DEFAULT_BROWSER_COMPLIANT_MODE;

  /**
   * If true the deprecated properties should be kept when reading, otherwise
   * they are discarded.
   *
   * @parameter property="keepDeprecatedProperties" default-value="false"
   * @since 7.0.4
   */
  private boolean keepDeprecatedProperties = CSSReaderSettings.DEFAULT_KEEP_DEPRECATED_PROPERTIES;

  /**
   * The encoding of the source CSS files to be used for reading the CSS file in
   * case neither a @charset rule nor a BOM is present.
   *
   * @parameter property="sourceEncoding" default-value="ISO-8859-1"
   */
  private String sourceEncoding = CSSReaderSettings.DEFAULT_CHARSET.name ();

  /**
   * The filename extension that should be used for the minified/compressed CSS
   * file.
   *
   * @parameter property="targetFileExtension" default-value=".min.css"
   */
  private String targetFileExtension = CCSS.FILE_EXTENSION_MIN_CSS;

  /**
   * The encoding of the target CSS files to be used for writing the CSS file.
   *
   * @parameter property="targetEncoding" default-value="UTF-8"
   * @since 1.4.0
   */
  private String targetEncoding = StandardCharsets.UTF_8.name ();

  /**
   * The new line mode to be used for writing the files. Valid values are
   * <code>win</code> to use "\r\n", <code>unix</code> to use "\n",
   * <code>mac</code> to use "\r" or <code>system</code> to use the system
   * default line ending. By default the Unix new line mode is used for
   * backwards compatibility.
   *
   * @parameter property="newLineMode"
   * @since 1.5.1
   */
  private ENewLineMode newLineMode = CSSWriterSettings.DEFAULT_NEW_LINE_MODE;

  @SuppressFBWarnings ({ "NP_UNWRITTEN_FIELD", "UWF_UNWRITTEN_FIELD" })
  public void setSourceDirectory (final File aDir) throws IOException
  {
    sourceDirectory = aDir;
    if (!sourceDirectory.isAbsolute ())
      sourceDirectory = new File (project.getBasedir (), aDir.getPath ()).getCanonicalFile ();
    if (!sourceDirectory.exists ())
      getLog ().error ("CSS source directory '" + sourceDirectory + "' does not exist!");
  }

  public void setTargetDirectory (final File aDir) throws IOException
  {
    targetDirectory = aDir;
    if (!targetDirectory.isAbsolute ())
      targetDirectory = new File (project.getBasedir (), aDir.getPath ()).getCanonicalFile ();
    // The creation happens below, if the prerequisites are fulfilled
  }

  public void setRecursive (final boolean bRecursive)
  {
    recursive = bRecursive;
  }

  public void setRemoveUnnecessaryCode (final boolean bRemoveUnnecessaryCode)
  {
    removeUnnecessaryCode = bRemoveUnnecessaryCode;
  }

  public void setQuoteURLs (final boolean bQuoteURLs)
  {
    quoteURLs = bQuoteURLs;
  }

  public void setWriteNamespaceRules (final boolean bWriteNamespaceRules)
  {
    writeNamespaceRules = bWriteNamespaceRules;
  }

  public void setWriteFontFaceRules (final boolean bWriteFontFaceRules)
  {
    writeFontFaceRules = bWriteFontFaceRules;
  }

  public void setWriteKeyframesRules (final boolean bWriteKeyframesRules)
  {
    writeKeyframesRules = bWriteKeyframesRules;
  }

  public void setWriteMediaRules (final boolean bWriteMediaRules)
  {
    writeMediaRules = bWriteMediaRules;
  }

  public void setWritePageRules (final boolean bWritePageRules)
  {
    writePageRules = bWritePageRules;
  }

  public void setWriteViewportRules (final boolean bWriteViewportRules)
  {
    writeViewportRules = bWriteViewportRules;
  }

  public void setWriteSupportsRules (final boolean bWriteSupportsRules)
  {
    writeSupportsRules = bWriteSupportsRules;
  }

  public void setWriteUnknownRules (final boolean bWriteUnknownRules)
  {
    writeUnknownRules = bWriteUnknownRules;
  }

  public void setForceCompress (final boolean bForceCompress)
  {
    forceCompress = bForceCompress;
  }

  public void setVerbose (final boolean bVerbose)
  {
    verbose = bVerbose;
  }

  public void setBrowserCompliantMode (final boolean bBrowserCompliantMode)
  {
    browserCompliantMode = bBrowserCompliantMode;
  }

  public void setKeepDeprecatedProperties (final boolean bKeepDeprecatedProperties)
  {
    keepDeprecatedProperties = bKeepDeprecatedProperties;
  }

  public void setSourceEncoding (final String sSourceEncoding)
  {
    // Throws an exception on an illegal charset
    CharsetHelper.getCharsetFromName (sSourceEncoding);
    sourceEncoding = sSourceEncoding;
  }

  public void setTargetFileExtension (final String sTargetFileExtension)
  {
    targetFileExtension = sTargetFileExtension;
  }

  public void setTargetEncoding (final String sTargetEncoding)
  {
    // Throws an exception on an illegal charset
    CharsetHelper.getCharsetFromName (sTargetEncoding);
    targetEncoding = sTargetEncoding;
  }

  public void setNewLineMode (final String sNewLineMode)
  {
    if ("win".equalsIgnoreCase (sNewLineMode) || "windows".equalsIgnoreCase (sNewLineMode))
      newLineMode = ENewLineMode.WINDOWS;
    else
      if ("linux".equalsIgnoreCase (sNewLineMode) || "unix".equalsIgnoreCase (sNewLineMode))
        newLineMode = ENewLineMode.UNIX;
      else
        if ("mac".equalsIgnoreCase (sNewLineMode) || "apple".equalsIgnoreCase (sNewLineMode))
          newLineMode = ENewLineMode.MAC;
        else
          if ("system".equalsIgnoreCase (sNewLineMode))
            newLineMode = ENewLineMode.DEFAULT;
  }

  /**
   * Check if the passed file is already compressed. The check is only done
   * using the file extension of the file name.
   *
   * @param sFilename
   *        The filename to be checked.
   * @return <code>true</code> if the file is already compressed.
   */
  private static boolean _isAlreadyCompressed (final String sFilename)
  {
    for (final String sExt : EXTENSIONS_CSS_COMPRESSED)
      if (sFilename.endsWith (sExt))
        return true;
    return false;
  }

  @Nonnull
  private String _getSourceRelativePath (@Nonnull final File aFile)
  {
    return aFile.getAbsolutePath ().substring (sourceDirectory.getAbsolutePath ().length () + 1);
  }

  private void _compressCSSFile (@Nonnull final File aSourceFile)
  {
    final String sSourceRelativePath = _getSourceRelativePath (aSourceFile);

    // Compress the file only if the compressed file is older than the original
    // file. Note: lastModified on a non-existing file returns 0L
    final boolean bTargetDirEqualsSourceDir = targetDirectory == null ||
                                              targetDirectory.getAbsolutePath ()
                                                             .equals (sourceDirectory.getAbsolutePath ());
    final File aCompressedFile;
    {
      if (bTargetDirEqualsSourceDir)
      {
        // Use the custom extension
        aCompressedFile = new File (FilenameHelper.getWithoutExtension (aSourceFile.getAbsolutePath ()) +
                                    targetFileExtension);
      }
      else
      {
        // Source and target are different
        aCompressedFile = new File (targetDirectory,
                                    FilenameHelper.getWithoutExtension (sSourceRelativePath) + targetFileExtension);
      }
    }

    if (aCompressedFile.lastModified () < aSourceFile.lastModified () || forceCompress)
    {
      if (verbose)
        getLog ().info ("Start compressing CSS file " + sSourceRelativePath);
      else
        getLog ().debug ("Start compressing CSS file " + sSourceRelativePath);

      final ICSSParseExceptionCallback aExHdl = ex -> getLog ().error ("Failed to parse CSS file " +
                                                                       sSourceRelativePath,
                                                                       ex);
      final Charset aFallbackCharset = CharsetHelper.getCharsetFromName (sourceEncoding);
      final CSSReaderSettings aSettings = new CSSReaderSettings ().setCSSVersion (ECSSVersion.CSS30)
                                                                  .setFallbackCharset (aFallbackCharset)
                                                                  .setCustomExceptionHandler (aExHdl)
                                                                  .setBrowserCompliantMode (browserCompliantMode)
                                                                  .setKeepDeprecatedProperties (keepDeprecatedProperties);
      final CascadingStyleSheet aCSS = CSSReader.readFromFile (aSourceFile, aSettings);
      if (aCSS != null)
      {
        // We read it!
        final FileSystemResource aDestFile = new FileSystemResource (aCompressedFile);
        try
        {
          final CSSWriterSettings aWriterSettings = new CSSWriterSettings (ECSSVersion.CSS30);
          aWriterSettings.setOptimizedOutput (true);
          aWriterSettings.setRemoveUnnecessaryCode (removeUnnecessaryCode);
          aWriterSettings.setNewLineMode (newLineMode);
          aWriterSettings.setQuoteURLs (quoteURLs);
          aWriterSettings.setWriteNamespaceRules (writeNamespaceRules);
          aWriterSettings.setWriteFontFaceRules (writeFontFaceRules);
          aWriterSettings.setWriteKeyframesRules (writeKeyframesRules);
          aWriterSettings.setWriteMediaRules (writeMediaRules);
          aWriterSettings.setWritePageRules (writePageRules);
          aWriterSettings.setWriteViewportRules (writeViewportRules);
          aWriterSettings.setWriteSupportsRules (writeSupportsRules);
          aWriterSettings.setWriteUnknownRules (writeUnknownRules);

          final Charset aTargetCharset = CharsetHelper.getCharsetFromName (targetEncoding);
          new CSSWriter (aWriterSettings).writeCSS (aCSS, aDestFile.getWriter (aTargetCharset, EAppend.TRUNCATE));
        }
        catch (final IOException ex)
        {
          getLog ().error ("Failed to write compressed CSS file '" + aCompressedFile.toString () + "' to disk", ex);
        }
      }
    }
    else
    {
      if (verbose)
        getLog ().info ("Ignoring already compressed CSS file " + sSourceRelativePath);
      else
        getLog ().debug ("Ignoring already compressed CSS file " + sSourceRelativePath);
    }
  }

  private void _scanDirectory (@Nonnull final File aDir)
  {
    for (final File aChild : FileHelper.getDirectoryContent (aDir))
    {
      if (aChild.isDirectory ())
      {
        // Shall we recurse into sub-directories?
        if (recursive)
          _scanDirectory (aChild);
      }
      else
        if (aChild.isFile () &&
            CSSFilenameHelper.isCSSFilename (aChild.getName ()) &&
            !_isAlreadyCompressed (aChild.getName ()))
        {
          // We're ready to rumble!
          _compressCSSFile (aChild);
        }
    }
  }

  public void execute () throws MojoExecutionException
  {
    if (verbose)
      getLog ().info ("Start compressing CSS files in directory " + sourceDirectory.getPath ());

    // Consistency check
    if (targetDirectory != null)
    {
      // Case insensitive comparison on Windows
      final boolean bCaseInsensitiveOS = EOperatingSystem.getCurrentOS ().isWindowsBased ();
      final Function <File, String> fGetPath = bCaseInsensitiveOS ? f -> f.getAbsolutePath ().toLowerCase (Locale.ROOT)
                                                                  : File::getAbsolutePath;

      if (recursive && fGetPath.apply (targetDirectory).startsWith (fGetPath.apply (sourceDirectory)))
      {
        throw new IllegalStateException ("Target directory MUST NOT be a child of the source directory in recursive mode");
      }

      // Make sure, target directory exists
      if (!targetDirectory.exists ())
      {
        getLog ().info ("CSS target directory '" + targetDirectory + "' does not exist and will be created!");
        FileOperationManager.INSTANCE.createDirRecursive (targetDirectory);
      }
    }

    _scanDirectory (sourceDirectory);
  }
}
