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
import io.jenkins.plugins.actions.PostBuild;
import io.jenkins.plugins.actions.PostBuildDescriptor;
import io.jenkins.plugins.api.ReleaseAPI;

public class UpdateRelease extends PostBuild {
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
    public UpdateRelease(String prefix, String name, String goal, String stage, String startdate,
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
    public boolean _perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new ReleaseAPI.ReleaseAPIBuilder(prefix, build, listener)
                .withName(name)
                .withGoal(goal)
                .withStage(stage)
                .withStartdate(startdate)
                .withEnddate(enddate)
                .withCustomFields(customFields)
                .build().update();
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.release_create();
        }

        public FormValidation doCheckPrefix(@QueryParameter final String goal) {
            return FormValidation.validateRequired(goal);
        }

        public FormValidation doCheckStage(@QueryParameter final String stage) {
            return FormValidation.validateRequired(stage);
        }
    }
}
