#!/bin/sh
export CLASSPATH=../lib/commons-io-1.4.jar:../lib/stax-api.jar:../lib/jaxb-xjc.jar:../lib/wstx.jar:../lib/tools.jar:../lib/stax-ex.jar:../lib/jaxb-impl.jar:../lib/jboss-microcontainer.jar:../lib/jboss-dependency.jar:../lib/xercesImpl.jar:../lib/jbossws-native-core.jar:../lib/jbossws-spi.jar:../lib/log4j.jar:../lib/jboss-xml-binding.jar:../lib/jboss-container.jar:../lib/jbossall-client.jar:../lib/commons-lang-2.3.jar:../lib/streambuffer.jar:../lib/commons-codec-1.3.jar:../lib/tools/jaxws-tools.jar:../lib/tools/jaxws-rt.jar:../../build/wise-core.jar:resources/
export JRUBY_PATH=/home/oracle/Desktop/jruby-1.1.6
$JRUBY_PATH/bin/jruby src/client/HelloWorldClient.rb
