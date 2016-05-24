package com.helger.css.supplementary.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import com.helger.commons.io.stream.NonBlockingPushbackReader;

public class CSSCodepointReader extends NonBlockingPushbackReader
{
  private int m_nLine = 1;
  private int m_nColumn = 1;

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
        throw new IOException ("Malformed Codepoint sequence");
      ret = Character.toCodePoint ((char) high, low);
    }
    else
      ret = high;

    if (ret == '\n')
    {
      ++m_nLine;
      m_nColumn = 1;
    }
    else
      ++m_nColumn;

    return ret;
  }

  public char readChar () throws IOException
  {
    return (char) read ();
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
