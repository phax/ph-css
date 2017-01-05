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

public abstract class AbstractCSS30TestCase
{
  protected static final String CSS1 = "/*\n" +
                                       "This file is part of pDAF.\n" +
                                       "Copyright (c) 2005-2008 by Gregorcic & Helger IT systems OEG.\n" +
                                       "All rights reserved.\n" +
                                       "http://www.phloc.com\n" +
                                       "*/\n" +
                                       "\n" +
                                       "#menu,\n" +
                                       "#menu+.foo\n" +
                                       "{\n" +
                                       "  background:url(\"../imgs/menu_bg.jpg\") repeat-x 0px 0px;\n" +
                                       "  position:relative;\n" +
                                       "  padding:0px;\n" +
                                       "  vertical-align:top;\n" +
                                       "  height:auto !important;\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation\n" +
                                       "{\n" +
                                       "  padding-left:15px;\n" +
                                       "  height:auto !important;\n" +
                                       "}\n" +
                                       "\n" +
                                       "tr.menu,\n" +
                                       "tr.menu td\n" +
                                       "{\n" +
                                       "  height:auto;\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry1,\n" +
                                       "#menu_navigation a.menuentry1 span.text,\n" +
                                       "#menu_navigation a.menuentry2,\n" +
                                       "#menu_navigation a.menuentry2 span.text,\n" +
                                       "#menu_navigation a.menuentry3,\n" +
                                       "#menu_navigation a.menuentry3 span.text,\n" +
                                       "#menu_navigation a.menuentry4,\n" +
                                       "#menu_navigation a.menuentry4 span.text,\n" +
                                       "#menu_navigation a.menuentry5,\n" +
                                       "#menu_navigation a.menuentry5 span.text,\n" +
                                       "#menu_navigation a.menuentry6,\n" +
                                       "#menu_navigation a.menuentry6 span.text\n" +
                                       "{\n" +
                                       "  display:block;\n" +
                                       "  color:#ffffff;\n" +
                                       "  text-decoration:none;\n" +
                                       "  position:relative;\n" +
                                       "  margin-bottom:1px;\n" +
                                       "  height:20px;\n" +
                                       "  overflow:hidden;\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry1:hover,\n" +
                                       "#menu_navigation a.menuentry1:hover span.text,\n" +
                                       "#menu_navigation a.menuentry2:hover,\n" +
                                       "#menu_navigation a.menuentry2:hover span.text,\n" +
                                       "#menu_navigation a.menuentry3:hover,\n" +
                                       "#menu_navigation a.menuentry3:hover span.text,\n" +
                                       "#menu_navigation a.menuentry4:hover,\n" +
                                       "#menu_navigation a.menuentry4:hover span.text,\n" +
                                       "#menu_navigation a.menuentry5:hover,\n" +
                                       "#menu_navigation a.menuentry5:hover span.text,\n" +
                                       "#menu_navigation a.menuentry6:hover,\n" +
                                       "#menu_navigation a.menuentry6:hover span.text\n" +
                                       "{\n" +
                                       "  color:#9bd84f;\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation span.text\n" +
                                       "{\n" +
                                       "  position:absolute;\n" +
                                       "  left:0px;\n" +
                                       "  top:0px;\n" +
                                       "  display:block;\n" +
                                       "  width:100%;\n" +
                                       "  text-decoration:none;\n" +
                                       "  line-height:20px;\n" +
                                       "  cursor:pointer;\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation span.bg\n" +
                                       "{\n" +
                                       "  position:absolute;\n" +
                                       "  left:0px;\n" +
                                       "  top:0px;\n" +
                                       "  display:block;\n" +
                                       "  background-color:#000000;\n" +
                                       "  height:20px;\n" +
                                       "  width:100%;\n" +
                                       "\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry1 span.text,\n" +
                                       "#menu_navigation a.menuentry1:link span.text,\n" +
                                       "#menu_navigation a.menuentry1:visited span.text,\n" +
                                       "#menu_navigation a.menuentry1:hover span.text,\n" +
                                       "#menu_navigation a.menuentry1:active span.text\n" +
                                       "{\n" +
                                       "  font-size:11.5px;\n" +
                                       "  text-indent:12px;\n" +
                                       "  font-weight:bold;\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry2 span.text,\n" +
                                       "#menu_navigation a.menuentry2:link span.text,\n" +
                                       "#menu_navigation a.menuentry2:visited span.text,\n" +
                                       "#menu_navigation a.menuentry2:hover span.text,\n" +
                                       "#menu_navigation a.menuentry2:active span.text\n" +
                                       "{\n" +
                                       "  font-size:11px;\n" +
                                       "  text-indent:22px;\n" +
                                       "  font-weight:normal;\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry3 span.text,\n" +
                                       "#menu_navigation a.menuentry3:link span.text,\n" +
                                       "#menu_navigation a.menuentry3:visited span.text,\n" +
                                       "#menu_navigation a.menuentry3:hover span.text,\n" +
                                       "#menu_navigation a.menuentry3:active span.text\n" +
                                       "{\n" +
                                       "  font-size:11px;\n" +
                                       "  text-indent:32px;\n" +
                                       "  font-weight:normal;\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry4 span.text,\n" +
                                       "#menu_navigation a.menuentry4:link span.text,\n" +
                                       "#menu_navigation a.menuentry4:visited span.text,\n" +
                                       "#menu_navigation a.menuentry4:hover span.text,\n" +
                                       "#menu_navigation a.menuentry4:active span.text,\n" +
                                       "#menu_navigation a.menuentry5 span.text,\n" +
                                       "#menu_navigation a.menuentry5:link span.text,\n" +
                                       "#menu_navigation a.menuentry5:visited span.text,\n" +
                                       "#menu_navigation a.menuentry5:hover span.text,\n" +
                                       "#menu_navigation a.menuentry5:active span.text,\n" +
                                       "#menu_navigation a.menuentry6 span.text,\n" +
                                       "#menu_navigation a.menuentry6:link span.text,\n" +
                                       "#menu_navigation a.menuentry6:visited span.text,\n" +
                                       "#menu_navigation a.menuentry6:hover span.text,\n" +
                                       "#menu_navigation a.menuentry6:active span.text\n" +
                                       "{\n" +
                                       "  font-size:10px;\n" +
                                       "  text-indent:42px;\n" +
                                       "  font-weight:normal;\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry1 span.bg\n" +
                                       "{\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\n" +
                                       "  opacity: 0.30;\n" +
                                       "  -moz-opacity: 0.30;\n" +
                                       "  filter: alpha(opacity=30);\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry2 span.bg\n" +
                                       "{\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\n" +
                                       "  opacity: 0.40;\n" +
                                       "  -moz-opacity: 0.40;\n" +
                                       "  filter: alpha(opacity=40);\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry3 span.bg\n" +
                                       "{\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\n" +
                                       "  opacity: 0.50;\n" +
                                       "  -moz-opacity: 0.50;\n" +
                                       "  filter: alpha(opacity=50);\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry4 span.bg\n" +
                                       "{\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\n" +
                                       "  opacity: 0.60;\n" +
                                       "  -moz-opacity: 0.60;\n" +
                                       "  filter: alpha(opacity=60);\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry1:hover span.bg\n" +
                                       "{\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\n" +
                                       "  opacity: 0.35;\n" +
                                       "  -moz-opacity: 0.35;\n" +
                                       "  filter: alpha(opacity=35);\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry2:hover span.bg\n" +
                                       "{\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\n" +
                                       "  opacity: 0.45;\n" +
                                       "  -moz-opacity: 0.45;\n" +
                                       "  filter: alpha(opacity=45);\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry3:hover span.bg\n" +
                                       "{\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\n" +
                                       "  opacity: 0.55;\n" +
                                       "  -moz-opacity: 0.55;\n" +
                                       "  filter: alpha(opacity=55);\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.menuentry4:hover span.bg\n" +
                                       "{\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\n" +
                                       "  opacity: 0.65;\n" +
                                       "  -moz-opacity: 0.65;\n" +
                                       "  filter: alpha(opacity=65);\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.selected\n" +
                                       "{\n" +
                                       "  color:#9bd84f;\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.selected span.text\n" +
                                       "{\n" +
                                       "  background:url(\"../imgs/menu_bullet.gif\") no-repeat 4px 7px;\n" +
                                       "  color:#9bd84f;\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation a.selected span.bg,\n" +
                                       "#menu_navigation a.selected:hover span.bg\n" +
                                       "{\n" +
                                       "  /* opacity: always set all 3 values for cross-browser compatibility*/\n" +
                                       "  opacity: 0.65;\n" +
                                       "  -moz-opacity: 0.65;\n" +
                                       "  filter: alpha(opacity=65);\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_navigation span.separator\n" +
                                       "{\n" +
                                       "  display:block;\n" +
                                       "  height:7px;\n" +
                                       "  padding:0px;\n" +
                                       "  line-height:0px;\n" +
                                       "  margin:0px;\n" +
                                       "  font-size:0px;\n" +
                                       "}\n" +
                                       "\n" +
                                       "#menu_phloc_link:link,\n" +
                                       "#menu_phloc_link:visited,\n" +
                                       "#menu_phloc_link:hover,\n" +
                                       "#menu_phloc_link:active\n" +
                                       "{\n" +
                                       "  color:#6aac16;\n" +
                                       "  text-decoration:none;\n" +
                                       "  font-size:10pt;\n" +
                                       "  width:155px;\n" +
                                       "  position:absolute;\n" +
                                       "  bottom:0px;\n" +
                                       "  text-align:center;\n" +
                                       "}\n" +
                                       "\n" +
                                       "#mp3player\n" +
                                       "{\n" +
                                       "filter:progid:DXImageTransform.Microsoft.Alpha(opacity=50);\n" +
                                       "bg:url(\"http://blablabla\");\n" +
                                       "bg:url(../../blablabla.gif);\n" +
                                       "}\n" +
                                       "::-moz-selection {\n" +
                                       "  background-color: #d3e9f7;\n" +
                                       "  color: #114669;\n" +
                                       "}";
  protected static final String CSS2 = "@charset 'utf-8';\n" +
                                       "@import url(../dijit.css);\n" +
                                       "@import url(\"../dijit.css\");\n" +
                                       "@import url(\"../dijit.css\") print;\n" +
                                       "@import url(\"../dijit.css\") print and (min-width : 80px);\n" +
                                       "@import url(../dijit.css) print and (min-width : 80px);\n" +
                                       "@import 'legacy.css';\n" +
                                       "a+b {\n" +
                                       "  border-top: 2px;\n" +
                                       "}\n" +
                                       "@page :Section1\n" +
                                       "    {\n" +
                                       "    size:8.5in 11.0in;\n" +
                                       "    margin:1.0in .75in 1.0in .75in;\n" +
                                       "}\n" +
                                       "@page {\n" +
                                       "  margin: 1in;\n" +
                                       "  size: portrait;\n" +
                                       "  marks: none;\n" +
                                       "}\n" +
                                       "@media print {\n" +
                                       "  /** make sure this fits the page **/\n" +
                                       "  div#container {\n" +
                                       "    width:100%;\n" +
                                       "    min-height:0px;\n" +
                                       "  }\n" +
                                       "}\n" +
                                       "#content input[type=radio],\n" +
                                       "#content input[type=checkbox]\n" +
                                       "{\n" +
                                       "  border:0px !important;\n" +
                                       "  background:none;\n" +
                                       "  \u0077idth:auto !important;\n" +
                                       "}\n" +
                                       "input {}\n" +
                                       "a, p, td{color:red;}\n" +
                                       "\n" +
                                       "#mitte #mitte_links p a[href~=\"/\"]:link:after,\n" +
                                       "#mitte #mitte_links p a[href~=\"/\"]:visited:after {\n" +
                                       "  /* hinter Links URL ausgeben (nur Gecko-Engine); wenn URL mit / anfängt, heise.de davor */\n" +
                                       "  content: \" [http://www.heise.de\" attr(href)\"] \";\n" +
                                       "}\n" +
                                       "@font-face {\n" +
                                       "  font-family: 'Soho';\n" +
                                       "  src: url('../fonts/SohoGothicPro-Regular.eot');\n" +
                                       "  src: local('Soho Gothic Pro'), local('SohoGothicPro-Regular'), url('../fonts/SohoGothicPro-Regular.woff') format('woff'), url('../fonts/SohoGothicPro-Regular.ttf') format('truetype');\n" +
                                       "}\n" +
                                       "\n" +
                                       "@font-face {\n" +
                                       "  font-family: 'SohoMedium';\n" +
                                       "  src: url('../fonts/SohoGothicPro-Medium.eot');\n" +
                                       "  src: local('Soho Gothic Pro Medium'), local('SohoGothicPro-Medium'), url('../fonts/SohoGothicPro-Medium.woff') format('woff'), url('../fonts/SohoGothicPro-Medium.ttf') format('truetype');\n" +
                                       "}\n" +
                                       "\n" +
                                       "\n" +
                                       "@font-face {\n" +
                                       "  font-family: 'SohoLight';\n" +
                                       "  src: url('../fonts/SohoGothicPro-Light.eot');\n" +
                                       "  src: local('Soho Gothic Pro Light'), local('SohoGothicPro-Light'), url('../fonts/SohoGothicPro-Light.woff') format('woff'), url('../fonts/SohoGothicPro-Light.ttf') format('truetype');\n" +
                                       "}\n" +
                                       "\n" +
                                       "@font-face {\n" +
                                       "  font-family: 'Clarendon';\n" +
                                       "  src: url('../fonts/ClareTexReg.eot');\n" +
                                       "  src: local('ClarendonText'), local('ClareTexReg'), url('../fonts/ClareTexReg.woff') format('woff'), url('../fonts/ClareTexReg.ttf') format('truetype');\n" +
                                       "}\n";

