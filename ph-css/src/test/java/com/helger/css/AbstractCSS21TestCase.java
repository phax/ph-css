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

public abstract class AbstractCSS21TestCase
{
  protected static final String CSS1 = "/*\r\n" +
                                       "This file is part of pDAF.\r\n" +
                                       "Copyright (c) 2005-2008 by Gregorcic & Helger IT systems OEG.\r\n" +
                                       "All rights reserved.\r\n" +
                                       "http://www.phloc.com\r\n" +
                                       "*/\r\n" +
                                       "\r\n" +
                                       "#menu,\r\n" +
                                       "#menu+.foo\r\n" +
                                       "{\r\n" +
                                       "  background:url(\"../imgs/menu_bg.jpg\") repeat-x 0px 0px;\r\n" +
                                       "  position:relative;\r\n" +
                                       "  padding:0px;\r\n" +
                                       "  vertical-align:top;\r\n" +
                                       "  height:auto !important;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation\r\n" +
                                       "{\r\n" +
                                       "  padding-left:15px;\r\n" +
                                       "  height:auto !important;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "tr.menu,\r\n" +
                                       "tr.menu td\r\n" +
                                       "{\r\n" +
                                       "  height:auto;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry1,\r\n" +
                                       "#menu_navigation a.menuentry1 span.text,\r\n" +
                                       "#menu_navigation a.menuentry2,\r\n" +
                                       "#menu_navigation a.menuentry2 span.text,\r\n" +
                                       "#menu_navigation a.menuentry3,\r\n" +
                                       "#menu_navigation a.menuentry3 span.text,\r\n" +
                                       "#menu_navigation a.menuentry4,\r\n" +
                                       "#menu_navigation a.menuentry4 span.text,\r\n" +
                                       "#menu_navigation a.menuentry5,\r\n" +
                                       "#menu_navigation a.menuentry5 span.text,\r\n" +
                                       "#menu_navigation a.menuentry6,\r\n" +
                                       "#menu_navigation a.menuentry6 span.text\r\n" +
                                       "{\r\n" +
                                       "  display:block;\r\n" +
                                       "  color:#ffffff;\r\n" +
                                       "  text-decoration:none;\r\n" +
                                       "  position:relative;\r\n" +
                                       "  margin-bottom:1px;\r\n" +
                                       "  height:20px;\r\n" +
                                       "  overflow:hidden;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry1:hover,\r\n" +
                                       "#menu_navigation a.menuentry1:hover span.text,\r\n" +
                                       "#menu_navigation a.menuentry2:hover,\r\n" +
                                       "#menu_navigation a.menuentry2:hover span.text,\r\n" +
                                       "#menu_navigation a.menuentry3:hover,\r\n" +
                                       "#menu_navigation a.menuentry3:hover span.text,\r\n" +
                                       "#menu_navigation a.menuentry4:hover,\r\n" +
                                       "#menu_navigation a.menuentry4:hover span.text,\r\n" +
                                       "#menu_navigation a.menuentry5:hover,\r\n" +
                                       "#menu_navigation a.menuentry5:hover span.text,\r\n" +
                                       "#menu_navigation a.menuentry6:hover,\r\n" +
                                       "#menu_navigation a.menuentry6:hover span.text\r\n" +
                                       "{\r\n" +
                                       "  color:#9bd84f;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation span.text\r\n" +
                                       "{\r\n" +
                                       "  position:absolute;\r\n" +
                                       "  left:0px;\r\n" +
                                       "  top:0px;\r\n" +
                                       "  display:block;\r\n" +
                                       "  width:100%;\r\n" +
                                       "  text-decoration:none;\r\n" +
                                       "  line-height:20px;\r\n" +
                                       "  cursor:pointer;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation span.bg\r\n" +
                                       "{\r\n" +
                                       "  position:absolute;\r\n" +
                                       "  left:0px;\r\n" +
                                       "  top:0px;\r\n" +
                                       "  display:block;\r\n" +
                                       "  background-color:#000000;\r\n" +
                                       "  height:20px;\r\n" +
                                       "  width:100%;\r\n" +
                                       "\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry1 span.text,\r\n" +
                                       "#menu_navigation a.menuentry1:link span.text,\r\n" +
                                       "#menu_navigation a.menuentry1:visited span.text,\r\n" +
                                       "#menu_navigation a.menuentry1:hover span.text,\r\n" +
                                       "#menu_navigation a.menuentry1:active span.text\r\n" +
                                       "{\r\n" +
                                       "  font-size:11.5px;\r\n" +
                                       "  text-indent:12px;\r\n" +
                                       "  font-weight:bold;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry2 span.text,\r\n" +
                                       "#menu_navigation a.menuentry2:link span.text,\r\n" +
                                       "#menu_navigation a.menuentry2:visited span.text,\r\n" +
                                       "#menu_navigation a.menuentry2:hover span.text,\r\n" +
                                       "#menu_navigation a.menuentry2:active span.text\r\n" +
                                       "{\r\n" +
                                       "  font-size:11px;\r\n" +
                                       "  text-indent:22px;\r\n" +
                                       "  font-weight:normal;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry3 span.text,\r\n" +
                                       "#menu_navigation a.menuentry3:link span.text,\r\n" +
                                       "#menu_navigation a.menuentry3:visited span.text,\r\n" +
                                       "#menu_navigation a.menuentry3:hover span.text,\r\n" +
                                       "#menu_navigation a.menuentry3:active span.text\r\n" +
                                       "{\r\n" +
                                       "  font-size:11px;\r\n" +
                                       "  text-indent:32px;\r\n" +
                                       "  font-weight:normal;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry4 span.text,\r\n" +
                                       "#menu_navigation a.menuentry4:link span.text,\r\n" +
                                       "#menu_navigation a.menuentry4:visited span.text,\r\n" +
                                       "#menu_navigation a.menuentry4:hover span.text,\r\n" +
                                       "#menu_navigation a.menuentry4:active span.text,\r\n" +
                                       "#menu_navigation a.menuentry5 span.text,\r\n" +
                                       "#menu_navigation a.menuentry5:link span.text,\r\n" +
                                       "#menu_navigation a.menuentry5:visited span.text,\r\n" +
                                       "#menu_navigation a.menuentry5:hover span.text,\r\n" +
                                       "#menu_navigation a.menuentry5:active span.text,\r\n" +
                                       "#menu_navigation a.menuentry6 span.text,\r\n" +
                                       "#menu_navigation a.menuentry6:link span.text,\r\n" +
                                       "#menu_navigation a.menuentry6:visited span.text,\r\n" +
                                       "#menu_navigation a.menuentry6:hover span.text,\r\n" +
                                       "#menu_navigation a.menuentry6:active span.text\r\n" +
                                       "{\r\n" +
                                       "  font-size:10px;\r\n" +
                                       "  text-indent:42px;\r\n" +
                                       "  font-weight:normal;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry1 span.bg\r\n" +
                                       "{\r\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\r\n" +
                                       "  opacity: 0.30;\r\n" +
                                       "  -moz-opacity: 0.30;\r\n" +
                                       "  filter: alpha(opacity=30);\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry2 span.bg\r\n" +
                                       "{\r\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\r\n" +
                                       "  opacity: 0.40;\r\n" +
                                       "  -moz-opacity: 0.40;\r\n" +
                                       "  filter: alpha(opacity=40);\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry3 span.bg\r\n" +
                                       "{\r\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\r\n" +
                                       "  opacity: 0.50;\r\n" +
                                       "  -moz-opacity: 0.50;\r\n" +
                                       "  filter: alpha(opacity=50);\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry4 span.bg\r\n" +
                                       "{\r\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\r\n" +
                                       "  opacity: 0.60;\r\n" +
                                       "  -moz-opacity: 0.60;\r\n" +
                                       "  filter: alpha(opacity=60);\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry1:hover span.bg\r\n" +
                                       "{\r\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\r\n" +
                                       "  opacity: 0.35;\r\n" +
                                       "  -moz-opacity: 0.35;\r\n" +
                                       "  filter: alpha(opacity=35);\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry2:hover span.bg\r\n" +
                                       "{\r\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\r\n" +
                                       "  opacity: 0.45;\r\n" +
                                       "  -moz-opacity: 0.45;\r\n" +
                                       "  filter: alpha(opacity=45);\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry3:hover span.bg\r\n" +
                                       "{\r\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\r\n" +
                                       "  opacity: 0.55;\r\n" +
                                       "  -moz-opacity: 0.55;\r\n" +
                                       "  filter: alpha(opacity=55);\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.menuentry4:hover span.bg\r\n" +
                                       "{\r\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\r\n" +
                                       "  opacity: 0.65;\r\n" +
                                       "  -moz-opacity: 0.65;\r\n" +
                                       "  filter: alpha(opacity=65);\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.selected\r\n" +
                                       "{\r\n" +
                                       "  color:#9bd84f;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.selected span.text\r\n" +
                                       "{\r\n" +
                                       "  background:url(\"../imgs/menu_bullet.gif\") no-repeat 4px 7px;\r\n" +
                                       "  color:#9bd84f;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation a.selected span.bg,\r\n" +
                                       "#menu_navigation a.selected:hover span.bg\r\n" +
                                       "{\r\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\r\n" +
                                       "  opacity: 0.65;\r\n" +
                                       "  -moz-opacity: 0.65;\r\n" +
                                       "  filter: alpha(opacity=65);\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_navigation span.separator\r\n" +
                                       "{\r\n" +
                                       "  display:block;\r\n" +
                                       "  height:7px;\r\n" +
                                       "  padding:0px;\r\n" +
                                       "  line-height:0px;\r\n" +
                                       "  margin:0px;\r\n" +
                                       "  font-size:0px;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#menu_phloc_link:link,\r\n" +
                                       "#menu_phloc_link:visited,\r\n" +
                                       "#menu_phloc_link:hover,\r\n" +
                                       "#menu_phloc_link:active\r\n" +
                                       "{\r\n" +
                                       "  color:#6aac16;\r\n" +
                                       "  text-decoration:none;\r\n" +
                                       "  font-size:10pt;\r\n" +
                                       "  width:155px;\r\n" +
                                       "  position:absolute;\r\n" +
                                       "  bottom:0px;\r\n" +
                                       "  text-align:center;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "#mp3player\r\n" +
                                       "{\r\n" +
                                       "filter:progid:DXImageTransform.Microsoft.Alpha(opacity=50);\r\n" +
                                       "bg:url(\"http://blablabla\");\r\n" +
                                       "bg:url(../../blablabla.gif);\r\n" +
                                       "}\r\n" +
                                       "::-moz-selection {\r\n" +
                                       "  background-color: #d3e9f7;\r\n" +
                                       "  color: #114669;\r\n" +
                                       "}";
  protected static final String CSS2 = "@import url(\"../dijit.css\");\r\n" +
                                       "@import 'legacy.css';\r\n" +
                                       "a+b {\r\n" +
                                       "  border-top: 2px;\r\n" +
                                       "}\r\n" +
                                       "@page :Section1\r\n" +
                                       "    {\r\n" +
                                       "    size:8.5in 11.0in;\r\n" +
                                       "    margin:1.0in .75in 1.0in .75in;\r\n" +
                                       "}\r\n" +
                                       "\r\n" +
                                       "@media print {\r\n" +
                                       "  /** make sure this fits the page **/\r\n" +
                                       "  div#container {\r\n" +
                                       "    width:100%;\r\n" +
                                       "    min-height:0px;\r\n" +
                                       "  }\r\n" +
                                       "}\r\n" +
                                       "#content input[type=radio],\r\n" +
                                       "#content input[type=checkbox]\r\n" +
                                       "{\r\n" +
                                       "  border:0px !important;\r\n" +
                                       "  background:none;\r\n" +
                                       "  \u0077idth:auto !important;\r\n" +
                                       "}\r\n" +
                                       "input {}\r\n" +
                                       "a, p, td{color:red;}\r\n" +
                                       "\r\n" +
                                       "#mitte #mitte_links p a[href~=\"/\"]:link:after,\r\n" +
                                       "#mitte #mitte_links p a[href~=\"/\"]:visited:after {\r\n" +
                                       "  /* hinter Links URL ausgeben (nur Gecko-Engine); wenn URL mit / anfängt, heise.de davor */\r\n" +
                                       "  content: \" [http://www.heise.de\" attr(href)\"] \";\r\n" +
                                       "}\r\n";
}
