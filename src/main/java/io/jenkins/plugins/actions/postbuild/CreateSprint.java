package io.jenkins.plugins.actions.postbuild;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.PostBuildDescriptor;
import io.jenkins.plugins.actions.SprintsPostBuilder;
import io.jenkins.plugins.api.SprintAPI;

public class CreateSprint extends SprintsPostBuilder {

    @DataBoundConstructor
    public CreateSprint(String prefix, String name, String description, String duration, String startdate,
            String enddate) {
        super(prefix, name, description, duration, startdate, enddate);
    }

    @Override
    public boolean _perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new SprintAPI(prefix, listener, build)
                .create(sprint);
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {
        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.sprint_create();
        }
    }

}
