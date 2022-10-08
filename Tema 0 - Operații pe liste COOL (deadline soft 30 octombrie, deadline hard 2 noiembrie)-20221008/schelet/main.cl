class StringTokenizer inherits IO {

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
    -- lists : List;
    looping : Bool <- true;
    somestr : String;

    main():Object {
        (
            let tokenizer : StringTokenizer <- new StringTokenizer, token : String, hasNextToken : Bool, empty : Int, newLine : Int, exitCon : Int
            in (
                while looping loop {
                    -- out_string("Your name: ");
                    somestr <- in_string();
                    token <- "";
                    hasNextToken <- true;
                    empty <- 0;
                    newLine <- 0;
                    exitCon <- 0;

                    if somestr = "" then
                        empty <- 1
                    else 0 fi;

                    if somestr = "\n" then
                        newLine <- 1
                    else 0 fi;

                    exitCon <- newLine + empty;

                    if exitCon = 1 then 
                        looping <- false
                    else 
                    {
                        tokenizer.reset();
                        tokenizer.withContent(somestr).withDelimiter(" ");
                        
                        while hasNextToken loop {
                            if token = somestr then
                                hasNextToken <- false
                            else 
                            {
                                token <- tokenizer.nextToken();
                                out_string(token.concat("\n"));
                            }
                            fi;
                        } pool;
                    }
                    fi;

                } pool
            )
        )

    };
};