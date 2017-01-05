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
package com.helger.css.supplementary.parser;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.collection.impl.RingBufferLifo;
import com.helger.commons.io.stream.NonBlockingPushbackReader;
import com.helger.commons.string.ToStringGenerator;

/**
 * A special CSS Codepoint reader that converts chars to Codepoints and keeps
 * track of the line and column number.
 *
 * @author Philip Helger
 */
public class CSSCodepointReader implements Closeable
{
  private static final class Pos
  {
    private int m_nLine;
    private int m_nCol;

    public Pos ()
    {
      this (1, 1);
    }

    public Pos (@Nonnegative final int nLine, @Nonnegative final int nCol)
    {
      m_nLine = nLine;
      m_nCol = nCol;
    }

    // Count line/column - \r and \f is handled in the input stream
    public void incPos (final int nCP)
    {
      if (nCP == '\n')
      {
        ++m_nLine;
        m_nCol = 1;
      }
      else
        ++m_nCol;
    }

    @Nonnull
    public Pos getClone ()
    {
      return new Pos (m_nLine, m_nCol);
    }

    @Override
    public String toString ()
    {
      return new ToStringGenerator (null).append ("Line", m_nLine).append ("Column", m_nCol).toString ();
    }
  }

  private static final int PUSHBACK_COUNT = 10;

  private final NonBlockingPushbackReader m_aReader;
  // Status vars
  private Pos m_aPos = new Pos ();
  private final RingBufferLifo <Pos> m_aPosRB = new RingBufferLifo <> (PUSHBACK_COUNT, true);
  private Pos m_aTokenStartPos;
  private final StringBuilder m_aTokenImage = new StringBuilder (1024);

  public CSSCodepointReader (@Nonnull final CSSInputStream aCSSIS, @Nonnull final Charset aCharset)
  {
    // Pushback maybe 2 chars each
    m_aReader = new NonBlockingPushbackReader (new InputStreamReader (aCSSIS, aCharset), PUSHBACK_COUNT * 2);
  }

  @Nonnull
  public CSSCodepoint read () throws IOException
  {
    final int nHigh = m_aReader.read ();
    if (nHigh == -1)
    {
      // No increment in line/column number
      return CSSCodepoint.createEOF ();
    }

    CSSCodepoint ret;
    if (Character.isHighSurrogate ((char) nHigh))
    {
      final char cLow = (char) m_aReader.read ();
      if (!Character.isLowSurrogate (cLow))
        throw new IOException ("Malformed Codepoint sequence - invalid low surrogate");
      ret = new CSSCodepoint ((char) nHigh, cLow);
      m_aTokenImage.append ((char) nHigh);
      m_aTokenImage.append (cLow);
    }
    else
    {
      ret = new CSSCodepoint (nHigh);
      m_aTokenImage.append ((char) nHigh);
    }

    // Create a copy before incrementing
    m_aPosRB.put (m_aPos.getClone ());
    m_aPos.incPos (nHigh);
    return ret;
  }

  public void unread (@Nonnull final CSSCodepoint aCP) throws IOException
  {
    final int nValue = aCP.getValue ();
    if (aCP.isSingleChar ())
    {
      // Quick one char version
      m_aReader.unread (nValue);
      m_aTokenImage.setLength (m_aTokenImage.length () - 1);
    }
    else
    {
      // Two char version (reverse order because of undo!)
      m_aReader.unread (Character.lowSurrogate (nValue));
      m_aReader.unread (Character.highSurrogate (nValue));
      m_aTokenImage.setLength (m_aTokenImage.length () - 2);
    }
    // Restore the previous position
    m_aPos = m_aPosRB.take ();
  }

  @Nonnull
  public CSSCodepoint peek () throws IOException
  {
    final int nHigh = m_aReader.read ();

    CSSCodepoint ret;
    if (Character.isHighSurrogate ((char) nHigh))
    {
      final char cLow = (char) m_aReader.read ();
      if (!Character.isLowSurrogate (cLow))
        throw new IOException ("Malformed Codepoint sequence - invalid low surrogate");
      ret = new CSSCodepoint ((char) nHigh, cLow);
      m_aReader.unread (cLow);
      m_aReader.unread (nHigh);
    }
    else
    {
      if (nHigh == -1)
        ret = CSSCodepoint.createEOF ();
      else
        ret = new CSSCodepoint (nHigh);
      m_aReader.unread (nHigh);
    }
    return ret;
  }

  public void close () throws IOException
  {
    m_aReader.close ();
  }

  @Nonnegative
  public int getLineNumber ()
  {
    return m_aPos.m_nLine;
  }

  @Nonnegative
  public int getColumnNumber ()
  {
    return m_aPos.m_nCol;
  }

  @Nonnull
  public CSSCodepoint startToken () throws IOException
  {
    // Reset token related stuff
    m_aTokenStartPos = m_aPos.getClone ();
    m_aTokenImage.setLength (0);

    final CSSCodepoint ret = read ();
    return ret;
  }

  @Nonnegative
  public int getTokenStartLineNumber ()
  {
    return m_aTokenStartPos.m_nLine;
  }

  @Nonnegative
  public int getTokenStartColumnNumber ()
  {
    return m_aTokenStartPos.m_nCol;
  }

  @Nonnull
  public String getTokenImage ()
  {
    return m_aTokenImage.toString ();
  }

  @Nonnull
  public CSSToken createToken (@Nonnull final ECSSTokenType eTokenType)
  {
    return new CSSToken (getTokenStartLineNumber (),
                         getTokenStartColumnNumber (),
                         getLineNumber (),
                         getColumnNumber (),
                         getTokenImage (),
                         eTokenType);
  }
}
