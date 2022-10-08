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

    getprice():Int{ price * 119 / 100 };

    toString():String {
        name.concat("(").concat(model).concat(",").concat(additionalData).concat(")")
    };
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
};

class Private inherits Rank {};

class Corporal inherits Private {};

class Sergent inherits Corporal {};

class Officer inherits Sergent {};