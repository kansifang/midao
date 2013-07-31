/*
 * Copyright 2013 Zakhar Prykhoda
 *
 *    midao.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.midao.jdbc.examples.derby;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.midao.jdbc.core.utils.MjdbcUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 */
public class DerbyParameters {
    public static Connection createConnection() throws SQLException {
        MjdbcUtils.loadDriver("org.apache.derby.jdbc.EmbeddedDataSource");

        EmbeddedDataSource ds = new org.apache.derby.jdbc.EmbeddedDataSource();

        ds.setCreateDatabase("create");
        ds.setDatabaseName("memory:testDb");

        Connection conn = ds.getConnection();

        return conn;
    }
}
