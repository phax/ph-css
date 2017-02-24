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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillNotClose;
import javax.annotation.concurrent.NotThreadSafe;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.base64.Base64;
import com.helger.commons.charset.CharsetManager;
import com.helger.commons.collection.ArrayHelper;
import com.helger.commons.equals.EqualsHelper;
import com.helger.commons.hashcode.HashCodeGenerator;
import com.helger.commons.mime.CMimeType;
import com.helger.commons.mime.IMimeType;
import com.helger.commons.mime.MimeType;
import com.helger.commons.mime.MimeTypeHelper;
import com.helger.commons.string.ToStringGenerator;

/**
 * This class represents a single CSS data URL (RFC 2397).<br>
 * Note: manual serialization is required, because {@link Charset} is not
 * Serializable.
 *
 * @author Philip Helger
 */
@NotThreadSafe
public class CSSDataURL implements Serializable
{
  private IMimeType m_aMimeType;
  private boolean m_bBase64Encoded;
  private byte [] m_aContent;
  private Charset m_aCharset;
  private String m_sContent;

  /**
   * Determine the charset from the passed MIME type. If no charset was found,
   * return the default charset.
   *
   * @param aMimeType
   *        The MIME type to investigate.
   * @return Never <code>null</code>.
   */
  @Nonnull
  public static Charset getCharsetFromMimeTypeOrDefault (@Nullable final IMimeType aMimeType)
  {
    final Charset ret = MimeTypeHelper.getCharsetFromMimeType (aMimeType);
    return ret != null ? ret : CSSDataURLHelper.DEFAULT_CHARSET;
  }

  /**
   * Default constructor. Default MIME type, no Base64 encoding and no content.
   */
  public CSSDataURL ()
  {
    this (CSSDataURLHelper.DEFAULT_MIME_TYPE.getClone (), false, new byte [0], CSSDataURLHelper.DEFAULT_CHARSET, "");
  }

  /**
   * Constructor
   *
   * @param aMimeType
   *        The MIME type to be used. If it contains a charset, this charset
   *        will be used otherwise the default charset will be used.
   * @param bBase64Encoded
   *        <code>true</code> if the content of this data should be Base64
   *        encoded. It is recommended to set this to <code>true</code> if you
   *        have binary data like images.
   * @param aContent
   *        The content of the data URL as a byte array. May not be
   *        <code>null</code>.
   */
  public CSSDataURL (@Nonnull final IMimeType aMimeType, final boolean bBase64Encoded, @Nonnull final byte [] aContent)
  {
    this (aMimeType, bBase64Encoded, aContent, getCharsetFromMimeTypeOrDefault (aMimeType), null);
  }

  /**
   * Full constructor
   *
   * @param aMimeType
   *        The MIME type to be used. May not be <code>null</code>. If you don't
   *        know provide the default MIME type from
   *        {@link CSSDataURLHelper#DEFAULT_MIME_TYPE}.
   * @param bBase64Encoded
   *        <code>true</code> if the data URL String representation should be
   *        Base64 encoded, <code>false</code> if not. It is recommended to set
   *        this to <code>true</code> if you have binary data like images.
   * @param aContent
   *        The content of the data URL as a byte array. May not be
   *        <code>null</code> but may be empty. This content may not be Base64
   *        encoded!
   * @param aCharset
   *        The charset to be used to encode the String. May not be
   *        <code>null</code>. The default is
   *        {@link CSSDataURLHelper#DEFAULT_CHARSET}.
   * @param sContent
   *        The String representation of the content. It must match the byte
   *        array in the specified charset. If this parameter is
   *        <code>null</code> than the String content representation is lazily
   *        created in {@link #getContentAsString()}.
   */
  public CSSDataURL (@Nonnull final IMimeType aMimeType,
                     final boolean bBase64Encoded,
                     @Nonnull final byte [] aContent,
                     @Nonnull final Charset aCharset,
                     @Nullable final String sContent)
  {
    ValueEnforcer.notNull (aMimeType, "MimeType");
    ValueEnforcer.notNull (aContent, "Content");
    ValueEnforcer.notNull (aCharset, "Charset");

    // Check if a charset is contained in the MIME type and if it matches the
    // provided charset
    final Charset aMimeTypeCharset = MimeTypeHelper.getCharsetFromMimeType (aMimeType);
    if (aMimeTypeCharset == null)
    {
      // No charset found in MIME type
      if (!aCharset.equals (CSSDataURLHelper.DEFAULT_CHARSET))
      {
        // append charset only if it is not the default charset
        m_aMimeType = ((MimeType) aMimeType.getClone ()).addParameter (CMimeType.PARAMETER_NAME_CHARSET,
                                                                       aCharset.name ());
      }
      else
      {
        // Default charset provided
        m_aMimeType = aMimeType;
      }
    }
    else
    {
      // MIME type has a charset - check if it matches the passed one
      if (!aMimeTypeCharset.equals (aCharset))
        throw new IllegalArgumentException ("The provided charset '" +
                                            aCharset.name () +
                                            "' differs from the charset in the MIME type: '" +
                                            aMimeTypeCharset.name () +
                                            "'");
      m_aMimeType = aMimeType;
    }
    m_bBase64Encoded = bBase64Encoded;
    m_aContent = ArrayHelper.getCopy (aContent);
    m_aCharset = aCharset;
    m_sContent = sContent;
  }

