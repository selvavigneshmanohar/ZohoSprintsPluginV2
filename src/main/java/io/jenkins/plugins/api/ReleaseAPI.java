package io.jenkins.plugins.api;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.sprints.OAuthClient;
import io.jenkins.plugins.sprints.RequestClient;
import net.sf.json.JSONObject;
import static io.jenkins.plugins.util.Util.getZSUserIds;
import static io.jenkins.plugins.util.Util.replaceEnvVaribaleToValue;
import static io.jenkins.plugins.util.Util.sprintsLogparser;
import static org.apache.commons.lang.StringUtils.isEmpty;

public final class ReleaseAPI {
    private String api, name, owners, goal, stage, startdate, enddate, customFields;
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
            this.api += String.format("no-%s/update/", builder.releaseNumber);
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
                this.owners = getZSUserIds(projectNumber, owners, build, listener);
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
        param.put("ownerIds", owners == null ? owners : owners.split(","));
        setCustomParam(param);
        try {
            OAuthClient client = new OAuthClient(api, RequestClient.METHOD_POST, param, listener, build);
            if (client.execute() != null) {
                listener.getLogger().println(sprintsLogparser("Release has been added", false));
                result = Boolean.TRUE;
            }
        } catch (Exception e) {
        }

        return result;
    }

    public boolean update() throws IOException, InterruptedException {
        boolean result = Boolean.FALSE;
        if (projectNumber == null || releaseNumber == null) {
            listener.error("Invalid Prefix");
            return result;
        }
        JSONObject param = new JSONObject();
        param.put("name", envReplacer(name));
        param.put("startdate", envReplacer(startdate));
        param.put("enddate", envReplacer(enddate));
        param.put("statusName", envReplacer(stage));
        param.put("goal", envReplacer(goal));
        setCustomParam(param);
        try {
            OAuthClient client = new OAuthClient(api, RequestClient.METHOD_POST, param, listener, build);
            if (client.execute() != null) {
                listener.getLogger().println(sprintsLogparser("Release has been updated", false));
                result = Boolean.TRUE;
            }
        } catch (Exception e) {

        }

        return result;
    }

    private void setCustomParam(JSONObject param) {
        if (!isEmpty(customFields)) {
            String[] fields = customFields.split("\n");
            for (String field : fields) {
                String[] fieldArr = field.split("=");
                param.put(fieldArr[0], fieldArr[1]);
            }
        }
    }

    public static class ReleaseAPIBuilder {
        private String prefix, name, owners, goal, stage, startdate, enddate, customFields;
        private Integer projectNumber, releaseNumber;
        private Run<?, ?> build;
        private TaskListener listener;
        public static final Pattern ZS_RELEASE = Pattern.compile("^(P|p)([0-9]+)(#(r|IR)([0-9]+))?$");

        public ReleaseAPIBuilder(String prefix, Run<?, ?> build, TaskListener listener)
                throws IOException, InterruptedException {
            withPrefix(replaceEnvVaribaleToValue(build, listener, prefix))
                    .withListener(listener).withBuild(build);
        }

        public String getPrefix() {
            return prefix;
        }

        public ReleaseAPIBuilder withPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public String getName() {
            return name;
        }

        public ReleaseAPIBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public String getOwners() {
            return owners;
        }

        public ReleaseAPIBuilder withOwners(String owners) {
            this.owners = owners;
            return this;
        }

        public String getGoal() {
            return goal;
        }

        public ReleaseAPIBuilder withGoal(String goal) {
            this.goal = goal;
            return this;
        }

        public String getStage() {
            return stage;
        }

        public ReleaseAPIBuilder withStage(String stage) {
            this.stage = stage;
            return this;
        }

        public String getStartdate() {
            return startdate;
        }

        public ReleaseAPIBuilder withStartdate(String startdate) {
            this.startdate = startdate;
            return this;
        }

        public String getEnddate() {
            return enddate;
        }

        public ReleaseAPIBuilder withEnddate(String enddate) {
            this.enddate = enddate;
            return this;
        }

        public String getCustomFields() {
            return customFields;
        }

        public ReleaseAPIBuilder withCustomFields(String customFields) {
            this.customFields = customFields;
            return this;
        }

        public Integer getProjectNo() {
            return projectNumber;
        }

        public ReleaseAPIBuilder withProjectNo(Integer projectNo) {
            this.projectNumber = projectNo;
            return this;
        }

        public Integer getReleaseNo() {
            return releaseNumber;
        }

        public ReleaseAPIBuilder withReleaseNo(Integer releaseNo) {
            this.releaseNumber = releaseNo;
            return this;
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
            if (matcher.find()) {
                this.projectNumber = Integer.parseInt(matcher.group(2));
                this.releaseNumber = Integer.parseInt(matcher.group(4));
            }
            return new ReleaseAPI(this);
        }
    }
}
