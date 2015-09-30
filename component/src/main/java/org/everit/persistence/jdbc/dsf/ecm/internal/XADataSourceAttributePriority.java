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

/**
 * Constants of priority.
 */
public final class XADataSourceAttributePriority {

  public static final int P11_JDBC_MAX_IDLE_TIME = 11;

  public static final int P12_JDBC_MAX_STATEMENTS = 12;

  public static final int P13_JDBC_PROPERTY_CYCLE = 13;

  public static final int P14_JDBC_INITIAL_POOL_SIZE = 14;

  public static final int P15_JDBC_MIN_POOL_SIZE = 15;

  public static final int P16_JDBC_MAX_POOL_SIZE = 16;

  private XADataSourceAttributePriority() {
  }
}
