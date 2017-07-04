package com.android.technophone;

/**
 * Created by User on 27.06.2017.
 */

import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.ServiceConnectionSE;

import java.io.IOException;

/**
 * Это вспомогательный класс для прохождения пароля
 */
public class HttpTransportBasicAuthSE extends HttpTransportSE {
    private String username;
    private String password;

    /**
     * Constructor with username and password
     *
     *@param url      The url address of the webservice endpoint
     *@param username Username for the Basic Authentication challenge RFC 2617
     *@param password Password for the Basic Authentication challenge RFC 2617
     */
    public HttpTransportBasicAuthSE(String url, String username, String password) {
        super(url); //Это тайм аут в милесекундах
        this.username = username;
        this.password = password;
    }

    public ServiceConnection getServiceConnection() throws IOException {
        ServiceConnection midpConnection = new ServiceConnectionSE(url);
        addBasicAuthentication(midpConnection);
        return midpConnection;
    }

    protected void addBasicAuthentication(ServiceConnection midpConnection) throws IOException {
        if (username != null && password != null) {
            StringBuffer buf = new StringBuffer(username);
            buf.append(':').append(password);
            byte[] raw = buf.toString().getBytes();
            buf.setLength(0);
            buf.append("Basic ");
            org.kobjects.base64.Base64.encode(raw, 0, raw.length, buf);
            midpConnection.setRequestProperty("Authorization", buf.toString());
        }
    }

}
