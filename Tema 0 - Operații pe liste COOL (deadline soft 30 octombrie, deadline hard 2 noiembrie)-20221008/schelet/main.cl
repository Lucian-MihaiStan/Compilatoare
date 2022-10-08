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

    main():Object {
        {
            
            loopingCommand();

            looping <- true;

            (
                let
                    command : String
                in
                ({
                    while looping loop {
                        command <- in_string();

                        if command = "load" then
                            loopingCommand()
                        else if command = "print" then
                            print()
                        else if command = "merge" then 
                            merge()
                        else if command = "filterBy" then
                            filterBy()
                        else if command = "sortBy" then
                            sortBy()
                        else 
                            abort()
                        fi fi fi fi fi;
                    } pool;
                })
            );
        }
    };

    print(): Object { 
        {
            (
                let
                    size : Int <- lists.size(),
                    index : Int <- 0,
                    currentList : List,
                    obj : Object
                in 
                ({
                    while not index = size loop
                    {
                        currentList <- tConv.dList(lists.getIndex(index));
                        if size = 1 then
                            out_string(currentList.toString().concat("\n"))
                        else {
                            out_int(index + 1);
                            out_string(": ".concat(currentList.toString()));
                        }
                        fi;
                        index <- index + 1;
                    }
                    pool;
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
                newObject : Object,
                atoiConverter : A2I <- new A2I
            in (
                while looping loop {
                    somestr <- in_string();
                    token <- "";
                    hasNextToken <- true;

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

                    if somestr = "\n" then
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

                        headToken <- tConv.dCString(tokensList.getHead());

                        if headToken = "Soda" then
                            newObject <- new Soda.init(
                                tConv.dCString(tokensList.getIndex(1)),
                                tConv.dCString(tokensList.getIndex(2)),
                                atoiConverter.a2i(tConv.dCString(tokensList.getIndex(3)))
                            )
                        else if headToken = "Coffee" then
                            newObject <- new Coffee.init(
                                tConv.dCString(tokensList.getIndex(1)),
                                tConv.dCString(tokensList.getIndex(2)),
                                atoiConverter.a2i(tConv.dCString(tokensList.getIndex(3)))
                            )
                        else if headToken = "Laptop" then
                            newObject <- new Laptop.init(
                                tConv.dCString(tokensList.getIndex(1)),
                                tConv.dCString(tokensList.getIndex(2)),
                                atoiConverter.a2i(tConv.dCString(tokensList.getIndex(3)))
                            )
                        else if headToken = "Router" then
                            newObject <- new Router.init(
                                tConv.dCString(tokensList.getIndex(1)),
                                tConv.dCString(tokensList.getIndex(2)),
                                atoiConverter.a2i(tConv.dCString(tokensList.getIndex(3)))
                            )
                        else if headToken = "Private" then
                            newObject <- new Private.init(tConv.dCString(tokensList.getIndex(1)))
                        else if headToken = "Corporal" then
                            newObject <- new Corporal.init(tConv.dCString(tokensList.getIndex(1)))
                        else if headToken = "Sergent" then
                            newObject <- new Sergent.init(tConv.dCString(tokensList.getIndex(1)))
                        else if headToken = "Officer" then
                            newObject <- new Officer.init(tConv.dCString(tokensList.getIndex(1)))
                        else 
                            abort()
                        fi fi fi fi fi fi fi fi;

                        currentList.add(newObject);
                    }
                    else 
                        looping <- false
                    fi;
                } pool
            )
        );
        0;
        }
    };
};