  private void writeObject (@Nonnull final ObjectOutputStream out) throws IOException
  {
    out.writeObject (m_aMimeType);
    out.writeBoolean (m_bBase64Encoded);
    out.writeObject (m_aContent);
    out.writeUTF (m_aCharset.name ());
    out.writeObject (m_sContent);
  }

  private void readObject (@Nonnull final ObjectInputStream in) throws IOException, ClassNotFoundException
  {
    m_aMimeType = (IMimeType) in.readObject ();
    m_bBase64Encoded = in.readBoolean ();
    m_aContent = (byte []) in.readObject ();
    final String sCharsetName = in.readUTF ();
    m_aCharset = CharsetManager.getCharsetFromName (sCharsetName);
    m_sContent = (String) in.readObject ();
  }

  /**
   * @return The MIME type of the data URL. If none was specified, than the
   *         default MIME Type {@link CSSDataURLHelper#DEFAULT_MIME_TYPE} must
   *         be used.
   */
  @Nonnull
  public IMimeType getMimeType ()
  {
    return m_aMimeType;
  }

  /**
   * @return <code>true</code> if the parsed data URL was Base64 encoded or if
   *         this data URL should be Base64 encoded.
   */
  public boolean isBase64Encoded ()
  {
    return m_bBase64Encoded;
  }

  /**
   * @return The length of the content in bytes. Always &ge; 0.
   */
  @Nonnegative
  public int getContentLength ()
  {
    return m_aContent.length;
  }

  /**
   * Get a copy of all content bytes. No Base64 encoding is performed in this
   * method.
   *
   * @return A copy of the binary data of the data URL. Neither
   *         <code>null</code> but maybe empty.
   */
  @Nonnull
  @ReturnsMutableCopy
  public byte [] getContentBytes ()
  {
    return ArrayHelper.getCopy (m_aContent);
  }

  /**
   * Write all the binary content to the passed output stream. No Base64
   * encoding is performed in this method.
   *
   * @param aOS
   *        The output stream to write to. May not be <code>null</code>.
   * @throws IOException
   *         from OutputStream
   */
  public void writeContentBytes (@Nonnull @WillNotClose final OutputStream aOS) throws IOException
  {
    aOS.write (m_aContent, 0, m_aContent.length);
  }

  /**
   * @return The charset to be used for String encoding. May not be
   *         <code>null</code>. The default is
   *         {@link CSSDataURLHelper#DEFAULT_CHARSET}.
   */
  @Nonnull
  public Charset getCharset ()
  {
    return m_aCharset;
  }

  /**
   * Get the data content of this Data URL as String. If no String
   * representation was provided in the constructor, than it is lazily created
   * inside this method in which case instances of this class are not
   * thread-safe. If a non-<code>null</code> String was provided in the
   * constructor, this object is immutable. No Base64 encoding is performed in
   * this method.
   *
   * @return The content in a String representation using the charset of this
   *         object. Never <code>null</code>.
   */
  @Nonnull
  public String getContentAsString ()
  {
    if (m_sContent == null)
      m_sContent = CharsetManager.getAsString (m_aContent, m_aCharset);
    return m_sContent;
  }

