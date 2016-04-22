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
import java.util.Properties;

import javax.sql.XADataSource;

import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ManualServices;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.everit.persistence.jdbc.dsf.ecm.DSFConstants;
import org.osgi.service.jdbc.DataSourceFactory;

/**
 * Simple component that registers XADataSource as an OSGi service.
 */
@ExtendComponent
@Component(componentId = DSFConstants.SERVICE_FACTORYPID_XA_DATASOURCE,
    configurationPolicy = ConfigurationPolicy.FACTORY, label = "Everit XADataSource",
    description = "Instantiates and registers a new XADataSource by using a DataSourceFactory"
        + " OSGi service.")
@StringAttributes({
    @StringAttribute(attributeId = DataSourceFactory.JDBC_MAX_IDLE_TIME, optional = true,
        priority = XADataSourceComponent.P11_JDBC_MAX_IDLE_TIME, label = "Max. idle time"),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_MAX_STATEMENTS, optional = true,
        priority = XADataSourceComponent.P12_JDBC_MAX_STATEMENTS, label = "Max. statements"),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_PROPERTY_CYCLE, optional = true,
        priority = XADataSourceComponent.P13_JDBC_PROPERTY_CYCLE, label = "Property lifecycle"),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_INITIAL_POOL_SIZE, optional = true,
        priority = XADataSourceComponent.P14_JDBC_INITIAL_POOL_SIZE, label = "Initial pool size"),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_MIN_POOL_SIZE, optional = true,
        priority = XADataSourceComponent.P15_JDBC_MIN_POOL_SIZE, label = "Min. pool size"),
    @StringAttribute(attributeId = DataSourceFactory.JDBC_MAX_POOL_SIZE, optional = true,
        priority = XADataSourceComponent.P16_JDBC_MAX_POOL_SIZE, label = "Max. pool size") })
@ManualServices(@ManualService(XADataSource.class))
public class XADataSourceComponent extends AbstractDataSourceComponent<XADataSource> {

  public static final int P11_JDBC_MAX_IDLE_TIME = 11;

  public static final int P12_JDBC_MAX_STATEMENTS = 12;

  public static final int P13_JDBC_PROPERTY_CYCLE = 13;

  public static final int P14_JDBC_INITIAL_POOL_SIZE = 14;

  public static final int P15_JDBC_MIN_POOL_SIZE = 15;

  public static final int P16_JDBC_MAX_POOL_SIZE = 16;

  @Override
  protected XADataSource createServiceObject(final Properties jdbcProps) throws SQLException {
    return dataSourceFactory.createXADataSource(jdbcProps);
  }

  @Override
  protected Class<XADataSource> getServiceType() {
    return XADataSource.class;
  }

}
