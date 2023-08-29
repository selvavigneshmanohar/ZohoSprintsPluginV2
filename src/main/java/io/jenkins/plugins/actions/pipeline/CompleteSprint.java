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

public class CompleteSprint extends Step {
    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    @DataBoundConstructor
    public CompleteSprint(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new CompleteSprintExecutor(this, context);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {

        /**
         *
         * @return function name of the add Sprint item Action
         */
        @Override
        public String getFunctionName() {
            return "CompleteSprint";
        }

        /**
         *
         * @return Display name in the UI
         */
        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.update_sprint_complete();
        }
    }

    public static class CompleteSprintExecutor extends SynchronousNonBlockingStepExecution<Void> {
        private static final long serialVersionUID = 5L;
        private final transient CompleteSprint step;

        protected CompleteSprintExecutor(CompleteSprint step, @Nonnull StepContext context) {
            super(context);
            this.step = step;
        }

        @Override
        protected Void run() throws Exception {
            new SprintAPI(step.prefix, getContext().get(TaskListener.class), getContext().get(Run.class))
                    .complete();
            return null;
        }
    }

}
