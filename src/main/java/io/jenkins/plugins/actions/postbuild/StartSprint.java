package io.jenkins.plugins.actions.postbuild;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.postbuild.builder.PostBuild;
import io.jenkins.plugins.actions.postbuild.descriptor.PostBuildDescriptor;
import io.jenkins.plugins.api.SprintAPI;
import io.jenkins.plugins.model.Sprint;

public class StartSprint extends PostBuild {

    @DataBoundConstructor
    public StartSprint(String prefix) {
        super(Sprint.getInstance(prefix));
    }

    public Sprint getForm() {
        return (Sprint) super.getForm();
    }

    @Override
    public String perform() throws Exception {
        return SprintAPI.getInstance().start(getForm());
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.update_sprint_start();
        }
    }

}
