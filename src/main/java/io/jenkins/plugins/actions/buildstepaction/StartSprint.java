package io.jenkins.plugins.actions.buildstepaction;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.buildstepaction.builder.BuildStep;
import io.jenkins.plugins.actions.buildstepaction.descriptor.BuildStepDescriptorImpl;
import io.jenkins.plugins.api.SprintAPI;
import io.jenkins.plugins.model.Sprint;

public class StartSprint extends BuildStep {
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
    public static class DescriptorImpl extends BuildStepDescriptorImpl {

        @Override
        public String getDisplayName() {
            return Messages.update_sprint_start();
        }

    }
}
