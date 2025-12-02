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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.annotation.Nonempty;
import com.helger.annotation.concurrent.Immutable;
import com.helger.base.enforce.ValueEnforcer;
import com.helger.base.string.StringHex;
import com.helger.base.tostring.ToStringGenerator;
import com.helger.css.parser.ParseException;
import com.helger.css.parser.Token;

/**
 * A logging implementation of {@link ICSSParseErrorHandler}. So in case a recoverable error occurs,
 * the details are logged to an SLF4J logger.
 *
 * @author Philip Helger
 */
@Immutable
public class LoggingCSSParseErrorHandler implements ICSSParseErrorHandler
{
  private static final Logger LOGGER = LoggerFactory.getLogger (LoggingCSSParseErrorHandler.class);
  private static final int TOKEN_EOF = 0;

  /**
   * Default constructor.
   */
  public LoggingCSSParseErrorHandler ()
  {}

  @NonNull
  @Nonempty
  public static String createLoggingStringParseError (@NonNull final ParseException ex)
  {
    if (ex.currentToken == null)
    {
      // Is null if the constructor with String only was used
      return ex.getMessage ();
    }
    return createLoggingStringParseError (ex.currentToken, ex.expectedTokenSequences, ex.tokenImage, null);
  }

  @NonNull
  @Nonempty
  public static String createLoggingStringParseError (@NonNull final Token aLastValidToken,
                                                      @NonNull final int [] [] aExpectedTokenSequencesVal,
                                                      @NonNull final String [] aTokenImageVal,
                                                      @Nullable final Token aLastSkippedToken)
  {
    ValueEnforcer.notNull (aLastValidToken, "LastValidToken");
    ValueEnforcer.notNull (aExpectedTokenSequencesVal, "ExpectedTokenSequencesVal");
    ValueEnforcer.notNull (aTokenImageVal, "TokenImageVal");

    final StringBuilder aExpected = new StringBuilder ();
    int nMaxSize = 0;
    for (final int [] aExpectedTokens : aExpectedTokenSequencesVal)
    {
      if (nMaxSize < aExpectedTokens.length)
        nMaxSize = aExpectedTokens.length;

      if (aExpected.length () > 0)
        aExpected.append (',');
      for (final int nExpectedToken : aExpectedTokens)
        aExpected.append (' ').append (aTokenImageVal[nExpectedToken]);
    }

    final StringBuilder retval = new StringBuilder (1024);
    retval.append ('[')
          .append (aLastValidToken.next.beginLine)
          .append (':')
          .append (aLastValidToken.next.beginColumn)
          .append (']');
    if (aLastSkippedToken != null)
    {
      retval.append ("-[")
            .append (aLastSkippedToken.endLine)
            .append (':')
            .append (aLastSkippedToken.endColumn)
            .append (']');
    }
    retval.append (" Encountered");
    Token aCurToken = aLastValidToken.next;
    for (int i = 0; i < nMaxSize; i++)
    {
      retval.append (' ');
      if (aCurToken.kind == TOKEN_EOF)
      {
        retval.append (aTokenImageVal[TOKEN_EOF]);
        break;
      }
      retval.append ("text '")
            .append (aCurToken.image)
            .append ("' corresponding to token ")
            .append (aTokenImageVal[aCurToken.kind]);
      aCurToken = aCurToken.next;
    }
    retval.append (". ");
    if (aLastSkippedToken != null)
      retval.append ("Skipped until token ").append (aLastSkippedToken).append (". ");
    retval.append (aExpectedTokenSequencesVal.length == 1 ? "Was expecting:" : "Was expecting one of:")
          .append (aExpected);
    return retval.toString ();
  }

  public void onCSSParseError (@NonNull final ParseException aParseEx, @Nullable final Token aLastSkippedToken)
                                                                                                                throws ParseException
  {
    if (aParseEx.expectedTokenSequences == null)
      LOGGER.warn (aParseEx.getMessage ());
    else
      LOGGER.warn (createLoggingStringParseError (aParseEx.currentToken,
                                                  aParseEx.expectedTokenSequences,
                                                  aParseEx.tokenImage,
                                                  aLastSkippedToken));
  }

