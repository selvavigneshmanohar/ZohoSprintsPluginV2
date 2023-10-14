package io.jenkins.plugins.actions.pipeline.step;

import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;

import io.jenkins.plugins.actions.pipeline.executor.PipelineStepExecutor;
import io.jenkins.plugins.model.BaseModel;

public abstract class PipelineStep extends Step {
    private BaseModel form;

    public BaseModel getForm() {
        return form;
    }

    public String getPrefix() {
        return form.getPrefix();
    }

    public PipelineStep(BaseModel form) {
        this.form = form;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new PipelineStepExecutor(form, context);
    }

}
