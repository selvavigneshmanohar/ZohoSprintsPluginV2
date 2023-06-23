package io.jenkins.plugins.actions.buildstepaction;

import static org.apache.commons.lang.StringUtils.isEmpty;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.BuildStepDescriptorImpl;
import io.jenkins.plugins.api.ItemAPI;

public class AddWorkItem extends Builder {
    private String prefix, name, description, status, type, priority, duration, startdate, enddate, customFields,
            assignee;

    public String getAssignee() {
        return assignee;
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

    @DataBoundConstructor
    public AddWorkItem(String prefix, String name, String description, String status, String type, String priority,
            String duration, String assignee, String startdate, String enddate, String customFields) {
        this.prefix = prefix;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.priority = priority;
        this.duration = duration;
        this.assignee = assignee;
        this.startdate = startdate;
        this.enddate = enddate;
        this.customFields = customFields;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new ItemAPI.ItemActionBuilder(prefix, build, listener)
                .withName(name)
                .withDescription(description)
                .withStatus(status)
                .withPriority(priority)
                .withType(type)
                .withAssignee(assignee)
                .withDuration(duration)
                .withComment(startdate)
                .withEnddate(enddate)
                .withCustomFields(customFields)
                .build().create();
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.create_item();
        }

        public FormValidation doCheckName(@QueryParameter final String name) {
            if (!name.isEmpty()) {
                return FormValidation.ok();
            }
            return FormValidation.validateRequired(name);
        }

        public FormValidation doCheckStatus(@QueryParameter final String status) {
            if (!isEmpty(status)) {
                return FormValidation.ok();
            }
            return FormValidation.validateRequired(status);
        }

        public FormValidation doCheckType(@QueryParameter final String type) {
            if (!isEmpty(type)) {
                return FormValidation.ok();
            }
            return FormValidation.validateRequired(type);
        }

        public FormValidation doCheckPriority(@QueryParameter final String priority) {
            if (!isEmpty(priority)) {
                return FormValidation.ok();
            }
            return FormValidation.validateRequired(priority);
        }

    }
}
