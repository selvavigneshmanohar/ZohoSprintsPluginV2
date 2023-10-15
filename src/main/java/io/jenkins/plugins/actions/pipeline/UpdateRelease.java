package io.jenkins.plugins.actions.pipeline;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.pipeline.descriptor.PipelineStepDescriptor;
import io.jenkins.plugins.actions.pipeline.executor.PipelineStepExecutor;
import io.jenkins.plugins.actions.pipeline.step.ReleasePipelineStep;
import io.jenkins.plugins.api.ReleaseAPI;
import io.jenkins.plugins.model.BaseModel;
import io.jenkins.plugins.model.Release;

public class UpdateRelease extends ReleasePipelineStep {
    @DataBoundConstructor
    public UpdateRelease(String prefix, String name, String goal, String stage, String startdate,
            String enddate, String customFields) {
        super(prefix, name, null, goal, stage, startdate, enddate, customFields);
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new UpdateReleaseExecutor(getForm(), context);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {
        @Override
        public String getFunctionName() {
            return "sprintsUpdateRelease";
        }

        @Override
        public String getDisplayName() {
            return Messages.release_update();
        }
    }

    public static class UpdateReleaseExecutor extends PipelineStepExecutor {

        protected UpdateReleaseExecutor(BaseModel form, StepContext context) {
            super(form, context);
        }

        protected String execute() throws Exception {
            return ReleaseAPI.getInstance().update((Release) getForm());
        }

    }
}
