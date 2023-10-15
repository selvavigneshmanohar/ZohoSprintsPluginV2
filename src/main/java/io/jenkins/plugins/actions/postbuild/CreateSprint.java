package io.jenkins.plugins.actions.postbuild;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.postbuild.builder.SprintsPostBuilder;
import io.jenkins.plugins.actions.postbuild.descriptor.PostBuildDescriptor;
import io.jenkins.plugins.api.SprintAPI;

public class CreateSprint extends SprintsPostBuilder {

    @DataBoundConstructor
    public CreateSprint(String prefix, String name, String description, String duration, String startdate,
            String enddate) {
        super(prefix, name, description, duration, startdate, enddate);
    }

    @Override
    public String perform() throws Exception {
        return SprintAPI.getInstance().create(getForm());
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.sprint_create();
        }
    }

}
