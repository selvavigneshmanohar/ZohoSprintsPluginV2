package io.jenkins.plugins.sprintsdata;

import hudson.EnvVars;
import hudson.Extension;
import hudson.matrix.MatrixRun;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import io.jenkins.plugins.api.ItemAPI;
import io.jenkins.plugins.model.Item;

import java.io.IOException;

import javax.annotation.Nonnull;

@Extension
public class RunTimeListener extends RunListener<Run<?, ?>> {
    @Override
    public void onCompleted(final Run<?, ?> run, @Nonnull final TaskListener listener) {
        try {
            EnvVars envVars = run.getEnvironment(listener);
            Boolean isIssueCreateConfigured = envVars.containsKey("SPRINTS_ISSUE_BUILD_ENVIRONMENT_AVAILABLE");

            if (isIssueCreateConfigured && Result.FAILURE.equals(run.getResult()) && !(run instanceof MatrixRun)) {
                String prefix = envVars.get("SPRINTS_ISSUE_PREFIX");
                String name = envVars.get("SPRINTS_ISSUE_NAME");
                String description = envVars.get("SPRINTS_ISSUE_DESCRIPTION");
                String assignee = envVars.get("SPRINTS_ISSUE_ASSIGNEE");
                String type = envVars.get("SPRINTS_ISSUE_TYPE");
                String status = envVars.get("SPRINTS_ISSUE_STATUS");
                String duration = envVars.get("SPRINTS_ISSUE_DURATION");
                String priority = envVars.get("SPRINTS_ISSUE_PRIORITY");
                String startdate = envVars.get("SPRINTS_ISSUE_STARTDATE");
                String enddate = envVars.get("SPRINTS_ISSUE_ENDDATE");
                String customfield = envVars.get("SPRINTS_ISSUE_CUSTOMFIELD");
                Item item = Item.getInstance().setName(name)
                        .setDescription(description)
                        .setStatus(status)
                        .setType(type)
                        .setPriority(priority)
                        .setDuration(duration)
                        .setAssignee(assignee)
                        .setStartdate(startdate)
                        .setEnddate(enddate)
                        .setCustomFields(customfield);
                new ItemAPI.ItemActionBuilder(prefix, run, listener, item)
                        .build()
                        .create();
            }

        } catch (IOException | InterruptedException e) {
            listener.error("Work Item not created");
        }
    }

}
