package io.jenkins.plugins.actions.buildstepaction;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.buildstepaction.builder.ItemStepBuilder;
import io.jenkins.plugins.actions.buildstepaction.descriptor.BuildStepDescriptorImpl;
import io.jenkins.plugins.api.WorkItemAPI;

public class AddItemComment extends ItemStepBuilder {

    @DataBoundConstructor
    public AddItemComment(String prefix, String note) {
        super(prefix, note);
    }

    @Override
    public String perform() throws Exception {
        return WorkItemAPI.getInstance().addComment(getForm());
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {

        public FormValidation doCheckNote(@QueryParameter final String note) {
            return FormValidation.validateRequired(note);
        }

        @Override
        public String getDisplayName() {
            return Messages.add_item_comment();
        }

    }

}
