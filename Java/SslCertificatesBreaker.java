package fr.kraiss.scratch.gist;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Pierrick Rassat
 * @see https://github.com/kraiss
 */
public class SslCertificatesBreaker {

    public static void breakCertificates(){
        // !! WARNING !! Use this with caution
        // This breaks all certification. Only use it to target trusted target !

        try {
            // Trust any certificates without validation
            TrustManager[] trustManagerArray = { new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};

            SSLContext sslc = SSLContext.getInstance("TLS");
            sslc.init(null, trustManagerArray, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory());

            // Trust any host name without validation
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
