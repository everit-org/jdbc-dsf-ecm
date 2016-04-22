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
package org.everit.persistence.jdbc.dsf.ecm.tests;

import javax.sql.XADataSource;

import org.everit.osgi.dev.testrunner.TestRunnerConstants;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for Auto SQLTemplates Component.
 */
@ExtendComponent
@Component(componentId = "DataSourceComponentTest")
@StringAttributes({
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TESTRUNNER_ENGINE_TYPE,
        defaultValue = "junit4"),
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TEST_ID,
        defaultValue = "dataSourceComponentTest") })
@Service(XADataSourceComponentTest.class)
public class XADataSourceComponentTest {

  private XADataSource xaDataSource;

  @ServiceRef(defaultValue = "")
  public void setXADataSource(final XADataSource xaDataSource) {
    this.xaDataSource = xaDataSource;
  }

  @Test
  public void testThatComponentIsAlive() {
    Assert.assertNotNull("DataSource is not binded.", xaDataSource);
  }
}
