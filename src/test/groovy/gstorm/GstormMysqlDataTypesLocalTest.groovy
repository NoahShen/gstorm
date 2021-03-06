package gstorm

import groovy.sql.Sql
import gstorm.builders.SQLDialect
import models.ClassWithDates
import models.ClassWithNumbers

import java.text.SimpleDateFormat
import java.util.logging.Level

class GstormMysqlDataTypesLocalTest extends GroovyTestCase {
    Gstorm gstorm
    Sql sql
    def dateFormat

    void setUp() {
        sql = Sql.newInstance("jdbc:MySQL://localhost:3306/test", "user", "123", "com.mysql.jdbc.Driver")
        gstorm = new Gstorm(sql, SQLDialect.MYSQL)
        gstorm.enableQueryLogging(Level.INFO)
        gstorm.stormify(ClassWithDates, true)
        gstorm.stormify(ClassWithNumbers, true)
        dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    }

    void tearDown() {
        sql.execute("drop table ClassWithDates")
        sql.execute("drop table ClassWithNumbers")
        sql.close()
    }

    void "test if Date can be saved"() {
        def cwd = new ClassWithDates(name: "newborn", dateOfBirth: new Date()).save()
        ClassWithDates result = ClassWithDates.get(cwd.id)
        assert result.dateOfBirth instanceof Date
    }

    void "test if Date can be updated"() {
        def cwd = new ClassWithDates(name: "nicedate", dateOfBirth: dateFormat.parse("2014-07-07")).save()
        cwd.dateOfBirth = dateFormat.parse("2014-08-07")
        cwd.save()

        assert ClassWithDates.get(cwd.id).dateOfBirth == dateFormat.parse("2014-08-07")
    }

    void "test if Numbers can be saved"() {
        def cwn = new ClassWithNumbers(name: "test", age: 1, percentage: 10.23, points: 123456789098765).save()

        final age = ClassWithNumbers.get(cwn.id).age

        assert age == 1 // it will come out as class: java.math.BigDecimal
    }

    void "test if Numbers can be updated"() {
        def cwn = new ClassWithNumbers(name: "test", age: 1, points: 123456789098765).save()
        cwn.age = 12
        cwn.points = 98765431123456789
        cwn.save()

        final updated = ClassWithNumbers.get(cwn.id)
        assert updated.age == 12
        assert updated.points == 98765431123456789
        assert ClassWithNumbers.count() == 1
    }

    void "test if floats are stored"() {
        def cwn = new ClassWithNumbers(name: "test", age: 1, percentage: 10.12).save()

        ClassWithNumbers classWithNumbers = ClassWithNumbers.get(cwn.id)
        assert classWithNumbers.percentage == 10.12f
    }

    void "test if bigdecimal are stored"() {
        def cwn = new ClassWithNumbers(name: "test", age: 1, percentage: 10.12, money: 100.87G).save()

        ClassWithNumbers classWithNumbers = ClassWithNumbers.get(cwn.id)
        assert classWithNumbers.money == 100.87
    }

}

