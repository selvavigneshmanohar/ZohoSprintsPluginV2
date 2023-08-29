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
import io.jenkins.plugins.actions.ItemStepBuilder;
import io.jenkins.plugins.api.ItemAPI;

public class UpdateWorkItem extends ItemStepBuilder {

    @DataBoundConstructor
    public UpdateWorkItem(String prefix, String name, String description, String status, String type, String priority,
            String duration, String startdate, String enddate, String customFields) {
        super(prefix, name, description, status, type, priority, duration, null, startdate, enddate, customFields);
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {

        return new ItemAPI.ItemActionBuilder(prefix, build, listener, item)
                .build()
                .update();

    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.update_item();
        }

    }
}