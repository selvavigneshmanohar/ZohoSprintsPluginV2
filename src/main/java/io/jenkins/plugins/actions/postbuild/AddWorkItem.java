package io.jenkins.plugins.actions.postbuild;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.postbuild.builder.ItemPostBuilder;
import io.jenkins.plugins.actions.postbuild.descriptor.PostBuildDescriptor;
import io.jenkins.plugins.api.WorkItemAPI;

public class AddWorkItem extends ItemPostBuilder {

    @DataBoundConstructor
    public AddWorkItem(String prefix, String name, String description, String status, String type, String priority,
            String duration, String assignee, String startdate, String enddate, String customFields) {
        super(prefix, name, description, status, type, priority, duration, assignee, startdate, enddate, customFields);
    }

    @Override
    public String perform() throws Exception {
        return WorkItemAPI.getInstance().addItem(getForm());
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {

        @Override
        public String getDisplayName() {
            return Messages.create_item();
        }

        public FormValidation doCheckName(@QueryParameter final String name) {
            return FormValidation.validateRequired(name);
        }

        public FormValidation doCheckStatus(@QueryParameter final String status) {
            return FormValidation.validateRequired(status);
        }

        public FormValidation doCheckType(@QueryParameter final String type) {
            return FormValidation.validateRequired(type);
        }

        public FormValidation doCheckPriority(@QueryParameter final String priority) {
            return FormValidation.validateRequired(priority);
        }

    }

}
