/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.persistence.jdbc.dsf.ecm.internal;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sql.CommonDataSource;

import org.everit.persistence.jdbc.dsf.ecm.DSFConstants;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.service.log.LogService;

/**
 * Utility to create DataSource.
 */
public final class DSFUtil {

  /**
   * Collect DataSource properties.
   *
   * @param componentProperties
   *          the component properties.
   * @return the {@link Properties} object.
   */
  public static Properties collectDataSourceProperties(
      final Map<String, Object> componentProperties) {
    Properties jdbcProps = new Properties();
    DSFUtil.putVisibleProperties(componentProperties, jdbcProps);
    DSFUtil.putIfNotNull(componentProperties, jdbcProps, DataSourceFactory.JDBC_PASSWORD);
    return jdbcProps;
  }

  /**
   * Collect DataSource service properties.
   *
   * @param componentProperties
   *          the component properties.
   * @param dsfServiceProperties
   *          the DataSourceFactory properties.
   * @return the service properties.
   */
  public static Hashtable<String, Object> collectDataSourceServiceProperties(
      final Map<String, Object> componentProperties,
      final Map<String, Object> dsfServiceProperties) {
    Hashtable<String, Object> serviceProperties = new Hashtable<String, Object>();
    DSFUtil.putIfNotNull(componentProperties, serviceProperties, "service.pid");
    DSFUtil.putVisibleProperties(componentProperties, serviceProperties);

    if (dsfServiceProperties != null) {
      DSFUtil.putIfNotNull(dsfServiceProperties, serviceProperties,
          DataSourceFactory.OSGI_JDBC_DRIVER_CLASS);
      DSFUtil.putIfNotNull(dsfServiceProperties, serviceProperties,
          DataSourceFactory.OSGI_JDBC_DRIVER_NAME);
      DSFUtil.putIfNotNull(dsfServiceProperties, serviceProperties,
          DataSourceFactory.OSGI_JDBC_DRIVER_VERSION);
      Object dsfServiceId = dsfServiceProperties.get("service.id");
      if (dsfServiceId != null) {
        serviceProperties.put("dataSourceFactory.service.id", dsfServiceId);
      }

      Object dsfServicePId = dsfServiceProperties.get("service.pid");
      if (dsfServicePId != null) {
        serviceProperties.put("dataSourceFactory.service.pid", dsfServicePId);
      }
    }
    return serviceProperties;
  }

  /**
   * Initialize DataSource.
   *
   * @param commonDataSource
   *          the common DataSource.
   * @param componentProperties
   *          the component properties.
   * @param logService
   *          the {@link LogService} instance.
   */
  public static void initializeDataSource(final CommonDataSource commonDataSource,
      final Map<String, Object> componentProperties, final LogService logService) {
    Integer loginTimeout = (Integer) componentProperties.get(DSFConstants.ATTR_LOGIN_TIMEOUT);
    if (loginTimeout != null) {
      try {
        commonDataSource.setLoginTimeout(loginTimeout);
      } catch (SQLException e) {
        throw new RuntimeException(
            "Could not set timeout on data source" + commonDataSource.toString(), e);
      }
    }

    try {
      commonDataSource.setLogWriter(new PrintWriter(new Writer() {

        ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<String>();

        @Override
        public void close() throws IOException {
          // Do nothing

        }

        @Override
        public void flush() throws IOException {
          StringBuilder sb = new StringBuilder();
          String message = messageQueue.poll();
          while (message != null) {
            sb.append(message);
            message = messageQueue.poll();
          }
          if (sb.length() > 0) {
            logService.log(LogService.LOG_INFO, sb.toString());
          }
        }

        @Override
        public void write(final char[] cbuf, final int off, final int len) throws IOException {
          String message = String.valueOf(cbuf, off, len);
          messageQueue.add(message);
        }
      }));
    } catch (SQLException e) {
      throw new RuntimeException(
          "Error during setting logWrtier to dataSource:" + commonDataSource.toString());
    }
  }

  private static void putIfNotNull(final Map<String, Object> source,
      final Hashtable<? super String, Object> target,
      final String key) {
    Object value = source.get(key);
    if ((value != null) && !"".equals(value.toString().trim())) {
      target.put(key, value);
    }
  }

  private static void putVisibleProperties(final Map<String, Object> source,
      final Hashtable<? super String, Object> target) {
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_URL);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_NETWORK_PROTOCOL);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_SERVER_NAME);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_PORT_NUMBER);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_DATABASE_NAME);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_USER);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_DATASOURCE_NAME);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_ROLE_NAME);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_DESCRIPTION);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_MAX_IDLE_TIME);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_MAX_STATEMENTS);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_PROPERTY_CYCLE);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_INITIAL_POOL_SIZE);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_MIN_POOL_SIZE);
    DSFUtil.putIfNotNull(source, target, DataSourceFactory.JDBC_MAX_POOL_SIZE);

    Object customPropertiesObj = source.get(DSFConstants.ATTR_CUSTOM_PROPERTIES);
    if (customPropertiesObj == null) {
      return;
    }
    if (!(customPropertiesObj instanceof String[])) {
      throw new IllegalArgumentException(DSFConstants.ATTR_CUSTOM_PROPERTIES
          + " property must have the type String[]: " + customPropertiesObj.getClass().getName());
    }
    String[] customProperties = (String[]) customPropertiesObj;
    for (String customProperty : customProperties) {
      if ((customProperty != null) && !"".equals(customProperty.trim())) {
        int indexOfEquals = customProperty.indexOf('=');
        if (indexOfEquals < 1) {
          throw new IllegalArgumentException(
              "Invalid syntax for custom property: " + customProperty);
        }
        if (!(indexOfEquals == (customProperty.length() - 1))) {
          String key = customProperty.substring(0, indexOfEquals);
          String value = customProperty.substring(indexOfEquals + 1, customProperty.length());
          target.put(key, value);
        }
      }
    }
  }

  private DSFUtil() {
  }
}
