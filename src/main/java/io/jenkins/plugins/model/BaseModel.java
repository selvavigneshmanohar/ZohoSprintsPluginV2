package io.jenkins.plugins.model;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseModel {
    public static final Pattern ZS_PROJECT = Pattern.compile("(?:P|p)([0-9]+).*");
    public static final Pattern ZS_SPRINT = Pattern.compile(".*(?:S|s)([0-9]+).*");
    public static final Pattern ZS_WORK_ITEM = Pattern.compile(".*(?:I|i)([0-9]+)");
    public static final Pattern ZS_RELEASE = Pattern.compile(".*(?:R|r)([0-9]+)");
    private String prefix;
    protected Function<String, String> enviroinmentVaribaleReplacer;

    public BaseModel(String prefix) {
        this.prefix = prefix;
    }

    public void setEnviroinmentVaribaleReplacer(Function<String, String> enviroinmentVaribaleReplacer) {
        this.enviroinmentVaribaleReplacer = enviroinmentVaribaleReplacer;
    }

    public String getPrefix() {
        return prefix;
    }

    public String replaceValue(Function<String, String> replacer) {
        return replacer.apply(prefix);
    }

    public String getProjectNumber() {
        return matcher(ZS_PROJECT);
    }

    public String getSprintNumber() {
        return matcher(ZS_SPRINT);
    }

    public String getReleaseNumber() {
        return matcher(ZS_RELEASE);
    }

    public String getItemNumber() {
        return matcher(ZS_WORK_ITEM);
    }

    protected String getValue(String key) {
        return enviroinmentVaribaleReplacer == null ? key : enviroinmentVaribaleReplacer.apply(key);
    }

    private String matcher(Pattern pattern) {
        Matcher matcher = pattern.matcher(enviroinmentVaribaleReplacer.apply(prefix));
        if (matcher.matches()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Invalid prefix " + prefix);
    }
}
