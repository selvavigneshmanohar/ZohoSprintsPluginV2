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
import io.jenkins.plugins.api.ProjectAPI;

public class AddFeedStatus extends Step {
    private String prefix, feed;

    public String getPrefix() {
        return prefix;
    }

    public String getFeed() {
        return feed;
    }

    @DataBoundConstructor
    public AddFeedStatus(String prefix, String feed) {
        this.prefix = prefix;
        this.feed = feed;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new AddFeedStatusExecutor(this, context);
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {
        @Override
        public String getFunctionName() {
            return "sprintsAddFeedStatus";
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.add_feed_status();
        }
    }

    public static class AddFeedStatusExecutor extends SynchronousNonBlockingStepExecution<Void> {
        private static final long serialVersionUID = 1L;
        private final transient AddFeedStatus step;

        protected AddFeedStatusExecutor(AddFeedStatus step, @Nonnull StepContext context) {
            super(context);
            this.step = step;
        }

        @Override
        protected Void run() throws Exception {
            new ProjectAPI(step.prefix, step.feed, getContext().get(Run.class),
                    getContext().get(TaskListener.class)).addFeed();
            return null;
        }
    }
}
