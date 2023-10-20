package io.jenkins.plugins.actions.postbuild;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.postbuild.builder.SprintsPostBuilder;
import io.jenkins.plugins.actions.postbuild.descriptor.PostBuildDescriptor;
import io.jenkins.plugins.api.SprintAPI;
import io.jenkins.plugins.util.Util;

public class CreateSprint extends SprintsPostBuilder {

    @DataBoundConstructor
    public CreateSprint(String prefix, String name, String description, String scrummaster, String users,
            String duration, String startdate, String enddate) {
        super(prefix, name, description, scrummaster, users, duration, startdate, enddate);
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

        public FormValidation doCheckName(@QueryParameter final String name) {
            return Util.validateRequired(name);
        }
    }

}
