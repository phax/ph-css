package com.helger.css.decl;

import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.ICSSVersionAware;
import com.helger.css.ICSSWriteable;

/**
 * Base interface for the list of selectors in an {@literal @}page rule.
 *
 * @author Philip Helger
 */
public interface ICSSPageSelectorContainer extends ICSSVersionAware, ICSSSourceLocationAware, ICSSWriteable
{
  /**
   * @return <code>true</code> if at least on selector is present.
   */
  boolean isNotEmpty ();
}
