package io.jenkins.plugins.actions.pipeline;

import java.util.function.Function;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import io.jenkins.plugins.actions.pipeline.descriptor.PipelineStepDescriptor;
import io.jenkins.plugins.actions.pipeline.executor.PipelineStepExecutor;
import io.jenkins.plugins.actions.pipeline.step.SprintsPipelineStep;
import io.jenkins.plugins.api.SprintAPI;
import io.jenkins.plugins.exception.ZSprintsException;
import io.jenkins.plugins.util.Util;

public class CreateSprint extends SprintsPipelineStep {

    @DataBoundConstructor
    public CreateSprint(String prefix, String name, String description, String scrummaster, String users,
            String duration, String startdate, String enddate) {
        super(prefix, name, description, scrummaster, users, duration, startdate, enddate);
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        setEnvironmentVariableReplacer(context);
        Function<String, String> executor = (key) -> {
            try {
                return SprintAPI.getInstance().create(getForm());
            } catch (Exception e) {
                throw new ZSprintsException(e.getMessage());
            }

        };
        return new PipelineStepExecutor(executor, context);
    }

    @Extension
    public static final class DescriptorImpl extends PipelineStepDescriptor {
        @Override
        public String getFunctionName() {
            return "createSprints";
        }

        @Override
        public String getDisplayName() {
            return Messages.sprint_create();
        }

        public FormValidation doCheckName(@QueryParameter final String name) {
            return Util.validateRequired(name);
        }
    }

}
