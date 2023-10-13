package io.jenkins.plugins;

import java.io.IOException;

import hudson.model.BuildListener;
import hudson.model.Run;
import hudson.model.TaskListener;

public class BuildDetails {
    private Run<?, ?> run;
    private TaskListener listener;

    public BuildDetails(final Run<?, ?> run, final BuildListener listener) {
        this.run = run;
        this.listener = listener;
    }

    public void print(String message) {
        listener.getLogger().println("[Zoho Sprints] " + message);
    }

    public void printError(String message) {
        listener.error(message);
    }

    public String replaceEnvVaribaleToValue(final String key) throws IOException, InterruptedException {
        return run.getEnvironment(listener).expand(key);
    }
}
