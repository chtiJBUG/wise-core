#!/bin/sh
#The classpath in the script below is generated using the classPath.groovy script and removing stax-api and asm libraries which are already included in the booclassloader of Groovy (2.1.0)
groovy -cp ../../lib/xstream-1.2.2.jar:../../lib/cxf-rt-ws-mex-2.6.4.jar:../../lib/cxf-rt-core-2.6.4.jar:../../lib/cxf-rt-transports-http-2.6.4.jar:../../lib/cxf-xjc-dv-2.6.0.jar:../../lib/commons-logging-1.0.4.jar:../../lib/cxf-rt-frontend-jaxws-2.6.4.jar:../../lib/cxf-rt-transports-local-2.6.4.jar:../../lib/cxf-rt-bindings-coloc-2.6.4.jar:../../lib/cxf-rt-frontend-simple-2.6.4.jar:../../lib/log4j-1.2.16.jar:../../lib/cxf-api-2.6.4.jar:../../lib/jboss-saaj-api_1.3_spec-1.0.1.Final.jar:../../lib/jbossws-spi-2.1.1.Final.jar:../../lib/cxf-rt-databinding-jaxb-2.6.4.jar:../../lib/freemarker-2.3.11.jar:../../lib/cxf-rt-bindings-object-2.6.4.jar:../../lib/wsdl4j-1.6.2.jar:../../lib/jbossws-common-2.1.1.Final.jar:../../lib/cxf-tools-wsdlto-databinding-jaxb-2.6.4.jar:../../lib/jcip-annotations-1.0.jar:../../lib/cxf-rt-bindings-xml-2.6.4.jar:../../lib/commons-lang-2.4.jar:../../lib/cxf-xjc-ts-2.6.0.jar:../../lib/commons-io-1.4.jar:../../lib/neethi-3.0.2.jar:../../lib/commons-httpclient-3.1.jar:../../lib/cxf-rt-ws-addr-2.6.4.jar:../../lib/opencsv-1.8.jar:../../lib/cxf-xjc-boolean-2.6.0.jar:../../lib/javassist-3.10.0.GA.jar:../../lib/cxf-tools-wsdlto-core-2.6.4.jar:../../lib/dtdparser-1.21.jar:../../lib/jboss-jaxb-api_2.2_spec-1.0.3.Final.jar:../../lib/wise-core-2.0.2.Final.jar:../../lib/jbossws-api-1.0.1.Final.jar:../../lib/jboss-jaxws-api_2.2_spec-2.0.0.Final.jar:../../lib/cxf-rt-bindings-soap-2.6.4.jar:../../lib/jbossws-cxf-factories-4.1.1.Final.jar:../../lib/commons-collections-3.2.1.jar:../../lib/commons-codec-1.2.jar:../../lib/jaxb-xjc-2.2.5.jar:../../lib/xpp3_min-1.1.3.4.O.jar:../../lib/xml-resolver-1.2.jar:../../lib/jboss-servlet-api_3.0_spec-1.0.0.Final.jar:../../lib/jbossws-cxf-client-4.1.1.Final.jar:../../lib/cxf-tools-validator-2.6.4.jar:../../lib/cxf-rt-ws-policy-2.6.4.jar:../../lib/milyn-commons-1.2.1.jar:../../lib/milyn-smooks-core-1.2.1.jar:../../lib/jaxb-impl-2.2.5.jar:../../lib/jsr181-api-1.0-MR1.jar:../../lib/xmlschema-core-2.0.3.jar:../../lib/milyn-smooks-validation-1.2.1.jar:../../lib/milyn-smooks-rules-1.2.1.jar:../../lib/milyn-smooks-javabean-1.2.1.jar:../../lib/cxf-tools-wsdlto-frontend-jaxws-2.6.4.jar:../../lib/velocity-1.7.jar:../../lib/mvel2-2.0.9.jar:../../lib/jbossws-common-tools-1.1.0.Final.jar:../../lib/wise-core-cxf-2.0.2.Final.jar:../../lib/cxf-tools-common-2.6.4.jar -d src/client/HelloWorldClient.groovy



