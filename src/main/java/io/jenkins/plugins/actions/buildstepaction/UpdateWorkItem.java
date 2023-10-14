package io.jenkins.plugins.actions.buildstepaction;

import java.util.function.Function;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.buildstepaction.builder.ItemStepBuilder;
import io.jenkins.plugins.actions.buildstepaction.descriptor.BuildStepDescriptorImpl;
import io.jenkins.plugins.api.WorkItemAPI;
import io.jenkins.plugins.model.Item;

public class UpdateWorkItem extends ItemStepBuilder {

    @DataBoundConstructor
    public UpdateWorkItem(String prefix, String name, String description, String status, String type, String priority,
            String duration, String startdate, String enddate, String customFields) {
        super(prefix, name, description, status, type, priority, duration, null, startdate, enddate, customFields);
    }

    @Override
    public String perform(Function<String, String> getValueFromEnviroinmentValue) throws Exception {
        Item itemForm = getForm();
        itemForm.setEnviroinmentVaribaleReplacer(getValueFromEnviroinmentValue);
        return WorkItemAPI.getInstance().updateItem(itemForm);
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptorImpl {

        @Override
        public String getDisplayName() {
            return Messages.update_item();
        }

    }
}