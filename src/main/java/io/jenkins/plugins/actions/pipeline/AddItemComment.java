package io.jenkins.plugins.actions.pipeline;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.pipeline.descriptor.PipelineStepDescriptor;
import io.jenkins.plugins.actions.pipeline.executor.PipelineStepExecutor;
import io.jenkins.plugins.actions.pipeline.step.ItemPipelineStep;
import io.jenkins.plugins.api.WorkItemAPI;
import io.jenkins.plugins.model.Item;

public class AddItemComment extends ItemPipelineStep {

    @DataBoundConstructor
    public AddItemComment(String prefix, String note) {
        super(prefix, note);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {
        @Override
        public String getFunctionName() {
            return "sprintsAddWorkItemComment";
        }

        @Override
        public String getDisplayName() {
            return Messages.add_item_comment();
        }
    }

    public static class AddItemCommentExecutor extends PipelineStepExecutor {

        protected AddItemCommentExecutor(Item form, StepContext context) {
            super(form, context);
        }

        protected String execute() throws Exception {
            return WorkItemAPI.getInstance().addComment((Item) form);
        }

    }

}
