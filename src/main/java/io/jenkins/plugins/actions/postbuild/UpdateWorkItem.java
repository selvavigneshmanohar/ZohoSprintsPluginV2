package io.jenkins.plugins.actions.postbuild;

import java.io.IOException;
import javax.annotation.Nonnull;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import io.jenkins.plugins.api.ItemAPI;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.ItemPostBuilder;
import io.jenkins.plugins.actions.PostBuildDescriptor;

public class UpdateWorkItem extends ItemPostBuilder {

    @DataBoundConstructor
    public UpdateWorkItem(String prefix, String name, String description, String status, String type, String priority,
            String duration, String startdate, String enddate, String customFields) {
        super(prefix, name, description, status, type, priority, duration, null, startdate, enddate, customFields);
    }

    @Override
    public boolean _perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        return new ItemAPI.ItemActionBuilder(prefix, build, listener, item)
                .build()
                .update();
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.update_item();
        }

    }
}
