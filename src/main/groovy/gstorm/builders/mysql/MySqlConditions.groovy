package gstorm.builders.mysql

import gstorm.builders.query.condition.AndCondition
import gstorm.builders.query.condition.Condition
import gstorm.builders.query.condition.OrCondition
import gstorm.metadata.ClassMetaData

/**
 * Created by noahshen on 14-11-5.
 */
class MySqlConditions {

    static String generateConditionSql(Condition condition, ClassMetaData classMetaData) {
        if (condition instanceof AndCondition) {
            return condition.conditions.collect {
                generateConditionSql(it, classMetaData)
            }.join(" AND ")
        } else if (condition instanceof OrCondition) {
            return condition.conditions.collect {
                generateConditionSql(it, classMetaData)
            }.join(" OR ")
        }
    }
}
