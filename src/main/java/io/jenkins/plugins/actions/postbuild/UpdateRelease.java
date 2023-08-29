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
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.PostBuildDescriptor;
import io.jenkins.plugins.actions.ReleasePostBuilder;
import io.jenkins.plugins.api.ReleaseAPI;

public class UpdateRelease extends ReleasePostBuilder {
    @DataBoundConstructor
    public UpdateRelease(String prefix, String name, String goal, String stage, String startdate,
            String enddate, String customFields) {
        super(prefix, name, null, goal, stage, startdate, enddate, customFields);
    }

    @Override
    public boolean _perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new ReleaseAPI.ReleaseAPIBuilder(prefix, build, listener, release)
                .build()
                .update();

    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.release_update();
        }

        public FormValidation doCheckPrefix(@QueryParameter final String goal) {
            return FormValidation.validateRequired(goal);
        }

        public FormValidation doCheckStage(@QueryParameter final String stage) {
            return FormValidation.validateRequired(stage);
        }
    }
}
