package io.jenkins.plugins.model;

public class Release {
    private String name, goal, stage, startdate, enddate, customFields, owners;

    public String getName() {
        return name;
    }

    public Release setName(String name) {
        this.name = name;
        return this;
    }

    public String getGoal() {
        return goal;
    }

    public Release setGoal(String goal) {
        this.goal = goal;
        return this;
    }

    public String getStage() {
        return stage;
    }

    public Release setStage(String stage) {
        this.stage = stage;
        return this;
    }

    public String getStartdate() {
        return startdate;
    }

    public Release setStartdate(String startdate) {
        this.startdate = startdate;
        return this;
    }

    public String getEnddate() {
        return enddate;
    }

    public Release setEnddate(String enddate) {
        this.enddate = enddate;
        return this;
    }

    public String getCustomFields() {
        return customFields;
    }

    public Release setCustomFields(String customFields) {
        this.customFields = customFields;
        return this;
    }

    public String getOwners() {
        return owners;
    }

    public Release setOwners(String owners) {
        this.owners = owners;
        return this;
    }

    public static Release getInstance() {
        return new Release();
    }

}
