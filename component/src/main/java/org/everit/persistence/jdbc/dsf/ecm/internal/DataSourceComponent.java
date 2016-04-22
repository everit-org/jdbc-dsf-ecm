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

import javax.sql.DataSource;

import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ManualServices;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.everit.persistence.jdbc.dsf.ecm.DSFConstants;

/**
 * Simple component that registers DataSource as an OSGi service.
 */
@ExtendComponent
@Component(componentId = DSFConstants.SERVICE_FACTORYPID_DATASOURCE,
    configurationPolicy = ConfigurationPolicy.FACTORY, label = "Everit DataSource",
    description = "Instantiates and registers a new DataSource by using a DataSourceFactory"
        + " OSGi service.")
@ManualServices(@ManualService(DataSource.class))
public class DataSourceComponent extends AbstractDataSourceComponent<DataSource> {

  @Override
  protected DataSource createServiceObject(final Properties jdbcProps) throws SQLException {
    DataSource dataSource = dataSourceFactory.createDataSource(jdbcProps);
    return dataSource;
  }

  @Override
  protected Class<DataSource> getServiceType() {
    return DataSource.class;
  }
}
