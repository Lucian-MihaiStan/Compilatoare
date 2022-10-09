(*******************************
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

class Officer inherits Sergent { getRank():Int {4}; };