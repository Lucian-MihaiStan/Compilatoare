public class DefinitionPassVisitor implements ASTVisitor<Void> {
    Scope currentScope = null;            

    @Override
    public Void visit(Program prog) {
        currentScope = new DefaultScope(null);
        for (var stmt: prog.stmts) {
            stmt.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Id id) {
        var symbol = currentScope.lookup(id.getToken().getText());
        
        // Semnalăm eroare dacă nu există.
        if (symbol == null) {
            ASTVisitor.error(id.getToken(),
                  id.getToken().getText() + " undefined");
            return null;
        }
        
        // Atașăm simbolul nodului din arbore.
        id.setSymbol((IdSymbol)symbol);
        
        return null;
    }

    @Override
    public Void visit(VarDef varDef) {
        /*
         TODO 2: La definirea unei variabile, creăm un nou simbol.
                 Adăugăm simbolul în domeniul de vizibilitate curent.
                 Atașăm simbolul nodului din arbore.
         */

        Id id = varDef.id;
        IdSymbol symbol = new IdSymbol(id.getToken().getText());

        /*
         TODO 3: Semnalăm eroare dacă există deja variabilă în
                 scope-ul curent.
         */

        if (!currentScope.add(symbol)) {
            ASTVisitor.error(id.getToken(), id.getToken().getText() + " redefined");
            return null;
        }

        id.setSymbol(symbol);

        if (varDef.initValue != null)
            varDef.initValue.accept(this);
        
        return null;
    }

    @Override
    public Void visit(FuncDef funcDef) {
        /*
         TODO 2: Asemeni variabilelor globale, vom defini un nou simbol
                 pentru funcții. Acest nou FunctionSymbol va avea că părinte scope-ul
                 curent currentScope și va avea numele funcției.
                 Nu uitați să updatati scope-ul curent înainte să fie parcurs corpul funcției,
                 și să îl restaurati la loc după ce acesta a fost parcurs.
         */

        Id id = funcDef.id;
        FunctionSymbol functionSymbol = new FunctionSymbol(id.getToken().getText(), currentScope);

        /*
         TODO 3: Verificăm faptul că o funcție cu același nume nu a mai fost
                 definită până acum.
         */

        if (!currentScope.add(functionSymbol)) {
            ASTVisitor.error(id.getToken(), id.getToken().getText() + " function redefined");
            return null;
        }

        id.setSymbol(functionSymbol);
        Scope previousScope = currentScope;
        currentScope = functionSymbol;

        for (var formal: funcDef.formals)
            formal.accept(this);

        funcDef.body.accept(this);

        currentScope = previousScope;
        // sau currentScope = currentScope.getParent();

        return null;
    }

    @Override
    public Void visit(Formal formal) {
        /*
         TODO 2: La definirea unei variabile, creăm un nou simbol.
                 Adăugăm simbolul în domeniul de vizibilitate curent.
                 Atașăm simbolul nodului din arbore si setăm scope-ul
                 pe variabila de tip Id, pentru a îl putea obține cu
                 getScope() în a doua trecere.
         */

        Id id = formal.id;
        IdSymbol formalSymbol = new IdSymbol(id.getToken().getText());

        /*
         TODO 3: Verificăm dacă parametrul deja există în scope-ul
                 curent.
         */

        if (!currentScope.add(formalSymbol)) {
            ASTVisitor.error(id.getToken(), "var " + id.getToken().getText() + " redefined");
            return null;
        }

        return null;
    }

    @Override
    public Void visit(If iff) {
        iff.cond.accept(this);
        iff.thenBranch.accept(this);
        iff.elseBranch.accept(this);
        return null;
    }

    @Override
    public Void visit(Type type) {
        return null;
    }

    @Override
    public Void visit(Assign assign) {
        assign.id.accept(this);
        assign.expr.accept(this);
        assign.id.setScope(currentScope);
        return null;
    }

    @Override
    public Void visit(Call call) {
        var id = call.id;
        for (var arg: call.args) {
            arg.accept(this);
        }
        id.setScope(currentScope);
        return null;
    }

    // Operații aritmetice.
    @Override
    public Void visit(UnaryMinus uMinus) {
        uMinus.expr.accept(this);
        return null;
    }

    @Override
    public Void visit(Div div) {
        div.left.accept(this);
        div.right.accept(this);
        return null;
    }

    @Override
    public Void visit(Mult mult) {
        mult.left.accept(this);
        mult.right.accept(this);
        return null;
    }

    @Override
    public Void visit(Plus plus) {
        plus.left.accept(this);
        plus.right.accept(this);
        return null;
    }

    @Override
    public Void visit(Minus minus) {
        minus.left.accept(this);
        minus.right.accept(this);
        return null;
    }

    @Override
    public Void visit(Relational relational) {
        return null;
    }

    // Tipurile de bază
    @Override
    public Void visit(Int intt) {
        return null;
    }

    @Override
    public Void visit(Bool bool) {
        return null;
    }

    @Override
    public Void visit(FloatNum floatNum) {
        return null;
    }
    
};