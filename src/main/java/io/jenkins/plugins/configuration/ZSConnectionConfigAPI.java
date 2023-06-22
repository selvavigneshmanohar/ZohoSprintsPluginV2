package io.jenkins.plugins.configuration;

import org.apache.commons.httpclient.HttpStatus;
import org.kohsuke.stapler.WebMethod;
import org.kohsuke.stapler.json.JsonBody;
import org.kohsuke.stapler.json.JsonHttpResponse;
import org.kohsuke.stapler.verb.POST;

import hudson.Extension;
import hudson.model.RootAction;
import net.sf.json.JSONObject;

@Extension
public class ZSConnectionConfigAPI implements RootAction {

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Sprints";
    }

    @Override
    public String getUrlName() {
        return "zohosprints";
    }

    @POST
    @WebMethod(name = "settings")
    public JsonHttpResponse doCreate(@JsonBody ZSConnection configuration) {
        JSONObject response = new JSONObject();
        int statusCode = HttpStatus.SC_CREATED;
        if (!configuration.isValid()) {
            response.put("status", "failed");
            response.put("message", "Mandatory filed(s) are missiong");
            statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
        } else {
            new ZSConnectionConfiguration(configuration).save();
            response.put("status", "success");
        }
        return new JsonHttpResponse(response, statusCode);
    }

    @POST
    @WebMethod(name = "reset")
    public JsonHttpResponse doReset() {
        JSONObject response = new JSONObject();
        new ZSConnectionConfiguration(new ZSConnection()).save();
        response.put("status", "success");
        return new JsonHttpResponse(response, 200);
    }
}
