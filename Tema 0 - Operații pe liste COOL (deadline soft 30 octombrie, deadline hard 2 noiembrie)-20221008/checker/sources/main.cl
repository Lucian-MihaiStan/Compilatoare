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
    atoiConverter : A2I <- new A2I;
    tokensList : List <- new List;

    main():Object {
        loopingCommand()
    };

    print(printIndex : Int): Object { 
        {
            (
                let
                    index : Int <- 0, size : Int <- lists.size(),
                    builderList : List <- new List,
                    obj : Object, 
                    stringBuilder : String,
                    elemBuilder : ElementBuilder
                in 
                (
                    if printIndex = 0 then {
                        while not index = size loop
                        {
                            stringBuilder <- tConv.dList(lists.getIndex(index)).toString();

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
                    } fi
                )
            );
            0;
        }
    };

    merge(): Object {{
        (
            let
                index1 : Int, index2 : Int,
                li1 : List, li2 : List
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
        0;
    }};

    filterBy(): Object {{
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
        0;
    }};

    sortBy(): Object {{
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
        0;
    }};

    loopingCommand():Object {{
        (
            let 
                hasNextToken : Bool,
                token : String, headToken : String,
                currentList : List <- new List,
                exitCon : Int,
                head : Object,
                newObject : Object
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

                        if headToken = "Soda" then
                            currentList.add(new Soda.init(tConv.dCString(tokensList.getIndex(0)), tConv.dCString(tokensList.getIndex(1)), tConv.dCString(tokensList.getIndex(2)), atoiConverter.a2i(tConv.dCString(tokensList.getIndex(3)))))
                        else if headToken = "Coffee" then
                            currentList.add(new Coffee.init(tConv.dCString(tokensList.getIndex(0)), tConv.dCString(tokensList.getIndex(1)), tConv.dCString(tokensList.getIndex(2)), atoiConverter.a2i(tConv.dCString(tokensList.getIndex(3)))))
                        else if headToken = "Laptop" then
                            currentList.add(new Laptop.init(tConv.dCString(tokensList.getIndex(0)), tConv.dCString(tokensList.getIndex(1)), tConv.dCString(tokensList.getIndex(2)), atoiConverter.a2i(tConv.dCString(tokensList.getIndex(3)))))
                        else if headToken = "Router" then
                            currentList.add(new Router.init(tConv.dCString(tokensList.getIndex(0)), tConv.dCString(tokensList.getIndex(1)), tConv.dCString(tokensList.getIndex(2)), atoiConverter.a2i(tConv.dCString(tokensList.getIndex(3)))))
                        else if headToken = "Private" then
                            currentList.add(new Private.init(tConv.dCString(tokensList.getIndex(0)), tConv.dCString(tokensList.getIndex(1))))
                        else if headToken = "Corporal" then
                            currentList.add(new Corporal.init(tConv.dCString(tokensList.getIndex(0)), tConv.dCString(tokensList.getIndex(1))))
                        else if headToken = "Sergent" then
                            currentList.add(new Sergent.init(tConv.dCString(tokensList.getIndex(0)), tConv.dCString(tokensList.getIndex(1))))
                        else if headToken = "Officer" then
                            currentList.add(new Officer.init(tConv.dCString(tokensList.getIndex(0)), tConv.dCString(tokensList.getIndex(1))))
                        else if headToken = "String" then 
                            currentList.add(tokensList.getIndex(1))
                        else if headToken = "Int" then 
                            currentList.add(atoiConverter.a2i(tConv.dCString(tokensList.getIndex(1))))
                        else if headToken = "Bool" then
                            if tConv.dCString(tokensList.getIndex(1)) = "true" then
                                currentList.add(true)
                            else
                                currentList.add(false)
                            fi
                        else if headToken = "IO" then currentList.add(new IO)
                        else if headToken = "print" then
                            if tokensList.size() = 0 then
                                print(0)
                            else
                                print(atoiConverter.a2i(tConv.dCString(tokensList.getIndex(1))))
                            fi
                        else if headToken = "load" then currentList <- new List
                        else if headToken = "merge" then merge()
                        else if headToken = "filterBy" then filterBy()
                        else if headToken = "sortBy" then sortBy()
                        else {
                            out_string("Unknown evaluation of command".concat(somestr));
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
    }};
};