package io.jenkins.plugins.sprints;

import hudson.ProxyConfiguration;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.Secret;
import io.jenkins.plugins.configuration.ZSConnectionConfiguration;
import io.jenkins.plugins.util.Util;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import static io.jenkins.plugins.util.Util.replaceEnvVaribaleToValue;;

/**
 * @author selvavignesh.m
 * @version 1.0
 */
public class RequestClient {

    private static final Logger LOGGER = Logger.getLogger(RequestClient.class.getName());
    private static final Pattern RELATIVE_URL_PATTERN = Pattern.compile("\\$(\\d{1,2})");
    public static final String METHOD_GET = "get";
    public static final String METHOD_POST = "post";
    public static final String METHOD_DELETE = "delete";
    public static final String CHARSET = "UTF-8";
    private String url = null;
    private String method = null;
    private Map<String, Object> param = new HashMap<>();
    private Map<String, String> header = new HashMap<>();
    private boolean isJSONBodyContent;
    private Object bodyContent = null;
    private TaskListener listener = null;
    private Run<?, ?> build = null;
    private int responsecode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

    public int getResponsecode() {
        return responsecode;
    }

    public RequestClient setListener(TaskListener listener) {
        this.listener = listener;
        return this;
    }

    public RequestClient setBuild(Run<?, ?> build) {
        this.build = build;
        return this;
    }

    /**
     * @param paramMap Query param of API
     * @return Instance of Class
     */
    public RequestClient setParam(final Map<String, Object> paramMap) {
        this.param = paramMap;
        return this;
    }

    /**
     * @param jsonBodyContent JSONType param
     * @return Instance of Class
     */
    public RequestClient setJSONBodyContent(final boolean jsonBodyContent) {
        isJSONBodyContent = jsonBodyContent;
        return this;
    }

    /**
     * @param fromurl    Sprints API
     * @param frommethod Type of API call
     * @param fromparam  Query param of API
     */
    public RequestClient(final String api, final String method, final Map<String, Object> param, String[] urlParams)
            throws Exception {
        this.method = method;
        this.param = param;
        setZSAPIDetails(constructUri(api, urlParams));
    }

    public RequestClient(final String api, final String method, final JSONObject param, String[] urlParams)
            throws Exception {
        this.method = method;
        this.bodyContent = param;
        setZSAPIDetails(constructUri(api, urlParams));
    }

    private HttpEntityEnclosingRequestBase setJSONBodyEntity(HttpEntityEnclosingRequestBase reqobject) {
        StringEntity entity = new StringEntity(bodyContent.toString(), "UTF-8");
        reqobject.setEntity(entity);
        return reqobject;
    }

    private String constructUri(String url, String urlParams[]) throws Exception {
        if (urlParams == null) {
            return url;
        }
        StringBuffer urlBuilder = new StringBuffer();
        Matcher matcher = RELATIVE_URL_PATTERN.matcher(url);
        while (matcher.find()) {
            matcher.appendReplacement(urlBuilder,
                    URLEncoder.encode(urlParams[Integer.parseInt(matcher.group(1)) - 1], StandardCharsets.UTF_8));
        }
        matcher.appendTail(urlBuilder);
        return urlBuilder.toString();
    }

    /**
     * @return HttpUriRequest function
     * @throws Exception Throws when any error occurs
     */
    private HttpUriRequest getMethod() throws Exception {

        if (method != null) {
            if (method.equals(METHOD_GET)) {
                HttpGet get = new HttpGet(url);
                if (!param.isEmpty()) {
                    get = constructUrl(get);
                }
                return get;
            } else if (method.equals(METHOD_POST)) {
                HttpPost post = new HttpPost(url);
                setEntity(post);
                return post;
            }
        }
        return null;
    }

    /**
     * @param httpreq httpUriRequest Object
     * @return HttpUriRequest function
     * @throws Exception Throws when any error occurs
     */
    private HttpUriRequest setHeader(HttpUriRequest httpreq) throws Exception {
        Map<String, String> headerMap = this.header;

        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpreq.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return httpreq;
    }

