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

public class StartSprint extends Step {
    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    @DataBoundConstructor
    public StartSprint(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new StartSprintExecutor(this, context);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {

        /**
         *
         * @return function name of the add Sprint item Action
         */
        @Override
        public String getFunctionName() {
            return "StartSprint";
        }

        /**
         *
         * @return Display name in the UI
         */
        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.update_sprint_start();
        }
    }

    public static class StartSprintExecutor extends SynchronousNonBlockingStepExecution<Void> {

        private final transient StartSprint step;

        protected StartSprintExecutor(StartSprint step, @Nonnull StepContext context) {
            super(context);
            this.step = step;
        }

        @Override
        protected Void run() throws Exception {
            new SprintAPI(step.prefix, getContext().get(TaskListener.class), getContext().get(Run.class))
                    .start();
            return null;
        }
    }

}
