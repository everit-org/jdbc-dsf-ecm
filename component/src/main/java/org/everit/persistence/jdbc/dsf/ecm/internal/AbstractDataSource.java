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

import java.util.Map;

import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.ThreeStateBoolean;
import org.everit.osgi.ecm.annotation.attribute.IntegerAttribute;
import org.everit.osgi.ecm.annotation.attribute.IntegerAttributes;
import org.everit.osgi.ecm.annotation.attribute.PasswordAttribute;
import org.everit.osgi.ecm.annotation.attribute.PasswordAttributes;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ServiceHolder;
import org.everit.persistence.jdbc.dsf.ecm.DSFConstants;
import org.everit.persistence.jdbc.dsf.ecm.PriorityConstants;
import org.osgi.framework.Constants;
import org.osgi.service.jdbc.DataSourceFactory;
import org.osgi.service.log.LogService;

/**
 * Abstract DataSource component information.
 */
@StringAttributes({
    @StringAttribute(attributeId = DataSourceFactory.JDBC_URL,
        priority = PriorityConstants.PRIORITY_02, label = "Jdbc URL",
        description = "The Jdbc Url that will be used to connect to the database."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_NETWORK_PROTOCOL,
        priority = PriorityConstants.PRIORITY_03, defaultValue = "", label = "Network protocol",
        description = "The network protocol of the JDBC channel. When Jdbc URL is provided, "
            + "normally this setting is ignored."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_SERVER_NAME,
        priority = PriorityConstants.PRIORITY_04, defaultValue = "", label = "Server name",
        description = "The name or IP address of the database server. When Jdbc URL is provided, "
            + "normally this setting is ignored."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_PORT_NUMBER,
        priority = PriorityConstants.PRIORITY_05, defaultValue = "", label = "Port",
        description = "The port where the database server listens on. When Jdbc URL is provided, "
            + "normally this setting is ignored."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_DATABASE_NAME,
        priority = PriorityConstants.PRIORITY_06, defaultValue = "", label = "Database name",
        description = "Name of the database on the server. When Jdbc URL is provided, "
            + "normally this setting is ignored."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_USER,
        priority = PriorityConstants.PRIORITY_07, defaultValue = "", label = "User name",
        description = "The name of the user that is used during database authentication."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_DATASOURCE_NAME,
        priority = PriorityConstants.PRIORITY_09, defaultValue = "", label = "DataSource name",
        description = "Name of the data source."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_DESCRIPTION,
        priority = PriorityConstants.PRIORITY_10, defaultValue = "",
        label = "DataSource description",
        description = "Description of the data source."),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_ROLE_NAME,
        priority = PriorityConstants.PRIORITY_17, defaultValue = "", label = "Role name",
        description = "The name of the role that the datasource will connect with."),
    @StringAttribute(attributeId = DSFConstants.ATTR_CUSTOM_PROPERTIES, defaultValue = "",
        multiple = ThreeStateBoolean.TRUE, priority = PriorityConstants.PRIORITY_18,
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
        priority = PriorityConstants.PRIORITY_07, label = "Password",
        description = "Password that is used during database authentication.") })
@IntegerAttributes({
    @IntegerAttribute(attributeId = DSFUtil.PROP_LOGIN_TIMEOUT, defaultValue = 0,
        priority = PriorityConstants.PRIORITY_19, label = "Login timeout",
        description = "Sets the maximum time in seconds that this data source will wait while "
            + "attempting to connect to a database.  A value of zero specifies that the timeout "
            + "is the default system timeout if there is one; otherwise, it specifies that there "
            + "is no timeout.") })
public abstract class AbstractDataSource {

  protected DataSourceFactory dataSourceFactory;

  protected Map<String, Object> dataSourceFactoryProperties;

  protected LogService logService;

  @ServiceRef(defaultValue = "", attributePriority = PriorityConstants.PRIORITY_01,
      label = "DataSourceFactory OSGi service filter",
      description = "Filter expression of the DataSourceFactory OSGi service.")
  public void setDataSourceFactory(final ServiceHolder<DataSourceFactory> dataSourceFactory) {
    this.dataSourceFactory = dataSourceFactory.getService();
    dataSourceFactoryProperties = dataSourceFactory.getAttributes();
  }

  @ServiceRef(defaultValue = "", attributePriority = PriorityConstants.PRIORITY_20,
      label = "LogService OSGi service filter",
      description = "Filter expression of the DataSourceFactory OSGi service.")
  public void setLogService(final LogService logService) {
    this.logService = logService;
  }
}
