(*
   The class A2I provides integer-to-string and string-to-integer
conversion routines.  To use these routines, either inherit them
in the class where needed, have a dummy variable bound to
something of type A2I, or simpl write (new A2I).method(argument).
*)


(*
   c2i   Converts a 1-character string to an integer.  Aborts
         if the string is not "0" through "9"
*)
class A2I {

     c2i(char : String) : Int {
	if char = "0" then 0 else
	if char = "1" then 1 else
	if char = "2" then 2 else
        if char = "3" then 3 else
        if char = "4" then 4 else
        if char = "5" then 5 else
        if char = "6" then 6 else
        if char = "7" then 7 else
        if char = "8" then 8 else
        if char = "9" then 9 else
        { abort(); 0; }  -- the 0 is needed to satisfy the typchecker
        fi fi fi fi fi fi fi fi fi fi
     };

(*
   i2c is the inverse of c2i.
*)
     i2c(i : Int) : String {
	if i = 0 then "0" else
	if i = 1 then "1" else
	if i = 2 then "2" else
	if i = 3 then "3" else
	if i = 4 then "4" else
	if i = 5 then "5" else
	if i = 6 then "6" else
	if i = 7 then "7" else
	if i = 8 then "8" else
	if i = 9 then "9" else
	{ abort(); ""; }  -- the "" is needed to satisfy the typchecker
        fi fi fi fi fi fi fi fi fi fi
     };

(*
   a2i converts an ASCII string into an integer.  The empty string
is converted to 0.  Signed and unsigned strings are handled.  The
method aborts if the string does not represent an integer.  Very
long strings of digits produce strange answers because of arithmetic 
overflow.

*)
     a2i(s : String) : Int {
        if s.length() = 0 then 0 else
	if s.substr(0,1) = "-" then ~a2i_aux(s.substr(1,s.length()-1)) else
        if s.substr(0,1) = "+" then a2i_aux(s.substr(1,s.length()-1)) else
           a2i_aux(s)
        fi fi fi
     };

(*
  a2i_aux converts the usigned portion of the string.  As a programming
example, this method is written iteratively.
*)
     a2i_aux(s : String) : Int {
	(let int : Int <- 0 in	
           {	
               (let j : Int <- s.length() in
	          (let i : Int <- 0 in
		    while i < j loop
			{
			    int <- int * 10 + c2i(s.substr(i,1));
			    i <- i + 1;
			}
		    pool
		  )
	       );
              int;
	    }
        )
     };

(*
    i2a converts an integer to a string.  Positive and negative 
numbers are handled correctly.  
*)
    i2a(i : Int) : String {
	if i = 0 then "0" else 
        if 0 < i then i2a_aux(i) else
          "-".concat(i2a_aux(i * ~1)) 
        fi fi
    };
	
(*
    i2a_aux is an example using recursion.
*)		
    i2a_aux(i : Int) : String {
        if i = 0 then "" else 
	    (let next : Int <- i / 10 in
		i2a_aux(next).concat(i2c(i - next * 10))
	    )
        fi
    };

};
class List inherits IO {
    head : Object;
    tail : List;

    cons(h : Object):List {
        (
            let list : List
            in (
                {
                    list <- new List;
                    list.setHead(h);
                }
            )
        )
    };

    append(l : List) : List {{
        if isvoid tail then
            tail <- l
        else 
            tail <- appendListToEnd(l, tail)
        fi;
        self;
    }};

    remove(index : Int) : SELF_TYPE {{
        (
            let
                tConv : DynamicCast <- new DynamicCast
            in ({
                if isvoid head then
                    abort()
                else 0 fi;

                if index = 1 then
                    tail <- tail.getTail()
                else 
                    tail.remove(index - 1)
                fi; 

                self;

            })
        );
    }};

    add(o : Object):SELF_TYPE {
        {
            if isvoid head then
                head <- o
            else
                tail <- addObjectToEnd(o, tail)
            fi;
            self;
        }
    };

    appendListToEnd(newList : List, prevTail : List) : List {
        (
            let
                newTail : List
            in ({
                if isvoid prevTail then
                    newTail <- newList
                else {
                    newTail <- prevTail.appendListToEnd(newList, prevTail.getTail());
                    prevTail.setTail(newTail);
                    newTail <- prevTail;
                }
                fi;
                newTail;
            })
        )
    };

