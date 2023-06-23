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
import io.jenkins.plugins.api.ReleaseAPI;

public class CreateRelease extends PostBuild {
    private String prefix, name, owners, goal, stage, startdate, enddate, customFields;

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public String getOwners() {
        return owners;
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
    public CreateRelease(String prefix, String name, String owners, String goal, String stage, String startdate,
            String enddate, String customFields) {
        this.prefix = prefix;
        this.name = name;
        this.owners = owners;
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
                .withOwners(owners)
                .withStage(stage)
                .withStartdate(startdate)
                .withEnddate(enddate)
                .withCustomFields(customFields)
                .build().create();
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
