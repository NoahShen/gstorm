package gstorm

import groovy.sql.Sql
import models.Person

class GstormDynamicFindTest extends GroovyTestCase {

    Gstorm gstorm
    Sql sql

    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        gstorm = new Gstorm(sql)
        gstorm.stormify(Person)
    }

    void tearDown() {
        sql.execute("drop table person if exists")
        sql.close()
    }

    void "test id property is created on model object and is set to null"() {
        def person = new Person(name: 'Spiderman', age: 30)
        person.save()

        assert person.id != null
    }

    // context : save
    void "test that find by with single result"() {
        new Person(name: 'Spiderman', age: 30).save()
        new Person(name: 'Batman', age: 31).save()
        new Person(name: 'Superman', age: 32).save()
        new Person(name: 'Ironman', age: 32).save()

        def persons = Person.findByName("Batman")
        assert persons[0]?.age == 31

    }

    // context : save
    void "test that find by multi result"() {
        new Person(name: 'Spiderman', age: 30).save()
        new Person(name: 'Batman', age: 31).save()
        new Person(name: 'Superman', age: 32).save()
        new Person(name: 'Ironman', age: 32).save()

        def persons = Person.findByAge(32)
        assert persons.size() == 2
        assert persons*.name == ["Superman", "Ironman"]

    }

    // context : save
    void "test that find first"() {
        new Person(name: 'Spiderman', age: 30).save()
        new Person(name: 'Batman', age: 31).save()
        new Person(name: 'Superman', age: 32).save()
        new Person(name: 'Ironman', age: 32).save()

        def person = Person.findFirstByAge(32)
        assert person
        assert person.name == "Superman"

        def person2 = Person.findFirstByAge(100)
        assert !person2

    }

    // context : save
    void "test that find by multi condition"() {
        new Person(name: 'Spiderman', age: 30).save()
        new Person(name: 'Batman', age: 31).save()
        new Person(name: 'Superman', age: 32).save()
        new Person(name: 'Ironman', age: 32).save()

        def persons = Person.findByNameAndAge('Superman', 32)
        assert persons.size() == 1
        assert persons*.name == ["Superman"]

    }

}
