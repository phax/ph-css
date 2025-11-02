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
package com.helger.css.reader.errorhandler;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import com.helger.annotation.Nonempty;
import com.helger.annotation.Nonnegative;
import com.helger.annotation.concurrent.GuardedBy;
import com.helger.annotation.concurrent.ThreadSafe;
import com.helger.annotation.style.ReturnsMutableCopy;
import com.helger.base.concurrent.SimpleReadWriteLock;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.collection.commons.CommonsArrayList;
import com.helger.collection.commons.ICommonsList;
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
  private final ICommonsList <CSSParseError> m_aErrors = new CommonsArrayList <> ();

  public CollectingCSSParseErrorHandler ()
  {}

  public void onCSSParseError (@NonNull final ParseException aParseEx, @Nullable final Token aLastSkippedToken) throws ParseException
  {
    m_aRWLock.writeLocked ( () -> {
      if (aParseEx.expectedTokenSequences == null)
        m_aErrors.add (new CSSParseError (aParseEx.getMessage ()));
      else
        m_aErrors.add (new CSSParseError (aParseEx.currentToken, aParseEx.expectedTokenSequences, aParseEx.tokenImage, aLastSkippedToken));
    });
  }

  public void onCSSUnexpectedRule (@NonNull final Token aCurrentToken,
                                   @NonNull @Nonempty final String sRule,
                                   @NonNull @Nonempty final String sMsg) throws ParseException
  {
    m_aRWLock.writeLocked ( () -> m_aErrors.add (CSSParseError.createUnexpectedRule (aCurrentToken, sRule, sMsg)));
  }

  public void onCSSDeprecatedProperty (@NonNull final Token aPrefixToken, @NonNull final Token aIdentifierToken)
  {
    m_aRWLock.writeLocked ( () -> m_aErrors.add (CSSParseError.createDeprecatedProperty (aPrefixToken, aIdentifierToken)));
  }

  public void onCSSBrowserCompliantSkip (@Nullable final ParseException ex,
                                         @NonNull final Token aFromToken,
                                         @NonNull final Token aToToken) throws ParseException
  {
    m_aRWLock.writeLocked ( () -> m_aErrors.add (CSSParseError.createBrowserCompliantSkip (ex, aFromToken, aToToken)));
  }

  @Override
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
    return m_aRWLock.readLockedBoolean (m_aErrors::isNotEmpty);
  }

  /**
   * @return The number of contained parse errors. Always &ge; 0.
   */
  @Nonnegative
  public int getParseErrorCount ()
  {
    return m_aRWLock.readLockedInt (m_aErrors::size);
  }

  /**
   * @return A copy of the list with all contained errors. Never
   *         <code>null</code> but maybe empty.
   * @see #getParseErrorCount()
   * @see #hasParseErrors()
   */
  @NonNull
  @ReturnsMutableCopy
  public ICommonsList <CSSParseError> getAllParseErrors ()
  {
    return m_aRWLock.readLockedGet (m_aErrors::getClone);
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).append ("Errors", m_aErrors).getToString ();
  }
}
