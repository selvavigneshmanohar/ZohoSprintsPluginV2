package io.jenkins.plugins.api;

import static io.jenkins.plugins.util.Util.getZSUserIds;
import static io.jenkins.plugins.util.Util.replaceEnvVaribaleToValue;
import static io.jenkins.plugins.util.Util.sprintsLogparser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.sprints.OAuthClient;
import io.jenkins.plugins.sprints.RequestClient;
import io.jenkins.plugins.util.Util;
import net.sf.json.JSONArray;

public final class ItemAPI {
    private static final Logger LOGGER = Logger.getLogger(ItemAPI.class.getName());
    public static final Pattern ZS_ADD_ITEM = Pattern.compile("^(P|p)([0-9]+)#(s|S)([0-9]+)$");
    public static final Pattern ZS_ITEM = Pattern.compile("^(P|p)([0-9]+)#(s|S)([0-9]+)#(i|I)([0-9]+)$");
    private String comment, name, description, status, type, priority, duration, startdate, enddate, customFields,
            assignee;
    private JSONArray zsuids = null;
    private TaskListener listener;
    private Run<?, ?> build;
    private Integer projectNumber, sprintNumber, itemNumber;

    private ItemAPI(ItemActionBuilder builder) {
        this.listener = builder.listener;
        this.build = builder.build;
        this.comment = builder.comment;
        this.name = builder.name;
        this.description = builder.description;
        this.status = builder.status;
        this.type = builder.type;
        this.priority = builder.priority;
        this.duration = builder.duration;
        this.startdate = builder.startdate;
        this.enddate = builder.enddate;
        this.customFields = builder.customFields;
        this.projectNumber = builder.projectNumber;
        this.sprintNumber = builder.sprintNumber;
        this.itemNumber = builder.itemNumber;
        this.assignee = builder.assignee;
    }

    public boolean addComment() {
        boolean result = Boolean.FALSE;
        if (projectNumber == null || sprintNumber == null || itemNumber == null) {
            listener.error("Invalid Prefix");
            return result;
        }
        String url = String.format("/projects/no-%s/sprints/no-%s/item/no-%s/notes/", projectNumber, sprintNumber,
                itemNumber);
        Map<String, Object> param = new HashMap<>();
        param.put("name", comment);
        try {
            OAuthClient client = new OAuthClient(url, RequestClient.METHOD_POST, param, listener, build);
            if (client.execute() != null) {
                listener.getLogger().println(sprintsLogparser("Comment added successfully", false));
                result = Boolean.TRUE;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error at add Work Item Comment", e);
        }
        return result;

    }

    public boolean create() {
        boolean result = Boolean.FALSE;
        if (projectNumber == null || sprintNumber == null) {
            listener.error("Invalid Prefix");
            return result;
        }
        if (assignee != null) {
            try {
                this.zsuids = getZSUserIds(projectNumber, assignee, build, listener);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "", e);
                listener.error("Error occure while fetching assignees");
                return result;
            }
        }
        String url = String.format("/projects/no-%s/sprints/no-%s/item/", projectNumber, sprintNumber);
        return execute("additem", url);
    }

    public boolean update() {
        if (projectNumber == null || sprintNumber == null || itemNumber == null) {
            listener.error("Invalid Prefix");
            return Boolean.FALSE;
        }
        String url = String.format("/projects/no-%s/sprints/no-%s/item/no-%s/", projectNumber, sprintNumber,
                itemNumber);
        return execute("updateitem", url);
    }

    private boolean execute(String action, String url) {
        Map<String, Object> param = new HashMap<>();
        boolean isupdate = itemNumber != null;
        param.put("action", action);
        param.put("name", name);
        param.put("projitemtypename", type);
        param.put("duration", duration);
        param.put("description", description);
        param.put("startdate", startdate);
        param.put("enddate", enddate);
        param.put("priorityname", priority);
        param.put("statusname", status);
        param.put("users", zsuids);
        Util.setCustomFields(customFields, null, param);
        try {
            OAuthClient client = new OAuthClient(url, RequestClient.METHOD_POST, param, listener, build);
            String response = client.execute();
            if (response != null) {
                listener.getLogger()
                        .println(sprintsLogparser(isupdate ? "Item fields are updated" : "Item has been added", false));
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
        }

        return Boolean.FALSE;
    }

    public static class ItemActionBuilder {
        private String comment = null, name = null, description = null, status = null, type = null, priority = null,
                duration = null, startdate = null, enddate = null, customFields = null,
                prefix = null, assignee = null;

        private Integer projectNumber, itemNumber, sprintNumber;
        private Run<?, ?> build;
        private TaskListener listener;
        public static final Pattern ZS_ITEM = Pattern.compile("^(P|p)([0-9]+)#(s|S)([0-9]+)(#(i|I)([0-9]+))?");

        public ItemActionBuilder(@Nonnull String prefix, Run<?, ?> build, TaskListener listener)
                throws IOException, InterruptedException {
            withPrefix(replaceEnvVaribaleToValue(build, listener, prefix))
                    .withAbstractBuild(build)
                    .withBuildListener(listener);
        }

        private ItemActionBuilder withBuildListener(TaskListener listener) {
            this.listener = listener;
            return this;
        }

        private ItemActionBuilder withAbstractBuild(Run<?, ?> build) {
            this.build = build;
            return this;
        }

        public ItemActionBuilder withAssignee(String assignee) {
            this.assignee = assignee;
            return this;
        }

        public ItemActionBuilder withName(String name) {

            this.name = name;
            return this;
        }

        public ItemActionBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ItemActionBuilder withStatus(String status) {
            this.status = status;
            return this;
        }

        public ItemActionBuilder withType(String type) {
            this.type = type;
            return this;
        }

        public ItemActionBuilder withPriority(String priority) {
            this.priority = priority;
            return this;
        }

        public ItemActionBuilder withDuration(String duration) {
            this.duration = duration;
            return this;
        }

        public ItemActionBuilder withStartdate(String startdate) {
            this.startdate = startdate;
            return this;
        }

        public ItemActionBuilder withEnddate(String enddate) {
            this.enddate = enddate;
            return this;
        }

        public ItemActionBuilder withCustomFields(String customFields) {
            this.customFields = customFields;
            return this;
        }

        public ItemActionBuilder withPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public ItemActionBuilder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public ItemAPI build() {
            Matcher matcher = ZS_ITEM.matcher(prefix);
            if (matcher.matches()) {
                this.projectNumber = Integer.parseInt(matcher.group(2));
                this.sprintNumber = Integer.parseInt(matcher.group(4));
                this.itemNumber = matcher.group(7) != null ? Integer.parseInt(matcher.group(7)) : null;
            }
            return new ItemAPI(this);
        }

    }

}
