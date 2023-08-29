package io.jenkins.plugins.actions.pipeline;

import javax.annotation.Nonnull;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import io.jenkins.plugins.Messages;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.actions.ItemPipelineStep;
import io.jenkins.plugins.actions.PipelineStepDescriptor;
import io.jenkins.plugins.api.ItemAPI;

public class AddItemComment extends ItemPipelineStep {

    @DataBoundConstructor
    public AddItemComment(String prefix, String note) {
        super(prefix, note);
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new AddItemCommentExecutor(this, context);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {

        /**
         *
         * @return function name of the add Sprint item Action
         */
        @Override
        public String getFunctionName() {
            return "sprintsAddWorkItemComment";
        }

        /**
         *
         * @return Display name in the UI
         */
        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.add_item_comment();
        }
    }

    public static class AddItemCommentExecutor extends SynchronousNonBlockingStepExecution<Void> {
        private static final long serialVersionUID = 2L;
        private final transient AddItemComment step;

        protected AddItemCommentExecutor(AddItemComment step, @Nonnull StepContext context) {
            super(context);
            this.step = step;
        }

        @Override
        protected Void run() throws Exception {
            new ItemAPI.ItemActionBuilder(step.getPrefix(), getContext().get(Run.class),
                    getContext().get(TaskListener.class), step.item)
                    .build()
                    .addComment();
            return null;
        }
    }

}
