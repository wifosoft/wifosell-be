package com.wifosell.zeus.config.web;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HttpHttpsInterceptor implements HandlerInterceptor {

    // Defined in application.properties file
    @Value(value = "${server.ssl.key-store:}")
    private String sslKeyStore;

    // Defined in application.properties file
    @Value(value = "${server.http.port:80}")
    private int httpPort;

    // Defined in application.properties file
    @Value("${server.port:443}")
    int httpsPort;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // @return http or https
        String schema = request.getScheme();

        // System.out.println("Schema: " + schema);

        if("https".equals(schema)) {
            return true;
        }

        if(sslKeyStore == null || sslKeyStore.isEmpty()) {
            return true;
        }

        String serverName = request.getServerName();
        // System.out.println("Server Name: " + serverName);

        boolean isIP = this.isIP(serverName);
        // System.out.println("isIP: " + isIP);
        if (isIP) {
            // System.out.println("No Redirect isIP = "+ isIP);
            return true;
        }

        int requestedPort = request.getServerPort();
        // System.out.println("requestedPort: " + requestedPort);

        if (requestedPort == httpPort) { // This will still allow requests on :8080
            // System.out.println("Redirect to https");

            String queryString = request.getQueryString();
            if (queryString == null || queryString.isEmpty()) {
                if (httpsPort == 443) {
                    response.sendRedirect(
                            "https://" + request.getServerName() + request.getRequestURI());
                } else {
                    response.sendRedirect(
                            "https://" + request.getServerName() + ":" + httpsPort + request.getRequestURI());
                }
            } else {
                if (httpsPort == 443) {
                    response.sendRedirect(
                            "https://" + request.getServerName() + request.getRequestURI() + "?" + queryString);
                } else {
                    response.sendRedirect(
                            "https://" + request.getServerName()  + ":" + httpsPort + request.getRequestURI() + "?" + queryString);
                }
            }
            return false;
        }
        return true;
    }

    private boolean isIP(String remoteHost) {
        String s = remoteHost.replaceAll("\\.", "");
        // System.out.println("isIP? " + s);
        try {
            Long.parseLong(s);
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }
        return true;
    }

}