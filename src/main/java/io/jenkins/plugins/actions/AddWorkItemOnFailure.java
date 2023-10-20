package io.jenkins.plugins.actions;

import static io.jenkins.plugins.util.Util.validateRequired;

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
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.model.Item;
import jenkins.tasks.SimpleBuildWrapper;
import net.sf.json.JSONObject;

public class AddWorkItemOnFailure extends SimpleBuildWrapper {
    private Item item;

    @DataBoundConstructor
    public AddWorkItemOnFailure(String prefix, String name, String description, String status, String type,
            String priority,
            String duration, String assignee, String startdate, String enddate, String customFields) {
        item = Item.getInstance(prefix).setName(name)
                .setDescription(description)
                .setStatus(status)
                .setType(type)
                .setPriority(priority)
                .setDuration(duration)
                .setAssignee(assignee)
                .setStartdate(startdate)
                .setEnddate(enddate)
                .setCustomFields(customFields);
    }

    public Item getForm() {
        return this.item;
    }

    public String getPrefix() {
        return item.getPrefix();
    }

    public String getAssignee() {
        return item.getAssignee();
    }

    public String getName() {
        return item.getName();
    }

    public String getDescription() {
        return item.getDescription();
    }

    public String getStatus() {
        return item.getStatus();
    }

    public String getType() {
        return item.getType();
    }

    public String getPriority() {
        return item.getPriority();
    }

    public String getDuration() {
        return item.getDuration();
    }

    public String getStartdate() {
        return item.getStartdate();
    }

    public String getEnddate() {
        return item.getEnddate();
    }

    public String getCustomFields() {
        return item.getCustomFields();
    }

    public String getNote() {
        return item.getNote();
    }

    @Override
    public void setUp(Context context, Run<?, ?> build, FilePath workspace, Launcher launcher,
            TaskListener listener, EnvVars initialEnvironment) throws IOException, InterruptedException {
        final Map<String, String> issueParamMap = new HashMap<>();
        issueParamMap.put("ZSPRINTS_ISSUE_NAME", getName());
        issueParamMap.put("ZSPRINTS_ISSUE_PREFIX", getPrefix());
        issueParamMap.put("ZSPRINTS_ISSUE_DESCRIPTION", getDescription());
        issueParamMap.put("ZSPRINTS_ISSUE_ASSIGNEE", getAssignee());
        issueParamMap.put("ZSPRINTS_ISSUE_TYPE", getType());
        issueParamMap.put("ZSPRINTS_ISSUE_STATUS", getStatus());
        issueParamMap.put("ZSPRINTS_ISSUE_DURATION", getDuration());
        issueParamMap.put("ZSPRINTS_ISSUE_PRIORITY", getPriority());
        issueParamMap.put("ZSPRINTS_ISSUE_STARTDATE", getStartdate());
        issueParamMap.put("ZSPRINTS_ISSUE_ENDDATE", getEnddate());
        issueParamMap.put("ZSPRINTS_ISSUE_CUSTOMFIELD", getCustomFields());
        issueParamMap.put("ZSPRINTS_ISSUE_BUILD_ENVIRONMENT_AVAILABLE", Boolean.toString(true));
        context.getEnv().putAll(issueParamMap);
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
            return validateRequired(prefix);
        }

        public FormValidation doCheckName(@QueryParameter final String name) {
            return validateRequired(name);
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            req.bindJSON(this, json);
            save();
            return super.configure(req, json);
        }

        public FormValidation doCheckStatus(@QueryParameter final String status) {
            return validateRequired(status);
        }

        public FormValidation doCheckType(@QueryParameter final String type) {
            return validateRequired(type);
        }

        public FormValidation doCheckPriority(@QueryParameter final String priority) {
            return validateRequired(priority);
        }
    }
}
