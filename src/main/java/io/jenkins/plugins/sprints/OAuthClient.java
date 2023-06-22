package io.jenkins.plugins.sprints;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import hudson.model.Run;
import hudson.model.TaskListener;
import net.sf.json.JSONObject;
import io.jenkins.plugins.configuration.ZSConnectionConfiguration;
import jenkins.model.Jenkins;

public class OAuthClient {
    private static final Logger logger = Logger.getLogger(OAuthClient.class.getName());
    private RequestClient client;
    private TaskListener listener;

    public OAuthClient(String api, String method, Map<String, Object> queryParam, TaskListener listener,
            Run<?, ?> build) throws Exception {
        this.listener = listener;
        this.client = new RequestClient(api, method, queryParam)
                .setListener(listener).setBuild(build);
    }

    public OAuthClient(String api, String method, JSONObject body, TaskListener listener,
            Run<?, ?> build) throws Exception {
        this.listener = listener;
        this.client = new RequestClient(api, method, body)
                .setListener(listener).setBuild(build);
    }

    public String execute() throws Exception {
        String response = client.execute();
        if (isOAuthExpired()) {
            generateNewAccessToken();
            response = client.execute();
        }
        JSONObject respObject = JSONObject.fromObject(response);
        boolean isFailure = respObject.optString("status", "failure").toLowerCase().equals("success");
        if (isFailure) {
            listener.error(respObject.getString("message"));
            response = null;
        }
        return response;
    }

    private boolean isOAuthExpired() {
        return client.getResponsecode() == HttpServletResponse.SC_UNAUTHORIZED;
    }

    private void generateNewAccessToken() throws Exception {
        List<ZSConnectionConfiguration> extnList = Jenkins.getInstance()
                .getExtensionList(ZSConnectionConfiguration.class);
        ZSConnectionConfiguration config = extnList.get(0);
        logger.info("New Access token method called");
        Map<String, Object> param = new HashMap<>();
        String accessToken = null;
        param.put("grant_type", "refresh_token");
        param.put("client_id", config.getClientId());
        param.put("client_secret", config.getClientSecret());
        param.put("refresh_token", config.getRefreshToken());
        param.put("redirect_uri", config.getRedirectURL());

        RequestClient requestClient = new RequestClient(config.getAccountsDomain() + "/oauth/v2/token",
                RequestClient.METHOD_POST,
                param);
        String resp = requestClient.execute();
        if (resp != null && !resp.isEmpty() && resp.startsWith("{")) {
            JSONObject respObj = JSONObject.fromObject(resp);
            if (respObj.has("access_token")) {
                logger.info("New Access token created ");
                accessToken = respObj.getString("access_token");
                config.setAccessToken(accessToken);
                config.save();
            } else {
                logger.log(Level.INFO, "Error occurred during new access token creation Error - {0}", resp);
            }
        }
    }
}
