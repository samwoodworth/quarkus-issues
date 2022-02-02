package org.quarkus.issues.controller;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.quarkus.issues.entity.Issue;

@Path("/issues")
public class IssueController {

    @GET
    @Path("/get_issues")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Issue> issues = Issue.listAll();
        return Response.ok(issues).build();
    }

    @GET
    @Path("/get_issue/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return Issue.findByIdOptional(id)
            .map(issue -> Response.ok(issue).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/insert_issue")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Issue issue) {
        Issue.persist(issue);
        if(issue.isPersistent()) {
            return Response.created(URI.create("/issues" + issue.id)).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}