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

    @Inject
    Template create_one_result;

    @Inject
    Template create_N_form;

    @Inject
    Template create_N_result;

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

    @GET
    @Transactional
    @Path("create_N")
    public TemplateInstance createN() {
        return create_N_form.data("");
    }

    @POST
    @Transactional
    @Path("create_N/result")
    public TemplateInstance createNResult(@FormParam("num") int num) {
        List<Issue> issueList = new ArrayList<>();
        long count = Issue.count();

        for (int i=0; i<num; i++) {
            count += 1;
            issueList.add(new Issue("Issue #" + count, "Creator #" + count));
        }
        Issue.persist(issueList);
        return create_N_result.data("issues", issueList);
    }

    @POST
    @Path("create_one")
    @Transactional
    public TemplateInstance createOne(@QueryParam("user") String user) {
        long count = Issue.count()+1;
        Issue newIssue = new Issue("Issue #"+count, "Creator #"+count);
        Issue.persist(newIssue);
        return create_one_result.data("newIssue", newIssue);
    }
}