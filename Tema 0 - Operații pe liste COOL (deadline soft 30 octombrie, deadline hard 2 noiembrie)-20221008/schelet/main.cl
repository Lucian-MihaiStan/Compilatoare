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
    lists : List;
    looping : Bool <- true;
    somestr : String;

    main():Object {
        {

            (
                let 
                    tokenizer : StringTokenizer <- new StringTokenizer,
                    token : String, 
                    hasNextToken : Bool,
                    tConv : DynamicCast <- new DynamicCast,
                    tokensList : List,
                    headToken : String,
                    exitCon : Int,
                    newObject : Object,
                    atoiConverter : A2I <- new A2I,
                    isFirstToken : Bool <- true
                in (
                    while looping loop {
                        -- out_string("Your name: ");
                        somestr <- in_string();
                        token <- "";
                        hasNextToken <- true;
                        isFirstToken <- true;

                        if somestr = "END" then
                            exitCon <- 1
                        else 0 fi;

                        if somestr = "" then
                            exitCon <- 1
                        else 0 fi;

                        if somestr = "\n" then
                            exitCon <- 1
                        else 0 fi;

                        if exitCon = 1 then
                            looping <- false
                        else
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

                        }
                        fi;

                    } pool
                )
            );

            looping <- true;

            (
                let
                    command : String

                in
                ({
                    while looping loop {
                        command <- in_string();
                        out_string(command.concat("\n"));
                    } pool;
                })
            );
        }
    };
};