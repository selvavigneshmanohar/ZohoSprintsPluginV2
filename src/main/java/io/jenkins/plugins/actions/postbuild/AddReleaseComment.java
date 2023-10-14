package io.jenkins.plugins.actions.postbuild;

import java.util.function.Function;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.postbuild.builder.ReleasePostBuilder;
import io.jenkins.plugins.actions.postbuild.descriptor.PostBuildDescriptor;
import io.jenkins.plugins.api.ReleaseAPI;
import io.jenkins.plugins.model.Release;

public class AddReleaseComment extends ReleasePostBuilder {

    @DataBoundConstructor
    public AddReleaseComment(String prefix, String note) {
        super(prefix, note);
    }

    @Override
    public String perform(Function<String, String> getValueFromEnviroinmentValue) throws Exception {
        Release release = getForm();
        release.setEnviroinmentVaribaleReplacer(getValueFromEnviroinmentValue);
        return ReleaseAPI.getInstance().addComment(release);
    }

    // @Extension
    public static class DescriptorImpl extends PostBuildDescriptor {

        public FormValidation doCheckNote(@QueryParameter final String note) {
            if (StringUtils.isEmpty(note)) {
                return FormValidation.validateRequired(note);
            }
            return FormValidation.ok();
        }

        @Override
        public String getDisplayName() {
            return Messages.add_release_comment();
        }
    }

}
