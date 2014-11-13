package gstorm.builders.hsqldb

import gstorm.builders.query.condition.*
import gstorm.metadata.ClassMetaData

/**
 * Created by noahshen on 14-11-13.
 */
class HSQLDBConditions {

    static String generateConditionSql(Condition c, ClassMetaData metaData, List values) {
        if (c instanceof AndCondition) {
            def subConditions = c.conditions.collect {
                generateConditionSql(it, metaData, values)
            }.join(" AND ")
            return "( ${subConditions} )"
        } else if (c instanceof OrCondition) {
            def subConditions = c.conditions.collect {
                generateConditionSql(it, metaData, values)
            }.join(" OR ")
            return "( ${subConditions} )"
        } else if (c instanceof PropertyNameCondition) {
            def columnName = metaData[c.propertyName]?.columnName
            if (c instanceof PropertyValueCondition) {
                if (c instanceof Equals) {
                    values << c.value
                    return "${columnName} = ?"
                } else if (c instanceof NotEquals) {
                    values << c.value
                    return "${columnName} <> ?"
                } else if (c instanceof GreaterThan) {
                    values << c.value
                    return "${columnName} > ?"
                } else if (c instanceof GreaterThanEquals) {
                    values << c.value
                    return "${columnName} >= ?"
                }  else if (c instanceof LessThan) {
                    values << c.value
                    return "${columnName} < ?"
                } else if (c instanceof LessThanEquals) {
                    values << c.value
                    return "${columnName} <= ?"
                } else if (c instanceof In) {
                    def placeHolder = c.value.collect {
                        values << it
                        "?"
                    }.join(", ")
                    return "${columnName} IN (${placeHolder})"
                }  else if (c instanceof NotIn) {
                    def placeHolder = c.value.collect {
                        values << it
                        "?"
                    }.join(", ")
                    return "${columnName} NOT IN (${placeHolder})"
                } else if (c instanceof Like) {
                    values << c.value
                    return "${columnName} LIKE ?"
                } else if (c instanceof Between) {
                    values << c.from
                    values << c.to
                    return "${columnName} BETWEEN ? AND ?"
                }
            } else if (c instanceof PropertyComparisonCondition) {
                def otherColumnName = metaData[c.otherProperty]?.columnName
                if (c instanceof EqualsProperty) {
                    return "${columnName} = ${otherColumnName}"
                } else if (c instanceof NotEqualsProperty) {
                    return "${columnName} <> ${otherColumnName}"
                } else if (c instanceof GreaterThanEqualsProperty) {
                    return "${columnName} >= ${otherColumnName}"
                } else if (c instanceof GreaterThanProperty) {
                    return "${columnName} > ${otherColumnName}"
                } else if (c instanceof LessThanEqualsProperty) {
                    return "${columnName} <= ${otherColumnName}"
                } else if (c instanceof LessThanProperty) {
                    return "${columnName} < ${otherColumnName}"
                }
            } else if (c instanceof IsEmpty) {
                return "${columnName} = ''"
            } else if (c instanceof IsNotEmpty) {
                return "${columnName} <> ''"
            } else if (c instanceof IsNotNull) {
                return "${columnName} IS NOT NULL"
            } else if (c instanceof IsNull) {
                return "${columnName} IS NULL"
            }
        }
    }
}
