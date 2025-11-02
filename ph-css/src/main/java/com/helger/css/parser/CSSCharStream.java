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
package com.helger.css.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import org.jspecify.annotations.NonNull;

import com.helger.annotation.Nonnegative;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.io.stream.StreamHelper;

/**
 * This class is only used internally as the source for the generated parsers. <br>
 * An implementation of interface {@link CharStream}, where the stream is assumed to contain only
 * ASCII characters (with java-like unicode escape processing).
 *
 * @author Philip Helger
 */
public final class CSSCharStream implements CharStream
{
  private static final int DEFAULT_BUF_SIZE = 4096;

  private final Reader m_aReader;
  private int m_nLine;
  private int m_nColumn;
  private int m_nAvailable;
  private int m_nBufsize;
  private char [] m_aBuffer;
  private int [] m_aBufLine;
  private int [] m_aBufColumn;
  // Fixed size buf
  private final char [] m_aNextCharBuf;

  private boolean m_bPrevCharIsCR = false;
  private boolean m_bPrevCharIsLF = false;
  private int m_nTokenBegin = 0;
  private int m_nInPrefetchBuf = 0;
  private int m_nMaxNextCharInd = 0;
  private int m_nNextCharInd = -1;
  /** Position in buffer. */
  private int m_nBufpos = -1;

  private int m_nTabSize = 8;
  private boolean m_bTrackLineColumn = true;

  public CSSCharStream (@NonNull final Reader aReader)
  {
    this (aReader, 1, 1, DEFAULT_BUF_SIZE);
  }

  private CSSCharStream (@NonNull final Reader aReader,
                         @Nonnegative final int nStartLine,
                         @Nonnegative final int nStartColumn,
                         @Nonnegative final int nBufferSize)
  {
    ValueEnforcer.isGE0 (nBufferSize, "BufferSize");
    // Using a buffered reader gives a minimal speedup
    m_aReader = StreamHelper.getBuffered (ValueEnforcer.notNull (aReader, "Reader"));
    m_nLine = ValueEnforcer.isGE0 (nStartLine, "StartLine");
    m_nColumn = ValueEnforcer.isGE0 (nStartColumn, "StartColumn") - 1;

    m_nAvailable = nBufferSize;
    m_nBufsize = nBufferSize;
    m_aBuffer = new char [nBufferSize];
    m_aBufLine = new int [nBufferSize];
    m_aBufColumn = new int [nBufferSize];
    m_aNextCharBuf = new char [DEFAULT_BUF_SIZE];
  }

  public int getTabSize ()
  {
    return m_nTabSize;
  }

  public void setTabSize (final int nTabSize)
  {
    m_nTabSize = nTabSize;
  }

  private void _expandBuff (final boolean bWrapAround)
  {
    // Currently fixed - shall we expand to 50%?
    final int nDeltaToExpand = 2048;
    final char [] aNewBuffer = new char [m_nBufsize + nDeltaToExpand];
    final int [] aNewBufLine = new int [m_nBufsize + nDeltaToExpand];
    final int [] aNewBufColumn = new int [m_nBufsize + nDeltaToExpand];

    try
    {
      if (bWrapAround)
      {
        System.arraycopy (m_aBuffer, m_nTokenBegin, aNewBuffer, 0, m_nBufsize - m_nTokenBegin);
        System.arraycopy (m_aBuffer, 0, aNewBuffer, m_nBufsize - m_nTokenBegin, m_nBufpos);
        m_aBuffer = aNewBuffer;

        System.arraycopy (m_aBufLine, m_nTokenBegin, aNewBufLine, 0, m_nBufsize - m_nTokenBegin);
        System.arraycopy (m_aBufLine, 0, aNewBufLine, m_nBufsize - m_nTokenBegin, m_nBufpos);
        m_aBufLine = aNewBufLine;

        System.arraycopy (m_aBufColumn, m_nTokenBegin, aNewBufColumn, 0, m_nBufsize - m_nTokenBegin);
        System.arraycopy (m_aBufColumn, 0, aNewBufColumn, m_nBufsize - m_nTokenBegin, m_nBufpos);
        m_aBufColumn = aNewBufColumn;

        m_nBufpos += (m_nBufsize - m_nTokenBegin);
      }
      else
      {
        System.arraycopy (m_aBuffer, m_nTokenBegin, aNewBuffer, 0, m_nBufsize - m_nTokenBegin);
        m_aBuffer = aNewBuffer;

        System.arraycopy (m_aBufLine, m_nTokenBegin, aNewBufLine, 0, m_nBufsize - m_nTokenBegin);
        m_aBufLine = aNewBufLine;

        System.arraycopy (m_aBufColumn, m_nTokenBegin, aNewBufColumn, 0, m_nBufsize - m_nTokenBegin);
        m_aBufColumn = aNewBufColumn;

        m_nBufpos -= m_nTokenBegin;
      }
    }
    catch (final Exception ex)
    {
      throw new Error ("Something went wrong", ex);
    }

    m_nBufsize += nDeltaToExpand;
    m_nAvailable = m_nBufsize;
    m_nTokenBegin = 0;
  }

