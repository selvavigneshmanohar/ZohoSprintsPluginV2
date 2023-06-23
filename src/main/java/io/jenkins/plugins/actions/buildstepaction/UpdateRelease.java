package io.jenkins.plugins.actions.buildstepaction;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.Builder;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.BuildStepDescriptorImpl;
import io.jenkins.plugins.api.ReleaseAPI;

public class UpdateRelease extends Builder {
    private String prefix, name, goal, stage, startdate, enddate, customFields;

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public String getGoal() {
        return goal;
    }

    public String getStage() {
        return stage;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getCustomFields() {
        return customFields;
    }

    @DataBoundConstructor
    public UpdateRelease(String prefix, String name, String owners, String goal, String stage, String startdate,
            String enddate, String customFields) {
        this.prefix = prefix;
        this.name = name;
        this.goal = goal;
        this.stage = stage;
        this.startdate = startdate;
        this.enddate = enddate;
        this.customFields = customFields;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new ReleaseAPI.ReleaseAPIBuilder(prefix, build, listener)
                .withName(name)
                .withGoal(goal)
                .withStage(stage)
                .withStartdate(startdate)
                .withEnddate(enddate)
                .withCustomFields(customFields)
                .build().create();
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
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
