/*
 * Copyright (C) 2014-2023 Philip Helger (www.helger.com)
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

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.io.stream.NonBlockingPushbackReader;
import com.helger.commons.io.stream.StreamHelper;
import com.helger.commons.string.StringHelper;
import com.helger.css.reader.CSSReaderSettings;
import com.helger.css.reader.errorhandler.LoggingCSSParseErrorHandler;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This class is only used internally as the source for the generated parsers.
 * <br>
 * An implementation of interface {@link CharStream}, where the stream is
 * assumed to contain only ASCII characters (with java-like unicode escape
 * processing).
 *
 * @author Philip Helger
 */
@SuppressFBWarnings ("NM_METHOD_NAMING_CONVENTION")
public final class CSSCharStream implements CharStream
{
  /**
   * A special char iterator based on
   * https://www.w3.org/TR/css-syntax-3/#css-filter-code-points
   *
   * @author Philip Helger
   */
  private static final class CSSFilterCodePointReader implements AutoCloseable
  {
    private static final Logger LOGGER = LoggerFactory.getLogger (CSSCharStream.CSSFilterCodePointReader.class);

    private final NonBlockingPushbackReader m_aLocalReader;
    private boolean m_bBrowserCompliantMode = CSSReaderSettings.DEFAULT_BROWSER_COMPLIANT_MODE;
    private boolean m_bCSSUnescape = CSSReaderSettings.DEFAULT_CSS_UNESCAPE;
    private boolean m_bReadBytes = false;

    public CSSFilterCodePointReader (@Nonnull final Reader aSrcReader)
    {
      // 1 char look ahead is sufficient
      m_aLocalReader = new NonBlockingPushbackReader (aSrcReader, 1);
    }

    @Nonnull
    public CSSFilterCodePointReader setBrowserCompliantMode (final boolean bBrowserCompliantMode)
    {
      if (m_bReadBytes)
        throw new IllegalStateException ("Cannot change the CSS browser compliant mode after bytes have been processed");
      m_bBrowserCompliantMode = bBrowserCompliantMode;
      return this;
    }

    @Nonnull
    public CSSFilterCodePointReader setCSSUnescape (final boolean bCSSUnescape)
    {
      if (m_bReadBytes)
        throw new IllegalStateException ("Cannot change the CSS unescape status after bytes have been processed");
      m_bCSSUnescape = bCSSUnescape;
      return this;
    }

    public void close () throws IOException
    {
      m_aLocalReader.close ();
    }

    private static int _unifyCodePoint (final int nCP)
    {
      switch (nCP)
      {
        case 0:
          return 0xfffd;
        case '\f':
        case '\r':
          // No matter if followed by \n or not
          return '\n';
        default:
          return nCP;
      }
    }

    /**
     * @return Next character to come including pushing it back
     */
    private int _lookaheadCodePoint () throws IOException
    {
      final int nCP = m_aLocalReader.read ();
      m_aLocalReader.unread (nCP);
      return _unifyCodePoint (nCP);
    }

    /**
     * This is the method implementing
     * https://www.w3.org/TR/css-syntax-3/#css-filter-code-points
     *
     * @return Next code point. May read 1 or 2 chars.
     */
    private int _readFilteredCodePoint () throws IOException
    {
      // See
      int nCP = m_aLocalReader.read ();
      m_bReadBytes = true;

      switch (nCP)
      {
        case 0:
          // 0 means "unsupported character"
          nCP = 0xfffd;
          break;
        case '\f':
          // Form feed becomes \n
          nCP = '\n';
          break;
        case '\r':
        {
          // Read next
          final int next = m_aLocalReader.read ();
          if (next == '\n')
          {
            // Handle \r\n as one \n
          }
          else
            if (next != -1)
            {
              // Unread the char (except EOF)
              m_aLocalReader.unread (next);
            }
          // \r and \r\n becomes \n
          nCP = '\n';
          break;
        }
      }
      if (LOGGER.isTraceEnabled ())
      {
        if (nCP == -1)
          LOGGER.trace ("Read EOF");
        else
          LOGGER.trace ("Read " + LoggingCSSParseErrorHandler.createLoggingStringIllegalCharacter ((char) nCP));
      }
      return nCP;
    }

    private static boolean _isNewLine (final int nCP)
    {
      return nCP == '\n';
    }

    private static boolean _isWhitespace (final int nCP)
    {
      return _isNewLine (nCP) || nCP == '\t' || nCP == ' ';
    }

    private static boolean _isHexChar (final int nCP)
    {
      return (nCP >= '0' && nCP <= '9') || (nCP >= 'A' && nCP <= 'F') || (nCP >= 'a' && nCP <= 'f');
    }

