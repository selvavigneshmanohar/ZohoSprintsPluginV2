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
import io.jenkins.plugins.actions.SprintsStepBuilder;
import io.jenkins.plugins.api.SprintAPI;

public class UpdateSprint extends SprintsStepBuilder {
    @DataBoundConstructor
    public UpdateSprint(String prefix, String name, String description, String duration, String startdate,
            String enddate) {
        super(prefix, name, description, duration, startdate, enddate);
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new SprintAPI(prefix, listener, build)
                .update(sprint);
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {
        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.sprint_update();
        }
    }
}
