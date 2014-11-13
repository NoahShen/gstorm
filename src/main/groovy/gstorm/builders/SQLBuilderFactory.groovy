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

    BaseCreateTableSqlBuilder createCreateTableBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBCreateTableSqlBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlCreateTableSqlBuilder(classMetaData);
        }
    }

    BaseInsertSqlBuilder createInsertQueryBuilder(SQLDialect dialect, ClassMetaData classMetaData, entity) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBInsertSqlBuilder(classMetaData, entity)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlInsertSqlBuilder(classMetaData, entity);
        }
    }

    BaseUpdateSqlBuilder createUpdateQueryBuilder(SQLDialect dialect, ClassMetaData classMetaData, entity) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBUpdateSqlBuilder(classMetaData, entity)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlUpdateSqlBuilder(classMetaData, entity);
        }
    }

    BaseDeleteSqlBuilder createDeleteQueryBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBDeleteSqlBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlDeleteSqlBuilder(classMetaData);
        }
    }

    BaseCountSqlBuilder createCountQueryBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBCountSqlBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlCountSqlBuilder(classMetaData);
        }
    }

    BaseSelectSqlBuilder createSelectQueryBuilder(SQLDialect dialect, ClassMetaData classMetaData) {
        if (dialect == SQLDialect.HSQLDB) {
            new HSQLDBSelectSqlBuilder(classMetaData)
        } else if (dialect == SQLDialect.MYSQL) {
            new MySqlSelectSqlBuilder(classMetaData);
        }
    }
}
