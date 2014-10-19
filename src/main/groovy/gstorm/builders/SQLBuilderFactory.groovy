package gstorm.builders

import gstorm.builders.hsqldb.CreateTableQueryBuilder
import gstorm.builders.mysql.MySqlCreateTableQueryBuilder
import gstorm.metadata.ClassMetaData

/**
 * sql builder factory
 */
class SQLBuilderFactory {

    private static volatile SQLBuilderFactory instance

    private SQLBuilderFactory() {
    }

    static SQLBuilderFactory getInstance() {
        if (!instance) {
            synchronized (SQLBuilderFactory) {
                if (!instance) {
                    instance = new SQLBuilderFactory()
                }
            }
        }
        instance
    }

    AbstractQueryBuilder createCreateTableBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new CreateTableQueryBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlCreateTableQueryBuilder(classMetaData);
        }
    }
}
