package io.jenkins.plugins.actions;

import static io.jenkins.plugins.util.Util.isEmpty;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.matrix.MatrixProject;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.util.Util;
import jenkins.tasks.SimpleBuildWrapper;
import net.sf.json.JSONObject;

public class AddWorkItemOnFailure extends SimpleBuildWrapper {
    private String prefix, name, description, status, type, priority, duration, startdate, enddate, customFields;

    @DataBoundConstructor
    public AddWorkItemOnFailure(String prefix, String name, String description, String status, String type,
            String priority,
            String duration, String startdate, String enddate, String customFields) {
        this.prefix = prefix;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.priority = priority;
        this.duration = duration;
        this.startdate = startdate;
        this.enddate = enddate;
        this.customFields = customFields;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getPriority() {
        return priority;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getCustomFields() {
        return customFields;
    }

    private String replaceEnvVaribaleToValue(Run<?, ?> run, final TaskListener listener,
            final String key) throws IOException, InterruptedException {
        return run.getEnvironment(listener).expand(key);
    }

    @Override
    public void setUp(Context context, Run<?, ?> build, FilePath workspace, Launcher launcher,
            TaskListener listener, EnvVars initialEnvironment) throws IOException, InterruptedException {
        String itemPrefix, itemName;
        final Map<String, String> envMap = new HashMap<>();
        itemPrefix = replaceEnvVaribaleToValue(build, listener, prefix);
        itemName = replaceEnvVaribaleToValue(build, listener, name);
        envMap.put("SPRINTS_ISSUE_NAME", itemName);
        envMap.put("SPRINTS_ISSUE_DESCRIPTION", replaceEnvVaribaleToValue(build, listener, description));
        envMap.put("SPRINTS_ISSUE_PREFIX", itemPrefix);
        envMap.put("SPRINTS_ISSUE_ASSIGNEE", replaceEnvVaribaleToValue(build, listener, name));
        envMap.put("SPRINTS_ISSUE_TYPE", replaceEnvVaribaleToValue(build, listener, type));
        envMap.put("SPRINTS_ISSUE_STATUS", replaceEnvVaribaleToValue(build, listener, status));
        envMap.put("SPRINTS_ISSUE_DURATION", replaceEnvVaribaleToValue(build, listener, duration));
        envMap.put("SPRINTS_ISSUE_PRIORITY", replaceEnvVaribaleToValue(build, listener, priority));
        envMap.put("SPRINTS_ISSUE_STARTDATE", replaceEnvVaribaleToValue(build, listener, startdate));
        envMap.put("SPRINTS_ISSUE_ENDDATE", replaceEnvVaribaleToValue(build, listener, enddate));
        envMap.put("SPRINTS_ISSUE_CUSTOMFIELD", replaceEnvVaribaleToValue(build, listener, customFields));
        envMap.put("SPRINTS_ISSUE_BUILD_ENVIRONMENT_AVAILABLE", Boolean.toString(true));
        if (isEmpty(itemPrefix)) {
            listener.error("Prefix should not be empty or null");
            ((BuildListener) listener).finished(Result.FAILURE);
        }
        if (isEmpty(itemName)) {
            listener.error("Item name is not specified");
            ((BuildListener) listener).finished(Result.FAILURE);
        }

        context.getEnv().putAll(envMap);
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends BuildWrapperDescriptor {
        @Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            return !(item instanceof MatrixProject);

        }

        @Override
        public String getDisplayName() {
            return Messages.issue_in_failure();
        }

        public FormValidation doCheckPrefix(@QueryParameter final String prefix) {
            return Util.validateRequired(prefix);
        }

        public FormValidation doCheckName(@QueryParameter final String name) {
            return Util.validateRequired(name);
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            req.bindJSON(this, json);
            save();
            return super.configure(req, json);
        }

        public FormValidation doCheckStatus(@QueryParameter final String status) {
            if (!isEmpty(status)) {
                return FormValidation.ok();
            }
            return Util.validateRequired(status);
        }

        public FormValidation doCheckType(@QueryParameter final String type) {
            if (!isEmpty(type)) {
                return FormValidation.ok();
            }
            return Util.validateRequired(type);
        }

        public FormValidation doCheckPriority(@QueryParameter final String priority) {
            if (!isEmpty(priority)) {
                return FormValidation.ok();
            }
            return Util.validateRequired(priority);
        }
    }
}