    addObjectToEnd(o : Object, l : List) : List {
        (
            let
                newList : List
            in ({
                if isvoid l then
                    newList <- cons(o)
                else {
                    newList <- l.addObjectToEnd(o, l.getTail());
                    l.setTail(newList);
                    newList <- l;
                }
                fi;
                newList;
            })

        )
    };

    isEmpty() : Bool {
        isvoid head 
    };

    setTail(l : List) : SELF_TYPE {
        {
            tail <- l;
            self;
        }
    };
    
    setHead(h : Object) : SELF_TYPE {
        {
            head <- h;
            self;
        }
    };

    getTail() : List {
        tail
    };

    size() : Int {
        {
            (
                let sz : Int
                in 
                ({
                    if isEmpty() then
                        sz <- 0
                    else
                    {
                        if isvoid tail then
                            sz <- sz + 1
                        else
                            sz <- sz + 1 + tail.size()
                        fi;
                    }
                    fi;
                    sz;
                })
            );
        }
    };

    toString():String {
        (
            let 
                consString : String,
                atoiConverter : A2I <- new A2I,
                tConv : DynamicCast <- new DynamicCast
            in 
            ({
                case head of
                    s : String => consString <- "String(".concat(s).concat("), ");
                    p : Product => consString <- p.toString().concat(", ");
                    r : Rank => consString <- r.toString().concat(", ");
                    i : Int => consString <- "Int(".concat(atoiConverter.i2a(i)).concat("), ");
                    b : Bool => {
                        if b = true then
                            consString <- "Bool(true), "
                        else 
                            consString <- "Bool(false), "
                        fi;
                    };
                    io : IO => consString <- "IO(), ";
                    dE : DummyElement => consString <- ", ";
                    o : Object => { abort(); ""; };
                esac;
                
                -- out_string(tConv.dCString(head).concat("\n"));
                
                if not isvoid tail then {
                    consString <- consString.concat(tail.toString());
                }
                else 0 fi;

                consString;
            })
        )
    };

    merge(other : List):SELF_TYPE {
        self (* TODO *)
    };

    filterBy(filter : Filter):SELF_TYPE {{
        if not isvoid tail then {
            if filter.filter(tail.getHead()) then {
                tail <- tail.getTail();
                self.filterBy(filter);
            }
            else tail <- tail.filterBy(filter) fi;   
        }
        else 0 fi;
        self;
    }};

    replace(obj : Object, index : Int) : SELF_TYPE {{
        if index = 0 then
            head <- obj
        else
            replace(obj, index - 1)
        fi;
        self;
    }};

    sortBy(comp : Comparator, asc : Bool):SELF_TYPE {{
        (
            let
                i : Int <- 0,
                j : Int <- 0,
                e1 : Object,
                e2 : Object,
                aux : Object,
                size : Int <- size() 
            in ({

                while i < size - 1 loop {
                    j <- 0;
                    while j < size - i - 1 loop {
                        e1 <- getIndex(j);
                        e2 <- getIndex(j + 1);

                        if asc = true then
                            if not comp.compareTo(e1, e2) <= 0 then {
                                aux <- e1;
                                setAtIndex(j, e2);
                                setAtIndex(j + 1, aux);
                            } else 0 fi
                        else
                            if comp.compareTo(e1, e2) < 0 then {
                                aux <- e1;
                                setAtIndex(j, e2);
                                setAtIndex(j + 1, aux);
                            } else 0 fi
                        fi;
                        j <- j + 1;
                    } pool;
                    i <- i + 1;
                } pool;
                self;
            })
        );
        self;
    }};

    setAtIndex(index : Int, obj : Object) : SELF_TYPE {{
        if index = 0 then
            head <- obj
        else
        {
            if isvoid tail then
                abort()
            else 0 fi;
            tail.setAtIndex(index - 1, obj);
        }
        fi;
        self;
    }};

    getHead() : Object {
        head
    };

    getIndex(index : Int) : Object {
        if index = 0 then
            head
        else 
        {
            if isvoid tail then
                abort()
            else 0 fi;
            tail.getIndex(index - 1);
        }
        fi
    };

