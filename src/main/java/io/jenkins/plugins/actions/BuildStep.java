package io.jenkins.plugins.actions;

import hudson.tasks.Builder;

public abstract class BuildStep extends Builder {

    protected String prefix;

    protected BuildStep(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public BuildStepDescriptorImpl getDescriptor() {
        return (BuildStepDescriptorImpl) super.getDescriptor();
    }
}
