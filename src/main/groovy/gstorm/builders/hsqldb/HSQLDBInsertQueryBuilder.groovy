package gstorm.builders.hsqldb

import gstorm.builders.AbstractInsertQueryBuilder
import gstorm.metadata.ClassMetaData

class HSQLDBInsertQueryBuilder extends AbstractInsertQueryBuilder {

    HSQLDBInsertQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
    }

}
