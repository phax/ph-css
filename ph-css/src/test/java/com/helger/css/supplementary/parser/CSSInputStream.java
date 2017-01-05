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
