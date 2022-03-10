package org.quarkus.issues.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.quarkus.issues.entity.Issue;

@Path("/")
public class IssueController {

    @Inject
    Template get_issue;

    @GET
    @Path("/get_issues")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("user") String user) {
        List<Issue> issues = Issue.listAll();
        return Response.ok(issues).build();
    }

    @GET
    @Path("/get_issue/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public TemplateInstance getById(@PathParam("id") Long id, @QueryParam("user") String user) {
        Issue foundIssue = Issue.findById(id);
/*
            .map(issue -> Response.ok(issue).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());*/
        return get_issue.data("get_issue", foundIssue);
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
    @Path("add_many/{num}")
    @Transactional
    public Response createN(@PathParam("num") int number, @QueryParam("user") String user) {
        List<Issue> issueList = new ArrayList<>();
        for (int n=0; n<number; n++) {
            long count = Issue.count()+n+1;
            issueList.add(new Issue("Issue #" + count, "Creator #" + count));
        }
        Issue.persist(issueList);
        return Response.ok().build();
    }

    @POST
    @Path("add_one")
    @Transactional
    public Response createOne(@QueryParam("user") String user) {
        long count = Issue.count()+1;
        Issue.persist(new Issue("Issue #"+count, "Creator #"+count));
        return Response.ok().build();
    }
}