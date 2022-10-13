(*
    Laborator COOL.
*)

(*
    Exercițiul 1.

    Implementați funcția fibonacci, utilizând atât varianta recursivă,
    cât și cea iterativă.
*)
class Fibo {
    fibo_rec(n : Int) : Int {
        if n = 0 then
            0
        else if n = 1 then
            1
        else
            fibo_rec(n - 1) + fibo_rec(n - 2)
        fi fi
    };

    fibo_iter(n : Int) : Int {
        let
            it : Int <- 2, n_1 : Int, n_2 : Int, n_c : Int
        in {
            if n = 0 then
                n_c <- 0
            else if n = 1 then
                n_c <- 1
            else {
                n_c <- 1;
                n_2 <- 1;
                n_1 <- 1;
                while it < n loop {
                    n_c <- n_1 + n_2;
                    n_2 <- n_1;
                    n_1 <- n_c;
                    it <- it + 1;
                } pool;
            } fi fi;
            n_c;
        }
    };
};
    
(*
    Exercițiul 2.

    Pornind de la ierarhia de clase implementată la curs, aferentă listelor
    (găsiți clasele List și Cons mai jos), implementați următoarele funcții
    și testați-le. Este necesară definirea lor în clasa List și supradefinirea
    în clasa Cons.

    * append: întoarce o nouă listă rezultată prin concatenarea listei curente
        (self) cu lista dată ca parametru;
    * reverse: întoarce o nouă listă cu elementele în ordine inversă.
*)

(*
    Listă omogenă cu elemente de tip Int. Clasa List constituie rădăcina
    ierarhiei de clase reprezentând liste, codificând în același timp
    o listă vidă.

    Adaptare după arhiva oficială de exemple a limbajului COOL.
*)
class List inherits IO {
    isEmpty() : Bool { true };

    -- 0, deși cod mort, este necesar pentru verificarea tipurilor
    hd() : Int { { abort(); 0; } };

    -- Similar pentru self
    tl() : List { { abort(); self; } };

    cons(h : Int) : Cons {
        new Cons.init(h, self)
    };

    print() : IO { out_string("\n") };

    append(list : List) : List { { abort(); self; } };

    reverse() : List { { abort(); self; } };

    map(map_func : Map) : List { { abort(); self; } }; 

    filter(filter_func : Filter) : List { { abort(); self; } };
};

(*
    În privința vizibilității, atributele sunt implicit protejate, iar metodele,
    publice.

    Atributele și metodele utilizează spații de nume diferite, motiv pentru care
    hd și tl reprezintă nume atât de atribute, cât și de metode.
*)
class Cons inherits List {
    hd : Int;
    tl : List;

    init(h : Int, t : List) : Cons {
        {
            hd <- h;
            tl <- t;
            self;
        }
    };

    -- Supradefinirea funcțiilor din clasa List
    isEmpty() : Bool { false };

    hd() : Int { hd };

    tl() : List { tl };

    print() : IO {
        {
            out_int(hd);
            out_string(" ");
            -- Mecanismul de dynamic dispatch asigură alegerea implementării
            -- corecte a metodei print.
            tl.print();
        }
    };

    append(list : List) : List {
        let
            result : List <- new List,
            tmp : List <- self.reverse(),
            tmp_snd : List <- list.reverse()
        in {
            while (not tmp.isEmpty()) loop {
                result <- result.cons(tmp.hd());
                tmp <- tmp.tl();
            } pool;

            while (not tmp_snd.isEmpty()) loop {
                result <- result.cons(tmp_snd.hd());
                tmp_snd <- tmp_snd.tl();
            } pool;

            result;
        }
    };

    reverse() : List {
        let 
            result : List <- new List,
            tmp : List <- self
        in {

            while (not tmp.isEmpty()) loop {
                result <- result.cons(tmp.hd());
                tmp <- tmp.tl();
            } pool;

            result;
        }
    };

