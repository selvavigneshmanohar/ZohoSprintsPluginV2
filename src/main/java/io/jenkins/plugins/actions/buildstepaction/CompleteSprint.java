package io.jenkins.plugins.actions.buildstepaction;

import java.util.function.Function;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.buildstepaction.builder.BuildStep;
import io.jenkins.plugins.actions.buildstepaction.descriptor.BuildStepDescriptorImpl;
import io.jenkins.plugins.api.SprintAPI;
import io.jenkins.plugins.model.Sprint;

public class CompleteSprint extends BuildStep {

    @DataBoundConstructor
    public CompleteSprint(String prefix) {
        super(Sprint.getInstance(prefix));
    }

    public Sprint getForm() {
        return (Sprint) super.getForm();
    }

    @Override
    public String perform(Function<String, String> getValueFromEnviroinmentValue) throws Exception {
        Sprint sprint = getForm();
        sprint.setEnviroinmentVaribaleReplacer(getValueFromEnviroinmentValue);
        return SprintAPI.getInstance().complete(sprint);
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {

        @Override
        public String getDisplayName() {
            return Messages.update_sprint_complete();
        }

    }
}
