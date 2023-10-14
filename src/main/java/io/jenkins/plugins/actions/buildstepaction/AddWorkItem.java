package io.jenkins.plugins.actions.buildstepaction;

import static io.jenkins.plugins.util.Util.isEmpty;

import java.util.function.Function;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.buildstepaction.builder.ItemStepBuilder;
import io.jenkins.plugins.actions.buildstepaction.descriptor.BuildStepDescriptorImpl;
import io.jenkins.plugins.api.WorkItemAPI;
import io.jenkins.plugins.model.Item;

public class AddWorkItem extends ItemStepBuilder {

    @DataBoundConstructor
    public AddWorkItem(String prefix, String name, String description, String status, String type, String priority,
            String duration, String assignee, String startdate, String enddate, String customFields) {
        super(prefix, name, description, status, type, priority, duration, assignee, startdate, enddate, customFields);
    }

    @Override
    public String perform(Function<String, String> getValueFromEnviroinmentValue) throws Exception {
        Item itemForm = getForm();
        itemForm.setEnviroinmentVaribaleReplacer(getValueFromEnviroinmentValue);
        return WorkItemAPI.getInstance().addItem(itemForm);
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {
        @Override
        public String getDisplayName() {
            return Messages.create_item();
        }

        public FormValidation doCheckName(@QueryParameter final String name) {
            if (!name.isEmpty()) {
                return FormValidation.ok();
            }
            return FormValidation.validateRequired(name);
        }

        public FormValidation doCheckStatus(@QueryParameter final String status) {
            if (!isEmpty(status)) {
                return FormValidation.ok();
            }
            return FormValidation.validateRequired(status);
        }

        public FormValidation doCheckType(@QueryParameter final String type) {
            if (!isEmpty(type)) {
                return FormValidation.ok();
            }
            return FormValidation.validateRequired(type);
        }

        public FormValidation doCheckPriority(@QueryParameter final String priority) {
            if (!isEmpty(priority)) {
                return FormValidation.ok();
            }
            return FormValidation.validateRequired(priority);
        }

    }
}
