package org.quarkus.issues.interceptor;

import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpServerRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

@Provider
public class AuthInterceptor implements ContainerRequestFilter {

    @Context
    HttpServerRequest req;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        URLConnection con = new URL("http://localhost:8081/getAuth").openConnection();

        //Checking for quarkus-credentials cookie
        Set<Cookie> cookies = req.cookies("quarkus-credential");

        if (cookies.isEmpty()) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
