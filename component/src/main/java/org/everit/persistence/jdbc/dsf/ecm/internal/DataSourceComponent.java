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

import javax.sql.DataSource;

import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.persistence.jdbc.dsf.ecm.DSFConstants;
import org.osgi.framework.ServiceRegistration;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * Simple component that registers DataSource as an OSGi service.
 */
@Component(componentId = DSFConstants.SERVICE_FACTORYPID_DATASOURCE,
    configurationPolicy = ConfigurationPolicy.FACTORY, label = "Everit DataSource",
    description = "Using a DataSourceFactory, this component registers a new DataSource service.")
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
public class DataSourceComponent extends AbstractDataSource {

  private ServiceRegistration<DataSource> serviceRegistration;

  /**
   * Component activator method.
   */
  @Activate
  public void activate(final ComponentContext<XADataSourceComponent> componentContext) {
    Map<String, Object> componentProperties = componentContext.getProperties();

    Properties jdbcProps = DSFUtil.collectDataSourceProperties(componentProperties);

    try {
      DataSource dataSource = dataSourceFactory.createDataSource(jdbcProps);

      Hashtable<String, Object> serviceProperties =
          DSFUtil.collectDataSourceServiceProperties(componentProperties,
              dataSourceFactoryProperties);

      DSFUtil.initializeDataSource(dataSource, componentProperties, logService);

      serviceRegistration =
          componentContext.registerService(DataSource.class, dataSource, serviceProperties);
    } catch (SQLException e) {
      throw new RuntimeException("Error during creating DataSource with properties: "
          + componentProperties.toString(), e);
    }
  }

  /**
   * Component deactivate method.
   */
  @Deactivate
  public void deactivate() {
    if (serviceRegistration != null) {
      serviceRegistration.unregister();
    }
  }
}
