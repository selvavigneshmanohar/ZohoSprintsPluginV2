package io.jenkins.plugins.api;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.sprints.ZohoClient;
import io.jenkins.plugins.sprints.RequestClient;
import static io.jenkins.plugins.util.Util.sprintsLogparser;

public final class ProjectAPI {
    private static final Logger LOGGER = Logger.getLogger(ProjectAPI.class.getName());
    public static final Pattern ZS_PROJECT = Pattern.compile("^(P|p)([0-9]+)$");
    private static final String FEED_PUSH_API = "/projects/no-$1/feed/status/";
    private String feed;
    private Integer projectNumber;
    private Run<?, ?> build;
    private TaskListener listener;

    public ProjectAPI(String prefix, String feed, Run<?, ?> build, TaskListener listener) {
        Matcher matcher = ZS_PROJECT.matcher(prefix);
        this.projectNumber = matcher.matches() ? Integer.parseInt(matcher.group(2)) : null;
        this.feed = feed;
        this.build = build;
        this.listener = listener;
    }

    public boolean addFeed() {
        if (projectNumber == null) {
            listener.error("Invalid Prefix");
            return Boolean.FALSE;
        }
        Map<String, Object> param = new HashMap<>();
        param.put("name", feed);
        try {
            ZohoClient client = new ZohoClient(FEED_PUSH_API, RequestClient.METHOD_POST, param, listener, build,
                    projectNumber.toString());
            client.execute();
            if (client.isSuccessRequest()) {
                listener.getLogger().println(sprintsLogparser("Feed status successfully added", false));
                return Boolean.TRUE;
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error", e);
        }
        return Boolean.FALSE;
    }
}
