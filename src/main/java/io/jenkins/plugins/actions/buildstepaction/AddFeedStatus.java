package io.jenkins.plugins.actions.buildstepaction;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.Launcher;
import hudson.matrix.MatrixProject;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.api.ProjectAPI;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;

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
        return (DescriptorImpl) Jenkins.getInstance().getDescriptor(getClass());
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Builder> {
        Logger logger = Logger.getLogger(DescriptorImpl.class.getName());

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            logger.info("test" + jobType.getName());
            return !MatrixProject.class.equals(jobType);
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) {
            req.bindJSON(this, json);
            save();
            return true;
        }

        public FormValidation doCheckPrefix(@QueryParameter final String prefix) {
            return FormValidation.validateRequired(prefix);
        }

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
