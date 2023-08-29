package io.jenkins.plugins.actions;

import org.jenkinsci.plugins.workflow.steps.Step;

import io.jenkins.plugins.model.Release;

public abstract class ReleasePipelineStepBuilder extends Step {
    protected Release release;
    protected String prefix;

    public ReleasePipelineStepBuilder(String prefix, String name, String owners, String goal, String stage,
            String startdate,
            String enddate, String customFields) {
        this.prefix = prefix;
        release = Release.getInstance().setName(name)
                .setOwners(owners)
                .setGoal(goal)
                .setStage(stage)
                .setStartdate(startdate)
                .setEnddate(enddate)
                .setCustomFields(customFields);
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return release.getName();
    }

    public String getGoal() {
        return release.getGoal();
    }

    public String getStage() {
        return release.getStage();
    }

    public String getStartdate() {
        return release.getStartdate();
    }

    public String getEnddate() {
        return release.getEnddate();
    }

    public String getCustomFields() {
        return release.getCustomFields();
    }

    public String getOwners() {
        return release.getOwners();
    }
}
