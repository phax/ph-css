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
package com.helger.css.utils;

import java.nio.charset.Charset;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.annotation.PresentForCodeCoverage;
import com.helger.commons.base64.Base64;
import com.helger.commons.charset.CCharset;
import com.helger.commons.charset.CharsetManager;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.mime.EMimeQuoting;
import com.helger.commons.mime.IMimeType;
import com.helger.commons.mime.MimeType;
import com.helger.commons.mime.MimeTypeHelper;
import com.helger.commons.mime.MimeTypeParser;
import com.helger.commons.string.StringHelper;

/**
 * Provides data URL handling sanity methods (RFC 2397).
 *
 * @author Philip Helger
 */
@Immutable
public final class CSSDataURLHelper
{
  /** The default charset to be used for Data URLs: US-ASCII */
  public static final Charset DEFAULT_CHARSET = CCharset.CHARSET_US_ASCII_OBJ;

  /** The default MIME type for Data URLs: text/plain;charset=US-ASCII */
  public static final IMimeType DEFAULT_MIME_TYPE = new MimeType (CMimeType.TEXT_PLAIN).addParameter (CMimeType.PARAMETER_NAME_CHARSET,
                                                                                                      DEFAULT_CHARSET.name ());

  /** The default prefix for data URLs */
  public static final String PREFIX_DATA_URL = "data:";

  /** The marker for Base64 encoding */
  public static final String BASE64_MARKER = ";base64";

  /** The separator that starts the content */
  public static final char SEPARATOR_CONTENT = ',';

  /** Data URLs should use the URL code to quote values! */
  public static final EMimeQuoting MIME_QUOTING = EMimeQuoting.URL_ESCAPE;

  private static final Logger s_aLogger = LoggerFactory.getLogger (CSSDataURLHelper.class);

  @PresentForCodeCoverage
  private static final CSSDataURLHelper s_aInstance = new CSSDataURLHelper ();

  private CSSDataURLHelper ()
  {}

  /**
   * Check if the passed URL is a data URL. It is checked, whether the passed
   * URL starts with {@value #PREFIX_DATA_URL} (after trimming).
   *
   * @param sURL
   *        The URL to check. May be <code>null</code>.
   * @return <code>true</code> if the passed URL is a data URL,
   *         <code>false</code> if not.
   */
  public static boolean isDataURL (@Nullable final String sURL)
  {
    if (sURL == null)
      return false;
    final String sRealURL = sURL.trim ();
    return sRealURL.startsWith (PREFIX_DATA_URL);
  }

