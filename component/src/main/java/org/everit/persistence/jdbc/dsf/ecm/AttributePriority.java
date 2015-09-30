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
 * Constants of priority.
 */
public final class AttributePriority {

  public static final int P01_DATA_SOURCE_FACTORY = 1;

  public static final int P02_JDBC_URL = 2;

  public static final int P03_JDBC_NETWORK_PROTOCOL = 3;

  public static final int P04_JDBC_SERVER_NAME = 4;

  public static final int P05_JDBC_PORT_NUMBER = 5;

  public static final int P06_JDBC_DATABASE_NAME = 6;

  public static final int P07_JDBC_USER = 7;

  public static final int P08_JDBC_PASSWORD = 8;

  public static final int P09_JDBC_DATASOURCE_NAME = 9;

  public static final int P10_JDBC_DESCRIPTION = 10;

  public static final int P11_JDBC_MAX_IDLE_TIME = 11;

  public static final int P12_JDBC_MAX_STATEMENTS = 12;

  public static final int P13_JDBC_PROPERTY_CYCLE = 13;

  public static final int P14_JDBC_INITIAL_POOL_SIZE = 14;

  public static final int P15_JDBC_MIN_POOL_SIZE = 15;

  public static final int P16_JDBC_MAX_POOL_SIZE = 16;

  public static final int P17_JDBC_ROLE_NAME = 17;

  public static final int P18_CUSTOM_PROPERTIES = 18;

  public static final int P19_LOGIN_TIMEOUT = 19;

  public static final int P20_LOG_SERVICE = 20;

  public static final int PRIORITY_21 = 21;

  private AttributePriority() {
  }
}
