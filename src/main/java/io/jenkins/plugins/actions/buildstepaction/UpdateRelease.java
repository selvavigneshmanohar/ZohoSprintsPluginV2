package io.jenkins.plugins.actions.buildstepaction;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.BuildStepDescriptorImpl;
import io.jenkins.plugins.actions.ReleaseStepBuilder;
import io.jenkins.plugins.api.ReleaseAPI;

public class UpdateRelease extends ReleaseStepBuilder {

    @DataBoundConstructor
    public UpdateRelease(String prefix, String name, String goal, String stage, String startdate,
            String enddate, String customFields) {
        super(prefix, name, null, goal, stage, startdate, enddate, customFields);
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new ReleaseAPI.ReleaseAPIBuilder(prefix, build, listener, release)
                .build()
                .update();
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.release_update();
        }
    }
}
