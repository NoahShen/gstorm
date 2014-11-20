package gstorm.transform
import gstorm.annotation.Entity
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.Statement
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

    static final Map OPERATOR_TO_WHEREABLE_METHOD_MAP = [
            "==": "eq",
            "!=": "ne",
            ">" : "gt",
            "<" : "lt",
            ">=": "ge",
            "<=": "le",
            "in": "inList"
    ]

    static final Map SUB_CONDITION_OPERATOR_MAP = [
            "&&": "and",
            "||": "or"
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
        Class entityClass = extractEntityClass(objectExpression)
        if (!entityClass) {
            super.visitMethodCallExpression(call)
            return
        }
        if (!isWhereableMethod(methodExpression, WHEREABLE_METHODS)) {
            super.visitMethodCallExpression(call)
            return
        }
        ClosureExpression whereClosure = extractLastClosureArg(argumentsExpression)
        if (!whereClosure) {
            super.visitMethodCallExpression(call)
            return
        }
        transformClosureExpression(entityClass, whereClosure)
        super.visitMethodCallExpression(call)
    }

    private void transformClosureExpression(Class entityClass, ClosureExpression closureExpression) {
        List<String> propertyNames = getPropertyNames(entityClass)
        Statement oldStatement = closureExpression.code
        BlockStatement newStatement = new BlockStatement()
        if (oldStatement instanceof BlockStatement) {
            BlockStatement oldBockStatement = (BlockStatement) oldStatement
            addBlockStatementToNewQuery(oldBockStatement, newStatement, propertyNames)
            newStatement.variableScope = oldBockStatement.variableScope
            if (newStatement.statements) {
                closureExpression.code = newStatement
            }
        }
    }

    private void addBlockStatementToNewQuery(BlockStatement oldStatement, BlockStatement newStatement, List<String> propertyNames) {
        oldStatement.statements?.each { statement ->
            if (statement instanceof ExpressionStatement) {
                if (statement.expression instanceof BinaryExpression) {
                    BinaryExpression binaryExpression = statement.expression
                    String operation = binaryExpression.operation.text
                    if (SUB_CONDITION_OPERATOR_MAP.containsKey(operation)) {
                        ExpressionStatement subCondtionStatement = createSubConditionStatement(operation, binaryExpression)
                    }

                }
            }
        }
    }

    ExpressionStatement createSubConditionStatement(String operation, BinaryExpression binaryExpression) {
        String conditionMethod = SUB_CONDITION_OPERATOR_MAP.get(operation)

        new ExpressionStatement(
                new MethodCallExpression(
                        new VariableExpression("this"),
                        new ConstantExpression(conditionMethod),
                        new ArgumentListExpression(
                                new ClosureExpression()
                        )
                )
        )
    }

    private List<String> getPropertyNames(Class aClass) {
        aClass.declaredFields.findAll { !it.synthetic }.collect {
            it.name
        }
    }

    private ClosureExpression extractLastClosureArg(Expression expression) {
        if (expression instanceof ArgumentListExpression) {
            ArgumentListExpression ale = (ArgumentListExpression) expression
            def expressions = ale.expressions
            if (expressions) {
                Expression lastArgument = expressions[-1]
                if (lastArgument instanceof ClosureExpression) {
                    return lastArgument
                } else if (expression instanceof VariableExpression) {
                    // TODO closure var
                }
            }
        }
    }

    private Boolean isWhereableMethod(Expression expression, Set whereMethods) {
        if (expression instanceof ConstantExpression) {
            ConstantExpression constantExpression = expression
            String methodName = constantExpression.value
            return whereMethods.contains(methodName)
        }

        false
    }

    private Class extractEntityClass(Expression objectExpression) {
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

        }
        if (!clazzName) {
            return null
        }
        Class aClass = Class.forName(clazzName)
        Entity entityAnnotation = aClass.getAnnotation(Entity)
        if (entityAnnotation) {
            return aClass
        }

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
