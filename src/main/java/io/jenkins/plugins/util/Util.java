package io.jenkins.plugins.util;

import java.util.Iterator;
import java.util.List;

import io.jenkins.plugins.configuration.ZSConnectionConfiguration;
import io.jenkins.plugins.sprints.RequestClient;
import io.jenkins.plugins.sprints.ZohoClient;
import jenkins.model.Jenkins;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Util {
    private static final String GET_PROJECT_USER_API = "/projects/no-$1/user/details/";

    public static JSONArray getZSUserIds(String projectNumber, String mailIds)
            throws Exception {
        ZohoClient client = new ZohoClient(GET_PROJECT_USER_API, RequestClient.METHOD_GET, projectNumber)
                .addParameter("action", "projectusers")
                .addParameter("emailids", JSONArray.fromObject(mailIds.split(",")));
        String response = client.execute();

        JSONObject userObj = JSONObject.fromObject(response)
                .getJSONObject("userObj");

        Iterator<String> keys = userObj.keys();
        Object[] users = new Object[userObj.size()];
        int counter = 0;
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject value = JSONObject.fromObject(userObj.get(key));
            users[counter++] = value.get("zsuserId");
        }
        return JSONArray.fromObject(users);
    }

    public static ZSConnectionConfiguration getZSConnection() {
        List<ZSConnectionConfiguration> extnList = Jenkins.get().getExtensionList(ZSConnectionConfiguration.class);
        ZSConnectionConfiguration conf = extnList.get(0);
        conf.load();
        return conf;
    }

    public static void setCustomFields(String customFields, ZohoClient client) {
        if (customFields != null && customFields.length() > 0) {
            String[] fields = customFields.split("\n");
            for (String field : fields) {
                String[] fieldArr = field.split("=");
                String key = fieldArr[0];
                String value = fieldArr[1];
                client.addParameter(key, value);
            }
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
