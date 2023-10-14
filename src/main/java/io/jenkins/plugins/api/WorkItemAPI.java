package io.jenkins.plugins.api;

import io.jenkins.plugins.exception.ZSprintsException;
import io.jenkins.plugins.model.Item;
import io.jenkins.plugins.sprints.RequestClient;
import io.jenkins.plugins.sprints.ZohoClient;
import io.jenkins.plugins.util.Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WorkItemAPI {
    private static final String ADD_ITEM_COMMENT_API = "/projects/no-$1/sprints/no-$2/item/no-$3/notes/";
    private static final String ADD_ITEM_API = "/projects/no-$1/sprints/no-$2/item/";
    private static final String UPDATE_ITEM_API = "/projects/no-$1/sprints/no-$2/item/no-$3/";

    private WorkItemAPI() {

    }

    public static WorkItemAPI getInstance() {
        return new WorkItemAPI();
    }

    public String addComment(Item item) throws Exception {
        new ZohoClient(ADD_ITEM_COMMENT_API, RequestClient.METHOD_POST,
                item.getProjectNumber(),
                item.getSprintNumber(),
                item.getItemNumber())
                .addParameter("name", item.getNote())
                .execute();
        return "Work Item Comment added successfully";
    }

    public String addItem(Item item) throws Exception {
        JSONArray assigneeIds = null;
        String assignee = item.getAssignee();
        if (assignee != null && !assignee.trim().isEmpty()) {
            assigneeIds = Util.getZSUserIds(item.getProjectNumber(), assignee);
        }
        ZohoClient client = new ZohoClient(ADD_ITEM_API, RequestClient.METHOD_POST, item.getProjectNumber(),
                item.getSprintNumber())
                .addParameter("action", "additem")
                .addParameter("assignee", assigneeIds);
        return addOrupdateItem(item, client, "Item has been added");
    }

    public String updateItem(Item item) throws Exception {
        ZohoClient client = new ZohoClient(UPDATE_ITEM_API, RequestClient.METHOD_POST, item.getProjectNumber(),
                item.getSprintNumber(),
                item.getItemNumber())
                .addParameter("action", "updateitem");
        return addOrupdateItem(item, client, "Item fields are updated");
    }

    private String addOrupdateItem(Item item, ZohoClient client, String successMessage) throws Exception {
        client.addParameter("name", item.getName())
                .addParameter("projitemtypename", item.getType())
                .addParameter("duration", item.getDuration())
                .addParameter("description", item.getDescription())
                .addParameter("startdate", item.getStartdate())
                .addParameter("enddate", item.getEnddate())
                .addParameter("priorityname", item.getPriority())
                .addParameter("statusname", item.getStatus());
        Util.setCustomFields(item.getCustomFields(), client);
        String response = client.execute();
        String message = JSONObject.fromObject(response).optString("message", null);
        if (message == null) {
            return successMessage;
        }
        throw new ZSprintsException(message);
    }
}
