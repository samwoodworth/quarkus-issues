package org.quarkus.issues.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.quarkus.issues.entity.Issue;

@Path("/issues")
public class IssueController {

    @GET
    @Path("/get_issues")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("user") String user) {
        List<Issue> issues = Issue.listAll();
        return Response.ok(issues).build();
    }

    @GET
    @Path("/get_issue/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id, @QueryParam("user") String user) {
        return Issue.findByIdOptional(id)
            .map(issue -> Response.ok(issue).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/add_one")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addIssues(Issue issue, @QueryParam("user") String user) {
        Issue.persist(issue);
        if(issue.isPersistent())
            return Response.created(URI.create("/issues/get_issue/" + issue.id)).build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Path("add_one")
    @PermitAll
    @Transactional
    public Response createOne(@QueryParam("user") String user) {

        long count = Issue.count()+1;
        Issue.persist(new Issue("Issue #"+count, "Creator #"+count));
        return Response.created(URI.create("/issues/get_issue/" + count)).build();
    }

    @POST
    @Path("add_many/{num}")
    @PermitAll
    @Transactional
    public Response createN(@PathParam("num") int number, @QueryParam("user") String user) {
        List<Issue> userList = new ArrayList<>();
        System.out.println(Issue.count());
        for (int n=0; n<number; n++) {
            long count = Issue.count()+n+1;
            userList.add(new Issue("Issue #" + count, "Creator #" + count));
        }
        Issue.persist(userList);
        return Response.ok().build();
    }
}