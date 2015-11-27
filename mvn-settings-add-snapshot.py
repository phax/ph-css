#!/usr/bin/env python
#
# Copyright (C) 2014-2015 Philip Helger (www.helger.com)
# philip[at]helger[dot]com
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

import sys
import os
import os.path
import xml.dom.minidom

# Source and credits:
# https://gist.github.com/neothemachine/4060735

if os.environ["TRAVIS_SECURE_ENV_VARS"] == "false":
  print "no secure env vars available, skipping deployment"
  sys.exit()

homedir = os.path.expanduser("~")

m2 = xml.dom.minidom.parse(homedir + '/.m2/settings.xml')
settings = m2.getElementsByTagName("settings")[0]

serversNodes = settings.getElementsByTagName("servers")
if not serversNodes:
  serversNode = m2.createElement("servers")
  settings.appendChild(serversNode)
else:
  serversNode = serversNodes[0]
  
sonatypeServerNode = m2.createElement("server")

sonatypeServerId = m2.createElement("id")
# See the name "ossrh" in the ph-parent-pom project
# Original name was "sonatype-nexus-snapshots"
idNode = m2.createTextNode("ossrh")
sonatypeServerId.appendChild(idNode)
sonatypeServerNode.appendChild(sonatypeServerId)

sonatypeServerUser = m2.createElement("username")
userNode = m2.createTextNode(os.environ["SONATYPE_USERNAME"])
sonatypeServerUser.appendChild(userNode)
sonatypeServerNode.appendChild(sonatypeServerUser)

sonatypeServerPass = m2.createElement("password")
passNode = m2.createTextNode(os.environ["SONATYPE_PASSWORD"])
sonatypeServerPass.appendChild(passNode)
sonatypeServerNode.appendChild(sonatypeServerPass)

serversNode.appendChild(sonatypeServerNode)
  
m2Str = m2.toxml()
f = open(homedir + '/.m2/snapshot-settings.xml', 'w')
f.write(m2Str)
f.close()
