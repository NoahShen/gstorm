package gstorm.transform

import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.tools.ast.TransformTestHelper

/**
 * Created by noahshen on 14-11-18.
 */
class WhereableASTTransformationTest extends GroovyTestCase {
    void setUp() {
        super.setUp()
    }


    void testVisit() {
        def file = new File('/Users/noahshen/workspace/gstorm/PersonService.groovy')
        assert file.exists()

        def invoker = new TransformTestHelper(new WhereableASTTransformation(), CompilePhase.CONVERSION)
        def clazz = invoker.parse(file)
        def personService = clazz.newInstance()

        assert personService
    }

    void testName() {


    }
}
