package io.jenkins.plugins.actions.postbuild;

import java.util.function.Function;

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
    public String perform(Function<String, String> getValueFromEnviroinmentValue) throws Exception {
        Sprint sprint = getForm();
        sprint.setEnviroinmentVaribaleReplacer(getValueFromEnviroinmentValue);
        return SprintAPI.getInstance().start(sprint);
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.update_sprint_start();
        }
    }

}
