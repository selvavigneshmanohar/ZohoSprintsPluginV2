package io.jenkins.plugins.actions;

import io.jenkins.plugins.model.Item;
import io.jenkins.plugins.model.Release;

public abstract class ReleaseStepBuilder extends BuildStep {
    protected Release release;

    public ReleaseStepBuilder(String prefix, String name, String owners, String goal, String stage, String startdate,
            String enddate, String customFields) {
        super(prefix);
        release = Release.getInstance().setName(name)
                .setOwners(owners)
                .setGoal(goal)
                .setStage(stage)
                .setStartdate(startdate)
                .setEnddate(enddate)
                .setCustomFields(customFields);
    }

    public ReleaseStepBuilder(String prefix, String note) {
        super(prefix);
        release = Release.getInstance().setNote(note);
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
