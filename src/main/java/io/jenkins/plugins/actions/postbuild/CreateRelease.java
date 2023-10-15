package io.jenkins.plugins.actions.postbuild;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.postbuild.builder.ReleasePostBuilder;
import io.jenkins.plugins.actions.postbuild.descriptor.PostBuildDescriptor;
import io.jenkins.plugins.api.ReleaseAPI;

public class CreateRelease extends ReleasePostBuilder {

    @DataBoundConstructor
    public CreateRelease(String prefix, String name, String owners, String goal, String stage, String startdate,
            String enddate, String customFields) {
        super(prefix, name, owners, goal, stage, startdate, enddate, customFields);
    }

    @Override
    public String perform() throws Exception {
        return ReleaseAPI.getInstance().create(getForm());
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.release_create();
        }
    }
}
