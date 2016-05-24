/**
 * Copyright (C) 2014-2016 Philip Helger (www.helger.com)
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.io.stream.NonBlockingPushbackReader;

/**
 * A special CSS Codepoint reader that converts chars to Codepoints and keeps
 * track of the line and column number. Note: only use {@link #read()} and
 * {@link #unread(int)} but not the versions with a buffer! The input stream is
 * already buffered so no need to worry!
 *
 * @author Philip Helger
 */
public class CSSCodepointReader extends NonBlockingPushbackReader
{
  private int m_nLine = 1;
  private int m_nColumn = 1;
  private int m_nUnread = 0;

  public CSSCodepointReader (@Nonnull final CSSInputStream aCSSIS, @Nonnull final Charset aCharset)
  {
    super (new InputStreamReader (aCSSIS, aCharset), 10);
  }

  @Override
  public int read () throws IOException
  {
    int ret;
    final int high = super.read ();
    if (Character.isHighSurrogate ((char) high))
    {
      final char low = (char) super.read ();
      if (!Character.isLowSurrogate (low))
        throw new IOException ("Malformed Codepoint sequence - invalid low surrogate");
      ret = Character.toCodePoint ((char) high, low);
    }
    else
      ret = high;

    if (m_nUnread > 0)
    {
      // We already counted - so don't count twice
      m_nUnread--;
    }
    else
    {
      // Count
      if (ret == '\n')
      {
        ++m_nLine;
        m_nColumn = 1;
      }
      else
        ++m_nColumn;
    }

    return ret;
  }

  @Override
  public void unread (final int c) throws IOException
  {
    m_nUnread++;
    super.unread (c);
  }

  @Nonnegative
  public int getLineNumber ()
  {
    return m_nLine;
  }

  @Nonnegative
  public int getColumnNumber ()
  {
    return m_nColumn;
  }
}
