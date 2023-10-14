package io.jenkins.plugins.actions.postbuild;

import java.util.function.Function;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.postbuild.builder.SprintsPostBuilder;
import io.jenkins.plugins.actions.postbuild.descriptor.PostBuildDescriptor;
import io.jenkins.plugins.api.SprintAPI;
import io.jenkins.plugins.model.Sprint;

public class UpdateSprint extends SprintsPostBuilder {
    @DataBoundConstructor
    public UpdateSprint(String prefix, String name, String description, String duration, String startdate,
            String enddate) {
        super(prefix, name, description, duration, startdate, enddate);
    }

    @Override
    public String perform(Function<String, String> getValueFromEnviroinmentValue) throws Exception {
        Sprint sprint = getForm();
        sprint.setEnviroinmentVaribaleReplacer(getValueFromEnviroinmentValue);
        return SprintAPI.getInstance().update(sprint);
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.sprint_update();
        }
    }

}
