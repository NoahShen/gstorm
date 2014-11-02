package gstorm.builders

import gstorm.metadata.ClassMetaData

abstract class BaseSqlBuilder {

    public static final String SPACE = " "

    ClassMetaData classMetaData
    StringBuilder query

    BaseSqlBuilder(ClassMetaData classMetaData) {
        this.classMetaData = classMetaData
    }

    abstract String build()
}
