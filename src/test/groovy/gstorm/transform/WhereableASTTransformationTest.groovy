package gstorm.transform
import groovy.sql.Sql
import gstorm.Gstorm
import models.Person
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.tools.ast.TransformTestHelper

import java.util.logging.Level
/**
 * Created by noahshen on 14-11-18.
 */
class WhereableASTTransformationTest extends GroovyTestCase {

    Gstorm gstorm
    Sql sql

    void setUp() {
        sql = Sql.newInstance("jdbc:hsqldb:mem:database", "sa", "", "org.hsqldb.jdbc.JDBCDriver")
        gstorm = new Gstorm(sql)
        gstorm.enableQueryLogging(Level.INFO)
        gstorm.stormify(Person, true)    }


    void testVisit() {
        new Person(name:"Noah", age: 27).save()
        def file = new File('/Users/noahshen/workspace/gstorm/PersonService.groovy')
//        def file = new File('/Users/noahshen/Documents/my_ws/gstorm/PersonService.groovy')
        assert file.exists()

        def invoker = new TransformTestHelper(new WhereableASTTransformation(), CompilePhase.CONVERSION)
        def clazz = invoker.parse(file)
        def personService = clazz.newInstance()

        def persons = personService.findPerson(10)
        assert persons
    }

}
