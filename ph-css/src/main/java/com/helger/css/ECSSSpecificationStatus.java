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
package com.helger.css;

/**
 * Enumeration containing all W3C CSS specifications status. Source:
 * http://www.w3.org/Style/CSS/current-work<br>
 * For regular CSS parsing/writing this enum has no impact!
 *
 * @author Philip Helger
 */
public enum ECSSSpecificationStatus
{
  COMPLETED,
  STABLE,
  TESTING,
  REFINING,
  REVISING,
  EXPLORING,
  REWRITING,
  ABANDONED,
  /**
   * Special status that indicates that a CSS property is defined outside the
   * scope of the CSS specifications.
   */
  OUTSIDE_CSS;
}