  /**
   * Get the content as a Base64 encoded {@link String} in the {@link Charset}
   * specified by {@link #getCharset()}. The encoding is applied independent of
   * the {@link #isBase64Encoded()} state.
   *
   * @return Never <code>null</code>.
   */
  @Nonnull
  public String getContentAsBase64EncodedString ()
  {
    // Add Base64 encoded String
    final byte [] aEncoded = Base64.encodeBytesToBytes (m_aContent);
    // Print the string in the specified charset
    return CharsetManager.getAsString (aEncoded, m_aCharset);
  }

  /**
   * Get the data content of this Data URL as String in the specified charset.
   * No Base64 encoding is performed in this method.
   *
   * @param aCharset
   *        The charset to be used. May not be <code>null</code>.
   * @return The content in a String representation using the provided charset.
   *         Never <code>null</code>.
   */
  @Nonnull
  public String getContentAsString (@Nonnull final Charset aCharset)
  {
    return CharsetManager.getAsString (m_aContent, aCharset);
  }

  /**
   * @return The complete representation of the data URL, starting with "data:".
   *         All data is emitted, even if it is the default value. Base64
   *         encoding is performed in this method.
   */
  @Nonnull
  public String getAsString ()
  {
    // Return the non-optimized version
    return getAsString (false);
  }

  /**
   * @param bOptimizedVersion
   *        <code>true</code> to create optimized version
   * @return The complete representation of the data URL, starting with "data:".
   *         All data is emitted, even if it is the default value.
   */
  @Nonnull
  public String getAsString (final boolean bOptimizedVersion)
  {
    final StringBuilder aSB = new StringBuilder (CSSDataURLHelper.PREFIX_DATA_URL);

    if (bOptimizedVersion)
    {
      // Do not emit the default, if it is the optimized version
      if (!m_aMimeType.equals (CSSDataURLHelper.DEFAULT_MIME_TYPE))
        if (m_aMimeType.getAsStringWithoutParameters ()
                       .equals (CSSDataURLHelper.DEFAULT_MIME_TYPE.getAsStringWithoutParameters ()))
        {
          // Emit only the parameters
          aSB.append (m_aMimeType.getParametersAsString (CSSDataURLHelper.MIME_QUOTING));
        }
        else
        {
          // Non-default MIME type
          aSB.append (m_aMimeType.getAsString (CSSDataURLHelper.MIME_QUOTING));
        }
    }
    else
    {
      // Use URL escaping to quote MIME type parameter values!
      aSB.append (m_aMimeType.getAsString (CSSDataURLHelper.MIME_QUOTING));
    }

    // Base64 marker
    if (m_bBase64Encoded)
    {
      // Avoid the ";base64" if the content is empty
      if (m_aContent.length > 0 || !bOptimizedVersion)
        aSB.append (CSSDataURLHelper.BASE64_MARKER);
    }

    // Start content
    aSB.append (CSSDataURLHelper.SEPARATOR_CONTENT);
    if (m_aContent.length > 0)
    {
      if (m_bBase64Encoded)
      {
        // Add Base64 encoded String
        aSB.append (getContentAsBase64EncodedString ());
      }
      else
      {
        // Append String as is
        aSB.append (getContentAsString ());
      }
    }
    return aSB.toString ();
  }

  @Override
  public boolean equals (final Object o)
  {
    if (o == this)
      return true;
    if (o == null || !getClass ().equals (o.getClass ()))
      return false;
    final CSSDataURL rhs = (CSSDataURL) o;
    return m_aMimeType.equals (rhs.m_aMimeType) &&
           m_bBase64Encoded == rhs.m_bBase64Encoded &&
           EqualsHelper.equals (m_aContent, rhs.m_aContent) &&
           m_aCharset.equals (rhs.m_aCharset);
  }

  @Override
  public int hashCode ()
  {
    return new HashCodeGenerator (this).append (m_aMimeType)
                                       .append (m_bBase64Encoded)
                                       .append (m_aContent)
                                       .append (m_aCharset)
                                       .getHashCode ();
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("mimeType", m_aMimeType)
                                       .append ("base64Encoded", m_bBase64Encoded)
                                       .append ("content.length", m_aContent.length)
                                       .append ("charset", m_aCharset)
                                       .append ("hasStringContent", m_sContent != null)
                                       .getToString ();
  }
}
