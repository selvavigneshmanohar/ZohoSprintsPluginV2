package io.jenkins.plugins.actions.pipeline;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.pipeline.descriptor.PipelineStepDescriptor;
import io.jenkins.plugins.actions.pipeline.executor.PipelineStepExecutor;
import io.jenkins.plugins.actions.pipeline.step.ItemPipelineStep;
import io.jenkins.plugins.api.WorkItemAPI;
import io.jenkins.plugins.model.BaseModel;
import io.jenkins.plugins.model.Item;

public class AddWorkItem extends ItemPipelineStep {

    @DataBoundConstructor
    public AddWorkItem(String prefix, String name, String description, String status, String type, String priority,
            String duration, String startdate, String enddate, String customFields, String assignee) {
        super(prefix, name, description, status, type, priority, duration, assignee, startdate, enddate, customFields);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {

        @Override
        public String getFunctionName() {
            return "sprintsAddWorkItem";
        }

        @Override
        public String getDisplayName() {
            return Messages.create_item();
        }

    }

    public static class AddWorkItemExecutor extends PipelineStepExecutor {
        protected AddWorkItemExecutor(BaseModel form, StepContext context) {
            super(form, context);
        }

        protected String execute() throws Exception {
            return WorkItemAPI.getInstance().addItem((Item) form);
        }
    }
}
