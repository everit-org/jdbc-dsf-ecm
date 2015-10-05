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

import javax.sql.XADataSource;

import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.persistence.jdbc.dsf.ecm.DSFConstants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.jdbc.DataSourceFactory;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * Simple component that registers XADataSource as an OSGi service.
 */
@Component(componentId = DSFConstants.SERVICE_FACTORYPID_XA_DATASOURCE,
    configurationPolicy = ConfigurationPolicy.FACTORY, label = "Everit XADataSource",
    description = "Using a DataSourceFactory, this component registers a new XADataSource service.")
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = DataSourceFactory.JDBC_MAX_IDLE_TIME,
        priority = XADataSourceAttributePriority.P11_JDBC_MAX_IDLE_TIME, defaultValue = "",
        label = "Max. idle time"),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_MAX_STATEMENTS,
        priority = XADataSourceAttributePriority.P12_JDBC_MAX_STATEMENTS, defaultValue = "",
        label = "Max. statements"),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_PROPERTY_CYCLE,
        priority = XADataSourceAttributePriority.P13_JDBC_PROPERTY_CYCLE, defaultValue = "",
        label = "Property lifecycle"),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_INITIAL_POOL_SIZE,
        priority = XADataSourceAttributePriority.P14_JDBC_INITIAL_POOL_SIZE, defaultValue = "",
        label = "Initial pool size"),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_MIN_POOL_SIZE,
        priority = XADataSourceAttributePriority.P15_JDBC_MIN_POOL_SIZE, defaultValue = "",
        label = "Min. pool size"),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_MAX_POOL_SIZE,
        priority = XADataSourceAttributePriority.P16_JDBC_MAX_POOL_SIZE, defaultValue = "",
        label = "Max. pool size") })
@ManualService(XADataSource.class)
public class XADataSourceComponent extends DataSourceComponent {

  private ServiceRegistration<XADataSource> serviceRegistration;

  /**
   * Component activator method.
   */
  @Override
  @Activate
  public void activate(final ComponentContext<XADataSourceComponent> componentContext) {
    Map<String, Object> componentProperties = componentContext.getProperties();

    Properties jdbcProps = DSFUtil.collectDataSourceProperties(componentProperties);

    try {
      XADataSource xaDataSource = dataSourceFactory.createXADataSource(jdbcProps);

      Hashtable<String, Object> serviceProperties =
          DSFUtil.collectDataSourceServiceProperties(componentProperties,
              dataSourceFactoryProperties);

      DSFUtil.initializeDataSource(xaDataSource, componentProperties, logService);

      serviceRegistration =
          componentContext.registerService(XADataSource.class, xaDataSource, serviceProperties);
    } catch (SQLException e) {
      throw new RuntimeException("Error during creating XADataSource with properties: "
          + componentProperties.toString(), e);
    }
  }

  /**
   * Component deactivate method.
   */
  @Override
  @Deactivate
  public void deactivate() {
    if (serviceRegistration != null) {
      serviceRegistration.unregister();
    }
  }

}