    printList(size : Int) : String {{
        (
            let 
                stringBuilder : String <- new String,
                tmp : ElementBuilder,
                intConv : A2I <- new A2I,
                index : Int <- 0,
                tConv : DynamicCast <- new DynamicCast
            in ({
                if isvoid head then
                    abort()
                else 0 fi;
                
                if size = 1 then
                    stringBuilder <- (tConv.dElemBuilder(head)).getElement()
                else {
                    while not index = size loop {
                        tmp <- tConv.dElemBuilder(getIndex(index));
                        
                        if isvoid tmp then
                            abort()
                        else 0 fi;
                        
                        stringBuilder <- stringBuilder.concat(intConv.i2a(tmp.getIndex())).concat(": ").concat(tmp.getElement()).concat("\n");

                        index <- index + 1;
                    } pool;
                } fi;
                stringBuilder;
            })
        );
    }};

};
class StringTokenizer {

    content : String;
    delimiter : String;

    contentLength : Int;

    lastToken : Int <- 0;
    nextToken : Int <- 0;

    withContent(newContent : String):SELF_TYPE { 
        {   
            content <- newContent;
            contentLength <- content.length();
            self;
        }  
    };

    withDelimiter(newDelimiter : String):SELF_TYPE {
        {
            delimiter <- newDelimiter;
            self;
        }
    };

    reset():Int {
        {
            content <- "";
            delimiter <- "";
            contentLength <- 0;
            lastToken <- 0;
            nextToken <- 0;
            0;
        }
    };

    nextToken():String {       
        {
            (
                let nToken : String, retValue : String <- content
                in {
                    if delimiter = "" then 
                        abort()
                    else 0 fi;

                    if content = "" then
                        abort()
                    else 0 fi;

                    (let index : Int, tmpStr : String, foundNewToken : Bool <- false in
                        {
                            if lastToken = contentLength then 0
                            else {
                                tmpStr <- content.substr(lastToken, 1);
                                if tmpStr = delimiter then
                                    index <- lastToken + 1 
                                else 
                                    index = 0 
                                fi;

                                while index < contentLength loop {
                                    tmpStr <- content.substr(index, 1);

                                    if foundNewToken = false then
                                        if tmpStr = delimiter then 
                                            {
                                                nextToken <- index;
                                                foundNewToken <- true;
                                            }
                                        else 0 fi
                                    else 0 fi;

                                    index <- index + 1;
                                } pool;

                                if foundNewToken = false then
                                    if index = contentLength then 
                                        {
                                            nextToken <- contentLength;
                                            foundNewToken <- true;
                                        }
                                    else 0 fi
                                else 0 fi;

                                if foundNewToken = true then 
                                    {
                                        if lastToken = 0 then 0 else lastToken <- lastToken + 1 fi;
                                        retValue <- content.substr(lastToken, nextToken - lastToken);
                                        lastToken <- nextToken;
                                    }
                                else 0 fi;
                            } fi;
                        }
                    );
                    retValue;
                }
            );
        }
    };

};

class Main inherits IO{
    lists : List <- new List;
    looping : Bool <- true;
    somestr : String;
    tConv : DynamicCast <- new DynamicCast;
    tokenizer : StringTokenizer <- new StringTokenizer;

    main():Object {{
            loopingCommand();
    }};

    print(printIndex : Int): Object { 
        {
            (
                let
                    size : Int <- lists.size(),
                    index : Int <- 0,
                    currentList : List,
                    obj : Object, 
                    stringBuilder : String,
                    builderList : List <- new List,
                    elemBuilder : ElementBuilder
                in 
                ({
                    if printIndex = 0 then {
                        while not index = size loop
                        {
                            currentList <- tConv.dList(lists.getIndex(index));
                            
                            stringBuilder <- currentList.toString();

                            stringBuilder <- "[ ".concat(stringBuilder.substr(0, stringBuilder.length() - 2));
                            elemBuilder <- new ElementBuilder.init(index + 1, stringBuilder.concat(" ]"));

                            builderList.add(elemBuilder);
                            index <- index + 1;
                        }
                        pool;

                        out_string(builderList.printList(size));
                    } else {
                        stringBuilder <- tConv.dList(lists.getIndex(printIndex - 1)).toString();
                        stringBuilder <- "[ ".concat(stringBuilder.substr(0, stringBuilder.length() - 2));
                        out_string(stringBuilder.concat(" ]\n"));
                    } fi;
                })
            );
            0;
        }
    };

