package gstorm.builders

import gstorm.builders.query.Query
import gstorm.builders.query.condition.Condition
import gstorm.builders.query.condition.Conditions
import gstorm.metadata.ClassMetaData

/**
 * represents a builder that can have a where clause
 */
abstract class BaseWhereableSqlBuilder extends BaseSqlBuilder {

    Query queryCondition

    BaseWhereableSqlBuilder(ClassMetaData classMetaData) {
        super(classMetaData)
        queryCondition = new Query()
    }

    def where(String clause) {
        query.append(SPACE).append("WHERE ${clause}")
        this
    }

    def byId() {
        def id = classMetaData.idFieldName ?: "id"
        this.where("${id} = ?")
    }

    @Override
    String build() {
        query.toString()
    }

    BaseWhereableSqlBuilder eq(String propertyName, value) {
        addCondition Conditions.eq(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder nq(String propertyName, value) {
        addCondition Conditions.nq(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder inList(String propertyName, List value) {
        addCondition Conditions.'in'(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder 'in'(String propertyName, List value) {
        addCondition Conditions.'in'(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder notIn(String propertyName, List value) {
        addCondition Conditions.notIn(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder like(String propertyName, String expression) {
        addCondition Conditions.like(propertyName, expression)
        this
    }

    BaseWhereableSqlBuilder between(String propertyName, from, to) {
        addCondition Conditions.between(propertyName, from, to)
        this
    }

    BaseWhereableSqlBuilder gt(String propertyName, value) {
        addCondition Conditions.gt(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder lt(String propertyName, value) {
        addCondition Conditions.lt(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder gte(String propertyName, value) {
        addCondition Conditions.gte(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder lte(String propertyName, value) {
        addCondition Conditions.lte(propertyName, value)
        this
    }

    BaseWhereableSqlBuilder isNull(String propertyName) {
        addCondition Conditions.isNull(propertyName)
        this
    }

    BaseWhereableSqlBuilder isNotNull(String propertyName) {
        addCondition Conditions.isNotNull(propertyName)
        this
    }

    BaseWhereableSqlBuilder isEmpty(String propertyName) {
        addCondition Conditions.isEmpty(propertyName)
        this
    }

    BaseWhereableSqlBuilder isNotEmpty(String propertyName) {
        addCondition Conditions.isNotEmpty(propertyName)
        this
    }

    BaseWhereableSqlBuilder eqProperty(String propertyName, String otherProperty) {
        addCondition Conditions.eqProperty(propertyName, otherProperty)
        this
    }

    BaseWhereableSqlBuilder neProperty(String propertyName, String otherProperty) {
        addCondition Conditions.neProperty(propertyName, otherProperty)
        this
    }

    BaseWhereableSqlBuilder gtProperty(String propertyName, String otherProperty) {
        addCondition Conditions.gtProperty(propertyName, otherProperty)
        this
    }

    BaseWhereableSqlBuilder geProperty(String propertyName, String otherProperty) {
        addCondition Conditions.geProperty(propertyName, otherProperty)
        this
    }

    BaseWhereableSqlBuilder ltProperty(String propertyName, String otherProperty) {
        addCondition Conditions.ltProperty(propertyName, otherProperty)
        this
    }

    BaseWhereableSqlBuilder leProperty(String propertyName, String otherProperty) {
        addCondition Conditions.leProperty(propertyName, otherProperty)
        this
    }

    // TODO andCondition orCondition


    private addCondition(Condition c) {
        queryCondition.addCondition(c)
    }
}
