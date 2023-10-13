package io.jenkins.plugins.actions;

import io.jenkins.plugins.model.Sprint;

public abstract class SprintsStepBuilder extends BuildStep {
    public Sprint sprint;

    public SprintsStepBuilder(String prefix, String name, String description, String duration, String startdate,
            String enddate) {
        super(prefix);
        sprint = Sprint.getInstance().setName(name)
                .setDuration(duration)
                .setStartdate(startdate)
                .setEnddate(enddate);
    }

    public SprintsStepBuilder(String prefix, String note) {
        super(prefix);
        sprint = Sprint.getInstance().setNote(note);
    }

    public String getDurationType() {
        return sprint.getDurationType();
    }

    public String getName() {
        return sprint.getName();
    }

    public String getStartdate() {
        return sprint.getStartdate();
    }

    public String getEnddate() {
        return sprint.getEnddate();
    }

    public String getNote() {
        return sprint.getNote();
    }

    public String getDuration() {
        return sprint.getDuration();
    }

    public String getDescription() {
        return sprint.getDescription();
    }
}
