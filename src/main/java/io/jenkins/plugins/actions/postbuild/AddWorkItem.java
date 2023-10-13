package io.jenkins.plugins.actions.postbuild;

import java.io.IOException;
import javax.annotation.Nonnull;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.util.FormValidation;
import io.jenkins.plugins.api.ItemAPI;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.ItemPostBuilder;
import io.jenkins.plugins.actions.PostBuildDescriptor;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class AddWorkItem extends ItemPostBuilder {

    @DataBoundConstructor
    public AddWorkItem(String prefix, String name, String description, String status, String type, String priority,
            String duration, String assignee, String startdate, String enddate, String customFields) {
        super(prefix, name, description, status, type, priority, duration, assignee, startdate, enddate, customFields);
    }

    public boolean _perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new ItemAPI.ItemActionBuilder(prefix, build, listener, item)
                .build()
                .create();
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {

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
