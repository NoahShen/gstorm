package gstorm.builders.query

import gstorm.builders.query.condition.AndCondition

/**
 * Created by noahshen on 14-10-26.
 */
class Query {

    Integer max = -1

    Integer offset = 0

    List<Order> orderBy = []

    AndCondition andCondition

    static class Order {
        Direction direction = Direction.ASC;
        String property;

        static Order desc(String property) {
            return new Order(property: property, direction: Direction.DESC);
        }

        static Order asc(String property) {
            return new Order(property: property, direction: Direction.ASC);
        }

        static enum Direction {
            ASC, DESC
        }
    }
}
