package gstorm
import groovy.sql.Sql
import gstorm.builders.SQLDialect
import models.Item
import models.Person
import org.junit.After
import org.junit.Before
import org.junit.Test

class GstormMysqlLocalTest {
    Gstorm gstorm
    Sql sql
    def start, end

    @Before
    void setUp() {
        sql = Sql.newInstance("jdbc:MySQL://localhost:3306/test", "user", "123", "com.mysql.jdbc.Driver")
        gstorm = new Gstorm(sql, SQLDialect.MYSQL)
        start = System.nanoTime()
    }

    @After
    void tearDown() {
//        sql.execute("drop table person if exists")
        sql.close()
    }

    @Test
    void "check the time taken for 1000 inserts"() {
        gstorm.stormify(Person, true)
        1000.times {new Person(name: 'Spiderman', age: 30).save()}
        assert sql.rows("select count(*) as total_count from person").total_count == [1000]
        printTimeTakenFor("1000 inserts")
    }

    void printTimeTakenFor(activity) {
        end = System.nanoTime()
        println "Time taken for $activity ${(end - start) / 1000000} ms"
    }

    @Test
    void "date type"() {
        gstorm.stormify(Item, true)
        1.times {new Item(name: 'Spiderman', description: "desc", addedOn: new Date()).save()}
        assert sql.rows("select count(*) as total_count from Item").total_count == [1]
    }

}