  /**
   * Parse a data URL into this type.
   *
   * <pre>
   * Syntax
   *   dataurl    := "data:" [ mediatype ] [ ";base64" ] "," data
   *   mediatype  := [ type "/" subtype ] *( ";" parameter )
   *   data       := *urlchar
   *   parameter  := attribute "=" value
   * </pre>
   *
   * @param sDataURL
   *        The data URL to be parsed. May be <code>null</code>.
   * @return <code>null</code> if parsing failed
   */
  @Nullable
  public static CSSDataURL parseDataURL (@Nullable final String sDataURL)
  {
    if (!isDataURL (sDataURL))
      return null;

    // Skip the constant prefix
    final String sRest = StringHelper.trimStart (sDataURL.trim (), PREFIX_DATA_URL);
    if (StringHelper.hasNoText (sRest))
    {
      // Plain "data:" URL - no content
      return new CSSDataURL ();
    }

    // comma is a special character and must be quoted in MIME type parameters
    final int nIndexComma = sRest.indexOf (SEPARATOR_CONTENT);
    int nIndexBase64 = sRest.indexOf (BASE64_MARKER);
    boolean bBase64EncodingUsed = false;

    int nMIMETypeEnd;
    if (nIndexBase64 >= 0)
    {
      // We have Base64
      if (nIndexBase64 < nIndexComma || nIndexComma < 0)
      {
        // Base64 before comma or no comma
        // ==> check if it is a MIME type parameter name (in
        // which case it is followed by a '=' character before the comma) or if
        // it is really the base64-encoding flag (no further '=' or '=' after
        // the comma).
        while (true)
        {
          final int nIndexEquals = sRest.indexOf (CMimeType.SEPARATOR_PARAMETER_NAME_VALUE, nIndexBase64);
          if (nIndexEquals < 0 || nIndexEquals > nIndexComma || nIndexComma < 0)
          {
            // It's a real base64 indicator
            nMIMETypeEnd = nIndexBase64;
            bBase64EncodingUsed = true;
            break;
          }

          // base64 as a MIME type parameter - check for next ;base64
          nIndexBase64 = sRest.indexOf (BASE64_MARKER, nIndexBase64 + BASE64_MARKER.length ());
          if (nIndexBase64 < 0)
          {
            // Found no base64 encoding
            nMIMETypeEnd = nIndexComma;
            break;
          }

          // Found another base64 marker -> continue
        }
      }
      else
      {
        // Base64 as part of data!
        nMIMETypeEnd = nIndexComma;
      }
    }
    else
    {
      // No Base64 found
      nMIMETypeEnd = nIndexComma;
    }

    String sMimeType = nMIMETypeEnd < 0 ? null : sRest.substring (0, nMIMETypeEnd).trim ();
    IMimeType aMimeType;
    Charset aCharset = null;
    if (StringHelper.hasNoText (sMimeType))
    {
      // If no MIME type is specified, the default is used
      aMimeType = DEFAULT_MIME_TYPE.getClone ();
      aCharset = DEFAULT_CHARSET;
    }
    else
    {
      // A MIME type is present
      if (sMimeType.charAt (0) == CMimeType.SEPARATOR_PARAMETER)
      {
        // Weird stuff from the specs: if only ";charset=utf-8" is present than
        // text/plain should be used
        sMimeType = DEFAULT_MIME_TYPE.getAsStringWithoutParameters () + sMimeType;
      }

      // try to parse it
      aMimeType = MimeTypeParser.safeParseMimeType (sMimeType, EMimeQuoting.URL_ESCAPE);
      if (aMimeType == null)
      {
        s_aLogger.warn ("Data URL contains invalid MIME type: '" + sMimeType + "'");
        return null;
      }

      // Check if a "charset" MIME type parameter is present
      final String sCharsetParam = MimeTypeHelper.getCharsetNameFromMimeType (aMimeType);
      if (sCharsetParam != null)
      {
        try
        {
          aCharset = CharsetManager.getCharsetFromName (sCharsetParam);
        }
        catch (final IllegalArgumentException ex)
        {
          // Illegal charset
        }
        if (aCharset == null)
        {
          s_aLogger.warn ("Illegal charset '" +
                          sCharsetParam +
                          "' contained. Defaulting to '" +
                          DEFAULT_CHARSET.name () +
                          "'");
        }
      }
      if (aCharset == null)
        aCharset = DEFAULT_CHARSET;
    }

    // Get the main content data
    String sContent = nIndexComma < 0 ? "" : sRest.substring (nIndexComma + 1).trim ();
    byte [] aContent = CharsetManager.getAsBytes (sContent, aCharset);

    if (bBase64EncodingUsed)
    {
      // Base64 decode the content data
      aContent = Base64.safeDecode (aContent);
      if (aContent == null)
      {
        s_aLogger.warn ("Failed to decode Base64 value: " + sContent);
        return null;
      }

      // Ignore the String content (and don't create it) because for binary
      // stuff like images, it does not make sense and it is most likely, that
      // the String content will never be used. In case it is required, the
      // String content is lazily initialized.
      sContent = null;
    }

    final CSSDataURL ret = new CSSDataURL (aMimeType, bBase64EncodingUsed, aContent, aCharset, sContent);
    return ret;
  }
}
