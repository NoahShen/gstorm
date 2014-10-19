package gstorm.builders.hsqldb

import gstorm.builders.AbstractUpdateQueryBuilder
import gstorm.metadata.ClassMetaData

class HSQLDBUpdateQueryBuilder extends AbstractUpdateQueryBuilder {

    HSQLDBUpdateQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
