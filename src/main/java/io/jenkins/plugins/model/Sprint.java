package io.jenkins.plugins.model;

public class Sprint {
    private String name, description, startdate, enddate, duration, users, note, durationType;

    public String getDurationType() {
        return durationType;
    }

    public Sprint setDurationType(String durationType) {
        this.durationType = durationType;
        return this;
    }

    public Sprint setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Sprint setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getStartdate() {
        return startdate;
    }

    public Sprint setStartdate(String startdate) {
        this.startdate = startdate;
        return this;
    }

    public String getEnddate() {
        return enddate;
    }

    public Sprint setEnddate(String enddate) {
        this.enddate = enddate;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public Sprint setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getUsers() {
        return users;
    }

    public Sprint setUsers(String users) {
        this.users = users;
        return this;
    }

    public String getNote() {
        return note;
    }

    public Sprint setNote(String note) {
        this.note = note;
        return this;
    }

    public static Sprint getInstance() {
        return new Sprint();
    }

}
