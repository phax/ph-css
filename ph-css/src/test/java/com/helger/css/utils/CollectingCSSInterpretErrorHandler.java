/*
 * Copyright (C) 2014-2026 Philip Helger (www.helger.com)
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
package com.helger.css.utils;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.css.reader.errorhandler.ICSSInterpretErrorHandler;

public final class CollectingCSSInterpretErrorHandler implements ICSSInterpretErrorHandler
{
  private static final Logger LOGGER = LoggerFactory.getLogger (CollectingCSSInterpretErrorHandler.class);

  private final List <String> m_aWarnings = new ArrayList <> ();
  private final List <String> m_aErrors = new ArrayList <> ();

  @Override
  public void onCSSInterpretationWarning (@NonNull String sMessage)
  {
    m_aWarnings.add (sMessage);
    LOGGER.warn (sMessage);
  }

  @Override
  public void onCSSInterpretationError (@NonNull String sMessage)
  {
    m_aErrors.add (sMessage);
    LOGGER.error (sMessage);
  }

  @NonNull
  public List <String> getWarnings ()
  {
    return List.copyOf (m_aWarnings);
  }

  @NonNull
  public List <String> getErrors ()
  {
    return List.copyOf (m_aErrors);
  }
}
