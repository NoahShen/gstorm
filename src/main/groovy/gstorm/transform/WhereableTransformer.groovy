package gstorm.transform

import gstorm.annotation.Entity
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.control.SourceUnit
/**
 * Created by noahshen on 14-11-18.
 */
class WhereableTransformer extends ClassCodeVisitorSupport {

    static final Set WHEREABLE_METHODS = [
            "find",
            "where",
            "findWhere",
            "findFirst",
            "findFirstWhere",
            "findAll",
            "count",
            "getCount"
    ]

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

    @Override
    void visitStaticMethodCallExpression(StaticMethodCallExpression call) {
        super.visitStaticMethodCallExpression(call)
    }

    @Override
    void visitImports(ModuleNode node) {
        super.visitImports(node)
    }

    @Override
    void visitClass(ClassNode node) {
        super.visitClass(node)
    }

    @Override
    void visitMethodCallExpression(MethodCallExpression call) {
        Expression objectExpression = call.getObjectExpression();
        Expression methodExpression = call.getMethod();
        Expression argumentsExpression = call.getArguments();
        if (!isEntityClass(objectExpression)) {
            super.visitMethodCallExpression(call)
        }
        if (!isWhereableMethod(methodExpression, WHEREABLE_METHODS)) {
            super.visitMethodCallExpression(call)
        }
        println 123

    }

    private Boolean isWhereableMethod(Expression expression, Set whereMethods) {
        if (expression instanceof ConstantExpression) {
            ConstantExpression constantExpression = expression
            String methodName = constantExpression.value
            return whereMethods.contains(methodName)
        }

        false
    }

    private boolean isEntityClass(Expression objectExpression) {
        String clazzName = ""
        if (objectExpression instanceof ClassExpression) {
            ClassExpression classExpression = objectExpression
            ClassNode classNode = classExpression.getType()
            clazzName = classNode.getName()

        }
        if (objectExpression instanceof VariableExpression) {
            VariableExpression variableExpression = objectExpression
            String simpleName = variableExpression.getName()
            clazzName = extractClassNameFromImport(simpleName)
            if (!clazzName) {
                return false
            }
        }
        Class aClass = Class.forName(clazzName)
        Entity entityAnnotation = aClass.getAnnotation(Entity)
        return entityAnnotation != null
    }

    private String extractClassNameFromImport(String simpleName) {
        def importNodes = sourceUnit.getAST().imports
        def entityImportNode = importNodes.find {
            it.alias == simpleName
        }
        if (entityImportNode) {
            return entityImportNode.type.name
        }
    }

    private boolean isCandidateMethodCallForTransform(Expression objectExpression, Expression method, Expression arguments) {
        return ((objectExpression instanceof ClassExpression) || isObjectExpressionWhereCall(objectExpression)) &&
                isCandidateWhereMethod(method, arguments);
    }

    private boolean isObjectExpressionWhereCall(Expression objectExpression) {
        if (objectExpression instanceof VariableExpression) {
            VariableExpression ve = (VariableExpression) objectExpression;
            return isCandidateWhereMethod(mce.getMethodAsString(), mce.getArguments());
        }
        return false;
    }

    private boolean isCandidateWhereMethod(String methodName, Expression arguments) {
        return isCandidateMethod(methodName, arguments, WHEREABLE_METHODS);
    }


}
