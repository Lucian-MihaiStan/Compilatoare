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
                consString : String
            in 
            ({
                case head of
                    s : String => consString <- "[".concat(s).concat("]");
                    p : Product => consString <- p.toString();
                    r : Rank => consString <- r.toString();
                    o : Object => { abort(); ""; };
                esac;

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

    filterBy():SELF_TYPE {
        self (* TODO *)
    };

    sortBy():SELF_TYPE {
        self (* TODO *)
    };

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

};
