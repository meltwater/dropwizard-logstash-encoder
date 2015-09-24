package com.wikia.dropwizard.logstash.appender;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.net.SyslogConstants;

import com.fasterxml.jackson.annotation.JsonTypeName;

import net.logstash.logback.appender.LogstashSocketAppender;

import java.io.IOException;
import java.net.InetAddress;

@JsonTypeName("logstash-socket")
public class LogstashSocketAppenderFactory extends AbstractLogstashAppenderFactory {

  public LogstashSocketAppenderFactory() {
    port = SyslogConstants.SYSLOG_PORT;
  }

  @Override
  public Appender<ILoggingEvent> build(LoggerContext context, String applicationName,
                                       Layout<ILoggingEvent> layout) {
    final LogstashSocketAppender appender = new LogstashSocketAppender();

    appender.setName("logstash-socket-appender");
    appender.setContext(context);
    appender.setHost(host);
    appender.setPort(port);

    appender.setIncludeMdc(includeMdc);
    appender.setIncludeContext(includeContext);

    if (includeHostname) {
      try {
        addCustomField("host", InetAddress.getLocalHost().getHostName());
      } catch (IOException e) {
        System.out.println("unable to get hostname: " + e.getMessage());
      }
    }

    if (appId != null) {
      addCustomField("appid", appId);
    }

    if (instanceId != null) {
      addCustomField("instanceid", instanceId);
    }

    if (dockerHost != null) {
      addCustomField("HOSTNAME", dockerHost);
    }

    if (customFields != null) {
      try {
        String custom = LogstashAppenderFactoryHelper.getCustomFieldsFromHashMap(customFields);
        appender.setCustomFields(custom);
      } catch (IOException e) {
        System.out.println("unable to parse customFields: " + e.getMessage());
      }
    }

    if (fieldNames != null) {
      appender.setFieldNames(LogstashAppenderFactoryHelper.getFieldNamesFromHashMap(fieldNames));
    }

    addThresholdFilter(appender, threshold);
    appender.start();

    return wrapAsync(appender);
  }
}
