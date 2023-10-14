package io.jenkins.plugins.actions.pipeline.executor;

import java.io.IOException;

import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;

import hudson.model.Run;
import hudson.model.TaskListener;
import io.jenkins.plugins.model.BaseModel;

public class PipelineStepExecutor extends SynchronousNonBlockingStepExecution<Void> {
    protected BaseModel form;

    public PipelineStepExecutor(BaseModel form, StepContext context) {
        super(context);
        this.form = form;
    }

    protected String execute() throws Exception {
        return null;
    }

    @Override
    protected Void run() throws Exception {
        TaskListener listener = getContext().get(TaskListener.class);
        Run<?, ?> run = getContext().get(Run.class);
        try {
            form.setEnviroinmentVaribaleReplacer((key) -> {
                try {
                    return run.getEnvironment(listener).expand(key);
                } catch (IOException | InterruptedException e) {
                    return key;
                }
            });

            String message = execute();
            if (message != null) {
                listener.getLogger().println("[Zoho Sprints]" + message);
            }
        } catch (Exception e) {
            listener.error(e.getMessage());
        }
        return null;
    }

}
