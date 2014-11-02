package gstorm.builders.mysql

import gstorm.builders.BaseSelectSqlBuilder
import gstorm.metadata.ClassMetaData

class MySqlSelectSqlBuilder extends BaseSelectSqlBuilder {

    MySqlSelectSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
