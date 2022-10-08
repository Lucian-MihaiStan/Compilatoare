(* Think of these as abstract classes *)
class Comparator {
    compareTo(o1 : Object, o2 : Object):Int {0};
};

class Filter inherits IO {
    filter(o : Object):Bool {true};
};

class ProductFilter inherits Filter {
    filter(o : Object) : Bool {
        (
            let 
                isProduct : Bool <- false
            in ({
                if isvoid o then
                    abort()
                else 0 fi;

                case o of
                    p : Product => { isProduct <- true; p; };
                    s : String => { isProduct <- false; s; };
                    i : Int => { isProduct <- false; i; };
                    io : IO => { isProduct <- false; io; };
                    o : Object => { isProduct <- false; o; };
                esac;
                not isProduct;
            })
        )
    };
};

class RankFilter inherits Filter {
    filter(o : Object) : Bool {
        (
            let
                isRank : Bool <- false
            in ({
                if isvoid o then
                    abort()
                else 0 fi;
                
                case o of 
                    r : Rank => { isRank <- true ; r; };
                    s : String => { isRank <- false; s; };
                    i : Int => { isRank <- false; i; };
                    io : IO => { isRank <- false; io; };
                    o : Object => { isRank <- false; o; };
                esac;

                not isRank;
            })
        )
    };
};  

class SamePriceFilter inherits Filter {
    filter(o : Object) : Bool {{
        (
            let
                isFilter : Bool <- false
            in ({
                if isvoid o then
                    abort()
                else 0 fi;

                case o of
                    soda : Soda => { 
                        isFilter <- (soda.getprice() = (new Product.init("", "", "", soda.getHardWiredPrice())).getprice()); soda; };
                    coffee : Coffee => { isFilter <- (coffee.getprice() = (new Product.init("", "", "", coffee.getHardWiredPrice())).getprice()); coffee; };
                    laptop : Laptop => { isFilter <- (laptop.getprice() = (new Product.init("", "", "", laptop.getHardWiredPrice())).getprice()); laptop; };
                    router : Router => { isFilter <- (router.getprice() = (new Product.init("", "", "", router.getHardWiredPrice())).getprice()); router; };
                    obj : Object => isFilter <- false;
                esac;

                not isFilter;
            })
        );
    }};
};

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