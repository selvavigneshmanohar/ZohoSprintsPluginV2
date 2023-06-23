package io.jenkins.plugins.api;

import static io.jenkins.plugins.util.Util.getZSUserIds;
import static io.jenkins.plugins.util.Util.replaceEnvVaribaleToValue;
import static io.jenkins.plugins.util.Util.sprintsLogparser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.sprints.OAuthClient;
import io.jenkins.plugins.sprints.RequestClient;
import io.jenkins.plugins.util.Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public final class ReleaseAPI {
    private static final Logger LOGGER = Logger.getLogger(ReleaseAPI.class.getName());
    private String api, name, owners, goal, stage, startdate, enddate, customFields;
    private JSONArray ownerIds = null;
    private Integer projectNumber, releaseNumber;
    private Run<?, ?> build;
    private TaskListener listener;

    private ReleaseAPI(ReleaseAPIBuilder builder) {
        this.name = builder.name;
        this.owners = builder.owners;
        this.goal = builder.goal;
        this.stage = builder.stage;
        this.startdate = builder.startdate;
        this.enddate = builder.enddate;
        this.customFields = builder.customFields;
        this.projectNumber = builder.projectNumber;
        this.releaseNumber = builder.releaseNumber;
        this.build = builder.build;
        this.listener = builder.listener;
        this.api = String.format("/projects/no-%s/release/", builder.projectNumber);
        if (builder.releaseNumber != null) {
            this.api += String.format("%sno-%s/update/", this.api, builder.releaseNumber);
        }

    }

    private String envReplacer(String key) throws IOException, InterruptedException {
        return replaceEnvVaribaleToValue(build, listener, key);
    }

    public boolean create() throws IOException, InterruptedException {
        boolean result = Boolean.FALSE;
        if (projectNumber == null) {
            listener.error("Invalid Prefix");
            return result;
        }
        if (owners != null) {
            try {
                this.ownerIds = getZSUserIds(projectNumber, envReplacer(owners), build, listener);
            } catch (Exception e) {
                listener.error("Error occure while fetching assignees");
                return result;
            }
        }
        JSONObject param = new JSONObject();
        param.put("name", envReplacer(name));
        param.put("startdate", envReplacer(startdate));
        param.put("enddate", envReplacer(enddate));
        param.put("statusName", envReplacer(stage));
        param.put("goal", envReplacer(goal));
        param.put("ownerIds", (ownerIds == null | ownerIds.isEmpty()) ? null : ownerIds);
        return execute(param);
    }

    private boolean emptyCheck(String value) {
        return value != null && value.length() > 0;
    }

    public boolean update() throws IOException, InterruptedException {
        if (projectNumber == null || releaseNumber == null) {
            listener.error("Invalid Prefix");
            return Boolean.FALSE;
        }
        JSONObject param = new JSONObject();
        if (emptyCheck(name)) {
            param.put("name", envReplacer(name));
        }
        if (emptyCheck(startdate)) {
            param.put("startdate", envReplacer(startdate));
        }
        if (emptyCheck(enddate)) {
            param.put("enddate", envReplacer(enddate));
        }
        if (emptyCheck(stage)) {
            param.put("statusName", envReplacer(stage));
        }
        if (emptyCheck(goal)) {
            param.put("goal", envReplacer(goal));
        }
        if (ownerIds != null && !ownerIds.isEmpty()) {
            param.put("ownerIds", ownerIds);
        }

        return execute(param);
    }

    private boolean execute(JSONObject param) throws IOException, InterruptedException {
        Util.setCustomFields(customFields, param, null);
        try {
            OAuthClient client = new OAuthClient(api, RequestClient.METHOD_POST, param, listener, build);
            if (client.execute() != null) {
                listener.getLogger().println(sprintsLogparser(
                        releaseNumber != null ? "Release has been updated" : "Release has been added", false));
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error", e);
        }
        return Boolean.FALSE;
    }

    public static class ReleaseAPIBuilder {
        private String prefix, name, owners, goal, stage, startdate, enddate, customFields;
        private Integer projectNumber, releaseNumber;
        private Run<?, ?> build;
        private TaskListener listener;
        public static final Pattern ZS_RELEASE = Pattern.compile("^(P|p)([0-9]+)(#(r|IR)([0-9]+))?$");

        private String getEnvVaribaleToValue(String key) throws IOException, InterruptedException {
            return replaceEnvVaribaleToValue(build, listener, key);
        }

        public ReleaseAPIBuilder(String prefix, Run<?, ?> build, TaskListener listener)
                throws IOException, InterruptedException {
            withListener(listener).withBuild(build)
                    .withPrefix(replaceEnvVaribaleToValue(build, listener, prefix));

        }

        public String getPrefix() {
            return prefix;
        }

        public ReleaseAPIBuilder withPrefix(String value) throws IOException, InterruptedException {
            this.prefix = getEnvVaribaleToValue(value);
            return this;
        }

        public String getName() {
            return name;
        }

        public ReleaseAPIBuilder withName(String value) throws IOException, InterruptedException {
            this.name = getEnvVaribaleToValue(value);
            return this;
        }

        public String getOwners() {
            return owners;
        }

        public ReleaseAPIBuilder withOwners(String value) throws IOException, InterruptedException {
            this.owners = getEnvVaribaleToValue(value);
            return this;
        }

        public String getGoal() {
            return goal;
        }

        public ReleaseAPIBuilder withGoal(String value) throws IOException, InterruptedException {
            this.goal = getEnvVaribaleToValue(value);
            return this;
        }

        public String getStage() {
            return stage;
        }

        public ReleaseAPIBuilder withStage(String value) throws IOException, InterruptedException {
            this.stage = getEnvVaribaleToValue(value);
            return this;
        }

        public String getStartdate() {
            return startdate;
        }

        public ReleaseAPIBuilder withStartdate(String value) throws IOException, InterruptedException {
            this.startdate = getEnvVaribaleToValue(value);
            return this;
        }

        public String getEnddate() {
            return enddate;
        }

        public ReleaseAPIBuilder withEnddate(String value) throws IOException, InterruptedException {
            this.enddate = getEnvVaribaleToValue(value);
            return this;
        }

        public String getCustomFields() {
            return customFields;
        }

        public ReleaseAPIBuilder withCustomFields(String value) throws IOException, InterruptedException {
            this.customFields = getEnvVaribaleToValue(value);
            return this;
        }

        public Integer getProjectNo() {
            return projectNumber;
        }

        public Integer getReleaseNo() {
            return releaseNumber;
        }

        public Run<?, ?> getBuild() {
            return build;
        }

        private ReleaseAPIBuilder withBuild(Run<?, ?> build) {
            this.build = build;
            return this;
        }

        public TaskListener getListener() {
            return listener;
        }

        private ReleaseAPIBuilder withListener(TaskListener listener) {
            this.listener = listener;
            return this;
        }

        public ReleaseAPI build() {
            Matcher matcher = ZS_RELEASE.matcher(prefix);
            if (matcher.matches()) {
                this.projectNumber = Integer.parseInt(matcher.group(2));
                this.releaseNumber = Integer.parseInt(matcher.group(4));
            }
            return new ReleaseAPI(this);
        }
    }
}
