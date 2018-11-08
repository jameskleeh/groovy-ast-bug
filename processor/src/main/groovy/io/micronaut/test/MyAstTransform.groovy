package io.micronaut.test

import groovy.transform.CompilationUnitAware
import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.ast.PackageNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.control.CompilationUnit
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import java.util.stream.Collectors

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class MyAstTransform implements ASTTransformation, CompilationUnitAware {

    CompilationUnit compilationUnit

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        ModuleNode moduleNode = source.getAST()

        List<ClassNode> classes = []

        for (ClassNode classNode in  moduleNode.getClasses()) {
            classes.add(classNode)
            classes.addAll(classNode.interfaces.toList())
        }

        for (ClassNode classNode in classes) {
            classNode.getMethods()
                    .stream()
                    .flatMap({m -> Arrays.stream(m.getParameters())})
                    .forEach({ p ->
                        if (p.name.startsWith("param")) {
                            source.addError(new SyntaxException("Parameter has invalid name [" + p.name + "]", p))
                        }
                    })
        }
    }
}
