package io.jenkins.plugins.configuration;

import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

import hudson.Extension;
import hudson.security.Permission;
import hudson.util.Secret;
import jenkins.model.GlobalConfiguration;
import jenkins.model.Jenkins;

@Extension
public class ZSConnectionConfiguration extends GlobalConfiguration {

    private String accountsDomain, serviceDomain, serviceAPIDomain, redirectURL, zoid;
    private Secret clientId, clientSecret, refreshToken, zsheader, accessToken;
    private Long accessTokenUpdatedTime = null;

    public ZSConnectionConfiguration() {
    }

    @Restricted(NoExternalUse.class)
    public ZSConnectionConfiguration(ZSConnection connection) {
        Jenkins.getInstance().getACL().checkPermission(Permission.CONFIGURE);
        withAccountsDomain(connection.getAccountsDomain())
                .withServiceDomain(connection.getServiceDomain())
                .withServiceAPIDomain(connection.getServiceAPIDomain())
                .withRedirectURL(connection.getRedirectURL())
                .withZoid(connection.getZoid())
                .withClientId(connection.getClientId())
                .withClientSecret(connection.getClientSecret())
                .withRefreshToken(connection.getRefreshToken())
                .withZsheader(connection.getRefreshToken());
    }

    private Secret getSecret(String value) {
        return Secret.fromString(value);
    }

    private String getString(Secret value) {
        return Secret.toString(value);
    }

    public String getZsheader() {
        return getString(zsheader);
    }

    public ZSConnectionConfiguration withZsheader(String zsheader) {
        this.zsheader = getSecret(zsheader);
        return this;
    }

    public String getAccountsDomain() {
        return accountsDomain;
    }

    private ZSConnectionConfiguration withAccountsDomain(String value) {
        this.accountsDomain = value;
        return this;
    }

    public String getServiceDomain() {
        return serviceDomain;
    }

    private ZSConnectionConfiguration withServiceDomain(String serviceDomain) {
        this.serviceDomain = serviceDomain;
        return this;
    }

    public String getServiceAPIDomain() {
        return serviceAPIDomain;
    }

    private ZSConnectionConfiguration withServiceAPIDomain(String serviceAPIDomain) {
        this.serviceAPIDomain = serviceAPIDomain;
        return this;
    }

    public String getZoid() {
        return zoid;
    }

    private ZSConnectionConfiguration withZoid(String zoid) {
        this.zoid = zoid;
        return this;
    }

    public String getClientId() {
        return getString(clientId);
    }

    private ZSConnectionConfiguration withClientId(String value) {
        this.clientId = getSecret(value);
        return this;
    }

    public String getClientSecret() {
        return getString(clientSecret);
    }

    private ZSConnectionConfiguration withClientSecret(String value) {
        this.clientSecret = getSecret(value);
        return this;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    private ZSConnectionConfiguration withRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
        return this;
    }

    public String getRefreshToken() {
        return getString(refreshToken);
    }

    private ZSConnectionConfiguration withRefreshToken(String value) {
        this.refreshToken = getSecret(value);
        return this;
    }

    public String getAccessToken() {
        return getString(accessToken);
    }

    public void setAccessToken(String value) {
        this.accessToken = getSecret(value);
    }

    public Long getAccessTokenUpdatedTime() {
        return accessTokenUpdatedTime;
    }

    public void setAccessTokenUpdatedTime(Long accessTokenUpdatedTime) {
        this.accessTokenUpdatedTime = accessTokenUpdatedTime;
    }

    public String getZSApiPath() {
        return String.format("%s/zsapi/team/%s", getServiceAPIDomain(), getZoid());
    }
}