    private void setZSAPIDetails(final String api) throws Exception {
        ZSConnectionConfiguration conf = Util.getZSConnection();
        if (api.startsWith(conf.getAccountsDomain())) {
            this.url = api;
            return;
        }
        header.put("X-ZA-SOURCE", "eiULZMmzMCRXCgFljRnxrA==");
        header.put("Authorization", "Zoho-oauthtoken " + conf.getAccessToken());
        this.url = conf.getZSApiPath() + api;
        LOGGER.info(this.url);
    }

    /**
     * @return String format of response
     * @throws Exception Throws when any error occurs
     */
    public String execute() throws Exception {
        int connectionTimeOut = 30000;
        int socketTimeOut = 30000;
        String resp = "";
        HttpUriRequest request = this.getMethod();
        request = this.setHeader(request);
        if (isJSONBodyContent) {
            request.setHeader("Content-type", ContentType.APPLICATION_JSON.getMimeType()); // NO I18N
        } else if (request.getHeaders("Content-type") == null) {// NO I18N
            request.setHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8"); // no i18n
        }
        RequestConfig config = RequestConfig
                .custom()
                .setConnectTimeout(connectionTimeOut)
                .setSocketTimeout(socketTimeOut)
                .build();
        HttpClientBuilder builder = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(config);

        ProxyConfiguration proxy = Jenkins.get().proxy;
        if (proxy != null) {
            String hosturl = proxy.name;
            int port = proxy.port;
            String uname = proxy.getUserName();
            String password = Secret.toString(proxy.getSecretPassword());
            HttpHost host = new HttpHost(hosturl, port);
            builder = builder.useSystemProperties();
            builder.setProxy(host);
            if (uname != null && password != null) {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                AuthScope authScope = new AuthScope(host.getHostName(), port);
                credentialsProvider.setCredentials(authScope, new UsernamePasswordCredentials(uname, password));
                builder.setDefaultCredentialsProvider(credentialsProvider);
                builder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

            }
        }
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;

        try {
            client = builder.build();
            response = client.execute(request);
            this.responsecode = response.getStatusLine().getStatusCode();
            int respCode = response.getStatusLine().getStatusCode();
            LOGGER.log(Level.INFO, "Status code {0}", respCode);
            HttpEntity reponseEntity = response.getEntity();
            resp = getString(reponseEntity.getContent());
            if (respCode >= 400) {
                LOGGER.log(Level.INFO, " Error occurred in Sprints API call Error - {0}", resp);
            }
            EntityUtils.consume(reponseEntity);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "", e);
        } finally {
            if (client != null) {
                client.close();
            }
            if (response != null) {
                response.close();
            }
        }
        return resp;
    }

    /**
     * @param get HttpGet Object
     * @return HttpGet function
     */
    private HttpGet constructUrl(HttpGet get) {
        List<NameValuePair> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                list.add(new BasicNameValuePair(key, value.toString()));
            }
        }
        url = url + "?" + URLEncodedUtils.format(list, CHARSET);
        return new HttpGet(url);
    }

    /**
     * @param post HttpPost Object
     * @throws UnsupportedEncodingException Throws when unsupported Encoding happens
     */
    private void setEntity(HttpPost post) throws UnsupportedEncodingException, IOException, InterruptedException {

        if (param != null && !param.isEmpty()) {
            List<NameValuePair> entityList = new ArrayList<>();
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                Object value = entry.getValue();
                if (value != null && value.toString().length() > 0) {
                    entityList.add(new BasicNameValuePair(entry.getKey(),
                            (build != null && listener != null)
                                    ? replaceEnvVaribaleToValue(build, listener, value.toString())
                                    : value.toString()));
                }

            }
            post.setEntity(new UrlEncodedFormEntity(entityList));
        } else if (bodyContent != null) {
            setJSONBodyEntity(post);
        }

    }

    /**
     * @param stream Stream of the response
     * @return String format of response
     * @throws IOException Throws when error occurs at read/write
     */
    private String getString(final InputStream stream) throws IOException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];

            for (int length = 0; (length = stream.read(buffer)) > 0;) {
                result.write(buffer, 0, length);
            }

            return result.toString(CHARSET); // no i18n

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "RequestClient_Error_while_fetching_content=>", e);
        } finally {
            stream.close();
        }
        return null;
    }

}
