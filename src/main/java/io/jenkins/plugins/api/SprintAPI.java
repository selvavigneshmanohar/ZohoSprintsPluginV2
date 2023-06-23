package io.jenkins.plugins.api;

import static io.jenkins.plugins.util.Util.replaceEnvVaribaleToValue;
import static io.jenkins.plugins.util.Util.sprintsLogparser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.sprints.OAuthClient;
import io.jenkins.plugins.sprints.RequestClient;
import net.sf.json.JSONObject;

public class SprintAPI {
    private static final Logger LOGGER = Logger.getLogger(SprintAPI.class.getName());
    private static final Pattern ZS_SPRINT = Pattern.compile("^(P|p)([0-9]+)#(s|S)([0-9]+)$");
    private String comment;
    private String api;
    private TaskListener listener;
    private Run<?, ?> build;
    private Integer projectNumber, sprintNumber;

    public SprintAPI(String prefix, TaskListener listener, Run<?, ?> build) {
        Matcher matcher = ZS_SPRINT.matcher(prefix);
        if (matcher.find()) {
            this.projectNumber = Integer.parseInt(matcher.group(2));
            this.sprintNumber = Integer.parseInt(matcher.group(4));
        }
        this.build = build;
        this.listener = listener;
        this.api = String.format("/projects/no-%s/sprints/no-%s/", this.projectNumber, this.sprintNumber);
    }

    public SprintAPI withComment(String comment) {
        this.comment = comment;
        return this;
    }

    private boolean isValidPrefixNumbers() {
        boolean isNotValid = projectNumber == null || sprintNumber == null || sprintNumber <= 0;
        if (isNotValid) {
            listener.error("Invalid Prefix");
        }
        return isNotValid;
    }

    public boolean start() {
        if (isValidPrefixNumbers()) {
            return Boolean.FALSE;
        }
        try {
            OAuthClient client = new OAuthClient(api + "start/", RequestClient.METHOD_POST, new HashMap<>(), listener,
                    build);
            if (client.execute() != null) {
                listener.getLogger().println(sprintsLogparser("Sprint has been started successfully", false));
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error", e);
        }
        return Boolean.FALSE;

    }

    public boolean complete() {
        if (isValidPrefixNumbers()) {
            return Boolean.FALSE;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("action", "complete");
        try {
            OAuthClient client = new OAuthClient(api + "complete/", RequestClient.METHOD_POST, param, listener,
                    build);
            String response = client.execute();
            if (response != null) {
                JSONObject respObject = JSONObject.fromObject(response);
                if (respObject.has("completedDate")) {
                    listener.getLogger().println(sprintsLogparser("Sprint has been completed successfully", false));
                } else {
                    listener.error(
                            sprintsLogparser("Some items are in open status. So, unable to complete the sprint", true));
                    return Boolean.TRUE;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error", e);
        }
        return Boolean.FALSE;

    }

    public boolean addComment() {
        if (isValidPrefixNumbers()) {
            return Boolean.FALSE;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("name", this.comment);
        try {
            OAuthClient client = new OAuthClient(api + "notes/", RequestClient.METHOD_POST, param, listener,
                    build);
            if (client.execute() != null) {
                listener.getLogger().println(sprintsLogparser("Comment added successfully", false));
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error", e);
        }
        return Boolean.FALSE;
    }
}
