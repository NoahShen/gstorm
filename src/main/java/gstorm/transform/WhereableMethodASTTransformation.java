package gstorm.transform;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import java.util.List;

/**
 * Created by noahshen on 14-11-22.
 */
@GroovyASTTransformation(phase = CompilePhase.CONVERSION)
public class WhereableMethodASTTransformation implements ASTTransformation {

    @Override
    public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        WhereableTransformer whereableTransformer = new WhereableTransformer(sourceUnit);
        ModuleNode ast = sourceUnit.getAST();
        List<ClassNode> classes = ast.getClasses();
        for (ClassNode aClass : classes) {
            whereableTransformer.visitClass(aClass);
        }
    }
}
