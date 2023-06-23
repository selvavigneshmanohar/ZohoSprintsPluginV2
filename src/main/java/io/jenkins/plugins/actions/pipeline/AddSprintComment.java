package io.jenkins.plugins.actions.pipeline;

import javax.annotation.Nonnull;

import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.PipelineStepDescriptor;
import io.jenkins.plugins.api.SprintAPI;

public class AddSprintComment extends Step {
    private String prefix, note;

    public String getPrefix() {
        return prefix;
    }

    public String getNote() {
        return note;
    }

    @DataBoundConstructor
    public AddSprintComment(String prefix, String note) {
        this.prefix = prefix;
        this.note = note;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new AddSprintCommentExecutor(this, context);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {

        /**
         *
         * @return function name of the add Sprint item Action
         */
        @Override
        public String getFunctionName() {
            return "addSprintComment";
        }

        /**
         *
         * @return Display name in the UI
         */
        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.add_sprint_comment();
        }
    }

    public static class AddSprintCommentExecutor extends SynchronousNonBlockingStepExecution<Void> {
        private static final long serialVersionUID = 3L;
        private final transient AddSprintComment step;

        protected AddSprintCommentExecutor(AddSprintComment step, @Nonnull StepContext context) {
            super(context);
            this.step = step;
        }

        @Override
        protected Void run() throws Exception {
            new SprintAPI(step.prefix, getContext().get(TaskListener.class), getContext().get(Run.class)).addComment();
            return null;
        }
    }

}
