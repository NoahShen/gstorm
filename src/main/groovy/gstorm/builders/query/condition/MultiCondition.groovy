package gstorm.builders.query.condition

/**
 * multi query condition
 */
abstract class MultiCondition implements Condition {

    List<Condition> conditions

    MultiCondition() {
    }

    MultiCondition(Condition... conditions) {
        this.conditions = conditions?.toList()
    }

    MultiCondition addCondition(Condition c) {
        if (c) {
            conditions.add(c)
        }
        return this
    }
}
