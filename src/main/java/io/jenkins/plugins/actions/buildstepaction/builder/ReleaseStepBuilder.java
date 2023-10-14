package io.jenkins.plugins.actions.buildstepaction.builder;

import io.jenkins.plugins.model.Release;

public abstract class ReleaseStepBuilder extends BuildStep {
    public ReleaseStepBuilder(String prefix, String name, String owners, String goal, String stage, String startdate,
            String enddate, String customFields) {
        super(Release.getInstance(prefix).setName(name)
                .setOwners(owners)
                .setGoal(goal)
                .setStage(stage)
                .setStartdate(startdate)
                .setEnddate(enddate)
                .setCustomFields(customFields));
    }

    public ReleaseStepBuilder(String prefix, String note) {
        super(Release.getInstance(prefix).setNote(note));
    }

    public Release getForm() {
        return (Release) super.getForm();
    }

    public String getName() {
        return getForm().getName();
    }

    public String getGoal() {
        return getForm().getGoal();
    }

    public String getStage() {
        return getForm().getStage();
    }

    public String getStartdate() {
        return getForm().getStartdate();
    }

    public String getEnddate() {
        return getForm().getEnddate();
    }

    public String getCustomFields() {
        return getForm().getCustomFields();
    }

    public String getOwners() {
        return getForm().getOwners();
    }

    public String getNote() {
        return getForm().getNote();
    }
}
