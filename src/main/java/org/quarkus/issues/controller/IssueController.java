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
    Template home;

    @Inject
    Template get_issue_form;

    @Inject
    Template get_issue_result;

    @Inject
    Template get_issues;

    @Inject
    Template add_one_form;

    @Inject
    Template add_one_result;

    @GET
    @Path("home")
    public TemplateInstance home() {
        return home.data("home");
    }

    @GET
    @Path("get_issues")
    @Produces(MediaType.APPLICATION_JSON)
    public TemplateInstance getAll(@QueryParam("user") String user) {
        return get_issues.data("issues", Issue.listAll());
    }

    @GET
    @Path("get_issue")
    @Produces(MediaType.TEXT_PLAIN)
    public TemplateInstance getById(@QueryParam("user") String user) {
        return get_issue_form.data("");
    }

    @POST
    @Path("get_issue/result")
    public TemplateInstance getByIdResult(@FormParam("id") long id) {
        return get_issue_result.data("issue", Issue.findById(id));
    }


    @GET
    @Transactional
    @Path("add_one")
    public TemplateInstance addCustom() {
        return add_one_form.data("");
    }

    @POST
    @Transactional
    @Path("add_one/result")
    public TemplateInstance addCustomResult(@FormParam("issue") String issue, @FormParam("creatorName") String creatorName) {
        Issue newIssue = new Issue(issue, creatorName);
        Issue.persist(newIssue);
        return add_one_result.data("newIssue", newIssue);
    }

/*    @POST
    @Path("add_one")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addIssues(Issue issue, @QueryParam("user") String user) {
        Issue.persist(issue);
        if(issue.isPersistent())
            return Response.created(URI.create("/issues/get_issue/" + issue.id)).build();
        return Response.status(Response.Status.BAD_REQUEST).build();
    }*/

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