package com.helger.css.supplementary.parser;

import javax.annotation.Nonnull;

public class CSSTokenizeException extends Exception
{
  public CSSTokenizeException (@Nonnull final String sMsg)
  {
    super (sMsg);
  }
}
