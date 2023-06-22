package io.jenkins.plugins.util;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hudson.ProxyConfiguration;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.sprints.OAuthClient;
import io.jenkins.plugins.sprints.RequestClient;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;

/**
 * @author selvavignesh.m
 * @version 1.0
 */
public class Util {

    private static final Logger LOGGER = Logger.getLogger(Util.class.getName());
    public static final String SPRINTSANDITEMREGEX = "^P[0-9]+#(I|S)[0-9]+(:?,P[0-9]+#(I|S)[0-9]+)*$";
    public static final String PROJECT_REGEX = "^P[0-9]+$";
    public static final String RELEASE_REGEX = "^(P|p)([0-9]+)#(R|r)([0-9]+)$";
    public static final String ADD_ITEM_REGEX = "^P[0-9]+#S[0-9]+(#R[0-9]+)?$";
    public static final String ITEM_REGEX = "(^P[0-9]+#I[0-9]+(?:,P[0-9]+#I[0-9]+)*)$";
    private static final String PLUGIN_RESOUCE_PATH = "/plugin/zohosprints/";
    public static final String MAIL_REGEX = "^([a-zA-Z0-9]([\\w\\-\\.\\+\\']*)@([\\w\\-\\.]*)(\\.[a-zA-Z]{2,20}(\\.[a-zA-Z]{2}){0,2}))$";

    public static final Pattern ZS_RELEASE = Pattern.compile("^(P|p)([0-9]+)#(R|r)([0-9]+)$");
    public static final Pattern ZS_SPRINT = Pattern.compile("^(P|p)([0-9]+)#(s|S)([0-9]+)$");
    public static final Pattern ZS_ITEM = Pattern.compile("^(P|p)([0-9]+)#(s|S)([0-9]+)#(i|I)([0-9]+)$");
    public static final Pattern ZS_PROJECT = Pattern.compile("^(P|p)([0-9]+)$");

    private static boolean isMatch(Pattern pattern, String value) {
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

    public static boolean isItemRegex(String value) {
        return isMatch(ZS_ITEM, value);
    }

    public static boolean isSprintRegex(String value) {
        return isMatch(ZS_SPRINT, value);
    }

    /**
     *
     * @param build    Abstarct build Object of Build
     * @param listener Listener of build
     * @param key      key to be get original Value
     * @return original value of the Key
     */
    public static String expandContent(final AbstractBuild<?, ?> build, final BuildListener listener,
            final String key) {
        String value = null;
        try {
            value = build.getEnvironment(listener).expand(key);
            if (isEmpty(value)) {
                throw new IllegalArgumentException("No Key " + key + " available");
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "", e);
            listener.finished(Result.FAILURE);
        }
        return value;
    }

    public static String replaceEnvVaribaleToValue(final AbstractBuild<?, ?> build, final BuildListener listener,
            final String key) throws IOException, InterruptedException {
        return build != null && listener != null ? build.getEnvironment(listener).expand(key) : key;
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

    /**
     *
     * @return true/false {is Proxy configured in Jenkins}
     */
    public static boolean isProxyConfigured() {
        ProxyConfiguration proxy = Jenkins.getInstance().proxy;
        if (proxy != null) {
            return true;
        }
        return false;
    }

    public static String getZSUserIds(int projectNumber, String mailIds, Run<?, ?> build,
            TaskListener listener)
            throws Exception {
        mailIds = replaceEnvVaribaleToValue(build, listener, mailIds);
        String api = String.format("/projects/no-%s/user/details/", projectNumber);
        Map<String, Object> param = new HashMap<>();
        param.put("action", "projectusers");
        param.put("emailids", mailIds.split(","));
        OAuthClient client = new OAuthClient(api, RequestClient.METHOD_POST, param, listener, build);
        String response = client.execute();
        if (response != null) {
            JSONObject responseObj = JSONObject.fromObject(response);
            JSONObject userObj = responseObj.getJSONObject("userObj");
            Iterator<String> keys = userObj.keys();
            StringBuilder mailIdBuilder = new StringBuilder();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject value = JSONObject.fromObject(userObj.get(key));
                mailIdBuilder = mailIdBuilder.append(value.getString("zsuserId"));
                if (keys.hasNext()) {
                    mailIdBuilder.append(",");
                }
            }
            return mailIdBuilder.toString();
        }
        return null;
    }

}
