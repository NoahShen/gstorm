package gstorm.transform;

import gstorm.annotation.Entity;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ImportNode;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.SourceUnit;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by noahshen on 14-11-22.
 */
public class WhereableMethodTransformer  extends ClassCodeVisitorSupport {


    private static final Set<String> WHEREABLE_METHODS = new HashSet<String>(Arrays.asList("find", "where", "findWhere", "findFirst", "findFirstWhere", "findAll", "count", "getCount"));

    private static final Set<String> BASIC_OPERATOR = new HashSet<String>(Arrays.asList("eq", "nq", "gt", "lt", "gte", "lte"));

    private static final Map<String, String> WHEREABLE_OPERATOR = new HashMap<String, String>(){{
        put("==", "eq");
        put("!=", "nq");
        put(">", "gt");
        put("<", "lt");
        put(">=", "gte");
        put("<=", "lte");
        put("==", "eq");
        put("in", "inList");
    }};

    private static final Map<String, String> PROPERTY_WHEREABLE_OPERATOR = new HashMap<String, String>(){{
        put("eq", "eqProperty");
        put("nq", "neProperty");
        put("gt", "gtProperty");
        put("lt", "ltProperty");
        put("gte", "geProperty");
        put("lte", "leProperty");
    }};

    private static final Map<String, String> MULTI_CONDITION_OPERATOR_MAP = new HashMap<String, String>(){{
        put("&&", "and");
        put("||", "or");
    }};

    private SourceUnit sourceUnit;

    public WhereableMethodTransformer(SourceUnit sourceUnit) {
        this.sourceUnit = sourceUnit;
    }

    @Override
    protected SourceUnit getSourceUnit() {
        return this.sourceUnit;
    }


    @Override
    public void visitMethodCallExpression(MethodCallExpression call) {
        Expression objectExpression = call.getObjectExpression();
        Expression methodExpression = call.getMethod();
        Expression argumentsExpression = call.getArguments();
        Class entityClass = extractEntityClass(objectExpression);
        if (entityClass == null) {
            super.visitMethodCallExpression(call);
            return;
        }
        if (!isWhereableMethod(methodExpression, WHEREABLE_METHODS)) {
            super.visitMethodCallExpression(call);
            return;
        }
        ClosureExpression whereClosure = extractLastClosureArg(argumentsExpression);
        if (whereClosure == null) {
            super.visitMethodCallExpression(call);
            return;
        }
        transformClosureExpression(entityClass, whereClosure);
        super.visitMethodCallExpression(call);
    }

    private void transformClosureExpression(Class entityClass, ClosureExpression closureExpression) {
        List<String> propertyNames = getPropertyNames(entityClass);
        Statement oldStatement = closureExpression.getCode();
        BlockStatement newStatement = new BlockStatement();
        if (oldStatement instanceof BlockStatement) {
            BlockStatement oldBockStatement = (BlockStatement) oldStatement;
            addBlockStatementToNewQuery(oldBockStatement, newStatement, propertyNames);
            newStatement.setVariableScope(oldBockStatement.getVariableScope());
            if (newStatement.getStatements() != null && !newStatement.getStatements().isEmpty()) {
                closureExpression.setCode(newStatement);
            }
        }
    }

    private void addBlockStatementToNewQuery(BlockStatement oldStatement, BlockStatement newStatement, List<String> propertyNames) {
        List<Statement> oldStatements = oldStatement.getStatements();
        if (oldStatements != null) {
            for (Statement statement : oldStatements) {
                if (statement instanceof ExpressionStatement) {
                    Expression expression = ((ExpressionStatement) statement).getExpression();
                    if (expression instanceof BinaryExpression) {
                        BinaryExpression binaryExpression = (BinaryExpression) expression;
                        addNewStatement(binaryExpression, newStatement, propertyNames);
                    }
                }
            }
        }
    }