  protected static final String CSS3 = "h1 { color : red ; margin: 1px; } h2 { color: rgb(1,2,3);} h3{}" +
                                       " @keyframes x { from { align:left;color:#123;} to { x:y; } 50% {}}" +
                                       " @page {margin: 1in; marks: none; } @page :first {margin: 2in; } @page :last {}" +
                                       " @media print { div { width:100%; min-height:0; }} @media all { div { width:90%; }} @media tv { }" +
                                       "@font-face { font-family: 'Soho'; src: url('Soho.eot'); } @font-face { src: local('Soho Gothic Pro');} @font-face { }";

  protected static final String CSS4 = "@media print { " +
                                       "h1 { color : red ; margin: 1px; } h2 { color: rgb(1,2,3);} h3{}" +
                                       " @keyframes x { from { align:left;color:#123;} to { x:y; } 50% { } }" +
                                       " @page {margin: 1in; marks: none; } @page :first {margin: 2in; }" +
                                       "@font-face { font-family: 'Soho'; src: url('Soho.eot'); } @font-face { src: local('Soho Gothic Pro');} @font-face { }" +
                                       "}";
  protected static final String CSS5 = "h1 { color : red ; margin: 1px; }h2 { color : red ; margin: 1px; }";
  // Special test case for testing the URL visitor
  protected static final String CSS6 = "@page {\n" +
                                       "  content: url(foo2.png);\n" +
                                       "  @top-left-corner { content: ' '; border: solid green; }\n" +
                                       "  @top-right-corner { content: url(foo.png); border: solid green; }\n" +
                                       "  @bottom-right-corner { content: counter(page); border: solid green; }\n" +
                                       "  @bottom-left-corner { content: normal; border: solid green; }\n" +
                                       "}\n" +
                                       "\n" +
                                       "@-moz-keyframes bounce {\n" +
                                       "  from, to  {\n" +
                                       "    bottom: 0;\n" +
                                       "    -moz-animation-timing-function: ease-out;\n" +
                                       "  }\n" +
                                       "  50% {\n" +
                                       "    bottom: 220px;\n" +
                                       "    -moz-animation-timing-function: ease-in;\n" +
                                       "    background: url(../images/email.png) left center no-repeat;\n" +
                                       "  }\n" +
                                       "}\n";
}
