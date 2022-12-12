package cool.structures.custom.symbols.constants;

import cool.structures.custom.symbols.ClassTypeSymbol;

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

    ClassTypeSymbol OBJECT = new ClassTypeSymbol(OBJECT_STR, null);

    ClassTypeSymbol INT = new ClassTypeSymbol(INT_STR, OBJECT_STR);

    ClassTypeSymbol STRING = new ClassTypeSymbol(STRING_STR, OBJECT_STR);

    ClassTypeSymbol BOOL = new ClassTypeSymbol(BOOL_STR, OBJECT_STR);

    ClassTypeSymbol IO = new ClassTypeSymbol(IO_STR, OBJECT_STR);

    ClassTypeSymbol SELF_TYPE = new ClassTypeSymbol(SELF_TYPE_STR, OBJECT_STR);

}
