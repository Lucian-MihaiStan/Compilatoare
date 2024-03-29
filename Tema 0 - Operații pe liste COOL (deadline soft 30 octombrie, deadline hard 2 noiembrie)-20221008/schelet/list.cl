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
