package io.jenkins.plugins.actions.pipeline;

import javax.annotation.Nonnull;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.PipelineStepDescriptor;
import io.jenkins.plugins.actions.ReleasePipelineStepBuilder;
import io.jenkins.plugins.api.ReleaseAPI;

public class AddReleaseComment extends ReleasePipelineStepBuilder {

    @DataBoundConstructor
    public AddReleaseComment(String prefix, String note) {
        super(prefix, note);
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new AddReleaseCommentExecutor(this, context);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {

        /**
         *
         * @return function name of the add Sprint item Action
         */
        @Override
        public String getFunctionName() {
            return "AddReleaseComment";
        }

        /**
         *
         * @return Display name in the UI
         */
        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.add_release_comment();
        }
    }

    public static class AddReleaseCommentExecutor extends SynchronousNonBlockingStepExecution<Void> {
        private static final long serialVersionUID = 3L;
        private final transient AddReleaseComment step;

        protected AddReleaseCommentExecutor(AddReleaseComment step, @Nonnull StepContext context) {
            super(context);
            this.step = step;
        }

        @Override
        protected Void run() throws Exception {
            new ReleaseAPI.ReleaseAPIBuilder(step.getPrefix(), getContext().get(Run.class),
                    getContext().get(TaskListener.class), step.release)
                    .build()
                    .addComment();
            return null;
        }
    }

}
