package io.jenkins.plugins.actions.pipeline;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.pipeline.descriptor.PipelineStepDescriptor;
import io.jenkins.plugins.actions.pipeline.executor.PipelineStepExecutor;
import io.jenkins.plugins.actions.pipeline.step.SprintsPipelineStep;
import io.jenkins.plugins.api.SprintAPI;
import io.jenkins.plugins.model.BaseModel;
import io.jenkins.plugins.model.Sprint;

public class AddSprintComment extends SprintsPipelineStep {
    @DataBoundConstructor
    public AddSprintComment(String prefix, String note) {
        super(prefix, note);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {

        @Override
        public String getFunctionName() {
            return "addSprintComment";
        }

        @Override
        public String getDisplayName() {
            return Messages.add_sprint_comment();
        }
    }

    public static class AddSprintCommentExecutor extends PipelineStepExecutor {

        protected AddSprintCommentExecutor(BaseModel step, StepContext context) {
            super(step, context);
        }

        protected String execute() throws Exception {
            return SprintAPI.getInstance().addComment((Sprint) form);
        }
    }

}
