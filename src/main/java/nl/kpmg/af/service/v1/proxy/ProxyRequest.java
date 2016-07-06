/*
 * Copyright 2016 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.v1.proxy;

import nl.kpmg.af.service.data.core.Proxy;
import org.glassfish.grizzly.http.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.regex.Pattern;

/**
 * Helper object for passing a request to a proxy backend.
 *
 * @author mhoekstra
 */
public class ProxyRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyRequest.class);

    private static final TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
    };

    private static final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession sslSession) {
            return true;
        }
    };

    private final Request request;
    private final Proxy proxy;
    private boolean recursive = false;


    public ProxyRequest(Request request, Proxy proxy) {
        this.request = request;
        this.proxy = proxy;
        if(isRecursiveCall(request.getRequestURI(), proxy.getTarget())){
            recursive=true;
        }
    }

    public Response execute() {
        if (proxy != null) {
            if (proxy.getMethodsAllowed().contains(request.getMethod().getMethodString())) {

                try {
                    URL url = new URL(proxy.getTarget());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connectionInitialize(connection);
                    connectionDisableSSL(connection);
                    connectionAuthorize(connection);
                    connectionSendContent(connection);

                    return Response
                            .status(connection.getResponseCode())
                            .header("Access-Control-Allow-Origin", "*")
                            .entity(connection.getInputStream())
                            .build();
                } catch (MalformedURLException ex) {
                    LOGGER.error("Proxy defintion contains malformed URL", ex);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                } catch (IOException ex) {
                    if (!proxy.isDisableSSL() && CertificateException.class.isAssignableFrom(ex.getCause().getClass())) {
                        LOGGER.error("Call couldn't be made to proxied service - proxy target possibly has an unsigned "
                                + "certificate, this is avoidable by setting disable_ssl on the proxy object.", ex);
                    } else {
                        LOGGER.error("Call couldn't be made to proxied service", ex);
                    }
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                return Response.status(Response.Status.METHOD_NOT_ALLOWED).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Sets up some basic properties.
     */
    private void connectionInitialize(HttpURLConnection connection) throws ProtocolException {
        connection.setDoOutput(true);
        connection.setRequestMethod(request.getMethod().getMethodString());
        connection.setRequestProperty("User-Agent", "KPMG Proxy Service");
        connection.setRequestProperty("Accept", "*/*");
    }

    /**
     * Disable SSL validation on the connection.
     *
     * This is needed for unsigned SSL certificates.
     *
     * @param connection
     */
    private void connectionDisableSSL(HttpURLConnection connection) {
        if (proxy.isDisableSSL() && HttpsURLConnection.class.isAssignableFrom(connection.getClass())) {
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                ((HttpsURLConnection) connection).setSSLSocketFactory(sc.getSocketFactory());
                ((HttpsURLConnection) connection).setHostnameVerifier(hostnameVerifier);
            } catch (KeyManagementException | NoSuchAlgorithmException ex) {
                LOGGER.warn("Failed to disable SSL for proxied request.", ex);
            }
        }
    }

    /**
     * If an username is set on the proxy object this will be used to Authenticate against the backend.
     *
     * @param connection
     */
    private void connectionAuthorize(HttpURLConnection connection) {
        if (proxy.getUsername() != null) {
            String encoded = DatatypeConverter.printBase64Binary((proxy.getUsername() + ":" + proxy.getPassword()).getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encoded);
        }
    }

    /**
     * If the request contains a body here we try and pass it on.
     * <p>
     * This happens by tying the input to the output.
     */
    private void connectionSendContent(HttpURLConnection connection) throws IOException {
        if (request.getContentLength() > 0) {
            connection.setRequestProperty("Content-Type", request.getContentType());
            try (Reader reader = request.getReader()) {
                OutputStream outputStream = connection.getOutputStream();
                int intValueOfChar;
                while ((intValueOfChar = reader.read()) != -1) {
                    outputStream.write(intValueOfChar);
                }
            }
        } else {
            /* We have to set the Content-Type to something as silly as * / * if we have no content to send. By default
             * text/html is assumed by HttpURLConnection. This will break rest backends that don't expect any input.
             */
            connection.setRequestProperty("Content-Type", "*/*");
        }
    }

    public static boolean isRecursiveCall(String request, String proxy) {
        try {
            URL targetUrl = new URL(proxy);
            URL requestUrl = new URL(request);
           Pattern pattern = Pattern.compile(".*/v\\d+/proxy.*");

            if(requestUrl.getHost().equals(targetUrl.getHost())
                    && requestUrl.getProtocol().equals(targetUrl.getProtocol())
                    && requestUrl.getPort() == targetUrl.getPort()
                    && pattern.matcher(proxy).matches()){
                LOGGER.info("The proxy target is another call to the proxy server. To avoid recursive call this is not permitted");
                return true;
            }
        } catch (MalformedURLException e) {
            LOGGER.info("The targed URL {} is invalid");
            return false;
        }
        return false;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public boolean isRecursive() {
        return recursive;
    }
}
