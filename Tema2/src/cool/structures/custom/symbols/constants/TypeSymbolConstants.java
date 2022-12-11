package cool.structures.custom.symbols.constants;

import cool.structures.custom.symbols.TypeSymbol;

import java.util.Set;

public interface TypeSymbolConstants {

    String SELF_STR = "self";
    String INT_STR = "Int";
    String STRING_STR = "String";
    String BOOL_STR = "Bool";
    String IO_STR = "IO";
    String OBJECT_STR = "Object";

    String SELF_TYPE_STR = "SELF_TYPE";
    Set<String> illegalParents = Set.of(INT_STR, STRING_STR, BOOL_STR, SELF_TYPE_STR);

    TypeSymbol OBJECT = new TypeSymbol(OBJECT_STR, null);

    TypeSymbol INT = new TypeSymbol(INT_STR, OBJECT_STR);

    TypeSymbol STRING = new TypeSymbol(STRING_STR, OBJECT_STR);

    TypeSymbol BOOL = new TypeSymbol(BOOL_STR, OBJECT_STR);

    TypeSymbol IO = new TypeSymbol(IO_STR, OBJECT_STR);

    // TODO Lucian
//    TypeSymbol SELF_TYPE = new TypeSymbol("SELF_TYPE", OBJECT);

}
