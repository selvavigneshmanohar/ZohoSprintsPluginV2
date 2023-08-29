package io.jenkins.plugins.actions;

import io.jenkins.plugins.model.Item;

public class ItemStepBuilder extends BuildStep {
    protected Item item;

    public ItemStepBuilder(String prefix, String note) {
        super(prefix);
        item = Item.getInstance().setNote(note);
    }

    public ItemStepBuilder(String prefix, String name, String description, String status, String type, String priority,
            String duration, String assignee, String startdate, String enddate, String customFields) {
        super(prefix);
        item = Item.getInstance().setName(name)
                .setDescription(description)
                .setStatus(status)
                .setType(type)
                .setPriority(priority)
                .setDuration(duration)
                .setAssignee(assignee)
                .setStartdate(startdate)
                .setEnddate(enddate)
                .setCustomFields(customFields);
    }

    public String getAssignee() {
        return item.getAssignee();
    }

    public String getName() {
        return item.getName();
    }

    public String getDescription() {
        return item.getDescription();
    }

    public String getStatus() {
        return item.getStatus();
    }

    public String getType() {
        return item.getType();
    }

    public String getPriority() {
        return item.getPriority();
    }

    public String getDuration() {
        return item.getDuration();
    }

    public String getStartdate() {
        return item.getStartdate();
    }

    public String getEnddate() {
        return item.getEnddate();
    }

    public String getCustomFields() {
        return item.getCustomFields();
    }

    public String getNote() {
        return item.getNote();
    }

}
