package io.jenkins.plugins.actions.pipeline;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.pipeline.descriptor.PipelineStepDescriptor;
import io.jenkins.plugins.actions.pipeline.executor.PipelineStepExecutor;
import io.jenkins.plugins.actions.pipeline.step.PipelineStep;
import io.jenkins.plugins.api.FeedStatusAPI;
import io.jenkins.plugins.model.FeedStatus;

public class AddFeedStatus extends PipelineStep {
    @DataBoundConstructor
    public AddFeedStatus(String prefix, String feed) {
        super(FeedStatus.getInstance(prefix, feed));
    }

    public String getFeed() {
        return getForm().getFeed();
    }

    public FeedStatus getForm() {
        return (FeedStatus) super.getForm();
    }

    @Extension(optional = true)
    public static final class DescriptorImpl extends PipelineStepDescriptor {
        @Override
        public String getFunctionName() {
            return "sprintsAddFeedStatus";
        }

        @Override
        public String getDisplayName() {
            return Messages.add_feed_status();
        }
    }

    public static class AddFeedStatusExecutor extends PipelineStepExecutor {

        protected AddFeedStatusExecutor(FeedStatus form, StepContext context) {
            super(form, context);
        }

        @Override
        protected String execute() throws Exception {
            return new FeedStatusAPI().addFeed((FeedStatus) form);
        }

    }
}
