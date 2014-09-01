/**
 * Copyright (C) 2014 Philip Helger (www.helger.com)
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
package com.helger.css.supplementary.main;

import java.io.File;
import java.io.IOException;

import com.helger.jdk5.CreateJava5Version;

/**
 * This file is responsible for creating the JDK 5 version of this project!<br>
 * Just copy this file to each project which should be available in a JDK5
 * version
 * 
 * @author Philip Helger
 */
// SKIPJDK5
public class MainCreateJava5VersionCSS
{
  public static void main (final String [] args) throws IOException
  {
    CreateJava5Version.createJDK5Version (new File (""));
  }
}
