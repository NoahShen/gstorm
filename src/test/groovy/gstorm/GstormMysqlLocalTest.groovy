package gstorm
import groovy.sql.Sql
import gstorm.annotation.Column
import gstorm.annotation.Id
import gstorm.builders.SQLDialect
import org.junit.After
import org.junit.Before
import org.junit.Test

import java.util.logging.Level

class GstormMysqlLocalTest {


    class Item {
        @Id
        @Column(name = "ItemId")
        Integer uid

        @Column(name = "ItemName")
        String name

        @Column(name = "ItemDesc")
        def description

        @Column(name = "AddTime")
        Date addedOn
    }

    Gstorm gstorm
    Sql sql

    @Before
    void setUp() {
        sql = Sql.newInstance("jdbc:MySQL://192.168.7.104:3306/DianPingBA_FSSettle", "aspnet_dianping", "dp!@OpQW34bn", "com.mysql.jdbc.Driver")

//        sql = Sql.newInstance("jdbc:MySQL://localhost:3306/test", "user", "123", "com.mysql.jdbc.Driver")
        gstorm = new Gstorm(sql, SQLDialect.MYSQL)
        gstorm.enableQueryLogging(Level.INFO)
        gstorm.stormify(Item, true)
    }

    @After
    void tearDown() {
        sql.execute("drop table Item")
        sql.close()
    }


    @Test
    void "should be able to get by Id"() {
        def item = new Item(name: 'Spiderman', description: "desc", addedOn: new Date()).save()

        def result = Item.get(item.uid)

        assert result.name == 'Spiderman'
    }


    @Test
    void "should be able to delete by Id"() {
        def item = new Item(name: 'Spiderman', description: "desc", addedOn: new Date()).save()

        item.delete()

        assert Item.count() == 0
    }

    @Test
    void "should be able to update by annotated Id"() {
        def item = new Item(name: 'Spiderman', description: "desc", addedOn: new Date()).save()

        item.name = "updated_name"
        item.save()

        def result = Item.all()

        assert result.size() == 1
        assert result.first().name == "updated_name"
    }

}

