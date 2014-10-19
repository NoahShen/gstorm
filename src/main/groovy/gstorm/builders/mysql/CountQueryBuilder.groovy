package gstorm.builders.mysql

import gstorm.builders.AbstractWhereableQueryBuilder
import gstorm.metadata.ClassMetaData

class CountQueryBuilder extends AbstractWhereableQueryBuilder {

    CountQueryBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        this.query = new StringBuilder('SELECT count(*) as "count"') // be careful with quotes here
                .append(SPACE).append("FROM ${classMetaData.tableName}")
    }

}
