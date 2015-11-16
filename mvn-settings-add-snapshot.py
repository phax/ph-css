#!/usr/bin/env python
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
