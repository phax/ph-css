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
package com.helger.css;

/**
 * Enumeration containing all predefined CSS meta units.
 *
 * @author Philip Helger
 */
public enum ECSSMetaUnit
{
  PERCENTAGE,
  FONT_RELATIVE_LENGTH,
  FONT_RELATIVE_LENGTH_ROOT_BASED,
  /**
   * The viewport-percentage length units are based on four different viewport sizes: small, large,
   * dynamic, and default. The allowance for the different viewport sizes is in response to browser
   * interfaces expanding and retracting dynamically and hiding and showing the content underneath.
   */
  VIEWPORT_RELATIVE_LENGTH,
  ABSOLUTE_LENGTH,
  ANGLE,
  TIME,
  FREQUENZY,
  RESOLUTION,
  FLEX,
  /**
   * When applying styles to a container using container queries, you can use container query length
   * units. These units specify a length relative to the dimensions of a query container. Components
   * that use units of length relative to their container are more flexible to use in different
   * containers without having to recalculate concrete length values.
   */
  CONTAINER_RELATIVE_LENGTH;

  public boolean isRelativeLength ()
  {
    return this == FONT_RELATIVE_LENGTH ||
           this == FONT_RELATIVE_LENGTH_ROOT_BASED ||
           this == VIEWPORT_RELATIVE_LENGTH ||
           this == CONTAINER_RELATIVE_LENGTH;
  }

  public boolean isLength ()
  {
    return isRelativeLength () || this == ABSOLUTE_LENGTH;
  }
}
