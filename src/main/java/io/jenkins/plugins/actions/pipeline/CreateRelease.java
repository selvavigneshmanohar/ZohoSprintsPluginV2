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
import io.jenkins.plugins.actions.ReleasePipelineStepBuilder;
import io.jenkins.plugins.api.ReleaseAPI;

public class CreateRelease extends ReleasePipelineStepBuilder {

    @DataBoundConstructor
    public CreateRelease(String prefix, String name, String owners, String goal, String stage, String owner,
            String startdate,
            String enddate, String customFields) {
        super(prefix, name, owners, goal, stage, startdate, enddate, customFields);
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new CreateReleaseExecutor(this, context);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {
        @Override
        public String getFunctionName() {
            return "sprintsCreateRelease";
        }

        /**
         *
         * @return Display name in the UI
         */
        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.release_create();
        }
    }

    public static class CreateReleaseExecutor extends SynchronousNonBlockingStepExecution<Void> {
        private static final long serialVersionUID = 6L;
        private final transient CreateRelease step;

        protected CreateReleaseExecutor(CreateRelease step, @Nonnull StepContext context) {
            super(context);
            this.step = step;
        }

        @Override
        protected Void run() throws Exception {
            new ReleaseAPI.ReleaseAPIBuilder(step.prefix, getContext().get(Run.class),
                    getContext().get(TaskListener.class), step.release)
                    .build()
                    .create();
            return null;
        }

    }
}
