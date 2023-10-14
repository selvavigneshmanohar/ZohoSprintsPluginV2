package io.jenkins.plugins.actions.buildstepaction.builder;

import io.jenkins.plugins.model.Item;

public abstract class ItemStepBuilder extends BuildStep {
    public ItemStepBuilder(String prefix, String note) {
        super(Item.getInstance(prefix).setNote(note));
    }

    public ItemStepBuilder(String prefix, String name, String description, String status, String type, String priority,
            String duration, String assignee, String startdate, String enddate, String customFields) {
        super(Item.getInstance(prefix).setName(name)
                .setDescription(description)
                .setStatus(status)
                .setType(type)
                .setPriority(priority)
                .setDuration(duration)
                .setAssignee(assignee)
                .setStartdate(startdate)
                .setEnddate(enddate)
                .setCustomFields(customFields));
    }

    public Item getForm() {
        return (Item) super.getForm();
    }

    public String getAssignee() {
        return getForm().getAssignee();
    }

    public String getName() {
        return getForm().getName();
    }

    public String getDescription() {
        return getForm().getDescription();
    }

    public String getStatus() {
        return getForm().getStatus();
    }

    public String getType() {
        return getForm().getType();
    }

    public String getPriority() {
        return getForm().getPriority();
    }

    public String getDuration() {
        return getForm().getDuration();
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

    public String getNote() {
        return getForm().getNote();
    }

}