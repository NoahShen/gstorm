package gstorm.transform

import org.codehaus.groovy.ast.ClassCodeVisitorSupport
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.expr.ClosureExpression
import org.codehaus.groovy.control.SourceUnit

/**
 * Created by noahshen on 14-11-18.
 */
class WhereableTransformer extends ClassCodeVisitorSupport {

    SourceUnit sourceUnit

    WhereableTransformer(SourceUnit sourceUnit) {
        this.sourceUnit = sourceUnit
    }

    @Override
    protected SourceUnit getSourceUnit() {
        return this.sourceUnit
    }

    @Override
    void visitMethod(MethodNode node) {
        super.visitMethod(node)
    }

    @Override
    void visitField(FieldNode node) {
        super.visitField(node)
    }

    @Override
    void visitClosureExpression(ClosureExpression expression) {
        super.visitClosureExpression(expression)
    }
}