  private void _fillBuff () throws IOException
  {
    if (m_nMaxNextCharInd == DEFAULT_BUF_SIZE)
    {
      m_nMaxNextCharInd = 0;
      m_nNextCharInd = 0;
    }

    try
    {
      final int i = m_aReader.read (m_aNextCharBuf, m_nMaxNextCharInd, DEFAULT_BUF_SIZE - m_nMaxNextCharInd);
      if (i == -1)
      {
        m_aReader.close ();
        throw new IOException ("EOF in JavaCharStream");
      }
      m_nMaxNextCharInd += i;
    }
    catch (final IOException ex)
    {
      if (m_nBufpos != 0)
      {
        --m_nBufpos;
        backup (0);
      }
      else
      {
        m_aBufLine[m_nBufpos] = m_nLine;
        m_aBufColumn[m_nBufpos] = m_nColumn;
      }
      throw ex;
    }
  }

  private char _readByte () throws IOException
  {
    ++m_nNextCharInd;
    if (m_nNextCharInd >= m_nMaxNextCharInd)
      _fillBuff ();

    return m_aNextCharBuf[m_nNextCharInd];
  }

  /**
   * @return starting character for token.
   * @throws IOException
   *         from readChar
   */
  public char beginToken () throws IOException
  {
    if (m_nInPrefetchBuf > 0)
    {
      // Do we something in the local buffer?
      --m_nInPrefetchBuf;

      if (++m_nBufpos == m_nBufsize)
        m_nBufpos = 0;

      m_nTokenBegin = m_nBufpos;
      return m_aBuffer[m_nBufpos];
    }

    m_nTokenBegin = 0;
    m_nBufpos = -1;

    return readChar ();
  }

  private void _adjustBuffSize ()
  {
    if (m_nAvailable == m_nBufsize)
    {
      if (m_nTokenBegin > m_nBufsize / 2)
      {
        // Over 50%?
        m_nBufpos = 0;
        m_nAvailable = m_nTokenBegin;
      }
      else
        _expandBuff (false);
    }
    else
      if (m_nAvailable > m_nTokenBegin)
        m_nAvailable = m_nBufsize;
      else
        if ((m_nTokenBegin - m_nAvailable) < m_nBufsize / 2)
        {
          // Less then 50% available<
          _expandBuff (true);
        }
        else
          m_nAvailable = m_nTokenBegin;
  }

  private void _updateLineColumn (final char c)
  {
    m_nColumn++;

    if (m_bPrevCharIsLF)
    {
      // Char following \n
      m_bPrevCharIsLF = false;
      m_nColumn = 1;
      m_nLine++;
    }
    else
      if (m_bPrevCharIsCR)
      {
        // Char following \r
        m_bPrevCharIsCR = false;
        if (c == '\n')
          m_bPrevCharIsLF = true;
        else
        {
          m_nColumn = 1;
          m_nLine++;
        }
      }

    switch (c)
    {
      case '\r':
        m_bPrevCharIsCR = true;
        break;
      case '\n':
        m_bPrevCharIsLF = true;
        break;
      case '\t':
        m_nColumn--;
        m_nColumn += (m_nTabSize - (m_nColumn % m_nTabSize));
        break;
      default:
        break;
    }

    m_aBufLine[m_nBufpos] = m_nLine;
    m_aBufColumn[m_nBufpos] = m_nColumn;
  }

  /**
   * Read a character.
   *
   * @return The read character
   * @throws IOException
   *         if an I/O error occurs
   */
  public char readChar () throws IOException
  {
    if (m_nInPrefetchBuf > 0)
    {
      // Do we something in the local buffer?
      --m_nInPrefetchBuf;

      if (++m_nBufpos == m_nBufsize)
        m_nBufpos = 0;
      return m_aBuffer[m_nBufpos];
    }

    if (++m_nBufpos == m_nAvailable)
      _adjustBuffSize ();

    final char c = _readByte ();
    m_aBuffer[m_nBufpos] = c;

    // This would be the point to handle CSS (un)escaping
    if (m_bTrackLineColumn)
      _updateLineColumn (c);
    return c;
  }

