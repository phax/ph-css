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
package com.helger.css.reader.errorhandler;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import com.helger.commons.annotation.Nonempty;
import com.helger.commons.annotation.ReturnsMutableCopy;
import com.helger.commons.collection.ext.CommonsArrayList;
import com.helger.commons.collection.ext.ICommonsList;
import com.helger.commons.concurrent.SimpleReadWriteLock;
import com.helger.commons.string.ToStringGenerator;
import com.helger.css.parser.ParseException;
import com.helger.css.parser.Token;

/**
 * A collecting implementation of {@link ICSSParseErrorHandler}. So in case a
 * recoverable error occurs, it is remembered in the internal list and can be
 * retrieved by {@link #getAllParseErrors()}.
 *
 * @author Philip Helger
 */
@ThreadSafe
public class CollectingCSSParseErrorHandler implements ICSSParseErrorHandler
{
  private final SimpleReadWriteLock m_aRWLock = new SimpleReadWriteLock ();
  @GuardedBy ("m_aRWLock")
  private final ICommonsList <CSSParseError> m_aErrors = new CommonsArrayList<> ();

  public CollectingCSSParseErrorHandler ()
  {}

  public void onCSSParseError (@Nonnull final ParseException aParseEx,
                               @Nullable final Token aLastSkippedToken) throws ParseException
  {
    m_aRWLock.writeLocked ( () -> {
      if (aParseEx.expectedTokenSequences == null)
        m_aErrors.add (new CSSParseError (aParseEx.getMessage ()));
      else
        m_aErrors.add (new CSSParseError (aParseEx.currentToken,
                                          aParseEx.expectedTokenSequences,
                                          aParseEx.tokenImage,
                                          aLastSkippedToken));
    });
  }

  public void onCSSUnexpectedRule (@Nonnull final Token aCurrentToken,
                                   @Nonnull @Nonempty final String sRule,
                                   @Nonnull @Nonempty final String sMsg) throws ParseException
  {
    m_aRWLock.writeLocked ( () -> m_aErrors.add (CSSParseError.createUnexpectedRule (aCurrentToken, sRule, sMsg)));
  }

  public void onCSSBrowserCompliantSkip (@Nullable final ParseException ex,
                                         @Nonnull final Token aFromToken,
                                         @Nonnull final Token aToToken) throws ParseException
  {
    m_aRWLock.writeLocked ( () -> m_aErrors.add (CSSParseError.createBrowserCompliantSkip (ex, aFromToken, aToToken)));
  }

  public void onIllegalCharacter (final char cIllegalChar)
  {
    m_aRWLock.writeLocked ( () -> m_aErrors.add (CSSParseError.createIllegalCharacter (cIllegalChar)));
  }

  /**
   * @return <code>true</code> if at least one parse error is contained,
   *         <code>false</code> otherwise.
   */
  @Nonnegative
  public boolean hasParseErrors ()
  {
    return m_aRWLock.readLocked ( () -> m_aErrors.isNotEmpty ());
  }

  /**
   * @return The number of contained parse errors. Always &ge; 0.
   */
  @Nonnegative
  public int getParseErrorCount ()
  {
    return m_aRWLock.readLocked ( () -> m_aErrors.size ());
  }

  /**
   * @return A copy of the list with all contained errors. Never
   *         <code>null</code> but maybe empty.
   * @see #getParseErrorCount()
   * @see #hasParseErrors()
   */
  @Nonnull
  @ReturnsMutableCopy
  public ICommonsList <CSSParseError> getAllParseErrors ()
  {
    return m_aRWLock.readLocked ( () -> m_aErrors.getClone ());
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("errors", m_aErrors).getToString ();
  }
}