  /**
   * Create a common string to be used for unexpected rules.
   *
   * @param aCurrentToken
   *        The current token that caused an error. Never <code>null</code>.
   * @param sRule
   *        The name of the rule. Always starts with a '@'. May neither be <code>null</code> nor
   *        empty.
   * @param sMsg
   *        The custom error message. Neither <code>null</code> nor empty.
   * @return The concatenated string with source location, rule and message. May neither be
   *         <code>null</code> nor empty.
   */
  @NonNull
  @Nonempty
  public static String createLoggingStringUnexpectedRule (@NonNull final Token aCurrentToken,
                                                          @NonNull @Nonempty final String sRule,
                                                          @NonNull @Nonempty final String sMsg)
  {
    return "[" +
           aCurrentToken.beginLine +
           ":" +
           aCurrentToken.beginColumn +
           "] Unexpected rule '" +
           sRule +
           "': " +
           sMsg;
  }

  public void onCSSUnexpectedRule (@NonNull final Token aCurrentToken,
                                   @NonNull @Nonempty final String sRule,
                                   @NonNull @Nonempty final String sMsg) throws ParseException
  {
    LOGGER.warn (createLoggingStringUnexpectedRule (aCurrentToken, sRule, sMsg));
  }

  /**
   * Create a common string to be used for deprecated properties. To be called, if a deprecated old
   * IE 6/7 property is found.
   *
   * @param aPrefixToken
   *        The prefix token found (like '$' or '*'). Never <code>null</code>.
   * @param aIdentifierToken
   *        The identifier token found. Never <code>null</code>.
   * @return The concatenated string with source location, etc. May neither be <code>null</code> nor
   *         empty.
   */
  @NonNull
  @Nonempty
  public static String createLoggingStringDeprecatedProperty (@NonNull final Token aPrefixToken,
                                                              @NonNull final Token aIdentifierToken)
  {
    return "[" +
           aPrefixToken.beginLine +
           ":" +
           aPrefixToken.beginColumn +
           "] Deprecated property name '" +
           aPrefixToken.image +
           aIdentifierToken.image +
           "'";
  }

  public void onCSSDeprecatedProperty (@NonNull final Token aPrefixToken, @NonNull final Token aIdentifierToken)
  {
    LOGGER.warn (createLoggingStringDeprecatedProperty (aPrefixToken, aIdentifierToken));
  }

  @NonNull
  @Nonempty
  public static String createLoggingStringBrowserCompliantSkip (@Nullable final ParseException ex,
                                                                @NonNull final Token aFromToken,
                                                                @NonNull final Token aToToken)
  {
    final StringBuilder ret = new StringBuilder ("Browser compliant mode skipped CSS from [").append (aFromToken.beginLine)
                                                                                             .append (":")
                                                                                             .append (aFromToken.beginColumn)
                                                                                             .append ("] starting at token '")
                                                                                             .append (aFromToken.image)
                                                                                             .append ("' until [")
                                                                                             .append (aToToken.endLine)
                                                                                             .append (":")
                                                                                             .append (aToToken.endColumn)
                                                                                             .append ("] to token '")
                                                                                             .append (aToToken.image)
                                                                                             .append ("'");
    if (ex != null)
      ret.append (" (based on ")
         .append (ex.getClass ().getName ())
         .append (": ")
         .append (ex.getMessage ())
         .append (")");
    return ret.toString ();
  }

  public void onCSSBrowserCompliantSkip (@Nullable final ParseException ex,
                                         @NonNull final Token aFromToken,
                                         @NonNull final Token aToToken) throws ParseException
  {
    LOGGER.warn (createLoggingStringBrowserCompliantSkip (ex, aFromToken, aToToken));
  }

  @NonNull
  @Nonempty
  public static String createLoggingStringIllegalCharacter (final char cIllegalChar)
  {
    return "Found illegal character: " +
           cIllegalChar +
           " (0x" +
           StringHex.getHexStringLeadingZero (cIllegalChar, 4) +
           ")";
  }

  @Override
  public void onIllegalCharacter (final char cIllegalChar)
  {
    LOGGER.warn (createLoggingStringIllegalCharacter (cIllegalChar));
  }

  @Override
  public String toString ()
  {
    return new ToStringGenerator (this).getToString ();
  }
}
