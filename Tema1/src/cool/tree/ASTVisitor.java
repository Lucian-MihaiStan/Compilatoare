package cool.tree;

public interface ASTVisitor<T> {

    T visit(RfProgram rfProgram);

    T visit(RfClass rfClass);
}
