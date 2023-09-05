package io.jenkins.plugins.actions.buildstepaction;

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
import io.jenkins.plugins.actions.BuildStepDescriptorImpl;
import io.jenkins.plugins.actions.ReleaseStepBuilder;
import io.jenkins.plugins.api.ReleaseAPI;

public class AddReleaseComment extends ReleaseStepBuilder {

    @DataBoundConstructor
    public AddReleaseComment(String prefix, String note) {
        super(prefix, note);
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new ReleaseAPI.ReleaseAPIBuilder(prefix, build, listener, release)
                .build()
                .addComment();
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {

        public FormValidation doCheckNote(@QueryParameter final String note) {
            if (StringUtils.isEmpty(note)) {
                return FormValidation.validateRequired(note);
            }
            return FormValidation.ok();
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.add_release_comment();
        }

    }
}
