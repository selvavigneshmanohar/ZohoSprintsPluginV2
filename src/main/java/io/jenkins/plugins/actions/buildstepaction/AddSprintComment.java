package io.jenkins.plugins.actions.buildstepaction;

import java.util.function.Function;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.buildstepaction.builder.SprintsStepBuilder;
import io.jenkins.plugins.actions.buildstepaction.descriptor.BuildStepDescriptorImpl;
import io.jenkins.plugins.api.SprintAPI;
import io.jenkins.plugins.model.Sprint;

public class AddSprintComment extends SprintsStepBuilder {
    @DataBoundConstructor
    public AddSprintComment(String prefix, String note) {
        super(prefix, note);
    }

    @Override
    public String perform(Function<String, String> getValueFromEnviroinmentValue) throws Exception {
        Sprint sprint = getForm();
        sprint.setEnviroinmentVaribaleReplacer(getValueFromEnviroinmentValue);
        return SprintAPI.getInstance().addComment(sprint);
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
            return Messages.add_sprint_comment();
        }

    }
}
