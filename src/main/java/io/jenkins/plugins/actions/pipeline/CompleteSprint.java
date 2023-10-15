package io.jenkins.plugins.actions.pipeline;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.pipeline.descriptor.PipelineStepDescriptor;
import io.jenkins.plugins.actions.pipeline.executor.PipelineStepExecutor;
import io.jenkins.plugins.actions.pipeline.step.PipelineStep;
import io.jenkins.plugins.api.SprintAPI;
import io.jenkins.plugins.model.BaseModel;
import io.jenkins.plugins.model.Sprint;

public class CompleteSprint extends PipelineStep {
    @DataBoundConstructor
    public CompleteSprint(String prefix) {
        super(Sprint.getInstance(prefix));
    }

    public Sprint getForm() {
        return (Sprint) super.getForm();
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new CompleteSprintExecutor(getForm(), context);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {
        @Override
        public String getFunctionName() {
            return "CompleteSprint";
        }

        @Override
        public String getDisplayName() {
            return Messages.update_sprint_complete();
        }
    }

    public static class CompleteSprintExecutor extends PipelineStepExecutor {
        protected CompleteSprintExecutor(BaseModel form, StepContext context) {
            super(form, context);
        }

        protected String execute() throws Exception {
            return SprintAPI.getInstance().complete((Sprint) getForm());
        }
    }

}
