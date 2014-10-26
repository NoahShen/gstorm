package gstorm.builders

import gstorm.metadata.ClassMetaData

class AbstractCountQueryBuilder extends AbstractWhereableQueryBuilder {

    AbstractCountQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        this.query = new StringBuilder('SELECT count(1) as "count"') // be careful with quotes here
                .append(SPACE).append("FROM ${classMetaData.tableName}")
    }

}