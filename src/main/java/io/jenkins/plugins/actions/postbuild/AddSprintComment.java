package io.jenkins.plugins.actions.postbuild;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.postbuild.builder.SprintsPostBuilder;
import io.jenkins.plugins.actions.postbuild.descriptor.PostBuildDescriptor;
import io.jenkins.plugins.api.SprintAPI;

public class AddSprintComment extends SprintsPostBuilder {

    @DataBoundConstructor
    public AddSprintComment(String prefix, String note) {
        super(prefix, note);
    }

    @Override
    public String perform() throws Exception {
        return SprintAPI.getInstance().addComment(getForm());
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {

        public FormValidation doCheckNote(@QueryParameter final String note) {
            if (StringUtils.isEmpty(note)) {
                return FormValidation.validateRequired(note);
            }
            return FormValidation.ok();
        }

        @Override
        public String getDisplayName() {
            return Messages.add_sprint_comment();
        }
    }

}
