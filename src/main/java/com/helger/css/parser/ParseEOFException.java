package com.helger.css.parser;

/**
 * Special EOF exception to be caught in browser compliant mode.
 * 
 * @author Philip Helger
 */
public class ParseEOFException extends ParseException
{
  public ParseEOFException (final String sMessage)
  {
    super (sMessage);
  }
}