    private void addNewStatement(Expression expression,
                                 BlockStatement newStatement,
                                 List<String> propertyNames) {
        if (!(expression instanceof BinaryExpression)) {
            super.addError("unknown groovy-style whereable", expression);
            return;
        }
        BinaryExpression binaryExpression = (BinaryExpression) expression;
        if (MULTI_CONDITION_OPERATOR_MAP.containsKey(binaryExpression.getOperation().getText())) {
            addMultiConditionStatement(binaryExpression, newStatement, propertyNames);
        } else {
            addConditionStatement(binaryExpression, newStatement, propertyNames);
        }
    }

    private void addConditionStatement(BinaryExpression expression, BlockStatement newStatement, List<String> propertyNames) {
        String method = WHEREABLE_OPERATOR.get(expression.getOperation().getText());
        if (method == null) {
            super.addError("unknown groovy-style whereable", expression);
            return;
        }
        if (BASIC_OPERATOR.contains(method)) {
            addBasicCondition(method, expression, newStatement, propertyNames);
        } else if ("inList".equals(method)) {
            addInListCondition(expression, newStatement, propertyNames);
        } else {
            super.addError("unknown groovy-style whereable", expression);
        }
    }

    private void addInListCondition(BinaryExpression expression, BlockStatement newStatement, List<String> propertyNames) {
        Expression left = expression.getLeftExpression();
        if (!(left instanceof VariableExpression)) {
            super.addError("Invalid entity property", left);
            return;
        }
        String leftName = ((VariableExpression) left).getName();
        if (!propertyNames.contains(leftName)) {
            super.addError("Invalid entity property", left);
            return;
        }
        Expression right = expression.getRightExpression();
        if (right instanceof RangeExpression) {
            RangeExpression rightRangeExpr = (RangeExpression) right;
            ExpressionStatement newExpr = new ExpressionStatement(
                    new MethodCallExpression(
                            new VariableExpression("this"),
                            new ConstantExpression("inList"),
                            new ArgumentListExpression(
                                    new ConstantExpression(leftName),
                                    new RangeExpression(rightRangeExpr.getFrom(), rightRangeExpr.getTo(), rightRangeExpr.isInclusive())
                            )
                    )
            );
            newStatement.addStatement(newExpr);
        } else if (right instanceof VariableExpression) {
            VariableExpression rightVariableExpr = (VariableExpression) right;
            ExpressionStatement newExpr = new ExpressionStatement(
                    new MethodCallExpression(
                            new VariableExpression("this"),
                            new ConstantExpression("inList"),
                            new ArgumentListExpression(
                                    new ConstantExpression(leftName),
                                    new VariableExpression(rightVariableExpr.getName())
                            )
                    )
            );
            newStatement.addStatement(newExpr);
        } else {
            super.addError("Unknown expression", right);
        }
    }

    private void addBasicCondition(String method, BinaryExpression expression, BlockStatement newStatement, List<String> propertyNames) {
        Expression left = expression.getLeftExpression();
        if (!(left instanceof VariableExpression)) {
            super.addError("Invalid entity property", left);
            return;
        }
        String leftName = ((VariableExpression) left).getName();
        if (!propertyNames.contains(leftName)) {
            super.addError("Invalid entity property", left);
            return;
        }

        Expression right = expression.getRightExpression();
        if (right instanceof ConstantExpression) {
            ConstantExpression rightConstantExpr = (ConstantExpression) right;
            ExpressionStatement newExpr = new ExpressionStatement(
                    new MethodCallExpression(
                            new VariableExpression("this"),
                            new ConstantExpression(method),
                            new ArgumentListExpression(
                                    new ConstantExpression(leftName),
                                    new ConstantExpression(rightConstantExpr.getValue())
                            )
                    )
            );
            newStatement.addStatement(newExpr);
        } else if (right instanceof VariableExpression) {
            VariableExpression rightVariableExpr = (VariableExpression) right;
            ExpressionStatement newExpr;
            if (propertyNames.contains(rightVariableExpr.getName())) {
                newExpr = new ExpressionStatement(
                        new MethodCallExpression(
                                new VariableExpression("this"),
                                new ConstantExpression(PROPERTY_WHEREABLE_OPERATOR.get(method)),
                                new ArgumentListExpression(
                                        new ConstantExpression(leftName),
                                        new ConstantExpression(rightVariableExpr.getName())
                                )
                        )
                );
            } else {
                newExpr = new ExpressionStatement(
                        new MethodCallExpression(
                                new VariableExpression("this"),
                                new ConstantExpression(method),
                                new ArgumentListExpression(
                                        new ConstantExpression(leftName),
                                        new VariableExpression(((VariableExpression) right).getName())
                                )
                        )
                );
            }
            newStatement.addStatement(newExpr);
        } else {
            super.addError("Unknown expression", expression);
        }
    }

