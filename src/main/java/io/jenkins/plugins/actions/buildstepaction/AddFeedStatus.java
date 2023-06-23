package io.jenkins.plugins.actions.buildstepaction;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.BuildStepDescriptorImpl;
import io.jenkins.plugins.api.ProjectAPI;

public class AddFeedStatus extends Builder {
    private String prefix, feed;

    public String getPrefix() {
        return prefix;
    }

    public String getFeed() {
        return feed;
    }

    @DataBoundConstructor
    public AddFeedStatus(String prefix, String feed) {
        this.prefix = prefix;
        this.feed = feed;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new ProjectAPI(prefix, feed, build, listener).addFeed();
    }

    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {

        public FormValidation doCheckFeed(@QueryParameter final String feed) {
            return FormValidation.validateRequired(feed);
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.add_feed_status();
        }

    }

}
