package io.jenkins.plugins.actions.pipeline;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.pipeline.descriptor.PipelineStepDescriptor;
import io.jenkins.plugins.actions.pipeline.executor.PipelineStepExecutor;
import io.jenkins.plugins.actions.pipeline.step.ReleasePipelineStep;
import io.jenkins.plugins.api.ReleaseAPI;
import io.jenkins.plugins.model.BaseModel;
import io.jenkins.plugins.model.Release;

public class CreateRelease extends ReleasePipelineStep {

    @DataBoundConstructor
    public CreateRelease(String prefix, String name, String owners, String goal, String stage, String owner,
            String startdate,
            String enddate, String customFields) {
        super(prefix, name, owners, goal, stage, startdate, enddate, customFields);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {
        @Override
        public String getFunctionName() {
            return "sprintsCreateRelease";
        }

        @Override
        public String getDisplayName() {
            return Messages.release_create();
        }
    }

    public static class CreateReleaseExecutor extends PipelineStepExecutor {

        protected CreateReleaseExecutor(BaseModel form, StepContext context) {
            super(form, context);
        }

        protected String execute() throws Exception {
            return ReleaseAPI.getInstance().create((Release) form);
        }

    }
}