    private void addMultiConditionStatement(BinaryExpression binaryExpression,
                                            BlockStatement newStatement,
                                            List<String> propertyNames) {
        String conditionMethod = MULTI_CONDITION_OPERATOR_MAP.get(binaryExpression.getOperation().getText());
        BlockStatement subConditionStatement = new BlockStatement();
        addNewStatement(binaryExpression.getLeftExpression(), subConditionStatement, propertyNames);
        addNewStatement(binaryExpression.getRightExpression(), subConditionStatement, propertyNames);
        ExpressionStatement newExpr = new ExpressionStatement(
                new MethodCallExpression(
                        new VariableExpression("this"),
                        new ConstantExpression(conditionMethod),
                        new ArgumentListExpression(
                                new ClosureExpression(null, subConditionStatement)
                        )
                )
        );
        newStatement.addStatement(newExpr);
    }

    private List<String> getPropertyNames(Class aClass) {
        List<String> propertyNames = new LinkedList<String>();
        for (Field field : aClass.getDeclaredFields()) {
            if (!field.isSynthetic()) {
                propertyNames.add(field.getName());
            }
        }
        return propertyNames;
    }

    private ClosureExpression extractLastClosureArg(Expression expression) {
        if (expression instanceof ArgumentListExpression) {
            ArgumentListExpression ale = (ArgumentListExpression) expression;
            List<Expression> expressions = ale.getExpressions();
            if (expressions != null && !expressions.isEmpty()) {
                Expression lastArgument = expressions.get(expressions.size() - 1);
                if (lastArgument instanceof ClosureExpression) {
                    return (ClosureExpression) lastArgument;
                } else if (expression instanceof VariableExpression) {
                    // TODO closure var
                }
            }
        }
        return null;
    }

    private Boolean isWhereableMethod(Expression expression, Set whereMethods) {
        if (expression instanceof ConstantExpression) {
            ConstantExpression constantExpression = (ConstantExpression) expression;
            String methodName = (String) constantExpression.getValue();
            return whereMethods.contains(methodName);
        }
        return false;
    }

    private Class extractEntityClass(Expression objectExpression) {
        String clazzName = "";
        if (objectExpression instanceof ClassExpression) {
            ClassExpression classExpression = (ClassExpression) objectExpression;
            ClassNode classNode = classExpression.getType();
            clazzName = classNode.getName();
        }
        if (objectExpression instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) objectExpression;
            String simpleName = variableExpression.getName();
            clazzName = extractClassNameFromImport(simpleName);

        }
        if (clazzName == null) {
            return null;
        }
        Class aClass = null;
        try {
            aClass = Class.forName(clazzName);
        } catch (ClassNotFoundException e) {
            // invalide className
            return null;
        }
        Entity entityAnnotation = (Entity) aClass.getAnnotation(Entity.class);
        if (entityAnnotation != null) {
            return aClass;
        }
        return null;
    }

    private String extractClassNameFromImport(String simpleName) {
        List<ImportNode> importNodes = sourceUnit.getAST().getImports();
        if (importNodes != null) {
            for (ImportNode importNode : importNodes) {
                if (importNode.getAlias().equals(simpleName)) {
                    return importNode.getType().getName();
                }
            }
        }
        return null;
    }
}
