package gstorm.builders

import gstorm.builders.hsqldb.*
import gstorm.builders.mysql.*
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

    AbstractCreateTableQueryBuilder createCreateTableBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBCreateTableQueryBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlCreateTableQueryBuilder(classMetaData);
        }
    }

    AbstractInsertQueryBuilder createInsertQueryBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBInsertQueryBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlInsertQueryBuilder(classMetaData);
        }
    }

    AbstractUpdateQueryBuilder createUpdateQueryBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBUpdateQueryBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlUpdateQueryBuilder(classMetaData);
        }
    }

    AbstractDeleteQueryBuilder createDeleteQueryBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBDeleteQueryBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlDeleteQueryBuilder(classMetaData);
        }
    }

    AbstractCountQueryBuilder createCountQueryBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBCountQueryBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlCountQueryBuilder(classMetaData);
        }
    }

    AbstractSelectQueryBuilder createSelectQueryBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBSelectQueryBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlSelectQueryBuilder(classMetaData);
        }
    }
}
