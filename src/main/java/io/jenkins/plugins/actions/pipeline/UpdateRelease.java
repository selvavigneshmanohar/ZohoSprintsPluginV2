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
import io.jenkins.plugins.api.ReleaseAPI;

public class UpdateRelease extends Step {
    private String prefix, name, owners, goal, stage, startdate, enddate, customFields;

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public String getOwners() {
        return owners;
    }

    public String getGoal() {
        return goal;
    }

    public String getStage() {
        return stage;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getCustomFields() {
        return customFields;
    }

    @DataBoundConstructor
    public UpdateRelease(String prefix, String name, String owners, String goal, String stage, String startdate,
            String enddate, String customFields) {
        this.prefix = prefix;
        this.name = name;
        this.owners = owners;
        this.goal = goal;
        this.stage = stage;
        this.startdate = startdate;
        this.enddate = enddate;
        this.customFields = customFields;
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

        private final transient UpdateRelease step;

        protected UpdateReleaseExecutor(UpdateRelease step, @Nonnull StepContext context) {
            super(context);
            this.step = step;
        }

        @Override
        protected Void run() throws Exception {
            new ReleaseAPI.ReleaseAPIBuilder(step.prefix, getContext().get(Run.class),
                    getContext().get(TaskListener.class))
                    .withName(step.name)
                    .withGoal(step.goal)
                    .withOwners(step.owners)
                    .withStage(step.stage)
                    .withStartdate(step.startdate)
                    .withEnddate(step.enddate)
                    .withCustomFields(step.customFields)
                    .build().create();
            return null;
        }

    }
}
