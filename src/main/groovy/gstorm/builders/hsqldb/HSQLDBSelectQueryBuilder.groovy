package gstorm.builders.hsqldb

import gstorm.builders.AbstractSelectQueryBuilder
import gstorm.metadata.ClassMetaData

class HSQLDBSelectQueryBuilder extends AbstractSelectQueryBuilder {

    HSQLDBSelectQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
