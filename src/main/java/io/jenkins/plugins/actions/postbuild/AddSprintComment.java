package io.jenkins.plugins.actions.postbuild;

import java.io.IOException;
import javax.annotation.Nonnull;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.PostBuild;
import io.jenkins.plugins.actions.PostBuildDescriptor;
import io.jenkins.plugins.api.SprintAPI;

public class AddSprintComment extends PostBuild {
    private String note;

    public String getNote() {
        return note;
    }

    @DataBoundConstructor
    public AddSprintComment(String prefix, String note) {
        super(prefix);
        this.note = note;
    }

    public boolean _perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new SprintAPI(prefix, listener, build).withComment(note).addComment();
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {

        public FormValidation doCheckNote(@QueryParameter final String note) {
            if (StringUtils.isEmpty(note)) {
                return FormValidation.validateRequired(note);
            }
            return FormValidation.ok();
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.add_sprint_comment();
        }
    }

}
