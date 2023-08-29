package io.jenkins.plugins.model;

public class Item {
    private String name, description, status, type, priority, duration, startdate, enddate, customFields,
            assignee, note;

    public static Item getInstance() {
        return new Item();
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public Item setDescription(String description) {
        this.description = description;
        return this;
    }

    public Item setStatus(String status) {
        this.status = status;
        return this;
    }

    public Item setType(String type) {
        this.type = type;
        return this;
    }

    public Item setPriority(String priority) {
        this.priority = priority;
        return this;
    }

    public Item setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public Item setStartdate(String startdate) {
        this.startdate = startdate;
        return this;
    }

    public Item setEnddate(String enddate) {
        this.enddate = enddate;
        return this;
    }

    public Item setCustomFields(String customFields) {
        this.customFields = customFields;
        return this;
    }

    public Item setAssignee(String assignee) {
        this.assignee = assignee;
        return this;
    }

    public Item setNote(String note) {
        this.note = note;
        return this;
    }

    public Item() {
    }

    public String getAssignee() {
        return assignee;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getPriority() {
        return priority;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getCustomFields() {
        return customFields;
    }

    public String getNote() {
        return note;
    }
}