    merge(): Object {
        0
    };

    filterBy(): Object {
        0
    };

    sortBy(): Object {
        0
    };

    loopingCommand():Object {
        {
        (
            let 
                hasNextToken : Bool,
                tokensList : List <- new List,
                token : String, headToken : String,
                currentList : List <- new List,
                exitCon : Int,
                head : Object,
                newObject : Object,
                atoiConverter : A2I <- new A2I
            in (
                while looping loop {
                    somestr <- in_string();
                    token <- "";
                    hasNextToken <- true;

                    tokensList <- new List;
                    
                    if somestr = "END" then
                    {
                        if not currentList.isEmpty() then
                            lists.add(currentList)
                        else 0 fi;
                        exitCon <- 1;
                    }
                    else 0 fi;

                    if somestr = "" then
                        exitCon <- 1
                    else 0 fi;

                    if exitCon = 0 then
                    {
                        tokenizer.reset();
                        tokenizer.withContent(somestr).withDelimiter(" ");
                        
                        tokensList <- new List;

                        while hasNextToken loop {
                            if token = somestr then
                                hasNextToken <- false
                            else 
                            {
                                token <- tokenizer.nextToken();
                                if not token = somestr then
                                    tokensList.add(token)
                                else 0 fi;

                            }
                            fi;
                        } pool;
                        
                        if tokensList.isEmpty() then
                            head <- somestr
                        else
                            head <- tokensList.getHead()
                        fi;

                        headToken <- tConv.dCString(head);

                        if headToken = "Soda" then {
                            newObject <- new Soda.init(
                                tConv.dCString(tokensList.getIndex(0)),
                                tConv.dCString(tokensList.getIndex(1)),
                                tConv.dCString(tokensList.getIndex(2)),
                                atoiConverter.a2i(tConv.dCString(tokensList.getIndex(3)))
                            );
                            currentList.add(newObject);
                        }
                        else if headToken = "Coffee" then {
                            newObject <- new Coffee.init(
                                tConv.dCString(tokensList.getIndex(0)),
                                tConv.dCString(tokensList.getIndex(1)),
                                tConv.dCString(tokensList.getIndex(2)),
                                atoiConverter.a2i(tConv.dCString(tokensList.getIndex(3)))
                            );
                            currentList.add(newObject);
                        }
                        else if headToken = "Laptop" then {
                            newObject <- new Laptop.init(
                                tConv.dCString(tokensList.getIndex(0)),
                                tConv.dCString(tokensList.getIndex(1)),
                                tConv.dCString(tokensList.getIndex(2)),
                                atoiConverter.a2i(tConv.dCString(tokensList.getIndex(3)))
                            );
                            currentList.add(newObject);
                        }
                        else if headToken = "Router" then {
                            newObject <- new Router.init(
                                tConv.dCString(tokensList.getIndex(0)),
                                tConv.dCString(tokensList.getIndex(1)),
                                tConv.dCString(tokensList.getIndex(2)),
                                atoiConverter.a2i(tConv.dCString(tokensList.getIndex(3)))
                            );
                            currentList.add(newObject);
                        }
                        else if headToken = "Private" then {
                            newObject <- new Private.init(tConv.dCString(tokensList.getIndex(0)), tConv.dCString(tokensList.getIndex(1)));
                            currentList.add(newObject);
                        }
                        else if headToken = "Corporal" then {
                            newObject <- new Corporal.init(tConv.dCString(tokensList.getIndex(0)), tConv.dCString(tokensList.getIndex(1)));
                            currentList.add(newObject);
                        }
                        else if headToken = "Sergent" then {
                            newObject <- new Sergent.init(tConv.dCString(tokensList.getIndex(0)), tConv.dCString(tokensList.getIndex(1)));
                            currentList.add(newObject);
                        }
                        else if headToken = "Officer" then {
                            newObject <- new Officer.init(tConv.dCString(tokensList.getIndex(0)), tConv.dCString(tokensList.getIndex(1)));
                            currentList.add(newObject);
                        }
                        else if headToken = "String" then {
                            newObject <- tokensList.getIndex(1);
                            currentList.add(newObject);
                        }
                        else if headToken = "Int" then {
                            newObject <- atoiConverter.a2i(tConv.dCString(tokensList.getIndex(1)));
                            currentList.add(newObject);
                        }
                        else if headToken = "Bool" then {
                            if tConv.dCString(tokensList.getIndex(1)) = "true" then
                                newObject <- true
                            else
                                newObject <- false
                            fi;
                            currentList.add(newObject);
                        }
                        else if headToken = "IO" then {
                            newObject <- new IO;
                            currentList.add(newObject);
                        }
                        else if headToken = "print" then {
                            if tokensList.size() = 0 then
                                print(0)
                            else
                                print(atoiConverter.a2i(tConv.dCString(tokensList.getIndex(1))))
                            fi;
                        }
                        else if headToken = "load" then currentList <- new List
                        else if headToken = "merge" then {
                            (
                                let
                                    index1 : Int,
                                    index2 : Int,
                                    li1 : List,
                                    li2 : List
                                in ({
                                    index1 <- atoiConverter.a2i(tConv.dCString(tokensList.getIndex(1)));
                                    index2 <- atoiConverter.a2i(tConv.dCString(tokensList.getIndex(2)));

                                    li1 <- tConv.dList(lists.getIndex(index1 - 1));
                                    li2 <- tConv.dList(lists.getIndex(index2 - 1));

                                    li1 <- li1.append(li2);

                                    if index1 - 1 = 0 then  
                                        lists = new List
                                    else
                                        lists.remove(index1 - 1)
                                    fi;

                                    if index2 - 2 = 0 then
                                        lists = new List
                                    else
                                        lists.remove(index2 - 2)
                                    fi;

                                    lists.add(li1);

                                })
                            );
                        }
                        else if headToken = "filterBy" then {
                            (
                                let
                                    index : Int,
                                    li : List,
                                    filter : Filter,
                                    filterStr : String,
                                    shouldRemove : Bool <- false
                                in ({
                                    index <- atoiConverter.a2i(tConv.dCString(tokensList.getIndex(1)));
                                    li <- tConv.dList(lists.getIndex(index - 1));
                                    
                                    filterStr <- tConv.dCString(tokensList.getIndex(2));
                                    if filterStr = "ProductFilter" then
                                        filter <- new ProductFilter
                                    else if filterStr = "RankFilter" then
                                        filter <- new RankFilter
                                    else if filterStr = "SamePriceFilter" then
                                        filter <- new SamePriceFilter
                                    else 
                                        abort()
                                    fi fi fi;

                                    li <- li.filterBy(filter);
                                    
                                    if filter.filter(li.getHead()) then
                                        if li.size() = 1 then
                                            shouldRemove <- true
                                        else 
                                            li <- li.getTail()
                                        fi
                                    else 0 fi;
                                    
                                    if shouldRemove then {
                                        li <- new List;
                                        li.add(new DummyElement);
                                    }
                                    else 0 fi;

                                    lists.replace(li, index - 1);
                                })
                            );
                        }
                        else if headToken = "sortBy" then {
                            (
                                let 
                                    type : String,
                                    index : Int,
                                    compStr : String,
                                    comparator : Comparator,
                                    li : List,
                                    asc : Bool
                                in ({
                                    index <- atoiConverter.a2i(tConv.dCString(tokensList.getIndex(1)));
                                    compStr <- tConv.dCString(tokensList.getIndex(2));

                                    if compStr = "PriceComparator" then
                                        comparator <- new PriceComparator
                                    else if compStr = "RankComparator" then
                                        comparator <- new RankComparator
                                    else if compStr = "AlphabeticComparator" then
                                        comparator <- new AlphabeticComparator
                                    else
                                        abort()
                                    fi fi fi;

                                    type <- tConv.dCString(tokensList.getIndex(3));
                                    if type = "ascendent" then
                                        asc <- true
                                    else
                                        asc <- false
                                    fi;
 
                                    li <- tConv.dList(lists.getIndex(index - 1));
                                    li.sortBy(comparator, asc);
                                })
                            );
                        }
                        else {
                            out_string(somestr);
                            abort();
                        }
                        fi fi fi fi fi fi fi fi fi fi fi fi fi fi fi fi fi;
                    }
                    else
                        if not somestr = "END" then
                            looping <- false
                        else {
                            exitCon <- 0;
                            tokensList <- new List;
                        } fi
                    fi;
                } pool
            )
        );
        0;
        }
    };
};(*******************************
 *** Classes Product-related ***
 *******************************)
