package io.jenkins.plugins.actions;

import java.util.logging.Logger;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.matrix.MatrixProject;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import net.sf.json.JSONObject;

public class BuildStepDescriptorImpl extends BuildStepDescriptor<Builder> {
    Logger logger = Logger.getLogger(BuildStepDescriptorImpl.class.getName());

    public BuildStepDescriptorImpl() {
        super();
        load();
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        logger.info("" + jobType.getName());
        logger.info("" + !MatrixProject.class.equals(jobType));
        return !MatrixProject.class.equals(jobType);
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) {
        req.bindJSON(this, json);
        save();
        return true;
    }

    public FormValidation doCheckPrefix(@QueryParameter final String prefix) {
        return FormValidation.validateRequired(prefix);
    }
}
