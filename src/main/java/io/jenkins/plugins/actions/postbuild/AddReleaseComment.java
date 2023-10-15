package io.jenkins.plugins.actions.postbuild;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.postbuild.builder.ReleasePostBuilder;
import io.jenkins.plugins.actions.postbuild.descriptor.PostBuildDescriptor;
import io.jenkins.plugins.api.ReleaseAPI;

public class AddReleaseComment extends ReleasePostBuilder {

    @DataBoundConstructor
    public AddReleaseComment(String prefix, String note) {
        super(prefix, note);
    }

    @Override
    public String perform() throws Exception {
        return ReleaseAPI.getInstance().addComment(getForm());
    }

    // @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {

        public FormValidation doCheckNote(@QueryParameter final String note) {
            return FormValidation.validateRequired(note);
        }

        @Override
        public String getDisplayName() {
            return Messages.add_release_comment();
        }
    }

}
