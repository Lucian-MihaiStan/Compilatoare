(* Think of these as abstract classes *)
class Comparator inherits IO {
    compareTo(o1 : Object, o2 : Object):Int {0};
};

class PriceComparator inherits Comparator {
    compareTo(o1 : Object, o2 : Object):Int {
        (
            let 
                tConv : DynamicCast <- new DynamicCast,
                p1 : Int <- tConv.dcProduct(o1).getprice(),
                p2 : Int <- tConv.dcProduct(o2).getprice(),
                dif : Int <- 0
            in ({
                dif <- (p1 - p2);
                if dif = 0 then
                    if not tConv.dcProduct(o1).getAdditionalData() < tConv.dcProduct(o2).getAdditionalData() then
                        dif <- dif - 1
                    else dif <- 1 fi
                else 0 fi;
                dif;
            })
        )
    };
};

class RankComparator inherits Comparator {
    compareTo(o1 : Object, o2 : Object):Int {(
        let
            tConv : DynamicCast <- new DynamicCast,
            r1 : Int <- tConv.dCRank(o1).getRank(),
            r2 : Int <- tConv.dCRank(o2).getRank()
        in (r1 - r2)
    )};
};

class AlphabeticComparator inherits Comparator {
    compareTo(o1 : Object, o2 : Object):Int {(
        let
            tConv : DynamicCast <- new DynamicCast,
            s1 : String <- tConv.dCString(o1),
            s2 : String <- tConv.dCString(o2)
        in (
            if s1 < s2 then
                0 - 1
            else if s1 = s2 then
                0
            else  
                1
            fi fi
        )
    )};
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

    dCRank(o : Object) : Rank {
        case o of
            r : Rank => r;
        esac
    };

    dCInt(o : Object) : Int {
        case o of
            i : Int => i;
            obj : Object => { abort(); 0; };
        esac
    };

    dcProduct(o : Object) : Product {
        case o of 
            p : Product => { p; };
            obj : Object => { abort(); new Product; };
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