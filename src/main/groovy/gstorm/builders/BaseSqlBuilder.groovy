package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class BaseSqlBuilder {

    ClassMetaData classMetaData

    BaseSqlBuilder(ClassMetaData classMetaData) {
        this.classMetaData = classMetaData
    }

    abstract BuildResult buildSqlAndValues()
}
