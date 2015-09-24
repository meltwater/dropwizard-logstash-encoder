package com.wikia.dropwizard.logstash.appender;

import com.google.common.base.Strings;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.logging.AbstractAppenderFactory;

import java.util.HashMap;

abstract class AbstractLogstashAppenderFactory extends AbstractAppenderFactory {

  /**
   * <p>FOR LOGSTASH-SOCKET (UDP)!
   *
   * {@link host} and {@link port} need to be set if logstash-socket are to be used. The
   * corresponding counter-part for TCP (logstash-tcp) is {@link destinations}.
   *
   * Can be overridden by setting $DWLSEnc_HOST in your target environment.</p>
   */
  protected String host;

  /**
   * <p>FOR LOGSTASH-SOCKET (UDP)!
   *
   * {@link host} and {@link port} need to be set if logstash-socket are to be used. The
   * corresponding counter-part for TCP (logstash-tcp) is {@link destinations}.
   *
   * Can be overridden by setting $DWLSEnc_PORT in your target environment.</p>
   */
  protected int port;

  /**
   * <p>FOR LOGSTASH-TCP (TCP)!
   *
   * The string is a comma separated list of destinations in the form of hostName[:portNumber].
   *
   * If portNumber is not provided, then the configured {@link #port} will be used, which defaults
   * to {@value net.logstash.logback.appender.AbstractLogstashTcpSocketAppender#DEFAULT_PORT}
   *
   * For example, "host1.domain.com,host2.domain.com:5560".
   *
   * The corresponding counter-part is the combination {@link host} and {@link port} for UDP
   * (logstash-socket).
   *
   * Can be overridden by setting $DWLSEnc_DESTINATIONS in your target environment.</p>
   */
  protected String destinations;

  /**
   * Legacy
   */
  protected boolean includeContext = true;

  /**
   * Legacy
   */
  protected boolean includeMdc = true;

  /**
   * If set to true we will try to get the hostname from the given environment and add it as field
   * "host" to our JSON log line.
   *
   * This is NOT {@link dockerHost} (which will set "HOSTNAME" in the JSON log line)!
   */
  protected boolean includeHostname = true;

  /**
   * <p>Adds the Meltwater "appid" field to the JSON log line.
   *
   * Can be overridden by setting $SERVICE_NAME (a Meltwater-default variable) in your target
   * environment.</p>
   */
  protected String appId;

  /**
   * <p>Adds the Meltwater "instanceid" field to the JSON log line.
   *
   * Can be overridden by setting $DOCKER_MWAPP_application_instanceid (a Meltwater-default
   * variable) in your target environment.</p>
   */
  protected String instanceId;

  /**
   * <p>Adds the docker hostname to the JSON log line.
   *
   * Can be overridden by setting $DWLSEnc_DOCKER_HOST in your target environment.</p>
   */
  protected String dockerHost;

  /**
   * Map of custom key/value pairs to add to the JSON log line.
   */
  protected HashMap<String, String> customFields;

  /**
   * Map of default logback key/value pairs to add to the JSON log line.
   */
  protected HashMap<String, String> fieldNames;

  @JsonProperty
  public String getDestinations() {
    return destinations;
  }

  @JsonProperty
  public void setDestinations(String destinations) {
    this.destinations = getEnvVariableOrElse("DWLSEnc_DESTINATIONS", destinations);
  }

  @JsonProperty
  public void setHost(String host) {
    this.host = getEnvVariableOrElse("DWLSEnc_HOST", host);
  }

  @JsonProperty
  public String getHost() {
    return host;
  }

  @JsonProperty
  public void setPort(int port) {
    this.port = getEnvVariableOrElse("DWLSEnc_PORT", port);
  }

  @JsonProperty
  public int getPort() {
    return port;
  }

  @JsonProperty
  public boolean getIncludeContext() {
    return includeContext;
  }

  @JsonProperty
  public void setIncludeContext(boolean includeContext) {
    this.includeContext = includeContext;
  }

  @JsonProperty
  public boolean getIncludeMdc() {
    return includeMdc;
  }

  @JsonProperty
  public void setIncludeMdc(boolean includeMdc) {
    this.includeMdc = includeMdc;
  }

  @JsonProperty
  public boolean getIncludeHostname() {
    return includeHostname;
  }

  @JsonProperty
  public void setIncludeHostname(boolean includeHostname) {
    this.includeHostname = includeHostname;
  }

  @JsonProperty
  public String getAppId() {
    return appId;
  }

  @JsonProperty
  public void setAppId(String appId) {
    this.appId = getEnvVariableOrElse("SERVICE_NAME", appId);
  }

  @JsonProperty
  public String getInstanceId() {
    return instanceId;
  }

  @JsonProperty
  public void setInstanceId(String instanceId) {
    this.instanceId = getEnvVariableOrElse("DOCKER_MWAPP_application_instanceid", instanceId);
  }

  @JsonProperty
  public String getDockerHost() {
    return dockerHost;
  }

  @JsonProperty
  public void setDockerHost(String dockerHost) {
    this.dockerHost = getEnvVariableOrElse("DWLSEnc_DOCKER_HOST", dockerHost);
  }

  @JsonProperty
  public HashMap<String, String> getCustomFields() {
    return customFields;
  }

  @JsonProperty
  public void setCustomFields(HashMap<String, String> customFields) {
    this.customFields = customFields;
  }

  @JsonProperty
  public HashMap<String, String> getFieldNames() {
    return fieldNames;
  }

  @JsonProperty
  public void setFieldNames(HashMap<String, String> fieldNames) {
    this.fieldNames = fieldNames;
  }

  private String getEnvVariableOrElse(String envVariable, String defaultValue) {
    String varFromEnv = System.getenv(envVariable);
    return (!Strings.isNullOrEmpty(varFromEnv)) ? varFromEnv : defaultValue;
  }

  private int getEnvVariableOrElse(String envVariable, int defaultValue) {
    String varFromEnv = System.getenv(envVariable);
    return (!Strings.isNullOrEmpty(varFromEnv)) ? Integer.parseInt(varFromEnv) : defaultValue;
  }

  public void addCustomField(String fieldName, String value) {
    if (this.customFields == null) {
      this.customFields = new HashMap<>();
    }

    this.customFields.put(fieldName, value);
  }
}
