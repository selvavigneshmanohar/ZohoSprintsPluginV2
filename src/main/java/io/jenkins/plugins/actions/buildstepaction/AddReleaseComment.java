package io.jenkins.plugins.actions.buildstepaction;

import java.util.function.Function;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.buildstepaction.builder.ReleaseStepBuilder;
import io.jenkins.plugins.actions.buildstepaction.descriptor.BuildStepDescriptorImpl;
import io.jenkins.plugins.api.ReleaseAPI;
import io.jenkins.plugins.model.Release;

public class AddReleaseComment extends ReleaseStepBuilder {

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

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {

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
