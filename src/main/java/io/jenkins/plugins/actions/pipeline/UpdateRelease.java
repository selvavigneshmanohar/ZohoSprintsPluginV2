package io.jenkins.plugins.actions.pipeline;

import javax.annotation.Nonnull;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import io.jenkins.plugins.Messages;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.actions.PipelineStepDescriptor;
import io.jenkins.plugins.actions.ReleasePipelineStepBuilder;
import io.jenkins.plugins.api.ReleaseAPI;

public class UpdateRelease extends ReleasePipelineStepBuilder {
    @DataBoundConstructor
    public UpdateRelease(String prefix, String name, String goal, String stage, String startdate,
            String enddate, String customFields) {
        super(prefix, name, null, goal, stage, startdate, enddate, customFields);
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new UpdateReleaseExecutor(this, context);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {

        /**
         *
         * @return function name of the add Sprint item Action
         */
        @Override
        public String getFunctionName() {
            return "sprintsUpdateRelease";
        }

        /**
         *
         * @return Display name in the UI
         */
        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.release_update();
        }
    }

    public static class UpdateReleaseExecutor extends SynchronousNonBlockingStepExecution<Void> {
        private static final long serialVersionUID = 8L;
        private final transient UpdateRelease step;

        protected UpdateReleaseExecutor(UpdateRelease step, @Nonnull StepContext context) {
            super(context);
            this.step = step;
        }

        @Override
        protected Void run() throws Exception {
            new ReleaseAPI.ReleaseAPIBuilder(step.prefix, getContext().get(Run.class),
                    getContext().get(TaskListener.class), step.release)
                    .build()
                    .update();
            return null;
        }

    }
}
