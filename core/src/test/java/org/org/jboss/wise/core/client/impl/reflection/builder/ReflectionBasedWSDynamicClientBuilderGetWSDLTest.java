package org.jboss.wise.core.client.impl.reflection.builder;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import org.jboss.wise.core.exception.WiseRuntimeException;
import org.junit.Test;
import sun.misc.BASE64Encoder;

public class ReflectionBasedWSDynamicClientBuilderGetWSDLTest {

    private final ReflectionBasedWSDynamicClientBuilder builder = new ReflectionBasedWSDynamicClientBuilder(null);

    @Test
    public void userNameAndPasswordForBasicAuthenticationShouldReturnNullForNullUserOrPassword() throws Exception {
        builder.setUserName(null);
        builder.setPassword("password");
        assertThat(builder.getUserNameAndPasswordForBasicAuthentication(), is((String)null));
        builder.setUserName("user");
        builder.setPassword(null);
        assertThat(builder.getUserNameAndPasswordForBasicAuthentication(), is((String)null));
    }

    @Test
    public void userNameAndPasswordForBasicAuthenticationShouldReturnValidBaseAuthEncoding() throws Exception {
        builder.setUserName("username");
        builder.setPassword("password");
        assertThat(builder.getUserNameAndPasswordForBasicAuthentication(), is("username:password"));

    }

    // @Test
    // public void testTransferWSDL() throws Exception {
    // fail("Not yet implemented");
    // }

    @Test( expected = WiseRuntimeException.class )
    public void getWsdlInputStreamShouldThrowConnectExceptionIfHttpResultIsnt200() throws Exception {
        HttpURLConnection conn = mock(HttpURLConnection.class);
        when(conn.getResponseCode()).thenReturn(401);

        builder.getWsdlInputStream(conn);
    }

    @Test
    public void getWsdlInputStreamShouldReturnInputStreamIfHttpResultIsnt200() throws Exception {
        HttpURLConnection conn = mock(HttpURLConnection.class);
        when(conn.getResponseCode()).thenReturn(200);
        InputStream stream = mock(InputStream.class);
        when(conn.getInputStream()).thenReturn(stream);
        assertThat(builder.getWsdlInputStream(conn), is(stream));
    }

    @Test
    public void initConnectionShouldReturnWellInitializedConnectionWithUserNameAndPassword() throws Exception {
        HttpURLConnection conn = mock(HttpURLConnection.class);
        builder.initConnection("username:password", conn);
        verify(conn).setDoOutput(false);
        verify(conn).setDoInput(true);
        verify(conn).setUseCaches(false);
        verify(conn).setRequestMethod("GET");
        verify(conn).setRequestProperty("Accept",
                                        "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        verify(conn).setRequestProperty("Connection", "close");
        verify(conn).setRequestProperty("Authorization", "Basic " + (new BASE64Encoder()).encode("username:password".getBytes()));

    }

    @Test
    public void initConnectionShouldReturnWellInitializedConnectionWithEmptyUserNameAndPassword() throws Exception {
        HttpURLConnection conn = mock(HttpURLConnection.class);
        builder.initConnection("", conn);
        verify(conn).setDoOutput(false);
        verify(conn).setDoInput(true);
        verify(conn).setUseCaches(false);
        verify(conn).setRequestMethod("GET");
        verify(conn).setRequestProperty("Accept",
                                        "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        verify(conn).setRequestProperty("Connection", "close");

    }

    @Test
    public void initConnectionShouldReturnWellInitializedConnectionWithoutUserNameAndPassword() throws Exception {
        HttpURLConnection conn = mock(HttpURLConnection.class);
        builder.initConnection(null, conn);
        verify(conn).setDoOutput(false);
        verify(conn).setDoInput(true);
        verify(conn).setUseCaches(false);
        verify(conn).setRequestMethod("GET");
        verify(conn).setRequestProperty("Accept",
                                        "text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        verify(conn).setRequestProperty("Connection", "close");

    }

    @Test( expected = WiseRuntimeException.class )
    public void initConnectionShouldThrowWiseRuntimeExceptionWhenGotException() throws Exception {
        HttpURLConnection conn = mock(HttpURLConnection.class);
        doThrow(new ProtocolException()).when(conn).setRequestMethod(anyString());

        builder.initConnection("", conn);

    }
}
