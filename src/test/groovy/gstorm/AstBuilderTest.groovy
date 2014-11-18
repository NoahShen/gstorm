package gstorm

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.builder.AstBuilder

class AstBuilderTest extends GroovyTestCase {

    void "test tableName"() {
        AstBuilder astBuilder = new AstBuilder()
        1000.times {
            List<ASTNode> nodes = astBuilder.buildFromCode {
                (id > 1) && (name = "Noah") || (age > 20)
            }
            assert nodes
        }

    }

}
