package gstorm.builders.mysql

import gstorm.builders.BaseCountSqlBuilder
import gstorm.metadata.ClassMetaData

class MySqlCountSqlBuilder extends BaseCountSqlBuilder {

    MySqlCountSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
