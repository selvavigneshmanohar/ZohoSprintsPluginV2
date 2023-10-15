package io.jenkins.plugins.actions.buildstepaction;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.buildstepaction.builder.SprintsStepBuilder;
import io.jenkins.plugins.actions.buildstepaction.descriptor.BuildStepDescriptorImpl;
import io.jenkins.plugins.api.SprintAPI;

public class UpdateSprint extends SprintsStepBuilder {
    @DataBoundConstructor
    public UpdateSprint(String prefix, String name, String description, String duration, String startdate,
            String enddate) {
        super(prefix, name, description, duration, startdate, enddate);
    }

    @Override
    public String perform() throws Exception {
        return SprintAPI.getInstance().update(getForm());
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {
        @Override
        public String getDisplayName() {
            return Messages.sprint_update();
        }
    }
}
