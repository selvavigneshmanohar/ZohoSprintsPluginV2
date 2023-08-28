package io.jenkins.plugins.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.configuration.ZSConnectionConfiguration;
import io.jenkins.plugins.sprints.OAuthClient;
import io.jenkins.plugins.sprints.RequestClient;
import jenkins.model.Jenkins;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author selvavignesh.m
 * @version 1.0
 */
public class Util {

    private static final String PLUGIN_RESOUCE_PATH = "/plugin/zohosprints/";

    public static final Pattern ZS_RELEASE = Pattern.compile("^(P|p)([0-9]+)#(R|r)([0-9]+)$");
    public static final Pattern ZS_SPRINT = Pattern.compile("^(P|p)([0-9]+)#(s|S)([0-9]+)$");
    public static final Pattern ZS_ITEM = Pattern.compile("^(P|p)([0-9]+)#(s|S)([0-9]+)#(i|I)([0-9]+)$");
    public static final Pattern ZS_PROJECT = Pattern.compile("^(P|p)([0-9]+)$");

    public static String replaceEnvVaribaleToValue(final AbstractBuild<?, ?> build, final BuildListener listener,
            final String key) throws IOException, InterruptedException {
        return build.getEnvironment(listener).expand(key);
    }

    public static String replaceEnvVaribaleToValue(Run<?, ?> run, final TaskListener listener,
            final String key) throws IOException, InterruptedException {
        return run.getEnvironment(listener).expand(key);
    }

    /**
     * While write a messge in console Log Zoho Sprints message alone higlighted
     * with Product name
     * 
     * @param message Meesage to parse for Sprints Plugin
     * @return Prepend ZohoSprints in logger
     */
    public static String sprintsLogparser(final String message, final boolean isError) {
        StringBuffer buffer = new StringBuffer("[Zoho Sprints] ");
        if (isError) {
            buffer.append("[ Error ] ");
        }
        return buffer.append(message).toString();
    }

    /**
     * It will get Resourse path of Plugin
     * Path like /plugin/artifect-id/
     * 
     * @return plugin resource path
     */
    public static String getResourcePath() {
        return PLUGIN_RESOUCE_PATH;
    }

    /**
     *
     * @return If authendicated sprints.svg will return or sprints_icon.svg
     */
    public static String getSprintsIconByAuth() {
        return getResourcePath() + "sprints.svg";
    }

    public static JSONArray getZSUserIds(int projectNumber, String mailIds, Run<?, ?> build,
            TaskListener listener)
            throws Exception {
        mailIds = replaceEnvVaribaleToValue(build, listener, mailIds);
        String api = String.format("/projects/no-%s/user/details/", projectNumber);
        Map<String, Object> param = new HashMap<>();
        param.put("action", "projectusers");
        param.put("emailids", JSONArray.fromObject(mailIds.split(",")));
        OAuthClient client = new OAuthClient(api, RequestClient.METHOD_GET, param, listener, build);
        String response = client.execute();
        listener.getLogger().println(response);
        if (response != null) {
            JSONObject responseObj = JSONObject.fromObject(response);
            JSONObject userObj = responseObj.getJSONObject("userObj");

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

        throw new Exception();
    }

    public static ZSConnectionConfiguration getZSConnection() {
        List<ZSConnectionConfiguration> extnList = Jenkins.get().getExtensionList(ZSConnectionConfiguration.class);
        ZSConnectionConfiguration conf = extnList.get(0);
        conf.load();
        return conf;
    }

    public static void setCustomFields(String customFields, JSONObject param, Map<String, Object> queryParam) {
        if (customFields != null && customFields.length() > 0) {
            String[] fields = customFields.split("\n");
            for (String field : fields) {
                String[] fieldArr = field.split("=");
                String key = fieldArr[0];
                String value = fieldArr[1];
                if (param != null) {
                    param.put(key, value);
                }
                if (queryParam != null) {
                    queryParam.put(key, value);
                }
            }
        }
    }
}
