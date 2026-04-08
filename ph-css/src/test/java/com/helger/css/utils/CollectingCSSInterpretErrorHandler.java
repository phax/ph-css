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