    map(map_func : Map) : List {
        let
            mapped_list : List <- new List,
            tmp : List <- self
        in {

            while (not tmp.isEmpty()) loop {
                mapped_list <- mapped_list.cons(map_func.apply(tmp.hd()));
                tmp <- tmp.tl();
            } pool;

            mapped_list.reverse();
        }
    };

    filter(filter_func : Filter) : List {
        let
            filtered_list : List <- new List,
            tmp : List <- self,
            head : Int
        in {

            while (not tmp.isEmpty()) loop {
                head <- tmp.hd();
                if filter_func.apply(head) then
                    filtered_list <- filtered_list.cons(head)
                else 0 fi;
                tmp <- tmp.tl();
            } pool;

            filtered_list.reverse();
        }
    };
};

(*
    Exercițiul 3.

    Scopul este implementarea unor mecanisme similare funcționalelor
    map și filter din limbajele funcționale. map aplică o funcție pe fiecare
    element, iar filter reține doar elementele care satisfac o anumită condiție.
    Ambele întorc o nouă listă.

    Definiți clasele schelet Map, respectiv Filter, care vor include unica
    metodă apply, având tipul potrivit în fiecare clasă, și implementare
    de formă.

    Pentru a defini o funcție utilă, care adună 1 la fiecare element al listei,
    definiți o subclasă a lui Map, cu implementarea corectă a metodei apply.

    În final, definiți în cadrul ierarhiei List-Cons o metodă map, care primește
    un parametru de tipul Map.

    Definiți o subclasă a subclasei de mai sus, care, pe lângă funcționalitatea
    existentă, de incrementare cu 1 a fiecărui element, contorizează intern
    și numărul de elemente prelucrate. Utilizați static dispatch pentru apelarea
    metodei de incrementare, deja definită.

    Repetați pentru clasa Filter, cu o implementare la alegere a metodei apply.
*)

class Map inherits IO {
    
    apply(obj : Int) : Int { obj };

};

class Map_add inherits Map {

    apply(obj : Int) : Int {
        obj + 1
    };

};

class Filter inherits IO {

    apply(obj : Int) : Bool { { abort(); false; } };

};

class Filter_condition inherits Filter {

    apply(obj : Int) : Bool {
        obj < 5
    };
        

};

-- Testați în main.
class Main inherits IO {
    main() : Object {
        let list : List <- new List.cons(1).cons(2).cons(3),
            list_snd : List <- new List.cons(4).cons(5).cons(6),
            big_list : List <- new List.cons(1).cons(2).cons(3).cons(4).cons(5).cons(6).cons(7).cons(8),
            merge : List, reverse : List,
            temp : List <- list,
            fibo : Fibo <- new Fibo,
            index : Int <- 0,
            add_map : Map_add <- new Map_add,
            less_filter : Filter_condition <- new Filter_condition,
            mapped_list : List,
            filtered_list : List
        in
            {
                -- Afișare utilizând o buclă while. Mecanismul de dynamic
                -- dispatch asigură alegerea implementării corecte a metodei
                -- isEmpty, din clasele List, respectiv Cons.
                while (not temp.isEmpty()) loop
                    {
                        out_int(temp.hd());
                        out_string(" ");
                        temp <- temp.tl();
                    }
                pool;

                out_string("\n");

                -- Afișare utilizând metoda din clasele pe liste.
                list.print();

                index <- 0;
                while index < 15 loop {
                    out_int(fibo.fibo_rec(index));
                    out_string(" ");
                    index <- index + 1;
                } pool;

                out_string("\n");

                index <- 0;
                while index < 15 loop {
                    out_int(fibo.fibo_iter(index));
                    out_string(" ");
                    index <- index + 1 ;
                } pool;

                out_string("\n");

                list.print();
                list_snd.print();

                merge <- list.append(list_snd);
                merge.print();

                reverse <- list.reverse();
                reverse.print();

                mapped_list <- list.map(add_map);
                mapped_list.print();

                filtered_list <- big_list.filter(less_filter);
                filtered_list.print();
            }
    };
};