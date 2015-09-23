package com.wikia.dropwizard.logstash.appender;

import com.google.common.base.Strings;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.logging.AbstractAppenderFactory;

import java.util.HashMap;

abstract class AbstractLogstashAppenderFactory extends AbstractAppenderFactory {

  protected String host;

  protected int port;

  /**
   * <p>The string is a comma separated list of destinations in the form of hostName[:portNumber].
   *
   * If portNumber is not provided, then the configured {@link #port} will be used, which defaults
   * to {@value net.logstash.logback.appender.AbstractLogstashTcpSocketAppender#DEFAULT_PORT}
   *
   * For example, "host1.domain.com,host2.domain.com:5560"</p>
   */
  protected String destinations;

  protected boolean includeContext = true;

  protected boolean includeMdc = true;

  protected boolean includeHostname = true;

  protected HashMap<String, String> customFields;

  protected HashMap<String, String> fieldNames;

  @JsonProperty
  public String getDestinations() {
    return destinations;
  }

  @JsonProperty
  public void setDestinations(String destinations) {
    this.destinations = destinations;

    String destinationsFromEnv = System.getenv("DWLSEnc_DESTINATIONS");
    if (!Strings.isNullOrEmpty(destinationsFromEnv)) {
      this.destinations = destinationsFromEnv;
    }
  }

  @JsonProperty
  public void setHost(String host) {
    this.host = host;

    String hostFromEnv = System.getenv("DWLSEnc_HOST");
    if (!Strings.isNullOrEmpty(hostFromEnv)) {
      this.host = hostFromEnv;
    }
  }

  @JsonProperty
  public String getHost() {
    return host;
  }

  @JsonProperty
  public void setPort(int port) {
    this.port = port;

    String portFromEnv = System.getenv("DWLSEnc_PORT");
    if (!Strings.isNullOrEmpty(portFromEnv)) {
      this.port = Integer.parseInt(portFromEnv);
    }
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
}
