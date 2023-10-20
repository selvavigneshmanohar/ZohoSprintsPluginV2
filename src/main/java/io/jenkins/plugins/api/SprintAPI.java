package io.jenkins.plugins.api;

import io.jenkins.plugins.exception.ZSprintsException;
import io.jenkins.plugins.model.Sprint;
import io.jenkins.plugins.sprints.ZohoClient;
import io.jenkins.plugins.util.Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SprintAPI {
    private static final String CREATE_SPRINT_API = "/projects/no-$1/sprints/";
    private static final String UPDATE_SPRINTS_API = "/projects/no-$1/sprints/no-$2/";
    private static final String START_SPRINT_API = UPDATE_SPRINTS_API + "start/";
    private static final String COMPLETE_SPRINT_API = UPDATE_SPRINTS_API + "complete/";
    private static final String ADD_SPRINT_COMMENT_API = UPDATE_SPRINTS_API + "notes/";

    private SprintAPI() {
    }

    public static SprintAPI getInstance() {
        return new SprintAPI();
    }

    private JSONArray getUsers(String mailIds, String projectNumber) throws Exception {
        if (mailIds != null && !mailIds.trim().isEmpty()) {
            return Util.getZSUserIds(projectNumber, mailIds);
        }
        return new JSONArray();
    }

    public String create(Sprint sprint) throws Exception {

        ZohoClient client = new ZohoClient(CREATE_SPRINT_API, ZohoClient.METHOD_POST, sprint.getProjectNumber())
                .addParameter("name", sprint.getName())
                .addParameter("description", sprint.getDescription())
                .addParameter("duration", sprint.getDuration())
                .addParameter("startdate", sprint.getStartdate())
                .addParameter("enddate", sprint.getEnddate());
        JSONArray scrumMasterUserIds = getUsers(sprint.getScrummaster(), sprint.getProjectNumber());
        if (!scrumMasterUserIds.isEmpty()) {
            client.addParameter("scrummaster", "" + scrumMasterUserIds.get(0));
        }
        JSONArray sprintUsers = getUsers(sprint.getUsers(), sprint.getProjectNumber());
        if (!sprintUsers.isEmpty()) {
            client.addParameter("users", sprintUsers);
        }
        String response = client.execute();
        String message = JSONObject.fromObject(response).optString("message", null);
        if (message == null) {
            return "Sprint added successfully";
        }
        throw new ZSprintsException(message);
    }

    public String update(Sprint sprint) throws Exception {
        ZohoClient client = new ZohoClient(UPDATE_SPRINTS_API, ZohoClient.METHOD_POST, sprint.getProjectNumber(),
                sprint.getSprintNumber());
        String response = client.addParameter("name", sprint.getName())
                .addParameter("description", sprint.getDescription())
                .addParameter("duration", sprint.getDuration())
                .addParameter("startdate", sprint.getStartdate())
                .addParameter("enddate", sprint.getEnddate())
                .execute();

        String message = JSONObject.fromObject(response).optString("message", null);
        if (message == null) {
            return "Sprint updated successfully";
        }
        throw new ZSprintsException(message);
    }

    public String start(Sprint sprint) throws Exception {
        new ZohoClient(START_SPRINT_API, ZohoClient.METHOD_POST, sprint.getProjectNumber(),
                sprint.getSprintNumber()).execute();
        return "Sprint has been started successfully";
    }

    public String complete(Sprint sprint) throws Exception {
        String response = new ZohoClient(COMPLETE_SPRINT_API, ZohoClient.METHOD_POST, sprint.getProjectNumber(),
                sprint.getSprintNumber())
                .addParameter("action", "complete")
                .execute();
        int inProgressItemCount = JSONObject.fromObject(response).optInt("allItemCount", 0);
        if (inProgressItemCount == 0) {
            return "Sprint has been completed successfully";
        }
        throw new ZSprintsException("Unable to complete Sprint");

    }

    public String addComment(Sprint sprint) throws Exception {
        new ZohoClient(ADD_SPRINT_COMMENT_API, ZohoClient.METHOD_POST, sprint.getProjectNumber(),
                sprint.getSprintNumber())
                .addParameter("name", sprint.getNote())
                .execute();
        return "Sprint Comment added successfully";
    }
}
