package io.jenkins.plugins.model;

public class Sprint extends BaseModel {
    private String name, description, startdate, enddate, duration, users, note;

    public String getDurationType() {
        return getValue(description);
    }

    public Sprint setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return getValue(name);
    }

    public String getDescription() {
        return getValue(description);
    }

    public Sprint setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getStartdate() {
        return getValue(startdate);
    }

    public Sprint setStartdate(String startdate) {
        this.startdate = startdate;
        return this;
    }

    public String getEnddate() {
        return getValue(enddate);
    }

    public Sprint setEnddate(String enddate) {
        this.enddate = enddate;
        return this;
    }

    public String getDuration() {
        return getValue(duration);
    }

    public Sprint setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getUsers() {
        return getValue(users);
    }

    public Sprint setUsers(String users) {
        this.users = users;
        return this;
    }

    public String getNote() {
        return getValue(note);
    }

    public Sprint setNote(String note) {
        this.note = note;
        return this;
    }

    private Sprint(String prefix) {
        super(prefix);
    }

    public static Sprint getInstance(String prefix) {
        return new Sprint(prefix);
    }

}