  /** @return column of token start */
  public int getBeginColumn ()
  {
    return m_aBufColumn[m_nTokenBegin];
  }

  /** @return line number of token start */
  public int getBeginLine ()
  {
    return m_aBufLine[m_nTokenBegin];
  }

  /** @return end column. */
  public int getEndColumn ()
  {
    return m_aBufColumn[m_nBufpos];
  }

  /** @return end line. */
  public int getEndLine ()
  {
    return m_aBufLine[m_nBufpos];
  }

  /** Retreat. */
  public void backup (final int nAmount)
  {
    m_nInPrefetchBuf += nAmount;
    m_nBufpos -= nAmount;
    if (m_nBufpos < 0)
      m_nBufpos += m_nBufsize;
  }

  /** @return token image as String */
  public String getImage ()
  {
    if (m_nBufpos >= m_nTokenBegin)
      return new String (m_aBuffer, m_nTokenBegin, m_nBufpos - m_nTokenBegin + 1);

    return new String (m_aBuffer, m_nTokenBegin, m_nBufsize - m_nTokenBegin) + new String (m_aBuffer, 0, m_nBufpos + 1);
  }

  /** @return suffix */
  public char [] getSuffix (final int len)
  {
    final char [] ret = new char [len];

    if ((m_nBufpos + 1) >= len)
      System.arraycopy (m_aBuffer, m_nBufpos - len + 1, ret, 0, len);
    else
    {
      System.arraycopy (m_aBuffer, m_nBufsize - (len - m_nBufpos - 1), ret, 0, len - m_nBufpos - 1);
      System.arraycopy (m_aBuffer, 0, ret, len - m_nBufpos - 1, m_nBufpos + 1);
    }

    return ret;
  }

  /** Set buffers back to null when finished. */
  public void done ()
  {
    Arrays.fill (m_aNextCharBuf, '\u0000');
    m_aBuffer = null;
    m_aBufLine = null;
    m_aBufColumn = null;
  }

  /**
   * Method to adjust line and column numbers for the start of a token.
   *
   * @param nNewLine
   *        line index
   * @param nNewCol
   *        column index
   */
  public void adjustBeginLineColumn (final int nNewLine, final int nNewCol)
  {
    int nStart = m_nTokenBegin;
    int nRealNewLine = nNewLine;
    final int nLen;

    if (m_nBufpos >= m_nTokenBegin)
      nLen = m_nBufpos - m_nTokenBegin + m_nInPrefetchBuf + 1;
    else
      nLen = m_nBufsize - m_nTokenBegin + m_nBufpos + 1 + m_nInPrefetchBuf;

    int nIdx = 0;
    int j = 0;
    int nNextColDiff = 0;
    int nColumnDiff = 0;

    while (true)
    {
      if (nIdx >= nLen)
        break;

      j = nStart % m_nBufsize;
      ++nStart;
      final int k = nStart % m_nBufsize;
      if (m_aBufLine[j] != m_aBufLine[k])
        break;

      m_aBufLine[j] = nRealNewLine;
      nNextColDiff = nColumnDiff + m_aBufColumn[k] - m_aBufColumn[j];
      m_aBufColumn[j] = nNewCol + nColumnDiff;
      nColumnDiff = nNextColDiff;
      nIdx++;
    }

    if (nIdx < nLen)
    {
      m_aBufLine[j] = nRealNewLine++;
      m_aBufColumn[j] = nNewCol + nColumnDiff;

      while (nIdx++ < nLen)
      {
        j = nStart % m_nBufsize;
        ++nStart;
        final int k = nStart % m_nBufsize;
        if (m_aBufLine[j] != m_aBufLine[k])
        {
          m_aBufLine[j] = nRealNewLine;
          nRealNewLine++;
        }
        else
          m_aBufLine[j] = nRealNewLine;
      }
    }

    m_nLine = m_aBufLine[j];
    m_nColumn = m_aBufColumn[j];
  }

  public boolean isTrackLineColumn ()
  {
    return m_bTrackLineColumn;
  }

  public void setTrackLineColumn (final boolean bTrackLineColumn)
  {
    m_bTrackLineColumn = bTrackLineColumn;
  }
}
