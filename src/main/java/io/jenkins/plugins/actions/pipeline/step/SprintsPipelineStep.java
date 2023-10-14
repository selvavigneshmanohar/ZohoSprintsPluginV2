package io.jenkins.plugins.actions.pipeline.step;

import io.jenkins.plugins.model.Sprint;

public abstract class SprintsPipelineStep extends PipelineStep {
    public SprintsPipelineStep(String prefix) {
        super(Sprint.getInstance(prefix));
    }

    public SprintsPipelineStep(String prefix, String name, String description, String duration, String startdate,
            String enddate) {
        super(Sprint.getInstance(prefix).setName(name)
                .setDuration(duration)
                .setStartdate(startdate)
                .setEnddate(enddate));
    }

    public SprintsPipelineStep(String prefix, String note) {
        super(Sprint.getInstance(prefix).setNote(note));
    }

    public Sprint getForm() {
        return (Sprint) super.getForm();
    }

    public String getName() {
        return getForm().getName();
    }

    public String getStartdate() {
        return getForm().getStartdate();
    }

    public String getEnddate() {
        return getForm().getEnddate();
    }

    public String getNote() {
        return getForm().getNote();
    }

    public String getDuration() {
        return getForm().getDuration();
    }

    public String getDescription() {
        return getForm().getDescription();
    }
}
