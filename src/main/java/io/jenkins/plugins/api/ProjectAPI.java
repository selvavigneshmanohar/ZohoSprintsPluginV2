package io.jenkins.plugins.api;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.sprints.OAuthClient;
import io.jenkins.plugins.sprints.RequestClient;
import io.jenkins.plugins.util.Util;
import static io.jenkins.plugins.util.Util.sprintsLogparser;

public final class ProjectAPI {
    private String feed;
    private Integer projectNumber;
    private Run<?, ?> build;
    private TaskListener listener;

    public ProjectAPI(String prefix, String feed, Run<?, ?> build, TaskListener listener) {
        Matcher matcher = Util.ZS_PROJECT.matcher(prefix);
        this.projectNumber = matcher.find() ? Integer.parseInt(matcher.group(2)) : null;
        this.feed = feed;
        this.build = build;
        this.listener = listener;
    }

    public boolean addFeed() {
        if (projectNumber == null) {
            listener.error("Invalid Prefix");
            return Boolean.FALSE;
        }
        String url = String.format("/projects/no-%s/feed/status/", projectNumber);
        Map<String, Object> param = new HashMap<>();
        param.put("name", feed);
        try {
            OAuthClient client = new OAuthClient(url, RequestClient.METHOD_POST, param, listener, build);
            if (client.execute() != null) {
                listener.getLogger().println(sprintsLogparser("Feed status successfully added", false));
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }

    }
}
