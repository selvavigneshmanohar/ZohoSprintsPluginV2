package io.jenkins.plugins.actions.postbuild;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.PostBuild;
import io.jenkins.plugins.actions.PostBuildDescriptor;
import io.jenkins.plugins.actions.ReleasePostBuilder;
import io.jenkins.plugins.api.ReleaseAPI;

public class CreateRelease extends ReleasePostBuilder {

    @DataBoundConstructor
    public CreateRelease(String prefix, String name, String owners, String goal, String stage, String startdate,
            String enddate, String customFields) {
        super(prefix, name, owners, goal, stage, startdate, enddate, customFields);
    }

    @Override
    public boolean _perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new ReleaseAPI.ReleaseAPIBuilder(prefix, build, listener, release)
                .build()
                .create();

    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.release_create();
        }
    }
}
