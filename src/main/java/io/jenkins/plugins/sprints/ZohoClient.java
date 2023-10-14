package io.jenkins.plugins.sprints;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import io.jenkins.plugins.configuration.ZSConnectionConfiguration;
import io.jenkins.plugins.exception.ZSprintsException;
import io.jenkins.plugins.util.Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ZohoClient {
    private static final Logger logger = Logger.getLogger(ZohoClient.class.getName());
    private int responsecode;
    private Map<String, Object> queryParam = new HashMap<>();
    private String api;
    private String method;
    private String[] relativeUrlParams;
    private boolean isJsonBodyresponse = false;

    public ZohoClient(String api, String method,
            String... relativeUrlParams) throws Exception {
        this.api = api;
        this.method = method;
        this.relativeUrlParams = relativeUrlParams;
    }

    public ZohoClient addParameter(String key, String value) {
        logger.info("Key -->" + key + "  value -->" + value);
        if (value != null && !value.trim().isEmpty()) {
            queryParam.put(key, value);
        }
        return this;
    }

    public ZohoClient setJsonBodyresponse(boolean isJsonBodyresponse) {
        this.isJsonBodyresponse = isJsonBodyresponse;
        return this;
    }

    public ZohoClient addParameter(String key, JSONArray value) {
        logger.log(Level.INFO, "Key - {0} Value - {0}", new Object[] { key, value });
        if (value != null && !value.isEmpty()) {
            queryParam.put(key, value);
        }
        return this;
    }

    public boolean isSuccessRequest() {
        return responsecode == HttpServletResponse.SC_OK || responsecode == HttpServletResponse.SC_CREATED;
    }

    public String execute() throws Exception {
        isOAuthTokenAvailable();
        @SuppressWarnings("DLS_DEAD_LOCAL_STORE")
        RequestClient client = new RequestClient(api, method, relativeUrlParams)
                .setJSONBodyContent(isJsonBodyresponse)
                .setQueryParam(queryParam);
        String response = client.execute();
        if (isOAuthExpired(client, response)) {
            generateNewAccessToken();
            response = client.execute();
        }
        responsecode = client.getResponsecode();
        if (isSuccessRequest()) {
            return response;
        }
        throw new ZSprintsException(JSONObject.fromObject(response).toString());

    }

    private boolean isOAuthExpired(RequestClient client, String response) {
        logger.info("" + JSONObject.fromObject(response).optInt("code", 0));
        return client.getResponsecode() == HttpServletResponse.SC_BAD_REQUEST &&
                JSONObject.fromObject(response).optInt("code", 0) == 7601;
    }

    private void isOAuthTokenAvailable() throws Exception {
        ZSConnectionConfiguration config = Util.getZSConnection();
        if (config.getAccessToken() != null && config.getAccessToken().length() == 0) {
            generateNewAccessToken();
        }
    }

    public static synchronized void generateNewAccessToken() throws Exception {
        ZSConnectionConfiguration config = Util.getZSConnection();
        logger.info("New Token method called");
        String accessToken = null;
        RequestClient requestClient = new RequestClient(config.getAccountsDomain() + "/oauth/v2/token",
                RequestClient.METHOD_POST, null)
                .addParameter("grant_type", "refresh_token")
                .addParameter("client_id", config.getClientId())
                .addParameter("client_secret", config.getClientSecret())
                .addParameter("refresh_token", config.getRefreshToken())
                .addParameter("redirect_uri", config.getRedirectURL());
        String resp = requestClient.execute();
        if (resp != null && resp.startsWith("{")) {
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
