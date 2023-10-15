package io.jenkins.plugins.actions.pipeline;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.pipeline.descriptor.PipelineStepDescriptor;
import io.jenkins.plugins.actions.pipeline.executor.PipelineStepExecutor;
import io.jenkins.plugins.actions.pipeline.step.SprintsPipelineStep;
import io.jenkins.plugins.api.SprintAPI;
import io.jenkins.plugins.model.BaseModel;
import io.jenkins.plugins.model.Sprint;

public class StartSprint extends SprintsPipelineStep {
    @DataBoundConstructor
    public StartSprint(String prefix) {
        super(prefix);
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new StartSprintExecutor(getForm(), context);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {
        @Override
        public String getFunctionName() {
            return "StartSprint";
        }

        @Override
        public String getDisplayName() {
            return Messages.update_sprint_start();
        }
    }

    public static class StartSprintExecutor extends PipelineStepExecutor {

        protected StartSprintExecutor(BaseModel form, StepContext context) {
            super(form, context);
        }

        protected String execute() throws Exception {
            return SprintAPI.getInstance().start((Sprint) getForm());
        }
    }

}
