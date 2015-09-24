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
package org.everit.persistence.jdbc.dsf.ecm;

/**
 * Constants that make it possible to configure the DSF component programmatically.
 */
public final class DSFConstants {

  /**
   * Configuration property name of custom JDBC properties String array.
   */
  public static final String ATTR_CUSTOM_PROPERTIES = "customProperties";

  public static final String SERVICE_FACTORYPID_DATASOURCE = "org.everit.osgi.jdbc.dsf.DataSource";

  public static final String SERVICE_FACTORYPID_XA_DATASOURCE =
      "org.everit.osgi.jdbc.dsf.XADataSource";

  private DSFConstants() {
  }
}
