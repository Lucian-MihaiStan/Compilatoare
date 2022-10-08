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

};