package io.jenkins.plugins.actions.pipeline;

import java.util.function.Function;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.pipeline.descriptor.PipelineStepDescriptor;
import io.jenkins.plugins.actions.pipeline.executor.PipelineStepExecutor;
import io.jenkins.plugins.actions.pipeline.step.ItemPipelineStep;
import io.jenkins.plugins.api.WorkItemAPI;
import io.jenkins.plugins.exception.ZSprintsException;

public class UpdateWorkItem extends ItemPipelineStep {

    @DataBoundConstructor
    public UpdateWorkItem(String prefix, String name, String description, String status, String type, String priority,
            String duration, String startdate, String enddate, String customFields, String assignee) {
        super(prefix, name, description, status, type, priority, duration, null, startdate, enddate, customFields);
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        setEnvironmentVariableReplacer(context);
        Function<String, String> executor = (key) -> {
            try {
                return WorkItemAPI.getInstance().updateItem(getForm());
            } catch (Exception e) {
                throw new ZSprintsException(e.getMessage());
            }

        };
        return new PipelineStepExecutor(executor, context);
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
}
