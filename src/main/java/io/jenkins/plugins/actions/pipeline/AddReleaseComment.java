package io.jenkins.plugins.actions.pipeline;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.pipeline.descriptor.PipelineStepDescriptor;
import io.jenkins.plugins.actions.pipeline.executor.PipelineStepExecutor;
import io.jenkins.plugins.actions.pipeline.step.ReleasePipelineStep;
import io.jenkins.plugins.api.ReleaseAPI;
import io.jenkins.plugins.model.BaseModel;
import io.jenkins.plugins.model.Release;

public class AddReleaseComment extends ReleasePipelineStep {

    @DataBoundConstructor
    public AddReleaseComment(String prefix, String note) {
        super(prefix, note);
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new AddReleaseCommentExecutor(getForm(), context);
    }

    // @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {
        @Override
        public String getFunctionName() {
            return "AddReleaseComment";
        }

        @Override
        public String getDisplayName() {
            return Messages.add_release_comment();
        }
    }

    public static class AddReleaseCommentExecutor extends PipelineStepExecutor {

        protected AddReleaseCommentExecutor(BaseModel form, StepContext context) {
            super(form, context);
        }

        protected String execute() throws Exception {
            return ReleaseAPI.getInstance().addComment((Release) getForm());
        }

    }

}
