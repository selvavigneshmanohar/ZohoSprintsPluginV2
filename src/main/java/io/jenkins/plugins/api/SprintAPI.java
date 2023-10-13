package io.jenkins.plugins.api;

import static io.jenkins.plugins.util.Util.sprintsLogparser;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.sprints.ZohoClient;
import io.jenkins.plugins.model.Sprint;
import io.jenkins.plugins.sprints.RequestClient;
import net.sf.json.JSONObject;

public class SprintAPI {
    private static final Logger LOGGER = Logger.getLogger(SprintAPI.class.getName());
    private static final Pattern ZS_SPRINT = Pattern.compile("^(P|p)([0-9]+)(#(s|S)([0-9]+))?$");
    private static final String CREATE_SPRINT = "/projects/no-$1/sprints/";
    private static final String SPRINTS_API = "/projects/no-$1/sprints/no-$2/";
    private static final String START_SPRINT_API = SPRINTS_API + "start/";
    private static final String COMPLETE_SPRINT_API = SPRINTS_API + "complete/";
    private static final String ADD_SPRINT_COMMENT_API = SPRINTS_API + "notes/";
    private String comment;
    private TaskListener listener;
    private Run<?, ?> build;
    private Integer projectNumber, sprintNumber;

    public SprintAPI(String prefix, TaskListener listener, Run<?, ?> build) {
        Matcher matcher = ZS_SPRINT.matcher(prefix);
        if (matcher.find()) {
            this.projectNumber = Integer.parseInt(matcher.group(2));
            this.sprintNumber = matcher.group(5) != null ? Integer.parseInt(matcher.group(5)) : null;
        }
        this.build = build;
        this.listener = listener;
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

    private ZohoClient getClient(String api, Map<String, Object> param) throws Exception {
        return new ZohoClient(api, RequestClient.METHOD_POST, param, listener,
                build, projectNumber.toString(), sprintNumber == null ? null : sprintNumber.toString());
    }

    public boolean create(Sprint sprint) {
        if (projectNumber == null) {
            listener.error("Invalid Prefix");
            return false;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("name", sprint.getName());
        if (sprint.getDescription() != null) {
            param.put("description", sprint.getDescription());
        }
        if (sprint.getDuration() != null) {
            param.put("duration", sprint.getDuration());
        }
        if (sprint.getStartdate() != null) {
            param.put("startdate", sprint.getStartdate());
        }
        if (sprint.getEnddate() != null) {
            param.put("enddate", sprint.getEnddate());
        }
        try {
            ZohoClient client = getClient(CREATE_SPRINT, param);
            String response = client.execute();
            if (client.isSuccessRequest()) {
                String message = JSONObject.fromObject(response).optString("message", null);
                if (message == null) {
                    listener.getLogger().println(sprintsLogparser("Sprint added successfully", false));
                    return Boolean.TRUE;
                }
                listener.error(sprintsLogparser(message, true));
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error", e);
        }
        return Boolean.FALSE;
    }

    public boolean update(Sprint sprint) {
        if (isValidPrefixNumbers()) {
            return Boolean.FALSE;
        }
        Map<String, Object> param = new HashMap<>();
        if (sprint.getName() != null) {
            param.put("name", sprint.getName());
        }
        if (sprint.getDescription() != null) {
            param.put("description", sprint.getDescription());
        }
        if (sprint.getDuration() != null) {
            param.put("duration", sprint.getDuration());
        }
        if (sprint.getStartdate() != null) {
            param.put("startdate", sprint.getStartdate());
        }
        if (sprint.getEnddate() != null) {
            param.put("enddate", sprint.getEnddate());
        }
        try {
            ZohoClient client = getClient(SPRINTS_API, param);
            String response = client.execute();
            if (client.isSuccessRequest()) {
                String message = JSONObject.fromObject(response).optString("message", null);
                if (message == null) {
                    listener.getLogger().println(sprintsLogparser("Sprint updated successfully", false));
                    return Boolean.TRUE;
                }
                listener.error(sprintsLogparser(message, true));
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error", e);
        }
        return Boolean.FALSE;
    }

    public boolean start() {
        if (isValidPrefixNumbers()) {
            return Boolean.FALSE;
        }
        try {
            ZohoClient client = getClient(START_SPRINT_API, new HashMap<>());
            client.execute();
            if (client.isSuccessRequest()) {
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
            ZohoClient client = getClient(COMPLETE_SPRINT_API, param);
            String response = client.execute();
            if (client.isSuccessRequest()) {
                JSONObject respObject = JSONObject.fromObject(response);
                if (respObject.has("completedDate")) {
                    listener.getLogger().println(sprintsLogparser("Sprint has been completed successfully", false));
                } else {
                    listener.error(
                            sprintsLogparser("Some items are in open status. So, unable to complete the sprint", true));
                }
                return Boolean.TRUE;
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
            ZohoClient client = getClient(ADD_SPRINT_COMMENT_API, param);
            client.execute();
            if (client.isSuccessRequest()) {
                listener.getLogger().println(sprintsLogparser("Sprint Comment added successfully", false));
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error", e);
        }
        return Boolean.FALSE;
    }
}
