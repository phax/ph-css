package com.helger.css.supplementary.parser;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.io.stream.NonBlockingPushbackInputStream;
import com.helger.commons.io.stream.StreamHelper;

public final class CSSInputStream extends NonBlockingPushbackInputStream
{
  public CSSInputStream (@Nonnull final InputStream aIS)
  {
    super (StreamHelper.getBuffered (aIS), 1024);
    ValueEnforcer.notNull (aIS, "InputStream");
  }

  @Override
  public int read () throws IOException
  {
    // https://www.w3.org/TR/css-syntax-3/#input-preprocessing
    final int ch = super.read ();
    if (ch == '\r')
    {
      final int next = super.read ();
      if (next != '\n')
        super.unread (next);
      return '\n';
    }
    if (ch == '\f')
      return '\n';
    if (ch == 0)
      return 0xfffd;
    return ch;
  }
}