    // Handle https://www.w3.org/TR/css-syntax-3/#escaping
    private int _cssUnescapeCodePoint (final int nCPSrc) throws IOException
    {
      if (nCPSrc != '\\')
      {
        // Return as-is
        return nCPSrc;
      }

      // Check next char
      int nCPRet = 0;
      int nHexCount = 0;
      while (nHexCount < 6)
      {
        final int cNext = _lookaheadCodePoint ();
        if (_isHexChar (cNext))
        {
          nHexCount++;
          // Consume char
          _readFilteredCodePoint ();
          nCPRet = (nCPRet * 16) + StringHelper.getHexValue ((char) cNext);
        }
        else
          break;
      }

      if (nHexCount == 0)
      {
        // Check if the next char is a newline
        final int nCPNext = _lookaheadCodePoint ();
        if (_isNewLine (nCPNext))
        {
          // Consume newline char
          _readFilteredCodePoint ();
          // Return the code point following the newline
          nCPRet = _readFilteredCodePoint ();
        }
        else
        {
          // Return the backslash as is
          nCPRet = nCPSrc;
        }
      }
      else
      {
        if (nHexCount == 1 && m_bBrowserCompliantMode)
          if (nCPRet == 0 || nCPRet == 9)
          {
            // IE Hack fallback
            m_aLocalReader.unread (nCPRet);
            return nCPSrc;
          }

        // Hex chars found
        // Check for a trailing whitespace and evtl. skip it
        final int nCPNext = _lookaheadCodePoint ();
        if (_isWhitespace (nCPNext))
        {
          // Consume char
          _readFilteredCodePoint ();
        }
      }

      if (LOGGER.isTraceEnabled ())
        LOGGER.trace ("Unescaped CSS item " + nCPSrc + " to " + nCPRet);
      return _unifyCodePoint (nCPRet);
    }

    public int read (@Nonnull final char [] buf, @Nonnegative final int nOfs, @Nonnegative final int nLen)
                                                                                                           throws IOException
    {
      ValueEnforcer.notNull (buf, "buf");
      ValueEnforcer.isGE0 (nOfs, "Ofs");
      ValueEnforcer.isGE0 (nLen, "Len");

      if (LOGGER.isTraceEnabled ())
        LOGGER.trace ("## read (" + nOfs + ", " + nLen + ")");

      int nCPRead = 0;
      int nDstOfs = nOfs;
      for (int i = 0; i < nLen; ++i)
      {
        final int nCP = _readFilteredCodePoint ();
        if (nCP == -1)
        {
          // EOF
          break;
        }

        // Perform the "\" unescaping (if needed)
        final int nUnescapedCP = m_bCSSUnescape ? _cssUnescapeCodePoint (nCP) : nCP;

        if (nUnescapedCP <= Character.MAX_VALUE)
        {
          buf[nDstOfs] = (char) nUnescapedCP;
          nCPRead++;
          nDstOfs++;
        }
        else
        {
          // TODO handle code points cleanly
          LOGGER.warn ("Unsupported CSS code point found: " + nUnescapedCP);
        }
      }
      if (LOGGER.isTraceEnabled ())
        LOGGER.trace ("## read " + nCPRead + " code points");

      // -1 meaning EOF
      return nCPRead == 0 ? -1 : nCPRead;
    }
  }

  private static final int DEFAULT_BUF_SIZE = 4096;

  private final CSSFilterCodePointReader m_aReader;
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

  private int m_nTabSize = CSSReaderSettings.DEFAULT_TAB_SIZE;
  private boolean m_bTrackLineColumn = true;

  public CSSCharStream (@Nonnull final Reader aReader)
  {
    this (aReader, 1, 1, DEFAULT_BUF_SIZE);
  }

  private CSSCharStream (@Nonnull final Reader aReader,
                         @Nonnegative final int nStartLine,
                         @Nonnegative final int nStartColumn,
                         @Nonnegative final int nBufferSize)
  {
    ValueEnforcer.notNull (aReader, "Reader");
    ValueEnforcer.isGE0 (nStartLine, "StartLine");
    ValueEnforcer.isGE0 (nStartColumn, "StartColumn");
    ValueEnforcer.isGE0 (nBufferSize, "BufferSize");

    // Using a buffered reader gives a minimal speedup
    m_aReader = new CSSFilterCodePointReader (StreamHelper.getBuffered (aReader));
    m_nLine = nStartLine;
    m_nColumn = nStartColumn - 1;

    m_nAvailable = nBufferSize;
    m_nBufsize = nBufferSize;
    m_aBuffer = new char [nBufferSize];
    m_aBufLine = new int [nBufferSize];
    m_aBufColumn = new int [nBufferSize];
    m_aNextCharBuf = new char [DEFAULT_BUF_SIZE];
  }

  @Nonnull
  public CSSCharStream setBrowserCompliantMode (final boolean bBrowserCompliantMode)
  {
    m_aReader.setBrowserCompliantMode (bBrowserCompliantMode);
    return this;
  }

  @Nonnull
  public CSSCharStream setCSSUnescape (final boolean bCSSUnescape)
  {
    m_aReader.setCSSUnescape (bCSSUnescape);
    return this;
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
    final String sImage;
    if (m_nBufpos >= m_nTokenBegin)
      sImage = new String (m_aBuffer, m_nTokenBegin, m_nBufpos - m_nTokenBegin + 1);
    else
      sImage = new String (m_aBuffer, m_nTokenBegin, m_nBufsize - m_nTokenBegin) +
               new String (m_aBuffer, 0, m_nBufpos + 1);
    return sImage;
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
   * @param newCol
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

  @Nonnull
  public static CSSCharStream create (@Nonnull final Reader aReader, @Nonnull final CSSReaderSettings aSettings)
  {
    final CSSCharStream ret = new CSSCharStream (aReader);
    ret.setBrowserCompliantMode (aSettings.isBrowserCompliantMode ());
    ret.setCSSUnescape (aSettings.isCSSUnescape ());
    ret.setTabSize (aSettings.getTabSize ());
    return ret;
  }
}
