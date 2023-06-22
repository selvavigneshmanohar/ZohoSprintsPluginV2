package io.jenkins.plugins.actions;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.matrix.MatrixProject;
import hudson.model.BuildListener;
import hudson.model.Run;
import hudson.model.Result;
import hudson.model.AbstractProject;
import hudson.model.TaskListener;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.util.FormValidation;
import io.jenkins.plugins.Messages;
import jenkins.tasks.SimpleBuildWrapper;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.apache.commons.lang.StringUtils.isEmpty;
import static io.jenkins.plugins.util.Util.replaceEnvVaribaleToValue;

/**
 * @author selvavignesh.m
 * @version 1.0
 */
public class AddWorkItemOnFailure extends SimpleBuildWrapper {
    private String prefix, name, description, status, type, priority, duration, startdate, enddate, customFields;

    @DataBoundConstructor
    public AddWorkItemOnFailure(String prefix, String name, String description, String status, String type,
            String priority,
            String duration, String startdate, String enddate, String customFields) {
        this.prefix = prefix;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.priority = priority;
        this.duration = duration;
        this.startdate = startdate;
        this.enddate = enddate;
        this.customFields = customFields;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getPriority() {
        return priority;
    }

    public String getDuration() {
        return duration;
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

    /**
     *
     * @param context            BuildWrapper context
     * @param build              Current Build Object
     * @param workspace          File path of the Workspace
     * @param launcher           Responsible for inherit the Global Variable
     * @param listener           Listener Objetc of Task
     * @param initialEnvironment Environmental Variables
     * @throws InterruptedException when a thread that is sleeping, waiting, or is
     *                              occupied is interrupted
     * @throws IOException          Input/Output error
     */
    @Override
    public void setUp(Context context, Run<?, ?> build, FilePath workspace, Launcher launcher,
            TaskListener listener, EnvVars initialEnvironment) throws IOException, InterruptedException {
        String itemPrefix, itemName;
        final Map<String, String> envMap = new HashMap<>();
        try {
            itemPrefix = replaceEnvVaribaleToValue(build, listener, prefix);
            itemName = replaceEnvVaribaleToValue(build, listener, name);
            envMap.put("SPRINTS_ISSUE_NAME", itemName);
            envMap.put("SPRINTS_ISSUE_DESCRIPTION", replaceEnvVaribaleToValue(build, listener, description));
            envMap.put("SPRINTS_ISSUE_PREFIX", itemPrefix);
            // envMap.put("SPRINTS_ISSUE_ASSIGNEE", replaceEnvVaribaleToValue(build,
            // listener,name));
            envMap.put("SPRINTS_ISSUE_TYPE", replaceEnvVaribaleToValue(build, listener, type));
            envMap.put("SPRINTS_ISSUE_STATUS", replaceEnvVaribaleToValue(build, listener, status));
            envMap.put("SPRINTS_ISSUE_DURATION", replaceEnvVaribaleToValue(build, listener, duration));
            envMap.put("SPRINTS_ISSUE_PRIORITY", replaceEnvVaribaleToValue(build, listener, priority));
            envMap.put("SPRINTS_ISSUE_STARTDATE", replaceEnvVaribaleToValue(build, listener, startdate));
            envMap.put("SPRINTS_ISSUE_ENDDATE", replaceEnvVaribaleToValue(build, listener, enddate));
            envMap.put("SPRINTS_ISSUE_CUSTOMFIELD", replaceEnvVaribaleToValue(build, listener, customFields));
            envMap.put("SPRINTS_ISSUE_BUILD_ENVIRONMENT_AVAILABLE", Boolean.toString(true));
            if (isEmpty(itemPrefix)) {
                throw new IllegalArgumentException("Prefix should not be empty or null");
            }
            if (isEmpty(itemName)) {
                throw new IllegalArgumentException("Item name is not specified");
            }
        } catch (Exception e) {
            if (listener instanceof BuildListener) {
                ((BuildListener) listener).finished(Result.FAILURE);
            }
        }
        context.getEnv().putAll(envMap);
    }

    /**
     *
     * @return Descriptor of this Class
     */
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    /**
     * @author selvavignesh.m
     * @version 1.0
     */
    @Extension
    public static class DescriptorImpl extends BuildWrapperDescriptor {
        /**
         *
         * @param item project object
         * @return if Sprints plugin Authendicated the true, or false
         */
        @Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            return !(item instanceof MatrixProject);

        }

        /**
         *
         * @return Display Name of the Build ennvironmanatal pace
         */
        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.issue_in_failure();
        }

        /**
         *
         * @param prefix To where the item to be created {prefix} Backlog/sprint
         * @return if prefix matches the regex then true else error message
         */
        public FormValidation doCheckPrefix(@QueryParameter final String prefix) {
            return FormValidation.validateRequired(prefix);
        }

        /**
         *
         * @param name Name of the Sprints Item
         * @return if param is not null or empty then OK else Error
         */
        public FormValidation doCheckName(@QueryParameter final String name) {
            return FormValidation.validateRequired(name);
        }

        /**
         *
         * @param req  request obj
         * @param json Object which contains values and key
         * @return true/false
         * @throws FormException if querying of form throws an error
         */
        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
            req.bindJSON(this, json);
            save();
            return super.configure(req, json);
        }

        public FormValidation doCheckStatus(@QueryParameter final String status) {
            if (!isEmpty(status)) {
                return FormValidation.ok();
            }
            return FormValidation.validateRequired(status);
        }

        public FormValidation doCheckType(@QueryParameter final String type) {
            if (!isEmpty(type)) {
                return FormValidation.ok();
            }
            return FormValidation.validateRequired(type);
        }

        public FormValidation doCheckPriority(@QueryParameter final String priority) {
            if (!isEmpty(priority)) {
                return FormValidation.ok();
            }
            return FormValidation.validateRequired(priority);
        }
    }
}
