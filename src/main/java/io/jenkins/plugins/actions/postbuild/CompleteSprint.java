
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
import io.jenkins.plugins.api.SprintAPI;

public class CompleteSprint extends PostBuild {
    @DataBoundConstructor
    public CompleteSprint(String prefix) {
        super(prefix);
    }

    @Override
    public boolean _perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new SprintAPI(prefix, listener, build).complete();
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {
        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.update_sprint_complete();
        }
    }

}