class Product {
    name : String;
    model : String;
    additionalData : String;
    price : Int;

    init(n : String, m: String, a : String, p : Int):SELF_TYPE {{
        name <- n;
        model <- m;
        additionalData <- a;
        price <- p;
        self;
    }};

    initSimple(p: Int):SELF_TYPE {{
        price <- p;
        self;
    }};

    getName():String {name};

    getprice():Int{ price * 119 / 100 };

    getHardWiredPrice():Int {price};

    toString():String {
        name.concat("(").concat(model).concat(",").concat(additionalData)
        -- .concat(",").concat(new A2I.i2a(getprice()))
        -- .concat(",").concat(new A2I.i2a(getHardWiredPrice()))
        .concat(")")
    };

    getAdditionalData() : String  {
        additionalData
    };
};

class DummyElement {  
};

class Edible inherits Product {
    -- VAT tax is lower for foods
    getprice():Int { price * 109 / 100 };
};

class Soda inherits Edible {
    -- sugar tax is 20 bani
    getprice():Int {price * 109 / 100 + 20};
};

class Coffee inherits Edible {
    -- this is technically poison for ants
    getprice():Int {price * 119 / 100};
};

class Laptop inherits Product {
    -- operating system cost included
    getprice():Int {price * 119 / 100 + 499};
};

