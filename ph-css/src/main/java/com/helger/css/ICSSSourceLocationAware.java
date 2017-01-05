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

import javax.annotation.Nullable;

/**
 * Interface for objects that have a source location.
 *
 * @author Philip Helger
 */
public interface ICSSSourceLocationAware
{
  /**
   * Set the source location of the object, determined while parsing.
   *
   * @param aSourceLocation
   *        The source location to use. May be <code>null</code>.
   */
  void setSourceLocation (@Nullable CSSSourceLocation aSourceLocation);

  /**
   * @return The source location of this object when it was read by the parser.
   *         May be <code>null</code> if an object was not read but manually
   *         created.
   */
  @Nullable
  CSSSourceLocation getSourceLocation ();
}
