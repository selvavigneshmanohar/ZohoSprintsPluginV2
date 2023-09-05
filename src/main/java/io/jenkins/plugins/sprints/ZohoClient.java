package io.jenkins.plugins.sprints;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.configuration.ZSConnectionConfiguration;
import io.jenkins.plugins.util.Util;
import net.sf.json.JSONObject;

public class ZohoClient {
    private static final Logger logger = Logger.getLogger(ZohoClient.class.getName());
    private RequestClient client;
    private TaskListener listener;
    private int responsecode;

    public ZohoClient(String api, String method, Map<String, Object> queryParam, TaskListener listener,
            Run<?, ?> build, String... relativeUrlParams) throws Exception {
        this.listener = listener;
        this.client = new RequestClient(api, method, queryParam, relativeUrlParams)
                .setListener(listener).setBuild(build);
    }

    public ZohoClient(String api, String method, JSONObject body, TaskListener listener,
            Run<?, ?> build, String... relativeUrlParams) throws Exception {
        this.listener = listener;
        this.client = new RequestClient(api, method, body, relativeUrlParams)
                .setListener(listener).setBuild(build);
    }

    public int getResponsecode() {
        return responsecode;
    }

    public boolean isSuccessRequest() {
        return responsecode == HttpServletResponse.SC_OK;
    }

    public String execute() throws Exception {
        isOAuthTokenAvailable();
        String response = client.execute();
        if (isOAuthExpired(response)) {
            generateNewAccessToken();
            response = client.execute();
            responsecode = client.getResponsecode();
        }
        if (responsecode != HttpServletResponse.SC_OK) {
            listener.error(JSONObject.fromObject(response).toString());
        }
        return response;
    }

    private boolean isOAuthExpired(String response) {
        JSONObject respObject = JSONObject.fromObject(response);
        return respObject.optInt("code", 0) == 7601 || client.getResponsecode() == HttpServletResponse.SC_UNAUTHORIZED;
    }

    private void isOAuthTokenAvailable() throws Exception {
        ZSConnectionConfiguration config = Util.getZSConnection();
        if (config.getAccessToken() != null && config.getAccessToken().length() == 0) {
            generateNewAccessToken();
        }
    }

    public static void generateNewAccessToken() throws Exception {
        ZSConnectionConfiguration config = Util.getZSConnection();
        logger.info("New Token method called");
        Map<String, Object> param = new HashMap<>();
        String accessToken = null;
        param.put("grant_type", "refresh_token");
        param.put("client_id", config.getClientId());
        param.put("client_secret", config.getClientSecret());
        param.put("refresh_token", config.getRefreshToken());
        param.put("redirect_uri", config.getRedirectURL());
        RequestClient requestClient = new RequestClient(config.getAccountsDomain() + "/oauth/v2/token",
                RequestClient.METHOD_POST,
                param, null);
        String resp = requestClient.execute();
        if (resp != null && !resp.isEmpty() && resp.startsWith("{")) {
            JSONObject respObj = JSONObject.fromObject(resp);
            if (respObj.has("access_token")) {
                logger.info("New Access token created ");
                accessToken = respObj.getString("access_token");
                config.setAccessToken(accessToken);
                config.save();
                logger.info("New Token generated");
            } else {
                logger.log(Level.INFO, "Error occurred during new access token creation Error - {0}", resp);
            }
        }
    }
}