class Router inherits Product {};

(****************************
 *** Classes Rank-related ***
 ****************************)
class Rank {
    name : String;
    personName : String;

    init(n : String, p : String):SELF_TYPE {{
        name <- n;
        personName <- p;
        self;
    }};

    toString():String {
        name.concat("(").concat(personName).concat(")")
    };

    getRank():Int {0};
};

class Private inherits Rank { getRank():Int {1}; };

class Corporal inherits Private { getRank():Int {2}; };

class Sergent inherits Corporal { getRank():Int {3}; };

class Officer inherits Sergent { getRank():Int {4}; };(* Think of these as abstract classes *)
class Comparator inherits IO {
    compareTo(o1 : Object, o2 : Object):Int {0};
};

class PriceComparator inherits Comparator {
    compareTo(o1 : Object, o2 : Object):Int {
        (
            let 
                tConv : DynamicCast <- new DynamicCast,
                -- p1 : Int <- new Product.initSimple(tConv.dcProduct(o1).getHardWiredPrice()).getprice(),
                -- p2 : Int <- new Product.initSimple(tConv.dcProduct(o2).getHardWiredPrice()).getprice(),
                p1 : Int <- tConv.dcProduct(o1).getprice(),
                p2 : Int <- tConv.dcProduct(o2).getprice(),
                dif : Int <- 0
            in ({
                -- out_string(tConv.dcProduct(o1).toString().concat(" "));
                -- out_int(p1);
                -- out_string(" ");

                -- out_string(tConv.dcProduct(o2).toString().concat(" "));
                -- out_int(p2);
                -- out_string("\n");
                dif <- (p1 - p2);
                if dif = 0 then {
                    if not tConv.dcProduct(o1).getAdditionalData() < tConv.dcProduct(o2).getAdditionalData() then {
                        dif <- dif - 1;
                        -- out_string("DADA\n");
                    }
                    else dif <- 1 fi;
                    -- out_string("dif=0");
                }
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
        in ({
            r1 - r2;
        })
    )};
};

class AlphabeticComparator inherits Comparator {
    compareTo(o1 : Object, o2 : Object):Int {(
        let
            tConv : DynamicCast <- new DynamicCast,
            s1 : String <- tConv.dCString(o1),
            s2 : String <- tConv.dCString(o2)
        in ({
            if s1 < s2 then
                0 - 1
            else if s1 = s2 then
                0
            else  
                1
            fi fi;
        })
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