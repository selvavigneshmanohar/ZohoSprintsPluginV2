package io.jenkins.plugins.actions.postbuild;

import java.util.function.Function;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.postbuild.builder.ReleasePostBuilder;
import io.jenkins.plugins.actions.postbuild.descriptor.PostBuildDescriptor;
import io.jenkins.plugins.api.ReleaseAPI;
import io.jenkins.plugins.model.Release;

public class CreateRelease extends ReleasePostBuilder {

    @DataBoundConstructor
    public CreateRelease(String prefix, String name, String owners, String goal, String stage, String startdate,
            String enddate, String customFields) {
        super(prefix, name, owners, goal, stage, startdate, enddate, customFields);
    }

    @Override
    public String perform(Function<String, String> getValueFromEnviroinmentValue) throws Exception {
        Release release = getForm();
        release.setEnviroinmentVaribaleReplacer(getValueFromEnviroinmentValue);
        return ReleaseAPI.getInstance().create(release);
    }

    @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.release_create();
        }
    }
}
