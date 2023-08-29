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
import io.jenkins.plugins.api.ProjectAPI;

public class AddFeedStatus extends PostBuild {
    private String feed;

    public String getFeed() {
        return feed;
    }

    @DataBoundConstructor
    public AddFeedStatus(String prefix, String feed) {
        super(prefix);
        this.feed = feed;
    }

    @Override
    public boolean _perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new ProjectAPI(prefix, feed, build, listener).addFeed();
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {

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
