package gstorm.builders.mysql

import gstorm.builders.BaseInsertSqlBuilder
import gstorm.metadata.ClassMetaData

class MySqlInsertSqlBuilder extends BaseInsertSqlBuilder {

    MySqlInsertSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }
}
