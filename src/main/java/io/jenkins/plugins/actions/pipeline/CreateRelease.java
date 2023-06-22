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
import io.jenkins.plugins.api.ReleaseAPI;

public class CreateRelease extends Step {
    private String prefix, name, goal, stage, startdate, enddate, owner, customFields;

    public String getOwner() {
        return owner;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
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
    public CreateRelease(String prefix, String name, String owners, String goal, String stage, String owner,
            String startdate,
            String enddate, String customFields) {
        this.prefix = prefix;
        this.name = name;
        this.goal = goal;
        this.stage = stage;
        this.owner = owner;
        this.startdate = startdate;
        this.enddate = enddate;
        this.customFields = customFields;
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

        private final transient CreateRelease step;

        protected CreateReleaseExecutor(CreateRelease step, @Nonnull StepContext context) {
            super(context);
            this.step = step;
        }

        @Override
        protected Void run() throws Exception {
            new ReleaseAPI.ReleaseAPIBuilder(step.prefix, getContext().get(Run.class),
                    getContext().get(TaskListener.class))
                    .withName(step.name)
                    .withGoal(step.goal)
                    .withStage(step.stage)
                    .withStartdate(step.startdate)
                    .withEnddate(step.enddate)
                    .withOwners(step.owner)
                    .withCustomFields(step.customFields)
                    .build().update();
            return null;
        }

    }
}
