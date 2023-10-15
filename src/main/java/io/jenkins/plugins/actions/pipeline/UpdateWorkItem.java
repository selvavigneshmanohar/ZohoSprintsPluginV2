package io.jenkins.plugins.actions.pipeline;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.pipeline.descriptor.PipelineStepDescriptor;
import io.jenkins.plugins.actions.pipeline.executor.PipelineStepExecutor;
import io.jenkins.plugins.actions.pipeline.step.ItemPipelineStep;
import io.jenkins.plugins.api.WorkItemAPI;
import io.jenkins.plugins.model.BaseModel;
import io.jenkins.plugins.model.Item;

public class UpdateWorkItem extends ItemPipelineStep {

    @DataBoundConstructor
    public UpdateWorkItem(String prefix, String name, String description, String status, String type, String priority,
            String duration, String startdate, String enddate, String customFields, String assignee) {
        super(prefix, name, description, status, type, priority, duration, null, startdate, enddate, customFields);
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new UpdateWorkItemExecutor(getForm(), context);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {
        @Override
        public String getFunctionName() {
            return "sprintsUpdateWorkItem";
        }

        @Override
        public String getDisplayName() {
            return Messages.update_item();

        }

    }

    public static class UpdateWorkItemExecutor extends PipelineStepExecutor {

        protected UpdateWorkItemExecutor(BaseModel form, StepContext context) {
            super(form, context);
        }

        protected String execute() throws Exception {
            return WorkItemAPI.getInstance().updateItem((Item) getForm());
        }

    }
}
