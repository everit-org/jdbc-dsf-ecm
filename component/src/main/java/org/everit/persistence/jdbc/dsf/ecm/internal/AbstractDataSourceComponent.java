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

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.sql.CommonDataSource;

import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.ThreeStateBoolean;
import org.everit.osgi.ecm.annotation.attribute.IntegerAttribute;
import org.everit.osgi.ecm.annotation.attribute.IntegerAttributes;
import org.everit.osgi.ecm.annotation.attribute.PasswordAttribute;
import org.everit.osgi.ecm.annotation.attribute.PasswordAttributes;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.persistence.jdbc.dsf.ecm.DSFConstants;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.service.log.LogService;

/**
 * Simple class that contains configuration that is true for normal and xa datasources.
 *
 * @param <C>
 *          The type of the service that is registered.
 */
@StringAttributes({
    @StringAttribute(attributeId = DataSourceFactory.JDBC_URL,
        priority = AbstractDataSourceComponent.ATTR_P_JDBC_URL, label = "Jdbc URL",
        description = "The Jdbc Url that will be used to connect to the database."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_NETWORK_PROTOCOL,
        priority = AbstractDataSourceComponent.ATTR_P_JDBC_NETWORK_PROTOCOL, defaultValue = "",
        label = "Network protocol",
        description = "The network protocol of the JDBC channel. When Jdbc URL is provided, "
            + "normally this setting is ignored."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_SERVER_NAME,
        priority = AbstractDataSourceComponent.ATTR_P_JDBC_SERVER_NAME, defaultValue = "",
        label = "Server name",
        description = "The name or IP address of the database server. When Jdbc URL is provided, "
            + "normally this setting is ignored."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_PORT_NUMBER,
        priority = AbstractDataSourceComponent.ATTR_P_JDBC_PORT_NUMBER, defaultValue = "",
        label = "Port",
        description = "The port where the database server listens on. When Jdbc URL is provided, "
            + "normally this setting is ignored."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_DATABASE_NAME,
        priority = AbstractDataSourceComponent.ATTR_P_JDBC_DATABASE_NAME, defaultValue = "",
        label = "Database name",
        description = "Name of the database on the server. When Jdbc URL is provided, "
            + "normally this setting is ignored."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_USER,
        priority = AbstractDataSourceComponent.ATTR_P_JDBC_USER, defaultValue = "",
        label = "User name",
        description = "The name of the user that is used during database authentication."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_DATASOURCE_NAME,
        priority = AbstractDataSourceComponent.ATTR_P_JDBC_DATASOURCE_NAME, defaultValue = "",
        label = "DataSource name",
        description = "Name of the data source."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_DESCRIPTION,
        priority = AbstractDataSourceComponent.ATTR_P_JDBC_DESCRIPTION, defaultValue = "",
        label = "DataSource description",
        description = "Description of the data source."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_ROLE_NAME,
        priority = AbstractDataSourceComponent.ATTR_P_JDBC_ROLE_NAME, defaultValue = "",
        label = "Role name",
        description = "The name of the role that the datasource will connect with."),
    @StringAttribute(attributeId = DSFConstants.ATTR_CUSTOM_PROPERTIES, defaultValue = "",
        multiple = ThreeStateBoolean.TRUE,
        priority = AbstractDataSourceComponent.ATTR_P_CUSTOM_PROPERTIES,
        label = "Custom properties",
        description = "An array of custom properties that can be specified for DataSourceFactory "
            + "calls. The syntax is 'name=value' where the name cannot contain '=' character. "
            + "There are no escaping rules like in properties files, just type the text that "
            + "you need. Do not use spaces before and after the '=' character!"),
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION, defaultValue = "",
        label = "Service Description",
        description = "The description of this component configuration. It is used to easily "
            + "identify the service registered by this component.") })
@PasswordAttributes({
    @PasswordAttribute(attributeId = DataSourceFactory.JDBC_PASSWORD, defaultValue = "",
        priority = AbstractDataSourceComponent.ATTR_P_JDBC_PASSWORD, label = "Password",
        description = "Password that is used during database authentication.") })
@IntegerAttributes({
    @IntegerAttribute(attributeId = DSFConstants.ATTR_LOGIN_TIMEOUT, defaultValue = 0,
        priority = AbstractDataSourceComponent.ATTR_P_LOGIN_TIMEOUT, label = "Login timeout",
        description = "Sets the maximum time in seconds that this data source will wait while "
            + "attempting to connect to a database.  A value of zero specifies that the timeout "
            + "is the default system timeout if there is one; otherwise, it specifies that there "
            + "is no timeout.") })
public abstract class AbstractDataSourceComponent<C extends CommonDataSource> {

  public static final int ATTR_P_CUSTOM_PROPERTIES = 18;

  public static final int ATTR_P_DATA_SOURCE_FACTORY = 1;

  public static final int ATTR_P_JDBC_DATABASE_NAME = 6;

  public static final int ATTR_P_JDBC_DATASOURCE_NAME = 9;

  public static final int ATTR_P_JDBC_DESCRIPTION = 10;

  public static final int ATTR_P_JDBC_NETWORK_PROTOCOL = 3;

  public static final int ATTR_P_JDBC_PASSWORD = 8;

  public static final int ATTR_P_JDBC_PORT_NUMBER = 5;

  public static final int ATTR_P_JDBC_ROLE_NAME = 17;

  public static final int ATTR_P_JDBC_SERVER_NAME = 4;

  public static final int ATTR_P_JDBC_URL = 2;

  public static final int ATTR_P_JDBC_USER = 7;

  public static final int ATTR_P_LOG_SERVICE = 20;

  public static final int ATTR_P_LOGIN_TIMEOUT = 19;

  protected DataSourceFactory dataSourceFactory;

  protected LogService logService;

  private ServiceRegistration<C> serviceRegistration;

  /**
   * Component activator method.
   */
  @Activate
  public void activate(final ComponentContext<C> componentContext) {
    Map<String, Object> componentProperties = componentContext.getProperties();

    Hashtable<String, Object> serviceProperties =
        new Hashtable<String, Object>(componentProperties);

    Properties jdbcProps = DSFUtil.collectDataSourceProperties(componentProperties);

    try {
      C dataSource = createServiceObject(jdbcProps);

      DSFUtil.initializeDataSource(dataSource, componentProperties, logService);

      serviceRegistration =
          componentContext.registerService(getServiceType(), dataSource, serviceProperties);
    } catch (SQLException e) {
      throw new RuntimeException("Error during creating " + getServiceType() + " with properties: "
          + componentProperties.toString(), e);
    }
  }

  protected abstract C createServiceObject(final Properties jdbcProps) throws SQLException;

  /**
   * Component deactivate method.
   */
  @Deactivate
  public void deactivate() {
    if (serviceRegistration != null) {
      serviceRegistration.unregister();
    }
  }

  protected abstract Class<C> getServiceType();

  @ServiceRef(defaultValue = "",
      attributePriority = AbstractDataSourceComponent.ATTR_P_DATA_SOURCE_FACTORY,
      label = "DataSourceFactory OSGi service filter",
      description = "Filter expression of the DataSourceFactory OSGi service.")
  public void setDataSourceFactory(final DataSourceFactory dataSourceFactory) {
    this.dataSourceFactory = dataSourceFactory;
  }

  @ServiceRef(defaultValue = "", attributePriority = AbstractDataSourceComponent.ATTR_P_LOG_SERVICE,
      label = "LogService OSGi service filter",
      description = "Filter expression of the DataSourceFactory OSGi service.")
  public void setLogService(final LogService logService) {
    this.logService = logService;
  }
}
