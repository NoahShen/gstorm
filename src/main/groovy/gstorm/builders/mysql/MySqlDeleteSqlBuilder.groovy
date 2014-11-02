package gstorm.builders.mysql

import gstorm.builders.BaseDeleteSqlBuilder
import gstorm.metadata.ClassMetaData

class MySqlDeleteSqlBuilder extends BaseDeleteSqlBuilder {

    MySqlDeleteSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
