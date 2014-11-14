package gstorm.builders.hsqldb
import gstorm.builders.BaseUpdateSqlBuilder
import gstorm.builders.BuildResult
import gstorm.metadata.ClassMetaData

class HSQLDBUpdateSqlBuilder extends BaseUpdateSqlBuilder {

    HSQLDBUpdateSqlBuilder(ClassMetaData classMetaData, entity) {
        super(classMetaData, entity)
    }

    @Override
    BuildResult buildSqlAndValues() {

        def updateFieldPlaceHolder = []
        def values = []
        classMetaData.fields.each {
            updateFieldPlaceHolder << "${it.columnName} = ?"
            values << entity.getProperty(it.name)
        }
        def where = HSQLDBStatementBuilders.generateQuerySql(queryCondition, classMetaData, values)
        def sql = "UPDATE ${classMetaData.tableName} SET ${updateFieldPlaceHolder.join(", ")}${where}"
        new BuildResult(sql: sql, values: values)
    }
}
