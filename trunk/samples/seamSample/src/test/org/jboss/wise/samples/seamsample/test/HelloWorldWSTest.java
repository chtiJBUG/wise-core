package org.jboss.wise.samples.seamsample.test;

import org.testng.annotations.Test;
import org.jboss.seam.mock.SeamTest;

public class HelloWorldWSTest extends SeamTest {

	@Test
	public void test_helloWorldWS() throws Exception {
		new FacesRequest() {
			@Override
			protected void invokeApplication() {
				//call action methods here
				invokeMethod("#{helloWorldWS.helloWorldWS}");
			}
		}.run();
	}
}
