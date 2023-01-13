package cool.visitor.code.gen;

public interface CodeGenVisitorConstants {

    String SEQUENCE_PATTERN = "sequence";

    String SEQUENCE_SPACED_PATTERN = "sequenceSpaced";

    String E = "e";

    String PROGRAM_PATTERN = "program";

    String DISPATCH_TABLES = "dispatchTables";

    String INT_TAG = "intTag";

    String BOOL_TAG = "boolTag";

    String STRING_TAG = "stringTag";

    String DEFAULT_TAG_PATTERN = "defaultTag";

    String NAME_TAG = "nameTag";

    String TAG_ID = "tagId";

    String COUNT = "count";

    String SIZE = "size";

    String CLASS_NAME_LENGTH = "classNameLength";

    String CLASS_NAME = "className";

    String STR_CONST_PATTERN = "strConst";

    String INT_CONST =  "int_const";

    String INT_CONST_PATTERN = "intConst";

    String STR_CONSTANTS = "strConstants";

    String INT_CONSTANTS = "intConstants";

    String BOOL_CONSTANTS = "boolConstants";

    String EMPTY_STRING = "";
    String DEFAULT_VALUE = "defaultValue";

    String CLASS_NAME_TAB = "classNameTab";

    String CLASS_NAME_TAB_FORMAT = "\t.word\tstr_const%d";

    String CLASS_OBJECTS_TAB = "classObjectsTab";

    String CLASS_PROTOTYPE_OBJ_PATTERN = "classProtObj";
    int DEFAULT_SIZE_PROTOTYPE = 3;

    String CLASS_DISPATCH_TABLE = "classDispatchTable";
    String METHODS = "methods";

    String METHOD_DECLARATION_DISPATCH_TABLE = "methodDeclarationDispatchTable";
    String METHOD_NAME = "methodName";

    String CLASS_PROTOTYPES_TAB = "classPrototypesTab";

    String OBJECT_DECLARATION_TAB_PATTERN = "objectDeclarationTab";

    String MAIN = "Main";

    String HEAP_START = "\t.globl\theap_start";

    String HEAP_START_PATTERN = "heapStart";

    String HEAP_START_TAB = "heapStartTab";

    String GLOBAL_OBJECT_INIT_PATTERN = "globalObjectInit";

    String OBJECT_INITS = "objectInits";

    String MAIN_DOT_MAIN = "\t.globl Main.main";

    String INIT_CONSTRUCTOR_PATTERN = "initConstructor";

    String PARENT_CLASS_NAME = "parentClassName";

    String ATTRIB = "attrib";

    String INIT_CONSTRUCT_TAB = "initConstructTab";

    String CUSTOM_METHOD_PATTERN = "customMethod";
    String METHOD_BODY = "methodBody";
    String INCREMENT_STACK_NUMBER = "incrementStackNumber";
    String LITERAL_PATTERN = "literal";

    String ADDRESS_CONSTANT = "addressConstant";

    String CUSTOM_METHODS_TAB = "customMethodsTab";
    String DEFAULT_INT_PROTOTYPE_VALUE = "\t.word\t0\n";

    String DEFAULT_STRING_PROTOTYPE_VALUE = "\t.word\tint_const0\n\t.asciiz\t\"\"\n\t.align\t2\n";
    String DEFAULT_BOOL_PROTOTYPE_VALUE = "\t.word\t0\n";;

    String BOOL_CONST_PATTERN = "boolConst";
}
