package gstorm.builders

import gstorm.builders.query.condition.AndCondition
import gstorm.builders.query.condition.OrCondition
import gstorm.metadata.ClassMetaData
import spock.lang.Specification
/**
 * Created by noahshen on 14-11-4.
 */
class BaseWhereableSqlBuilderTest extends Specification {

    BaseWhereableSqlBuilder baseWhereableSqlBuilder

    class Person {
        def name
        int age
    }

    ClassMetaData classMetaData

    void setup() {
        classMetaData = new ClassMetaData(Person)
        baseWhereableSqlBuilder = new BaseWhereableSqlBuilderMockTest(classMetaData)
    }

    def "And"() {
        setup:

        when:
        baseWhereableSqlBuilder.eq("name", "Noah").and {
            gte("age", 1)
            lt("age", 20)
        }

        then:
        baseWhereableSqlBuilder.queryCondition.conditions.size() == 2
        baseWhereableSqlBuilder.queryCondition.conditions[-1] instanceof AndCondition
    }

    def "Or"() {
        setup:

        when:
        baseWhereableSqlBuilder.eq("name", "Noah").or {
            lt("age", 18)
            gt("age", 60)
        }

        then:
        baseWhereableSqlBuilder.queryCondition.conditions.size() == 2
        baseWhereableSqlBuilder.queryCondition.conditions[-1] instanceof OrCondition
    }

    def "nested And" () {
        setup:

        when:
        baseWhereableSqlBuilder.eq("name", "Noah")
        baseWhereableSqlBuilder.and {
            eq("age", 10)
            or {
                gte("age", 30)
                lt("age", 40)
            }
        }

        then:
        baseWhereableSqlBuilder.queryCondition.conditions.size() == 2
        baseWhereableSqlBuilder.queryCondition.conditions[-1] instanceof AndCondition
        baseWhereableSqlBuilder.queryCondition.conditions[-1].conditions[-1] instanceof OrCondition
    }

    private class BaseWhereableSqlBuilderMockTest extends BaseWhereableSqlBuilder {

        BaseWhereableSqlBuilderMockTest(ClassMetaData classMetaData) {
            super(classMetaData)
        }

        @Override
        String buildSql() {
            return null
        }
    }
}
