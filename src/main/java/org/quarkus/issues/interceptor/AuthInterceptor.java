package org.quarkus.issues.interceptor;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

@Provider
public class AuthInterceptor implements ContainerRequestFilter {

    @Context
    HttpServerRequest req;
    @Context
    HttpServerResponse res;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String responseBody;
        String username = req.getParam("user");
        System.out.println("Param is: " + username);

        if (username != null) {
            URLConnection con = new URL("http://localhost:8081/isAuth?user=" + username).openConnection();
            InputStream inputStream = con.getInputStream();

            try (Scanner scanner = new Scanner(inputStream)) {
                responseBody = scanner.useDelimiter("\\A").next();
            }

            System.out.println("Responsebody is: " + responseBody);

            if (responseBody.equals("false")) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }
    }
}
