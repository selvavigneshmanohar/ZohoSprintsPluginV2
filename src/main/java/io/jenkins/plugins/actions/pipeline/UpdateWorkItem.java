package io.jenkins.plugins.actions.pipeline;

import javax.annotation.Nonnull;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.ItemPipelineStep;
import io.jenkins.plugins.actions.PipelineStepDescriptor;
import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.api.ItemAPI;

public class UpdateWorkItem extends ItemPipelineStep {

    @DataBoundConstructor
    public UpdateWorkItem(String prefix, String name, String description, String status, String type, String priority,
            String duration, String startdate, String enddate, String customFields, String assignee) {
        super(prefix, name, description, status, type, priority, duration, null, startdate, enddate, customFields);
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new UpdateWorkItemExecutor(this, context);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {
        @Override
        public String getFunctionName() {
            return "sprintsUpdateWorkItem";
        }

        /**
         *
         * @return Display name in the UI
         */
        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.update_item();

        }

    }

    public static class UpdateWorkItemExecutor extends SynchronousNonBlockingStepExecution<Void> {
        private static final long serialVersionUID = 9L;
        private final transient UpdateWorkItem step;

        protected UpdateWorkItemExecutor(UpdateWorkItem step, @Nonnull StepContext context) {
            super(context);
            this.step = step;
        }

        @Override
        protected Void run() throws Exception {
            new ItemAPI.ItemActionBuilder(step.prefix, getContext().get(Run.class),
                    getContext().get(TaskListener.class), step.item)
                    // .withName(step.name)
                    // .withDescription(step.description)
                    // .withStatus(step.status)
                    // .withPriority(step.priority)
                    // .withType(step.type)
                    // .withDuration(step.duration)
                    // .withComment(step.startdate)
                    // .withEnddate(step.enddate)
                    // .withCustomFields(step.customFields)
                    .build()
                    .update();
            return null;
        }

    }
}
