(* Think of these as abstract classes *)
class Comparator {
    compareTo(o1 : Object, o2 : Object):Int {0};
};

class Filter {
    filter(o : Object):Bool {true};
};

(* TODO: implement specified comparators and filters*)

class DynamicCast {

    dCString(o : Object) : String {
        case o of
            s : String => s;
            obj : Object => { abort(); ""; };
        esac
    };

    dCInt(o : Object) : Int {
        case o of
            i : Int => i;
            obj : Object => { abort(); 0; };
        esac
    };

    dList(o : Object) : List {
        case o of 
            l : List => l;
        esac
    };

    dElemBuilder(o : Object) : ElementBuilder {
        case o of
            e : ElementBuilder => e;
        esac
    };

};

class ElementBuilder {
    index : Int;
    elem : String;

    init(i : Int, l : String) : SELF_TYPE {{
        index <- i;
        elem <- l;
        self;
    }};

    getElement() : String {
        elem
    };

    getIndex() : Int {
        index
    };
};