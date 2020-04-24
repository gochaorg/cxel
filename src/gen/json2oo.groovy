import groovy.json.*

def jsonFile = new File( '/home/user/code/cxel/src/gen/binary-ops.json' )
json = new JsonSlurper().parseText( jsonFile.text )
