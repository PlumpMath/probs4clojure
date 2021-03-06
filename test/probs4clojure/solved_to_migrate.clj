(ns probs4clojure.solved-to-migrate)

;; ;; See test/probs4clojure/core_test.clj for more recent solutions...



;; ;; Problem 171
;; ;; Intervals
;; ;;  
;; ;; Difficulty:	Medium
;; ;; Topics:	
;; ;; 
;; ;; 
;; ;; Write a function that takes a sequence of integers and returns a sequence of "intervals". Each interval is a a vector of two integers, start and end, such that all integers between start and end (inclusive) are contained in the input sequence.
;; ;; 	
;; ;; (= (__ [1 2 3]) [[1 3]])
;; ;; 	
;; ;; (= (__ [10 9 8 1 2 3]) [[1 3] [8 10]])
;; ;; 	
;; ;; (= (__ [1 1 1 1 1 1 1]) [[1 1]])
;; ;; 	
;; ;; (= (__ []) [])
;; ;; 	
;; ;; (= (__ [19 4 17 1 3 10 2 13 13 2 16 4 2 15 13 9 6 14 2 11])
;; ;;        [[1 4] [6 6] [9 11] [13 17] [19 19]])

;; ((fn [l]
;;    (if-not (seq l)
;;      []
;;      (let [sorted (distinct (sort l))]
;;        (loop [s sorted, ret [], a (first s), b a]
;;          (if (seq s)
;;            (if (> (first s) (inc b))
;;              (recur (rest s)
;;                     (conj ret [a b])
;;                     (first s)
;;                     (first s))
;;              (recur (rest s)
;;                     ret
;;                     a
;;                     (first s)))
;;            (conj ret [a b]))))))
;;   [19 4 17 1 3 10 2 13 13 2 16 4 2 15 13 9 6 14 2 11])


;; ;; 173
;; ;; Intro to Destructuring 2
;; ;;
;; ;; Difficulty:	Easy
;; ;; Topics:	Destructuring
;; ;;
;; ;;
;; ;; Sequential destructuring allows you to bind symbols to parts of
;; ;; sequential things (vectors, lists, seqs, etc.): (let [bindings* ]
;; ;; exprs*) Complete the bindings so all let-parts evaluate to 3.
;; ;;
;; ;; (= 3
;; ;;   (let [[__] [+ (range 3)]] (apply __))
;; ;;   (let [[[__] b] [[+ 1] 2]] (__ b))
;; ;;   (let [[__] [inc 2]] (__)))



;; (= 3
;;   (let [[f x] [+ (range 3)]] (apply f x))
;;   (let [[[f x] b] [[+ 1] 2]] (f x b))
;;   (let [[f x] [inc 2]] (f x)))



;; "
;; #158 Decurry

;; Difficulty:Medium
;; Topics:partial-functions


;; Write a function that accepts a curried function of unknown arity
;; n. Return an equivalent function of n arguments.  You may wish to
;; read this: http://en.wikipedia.org/wiki/Currying
;; "

;; (defn g [f]
;;   (fn l [& xs]
;;     (loop [f f
;;            [x & xs] xs]
;;       (if-not (seq? xs)
;;         (f x)
;;         (recur (f x) xs)))))

;; (expect 10 ((g (fn [a]
;;              (fn [b]
;;                (fn [c]
;;                  (fn [d]
;;                    (+ a b c d))))))
;;        1 2 3 4))

;; (expect 24 ((g (fn [a]
;;              (fn [b]
;;                (fn [c]
;;                  (fn [d]
;;                    (* a b c d))))))
;;        1 2 3 4))

;; (expect 25 ((g (fn [a]
;;              (fn [b]
;;                (* a b))))
;;        5 5))



;; ;; #131: Sum Some Set Subsets
;; ;;
;; ;; Difficulty:Medium
;; ;; Topics:math
;; ;;
;; ;;
;; ;; Given a variable number of sets of integers, create a function
;; ;; which returns true iff all of the sets have a non-empty subset with
;; ;; an equivalent summation.

;; (defn gg [& ss]
;;   "
;;   Certainly there is a more elegant way to do this, but the approach is:
;;   - form a set of non-empty subsets of each sequence; this is done by
;;     creating a set of true or false variables indicating whether each element
;;     is in the subset;
;;   - sum each of these subsets;
;;   - look for a nonempty intersection of sums across the sequences.
;;   "
;;   (letfn [(tmult [s]
;;             (if (seq s)
;;               (for [el s, tf [true false]] (conj el tf))
;;               [[true] [false]]))
;;           (subsets [s]
;;             (let [v (vec s)
;;                   n (count s)
;;                   tfs (nth (iterate tmult []) n)
;;                   sets (filter (complement empty?)
;;                                (for [bits tfs]
;;                                  (filter (complement nil?)
;;                                          (for [i (range n)]
;;                                            (if (nth bits i)
;;                                              (nth v i))))))]
;;               sets))
;;           (sums [s]
;;             (map #(apply + %) (subsets s)))
;;           (intersection [a b]
;;             (set (for [el b :when (contains? a el)] el)))
;;           (intersections [ss]
;;             (reduce intersection ss))]
;;     (let [allsums (map (comp set sums) ss)
;;           common (intersections allsums)]
;;       (not (empty? common)))))


;; (expect true  (gg #{-1 1 99}
;;              #{-2 2 888}
;;              #{-3 3 7777})) ; ex. all sets have a subset which sums to zero

;; (expect false (gg #{1}
;;                   #{2}
;;                   #{3}
;;                   #{4}))

;; (expect true  (gg #{1}))


;; (expect false (gg #{1 -3 51 9}
;;              #{0}
;;              #{9 2 81 33}))

;; (expect true  (gg #{1 3 5}
;;              #{9 11 4}
;;              #{-3 12 3}
;;              #{-3 4 -2 10}))

;; (expect false (gg #{-1 -2 -3 -4 -5 -6}
;;              #{1 2 3 4 5 6 7 8 9}))

;; (expect true  (gg #{1 3 5 7}
;;              #{2 4 6 8}))

;; (expect true  (gg #{-1 3 -5 7 -9 11 -13 15}
;;              #{1 -3 5 -7 9 -11 13 -15}
;;              #{1 -1 2 -2 4 -4 8 -8}))

;; (expect true  (gg #{-10 9 -8 7 -6 5 -4 3 -2 1}
;;                           #{10 -9 8 -7 6 -5 4 -3 2 -1}))



;; ;; The Big Divide
;; ;;
;; ;; Difficulty:Medium
;; ;; Topics:math
;; ;;
;; ;;
;; ;; Write a function which calculates the sum of all natural numbers
;; ;; under n (first argument) which are evenly divisible by at least one
;; ;; of a and b (second and third argument). Numbers a and b are
;; ;; guaranteed to be coprimes.
;; ;;
;; ;; Note: Some test cases have a very large n, so the most obvious
;; ;; solution will exceed the time limit.


;; (defn gg
;;   [T a b]
;;   (letfn
;;       [(sumrange [m] (/ (* m (inc m)) 2))
;;        (sumproducts [T a]
;;          (* a (sumrange (bigint (/ (dec (bigint T)) a)))))]
;;     (- (+ (sumproducts T a) (sumproducts T b))
;;        (sumproducts T (* a b)))))

;; (expect 0 (gg 3 17 11))

;; (expect 23 (gg 10 3 5))

;; (expect 233168 (gg 1000 3 5))

;; (expect "2333333316666668"
;;         (str (gg 100000000 3 5)))

;; (expect "110389610389889610389610"
;;         (str (gg (* 10000 10000 10000) 7 11)))


;; (expect "1277732511922987429116"
;;         (str (gg (* 10000 10000 10000) 757 809)))

;; (expect "4530161696788274281"
;;         (str (gg (* 10000 10000 1000) 1597 3571)))



;; ;; Sequs Horribilis
;; ;;
;; ;; Difficulty:Medium
;; ;; Topics:seqs
;; ;;
;; ;; Create a function which takes an integer and a nested collection of
;; ;; integers as arguments. Analyze the elements of the input collection
;; ;; and return a sequence which maintains the nested structure, and
;; ;; which includes all elements starting from the head whose sum is
;; ;; less than or equal to the input integer.
;; ;;
;; ;; (=  (__ 10 [1 2 [3 [4 5] 6] 7])
;; ;;     '(1 2 (3 (4))))
;; ;;
;; ;; (=  (__ 30 [1 2 [3 [4 [5 [6 [7 8]] 9]] 10] 11])
;; ;;     '(1 2 (3 (4 (5 (6 (7)))))))
;; ;;
;; ;; (=  (__ 9 (range))
;; ;;     '(0 1 2 3))
;; ;;
;; ;; (=  (__ 1 [[[[[1]]]]])
;; ;;     '(((((1))))))
;; ;;
;; ;; (=  (__ 0 [1 2 [3 [4 5] 6] 7])
;; ;;     '())
;; ;;
;; ;; (=  (__ 0 [0 0 [0 [0]]])
;; ;;     '(0 0 (0 (0))))
;; ;;
;; ;; (=  (__ 1 [-10 [1 [2 3 [4 5 [6 7 [8]]]]]])
;; ;;        '(-10 (1 (2 3 (4)))))

;; (defn ff [T s]
;;   (letfn [(inner [T s S]
;;             (loop [s s, S S, ret []]
;;               (let [[x & xs] s
;;                     nS (if (or (coll? x) (nil? x)) S (+ x S))
;;                     [fs fS] (if (coll? x) (inner T x nS) [x nS])]
;;                 (if (and (seq s) (<= fS T))
;;                   (recur xs fS (conj ret fs))
;;                   [ret S]))))]
;;     ((inner T s 0) 0)))

;; (expect  (ff 10 [1 2 [3 [4 5] 6] 7])
;;          '(1 2 (3 (4))))

;; (expect  (ff 30 [1 2 [3 [4 [5 [6 [7 8]] 9]] 10] 11])
;;          '(1 2 (3 (4 (5 (6 (7)))))))

;; (expect  (ff 9 (range))
;;          '(0 1 2 3))

;; (expect (ff 1 [[[[[1]]]]])
;;         '(((((1))))))

;; (expect  (ff 0 [1 2 [3 [4 5] 6] 7])
;;          '())

;; (expect  (ff 0 [0 0 [0 [0]]])
;;          '(0 0 (0 (0))))

;; (expect  (ff 1 [-10 [1 [2 3 [4 5 [6 7 [8]]]]]])
;;          '(-10 (1 (2 3 (4)))))




;; ;; Universal Computation Engine
;; ;;
;; ;; Difficulty:Medium
;; ;; Topics:functions
;; ;;
;; ;; Given a mathematical formula in prefix notation, return a function
;; ;; that calculates the value of the formula. The formula can contain
;; ;; nested calculations using the four basic mathematical operators,
;; ;; numeric constants, and symbols representing variables. The returned
;; ;; function has to accept a single parameter containing the map of
;; ;; variable names to their values.
;; ;;
;; ;; (= 2 ((__ '(/ a b))
;; ;;       '{b 8 a 16}))
;; ;;
;; ;; (= 8 ((__ '(+ a b 2))
;; ;;       '{a 2 b 4}))
;; ;;
;; ;; (= [6 0 -4]
;; ;;    (map (__ '(* (+ 2 a)
;; ;;                 (- 10 b)))
;; ;;         '[{a 1 b 8}
;; ;;           {b 5 a -2}
;; ;;           {a 2 b 11}]))
;; ;;
;; ;; (= 1 ((__ '(/ (+ x 2)
;; ;;               (* 3 (+ y 1))))
;; ;;             '{x 4 y 1}))

;; (defn f [expr]
;;   (cond
;;    (number? expr) (fn [m] expr)
;;    (symbol? expr) (fn [m] (m expr))
;;    :else (fn [m]
;;            (let [[op & args] expr
;;                  mapseq (map #((f %) m) args)
;;                  opmap {'/ /, '* *, '+ +, '- -}]  ;; FIXME -- maybe a cleaner way?
;;              (apply (opmap op) (map #((f %) m) args))))))


;; (expect 3 ((f 'a) '{a 3}))
;; (expect 2 ((f 2) '{}))

;; (expect 2 ((f '(/ a b))
;;            '{b 8 a 16}))

;; (expect 8 ((f '(+ a b 2))
;;            '{a 2 b 4}))

;; (expect 1 ((f '(/ (+ x 2)
;;                   (* 3 (+ y 1))))
;;            '{x 4 y 1}))

;; (expect [6 0 -4]
;;         (map (f '(* (+ 2 a)
;;                     (- 10 b)))
;;              '[{a 1 b 8}
;;                {b 5 a -2}
;;                {a 2 b 11}]))



;; ;; Prime Sandwich
;; ;;
;; ;; Difficulty:Medium
;; ;; Topics:math
;; ;;
;; ;; A balanced prime is a prime number which is also the mean of the
;; ;; primes directly before and after it in the sequence of valid
;; ;; primes. Create a function which takes an integer n, and returns
;; ;; true iff it is a balanced prime.
;; ;;
;; ;; (= false (__ 4))
;; ;;
;; ;; (= true (__ 563))
;; ;;
;; ;; (= 1103 (nth (filter __ (range)) 15))

;; (defn is-balanced-prime [x]
;;   (letfn [(is-prime [x]
;;             (not (some #(= (rem x %) 0) (range 2 x))))
;;           (next-prime [x]
;;             (first (filter is-prime (drop (inc x) (range)))))
;;           (prev-prime [x]
;;             (first (filter is-prime (range (dec x) 0 -1))))]
;;     (and (> x 2)
;;          (is-prime x)
;;          (let [prev-prime (prev-prime x)
;;                next-prime (next-prime x)]
;;            (= x (/ (+ prev-prime next-prime) 2))))))

;; (expect false (is-balanced-prime 4))

;; (expect true (is-balanced-prime 563))

;; (expect (nth (filter is-balanced-prime (range)) 15)
;;         1103)



;; ;; Oscilrate
;; ;;
;; ;; Difficulty:Medium
;; ;; Topics:sequences
;; ;;
;; ;; Write an oscillating iterate: a function that takes an initial
;; ;; value and a variable number of functions. It should return a lazy
;; ;; sequence of the functions applied to the value in order, restarting
;; ;; from the first function after it hits the end.
;; ;;
;; ;; (= (take 3 (__ 3.14 int double)) [3.14 3 3.0])
;; ;;
;; ;; (= (take 5 (__ 3 #(- % 3) #(+ 5 %))) [3 0 5 2 7])
;; ;;
;; ;; (= (take 12 (__ 0 inc dec inc dec inc)) [0 1 0 1 0 1 2 1 2 1 2 3])


;; (defn f [x & s]
;;   (letfn [(g [x s]
;;             (lazy-seq
;;                (cons x
;;                      (g ((first s) x)
;;                         (next s)))))]
;;     (g x (cycle s))))

;; (expect (take 3 (f 3.14 int double)) [3.14 3 3.0])
;; (expect (take 5 (f 3 #(- % 3) #(+ 5 %))) [3 0 5 2 7])
;; (expect (take 12 (f 0 inc dec inc dec inc)) [0 1 0 1 0 1 2 1 2 1 2 3])


;; ;; Insert between two items
;; ;;
;; ;; Difficulty:Medium
;; ;; Topics:seqs core-functions
;; ;;
;; ;;
;; ;; Write a function that takes a two-argument predicate, a value, and
;; ;; a collection; and returns a new collection where the value is
;; ;; inserted between every two items that satisfy the predicate.
;; ;;
;; ;; (= '(1 :less 6 :less 7 4 3) (__ < :less [1 6 7 4 3]))
;; ;;
;; ;; (= '(2) (__ > :more [2]))
;; ;;
;; ;; (= [0 1 :x 2 :x 3 :x 4]  (__ #(and (pos? %) (< % %2)) :x (range 5)))
;; ;;
;; ;; (empty? (__ > :more ()))
;; ;;
;; ;; (= [0 1 :same 1 2 3 :same 5 8 13 :same 21]
;; ;;    (take 12 (->> [0 1]
;; ;;                  (iterate (fn [[a b]] [b (+ a b)]))
;; ;;                  (map first) ; fibonacci numbers
;; ;;                  (__ (fn [a b] ; both even or both odd
;; ;;                        (= (mod a 2) (mod b 2)))
;; ;;                                           :same))))

;; (defn f [pred kw s]
;;   (if (seq s)
;;     (let [p (partition 2 1 s)]
;;       (concat [(first s)]
;;               (apply concat (for [[a b] p]
;;                               (if (pred a b)
;;                                 [kw b]
;;                                 [b])))))
;;     ()))

;; (expect '(1 :less 6 :less 7 4 3) (f < :less [1 6 7 4 3]))
;; (expect '(2) (f > :more [2]))
;; (expect [0 1 :x 2 :x 3 :x 4]  (f #(and (pos? %) (< % %2)) :x (range 5)))
;; (expect (empty? (f > :more ())) true)
;; (expect [0 1 :same 1 2 3 :same 5 8 13 :same 21]
;;         (take 12 (->> [0 1]
;;                       (iterate (fn [[a b]] [b (+ a b)]))
;;                       (map first) ; fibonacci numbers
;;                       (f (fn [a b] ; both even or both odd
;;                            (= (mod a 2) (mod b 2)))
;;                          :same))))



;; ;; Global take-while

;; ;; Difficulty:Medium
;; ;; Topics:seqs higher-order-functions


;; ;; take-while is great for filtering sequences, but it limited: you can
;; ;; only examine a single item of the sequence at a time. What if you need
;; ;; to keep track of some state as you go over the sequence?

;; ;; Write a function which accepts an integer n, a predicate p, and a
;; ;; sequence. It should return a lazy sequence of items in the list up to,
;; ;; but not including, the nth item that satisfies the predicate.



;; ;; (= [2 3 5 7 11 13]
;; ;;    (__ 4 #(= 2 (mod % 3))
;; ;;        [2 3 5 7 11 13 17 19 23]))

;; ;; (= ["this" "is" "a" "sentence"]
;; ;;    (__ 3 #(some #{\i} %)
;; ;;        ["this" "is" "a" "sentence" "i" "wrote"]))

;; ;; (= ["this" "is"]
;; ;;    (__ 1 #{"a"}
;; ;;                 ["this" "is" "a" "sentence" "i" "wrote"]))


;; (defn f
;;   [n p s]
;;   (lazy-seq
;;    (loop [s s, ret [], np 0]
;;      (if (and (< np n) (not (and (= np (dec n)) (p (first s)))))
;;        (recur (rest s)
;;               (conj ret (first s))
;;               (if (p (first s)) (inc np) np))
;;        ret))))

;; (println (f 4 #(= 2 (mod % 3)) [2 3 5 7 11 13 17 19 23]))

;; (println
;;    (f 3 #(some #{\i} %)
;;        ["this" "is" "a" "sentence" "i" "wrote"]))

;; (println
;;    (f 1 #{"a"}
;;                 ["this" "is" "a" "sentence" "i" "wrote"]))


;; ;; initially:
;; ;; n 4  s [2 3 5 7 11 13 17 19 23]

;; ;; initially:
;; ;; n 4  s [2 3 5 7 11 13 17 19 23]


;; ;; loop begin:

;; ;; s [2 3 5 7 11 13 17 19 23] ret []        np 0   (< np n) true

;; ;; recur
;; ;;     [3 5 7 11 13 17 19 23]     [2]       np 1
;; ;;                                                 (< np n) true
;; ;;       [5 7 11 13 17 19 23]     [2 3]     np 1
;; ;;                                                 (< np n) true
;; ;;         [7 11 13 17 19 23]     [2 3 5]   np 2
;; ;;                                                 (< np n) true
;; ;;           [11 13 17 19 23]     [2 3 5 7] np 2
;; ;;                                                 (< np n) true
;; ;;              [13 17 19 23]     [2 3 5 7 11]
;; ;;                                          np 3
;; ;;                                                 (< np n) true
;; ;;                 [17 19 23]     [2 3 5 7 11 13]
;; ;;                                          np 3
;; ;;                                                 (< np n) true
;; ;;                    [19 23]     [2 3 5 7 11 13 17]
;; ;;                                          np 4
;; ;;                                                 (< np n) false
;; ;;                          ----->[2 3 5 7 11 13 17]







;; ; Write Roman Numerals
;; ;
;; ;  Difficulty:	Medium
;; ;  Topics:	strings math
;; ;
;; ;
;; ;  This is the inverse of Problem 92, but much easier. Given an integer
;; ;  smaller than 4000, return the corresponding roman numeral in uppercase,
;; ;  adhering to the subtractive principle.
;; ;
;; ;  (= "I" (__ 1))
;; ;
;; ;  (= "XXX" (__ 30))
;; ;
;; ;  (= "IV" (__ 4))
;; ;
;; ;  (= "CXL" (__ 140))
;; ;
;; ;  (= "DCCCXXVII" (__ 827))
;; ;
;; ;  (= "MMMCMXCIX" (__ 3999))
;; ;
;; ;  (= "XLVIII" (__ 48))

;; (defn R [i]
;;   (let [romans (into (sorted-map-by #(> %1 %2))
;;                      {1000 "M"
;;                       900  "CM"
;;                       500  "D"
;;                       400  "CD"
;;                       100  "C"
;;                       90   "XC"
;;                       50   "L"
;;                       40   "XL"
;;                       10   "X"
;;                       9    "IX"
;;                       5    "V"
;;                       4    "IV"
;;                       1    "I"})
;;         allchars (loop [remain i, charlist [], romans romans, ret []]
;;                    (let [[n char] (first romans)
;;                          x (quot remain n)
;;                          nx-chars (concat charlist (repeat x char))]
;;                      (if (seq (rest romans))
;;                        (recur (- remain (* x n))
;;                               nx-chars
;;                               (rest romans)
;;                               (concat ret nx-chars))
;;                        nx-chars)))]
;;     (apply str (concat allchars))))


;; (expect "I" (R 1))
;; (expect "II" (R 2))
;; (expect "III" (R 3))
;; (expect "IV" (R 4))
;; (expect "V" (R 5))
;; (expect "VI" (R 6))
;; (expect "VII" (R 7))
;; (expect "VIII" (R 8))
;; (expect "IX" (R 9))
;; (expect "X" (R 10))
;; (expect "XX" (R 20))
;; (expect "XLVIII" (R 48))
;; (expect "L" (R 50))
;; (expect "C" (R 100))
;; (expect "CL" (R 150))
;; (expect "CXL" (R 140))
;; (expect "CC" (R 200))
;; (expect "M" (R 1000))
;; (expect "MD" (R 1500))
;; (expect "MM" (R 2000))
;; (expect "MMM" (R 3000))
;; (expect "CXL" (R 140))
;; (expect "DCCCXXVII" (R 827))
;; (expect "MMMCMXCIX" (R 3999))



;; ;  Digits and bases
;; ;
;; ;  Difficulty:	Medium
;; ;  Topics:	math
;; ;
;; ;
;; ;  Write a function which returns a sequence of digits of a non-negative number
;; ;  (first argument) in numerical system with an arbitrary base (second argument).
;; ;  Digits should be represented with their integer values, e.g. 15 would be
;; ;  [1 5] in base 10, [1 1 1 1] in base 2 and [15] in base 16.
;; ;
;; ;  (= [1 2 3 4 5 0 1] (__ 1234501 10))
;; ;
;; ;  (= [0] (__ 0 11))
;; ;
;; ;  (= [1 0 0 1] (__ 9 2))
;; ;
;; ;  (= [1 0] (let [n (rand-int 100000)](__ n n)))
;; ;
;; ;  (= [16 18 5 24 15 1] (__ Integer/MAX_VALUE 42))

;; (def f
;;   (fn [n base]
;;     (loop [n n
;;            ret []]
;;       (let [dig (rem n base)
;;             nxt (int (Math/floor (/ n base)))]
;;         (if (zero? nxt)
;;           (cons dig ret)
;;           (recur nxt (cons dig ret))))))
;;   )

;; (expect [1 2 3 4 5 0 1] (f 1234501 10))
;; (expect [16 18 5 24 15 1] (f Integer/MAX_VALUE 42))




;; ;  Indexing Sequences
;; ;
;; ;  Difficulty:	Easy
;; ;  Topics:	seqs
;; ;
;; ;
;; ;  Transform a sequence into a sequence of pairs containing the original
;; ;  elements along with their index.
;; ;
;; ;  (= (__ [:a :b :c]) [[:a 0] [:b 1] [:c 2]])
;; ;
;; ;  (= (__ [0 1 3]) '((0 0) (1 1) (3 2)))
;; ;
;; ;  (= (__ [[:foo] {:bar :baz}]) [[[:foo] 0] [{:bar :baz} 1]])

;; ;;; HINT: http://stackoverflow.com/questions/4830900/how-do-i-find-the-index-of-an-item-in-a-vector



;; (def x (fn [l]
;;          (for [[x y] (map-indexed vector l)]
;;            [y x])))

;; (println (for [[x y] (map-indexed vector [:a :b :c])]
;;            [y x]))
;; (println (x [:a :b :c]))

;; (expect (x [:a :b :c]) [[:a 0] [:b 1] [:c 2]])
;; (expect (x [0 1 3]) '((0 0) (1 1) (3 2)))


;; ;; Sequence of pronunciations
;; ;;
;; ;; Difficulty:Medium
;; ;; Topics:seqs
;; ;;
;; ;;
;; ;; Write a function that returns a lazy sequence of "pronunciations"
;; ;; of a sequence of numbers. A pronunciation of each element in the
;; ;; sequence consists of the number of repeating identical numbers and
;; ;; the number itself. For example, [1 1] is pronounced as [2 1] ("two
;; ;; ones"), which in turn is pronounced as [1 2 1 1] ("one two, one
;; ;; one").
;; ;;
;; ;; Your function should accept an initial sequence of numbers, and
;; ;; return an infinite lazy sequence of pronunciations, each element
;; ;; being a pronunciation of the previous element.
;; ;;
;; ;;
;; ;;
;; ;; (= [[1 1] [2 1] [1 2 1 1]] (take 3 (__ [1])))
;; ;;
;; ;; (= [3 1 2 4] (first (__ [1 1 1 4 4])))
;; ;;
;; ;; (= [1 1 1 3 2 1 3 2 1 1] (nth (__ [1]) 6))
;; ;;
;; ;; (= 338 (count (nth (__ [3 2]) 15)))

;; (defn pronunseq [s]
;;   (letfn [(next-term [s]
;;             (loop [l s
;;                    prev nil
;;                    ret []]
;;               (if (seq l)
;;                 (let [[newval & rst] l,
;;                       cntpos (- (count ret) 2)
;;                       oldcnt (get ret cntpos)
;;                       same? (= prev newval)]
;;                   (recur rst
;;                          newval
;;                          (if same?
;;                            (assoc ret cntpos (inc oldcnt))
;;                            (vec (concat ret [1 newval])))))
;;                 ret)))]
;;     (rest (iterate next-term s))))

;; (expect (take 3 (pronunseq [1])) [[1 1] [2 1] [1 2 1 1]])
;; (expect (count (nth (pronunseq [3 2]) 15)) 338)


;; ;; Partially Flatten a Sequence
;; ;;
;; ;; Difficulty:Medium
;; ;; Topics:seqs
;; ;;
;; ;;
;; ;; Write a function which flattens any nested combination of
;; ;; sequential things (lists, vectors, etc.), but maintains the lowest
;; ;; level sequential items. The result should be a sequence of
;; ;; sequences with only one level of nesting.
;; ;;
;; ;; (= (__ [["Do"] ["Nothing"]])
;; ;;    [["Do"] ["Nothing"]])
;; ;;
;; ;; (= (__ [[[[:a :b]]] [[:c :d]] [:e :f]])
;; ;;    [[:a :b] [:c :d] [:e :f]])
;; ;;
;; ;; (= (__ '((1 2) ((3 4) ((((5 6)))))))
;; ;;       '((1 2) (3 4) (5 6)))

;; (defn q [x]
;;   (letfn [(flat-seq? [x]
;;             (empty? (filter (fn [y] (coll? y)) x)))]
;;     (loop [x x, ret []]
;;       (cond
;;        (not (seq x)) ret
;;        (flat-seq? x) x
;;        (flat-seq? (first x)) (recur (rest x)
;;                                     (concat ret [(first x)]))
;;        :else (recur (rest x)
;;                     (concat ret (q (first x))))))))

;; (expect (q []) [])
;; (expect (q [1]) [1])
;; (expect (q [1 2]) [1 2])
;; (expect (q [[1 2]]) [[1 2]])
;; (expect (q [[1 2] [3 4]]) [[1 2] [3 4]])
;; (expect (q [[1 2] [3 4] [5 6]]) [[1 2] [3 4] [5 6]])
;; (expect (q [[[1 2]]]) [[1 2]])
;; (expect (q [[1 2] [[3 4] [[[[5 6]]]]]]) [[1 2] [3 4] [5 6]])
;; (expect (q [[[[:a :b]]] [[:c :d]] [:e :f]]) [[:a :b] [:c :d] [:e :f]])
;; (expect (q '((1 2) ((3 4) ((((5 6))))))) '((1 2) (3 4) (5 6)))
;; (expect (q [["Do"] ["Nothing"]]) [["Do"] ["Nothing"]])


;; ;; Identify keys and values
;; ;;
;; ;; Difficulty:Medium
;; ;; Topics:maps seqs
;; ;;
;; ;;
;; ;; Given an input sequence of keywords and numbers, create a map such
;; ;; that each key in the map is a keyword, and the value is a sequence of
;; ;; all the numbers (if any) between it and the next keyword in the
;; ;; sequence.

;; ;; (= {} (__ []))

;; ;; (= {:a [1]} (__ [:a 1]))

;; ;; (= {:a [1], :b [2]} (__ [:a 1, :b 2]))

;; ;; (= {:a [1 2 3], :b [], :c [4]} (__ [:a 1 2 3 :b :c 4]))


;; (defn unflatten-map [s]
;;   (loop [ret {}
;;          prev-kw []
;;          s s]
;;     (let [f (first s)
;;           kw? (keyword? f)
;;           num? (not kw?)
;;           this-kw (if kw? f prev-kw)
;;           prev-list (vec (ret this-kw))
;;           next-value (vec (concat (ret this-kw) (if num? [f] [])))
;;           next-map (assoc ret this-kw (if num? next-value []))]
;;       (if (seq s)
;;         (recur next-map this-kw (rest s))
;;         ret))))



;; ;; The Balance of N
;; ;;
;; ;; Difficulty:Medium
;; ;; Topics:math
;; ;;
;; ;; A balanced number is one whose component digits have the same sum
;; ;; on the left and right halves of the number. Write a function which
;; ;; accepts an integer n, and returns true iff n is balanced.

;; ;; (= true (__ 11))

;; ;; (= true (__ 121))

;; ;; (= false (__ 123))

;; ;; (= true (__ 0))

;; ;; (= false (__ 88099))

;; ;; (= true (__ 89098))

;; ;; (= true (__ 89089))

;; ;; (= (take 20 (filter __ (range)))
;; ;;    [0 1 2 3 4 5 6 7 8 9 11 22 33 44 55 66 77 88 99 101])


;; (defn balanced [n]
;;   (let [digits (map #(Integer/parseInt (str %)) (str n))
;;         len (count digits)
;;         pivot (int (/ len 2))
;;         [l r] (split-at pivot digits)
;;         r (if (even? len) r (rest r))]
;;     (= (apply + l) (apply + r))))

;; (expect true (balanced 11))

;; (expect (take 20 (filter balanced (range)))
;;         [0 1 2 3 4 5 6 7 8 9 11 22 33 44 55 66 77 88 99 101])


;; ;; Difficulty:Easy
;; ;; Topics:set-theory
;; ;; Given a set of sets, create a function which returns true if no two
;; ;; of those sets have any elements in common1 and false
;; ;; otherwise. Some of the test cases are a bit tricky, so pay a little
;; ;; more attention to them.

;; ;; 1 Such sets are usually called pairwise disjoint or mutually
;; ;; disjoint.

;; ;; (= (__ #{#{\U} #{\s} #{\e \R \E} #{\P \L} #{\.}})
;; ;;    true)

;; ;; (= (__ #{#{:a :b :c :d :e}
;; ;;          #{:a :b :c :d}
;; ;;          #{:a :b :c}
;; ;;          #{:a :b}
;; ;;          #{:a}})
;; ;;    false)

;; ;; (= (__ #{#{[1 2 3] [4 5]}
;; ;;          #{[1 2] [3 4 5]}
;; ;;          #{[1] [2] 3 4 5}
;; ;;          #{1 2 [3 4] [5]}})
;; ;;    true)

;; ;; (= (__ #{#{'a 'b}
;; ;;          #{'c 'd 'e}
;; ;;          #{'f 'g 'h 'i}
;; ;;          #{''a ''c ''f}})
;; ;;    true)

;; ;; (= (__ #{#{'(:x :y :z) '(:x :y) '(:z) '()}
;; ;;          #{#{:x :y :z} #{:x :y} #{:z} #{}}
;; ;;          #{'[:x :y :z] [:x :y] [:z] [] {}}})
;; ;;    false)

;; ;; (= (__ #{#{(= "true") false}
;; ;;          #{:yes :no}
;; ;;          #{(class 1) 0}
;; ;;          #{(symbol "true") 'false}
;; ;;          #{(keyword "yes") ::no}
;; ;;          #{(class '1) (int \0)}})
;; ;;    false)

;; ;; (= (__ #{#{distinct?}
;; ;;          #{#(-> %) #(-> %)}
;; ;;          #{#(-> %) #(-> %) #(-> %)}
;; ;;          #{#(-> %) #(-> %) #(-> %)}})
;; ;;    true)

;; ;; (= (__ #{#{(#(-> *)) + (quote mapcat) #_ nil}
;; ;;          #{'+ '* mapcat (comment mapcat)}
;; ;;          #{(do) set contains? nil?}
;; ;;          #{, , , #_, , empty?}})
;; ;;       false)

;; (def samp1 #{#{\U} #{\s} #{\e \R \E} #{\P \L} #{\.}})

;; (def samp2 #{#{:a :b :c :d :e}
;;              #{:a :b :c :d}
;;              #{:a :b :c}
;;              #{:a :b}
;;              #{:a}})

;; (def samp3 #{#{[1 2 3] [4 5]}
;;              #{[1 2] [3 4 5]}
;;              #{[1] [2] 3 4 5}
;;              #{1 2 [3 4] [5]}})

;; (def samp4 #{#{(#(-> *)) + (quote mapcat) #_ nil}
;;              #{'+ '* mapcat (comment mapcat)}
;;              #{(do) set contains? nil?}
;;              #{, , , #_, , empty?}})

;; (defn in?
;;   [seq elm]
;;   (some #(= elm %) seq))

;; (def f
;;   (fn [ss]
;;     (empty?
;;      (for [ss0 ss
;;            ss1 ss :when (not (= ss0 ss1))
;;            el ss0 :when (some #(= el %) ss1)]
;;        el))))

;; (expect (f samp1) true)
;; (expect (f samp2) false)
;; (expect (f samp3) true)
;; (expect (f samp4) false)




;; ;; Power set

;; "
;; Difficulty:Medium
;; Topics:set-theory


;; Write a function which generates the power set of a given set. The
;; power set of a set x is the set of all subsets of x, including the
;; empty set and x itself.

;; (= (__ #{1 :a}) #{#{1 :a} #{:a} #{} #{1}})

;; (= (__ #{}) #{#{}})

;; (= (__ #{1 2 3})
;;    #{#{} #{1} #{2} #{3} #{1 2} #{1 3} #{2 3} #{1 2 3}})

;; (= (count (__ (into #{} (range 10)))) 1024)

;; "

;; (defn pwrset [S]
;;   (let [pwr2 (fn [n] (apply * (repeat n 2)))
;;         has-bit-set (fn [num digit]
;;                       (> (bit-and num (pwr2 digit)) 0))
;;         nitems (count S)
;;         itemlist (apply list S)
;;         num_combinations (pwr2 nitems)]
;;     (set
;;      (for [c (range num_combinations)]
;;        (set (for [i (range nitems) :when (has-bit-set c i)]
;;               (nth itemlist i)))))))

;; (println (combi #{:a :b :c}))


;; (defn pwr2 [n] (apply * (repeat n 2)))

;; (defn has-bit-set [num digit]
;;   (> (bit-and num (pwr2 digit)) 0))

;; (expect (pwr2 2) 4)
;; (expect (pwr2 10) 1024)
;; (expect (has-bit-set 0 0) false)
;; (expect (has-bit-set 1 0) true)
;; (expect (map (partial has-bit-set 0xFF) (range 8)) (repeat 8 true))

;; (defn works [S]  ;; But requires S of length 3!!!
;;   (set
;;    (for [tf1 [true false], e1 S :when tf1,
;;          tf2 [true false], e2 S :when tf2,
;;          tf3 [true false], e3 S :when tf3]
;;      (set [e1 e2 e3]))))


       
;; ;; Equivalence relations

;; "
;; (= (__ #(* % %) #{-2 -1 0 1 2})
;;    #{#{0} #{1 -1} #{2 -2}})

;; (= (__ #(rem % 3) #{0 1 2 3 4 5 })
;;    #{#{0 3} #{1 4} #{2 5}})

;; (= (__ identity #{0 1 2 3 4})
;;    #{#{0} #{1} #{2} #{3} #{4}})

;; (= (__ (constantly true) #{0 1 2 3 4})
;;    #{#{0 1 2 3 4}})
;; "

;; (defn equiv
;;   [f D]
;;   (set
;;    (for [a D]
;;      (set
;;       (for [b D :when (= (f a) (f b))]
;;         b)))))

;; (expect (equiv #(* % %) #{-2 -1 0 1 2})
;;         #{#{0} #{1 -1} #{2 -2}})

;; (expect (equiv #(rem % 3) #{0 1 2 3 4 5 })
;;         #{#{0 3} #{1 4} #{2 5}})

;; (expect (equiv identity #{0 1 2 3 4})
;;         #{#{0} #{1} #{2} #{3} #{4}})

;; (expect (equiv (constantly true) #{0 1 2 3 4})
;;         #{#{0 1 2 3 4}})


;; ;; Euler's totient

;; (def totient-f
;;   (fn totient-f [x]
;;     (let [gcd (fn [a b]
;;                 (let [aa (max a b)
;;                       bb (min a b)]
;;                   (if (zero? b)
;;                     a
;;                     (recur b (mod a b))))),
;;           coprime (fn [a b]
;;                     (= (gcd a b) 1))]
;;       (if (= x 1)
;;         1
;;         (let [r (range 1 x)
;;               coprimes (filter #(coprime % x) r)]
;;           (count coprimes))))))

;; (expect (totient-f 1) 1)
;; (expect (totient-f 10) 4)
;; (expect (totient-f 40) 16)
;; (expect (totient-f 99) 60)


;; ;; intoCamelCase

;; ((fn [s]
;;    (let [words (re-seq #"[^-]+" s)]
;;        (apply str (cons (first words)
;;                         (for [w (rest words)]
;;                           (str (.toUpperCase (str (first w))) (apply str (rest w))))))))
;;      "multi-word-key")


;; ;; Merge with a function

;; ((fn [fun & maps]
;;       (let [pairs (apply concat
;;                          (for [mm maps]
;;                            (for [[k, v] mm] [k v])))]
;;         (loop [p pairs, ret {}]
;;           (if (seq p)
;;             (let [[k, v] (first p),
;;                   lookup (ret k)
;;                   insert (if lookup (fun lookup v) v)]
;;               (recur (rest p)
;;                      (conj ret (assoc {} k insert))))
;;             ret))))
;;      - {1 10, 2 20} {1 3, 2 10, 3 15})


;; ;; Sequence reductions

;; ((fn redu
;;    ([f i s]
;;      (if (seq s)
;;        (lazy-seq
;;          (cons i
;;                (redu f (f i (first s)) (rest s))))
;;        [i]))
;;    ([f s]
;;      (redu f (first s) (rest s))))
;;   conj [1] [2 3 4])



;; ;; Black Box Testing

;; (map (fn [s]
;;    (let [r (conj s [4 5] [5 4] [5 5]),
;;          a (- (count r) (count s)),
;;          b (first r),
;;          c (last r)]
;;      (cond (= a 2) :map,
;;            (and (= a 3) (= b [5 4])) :set,
;;            (and (= a 3) (= c [5 5])) :vector,
;;            (and (= a 3) (= b [5 5])) :list,
;;            :true :something-else))) [{:a 1, :b 2} #{:a 1 :b 2} [:a 1 :b 2] '(:a 1 :b 2) [1 2 3 4 5 6]])



;; ;  Filter Perfect Squares
;; ;
;; ;  Difficulty:	Medium
;; ;  Topics:
;; ;
;; ;
;; ;  Given a string of comma separated integers, write a function which
;; ;  returns a new comma separated string that only contains the numbers
;; ;  which are perfect squares.
;; ;
;; ;  (= (__ "4,5,6,7,8,9") "4,9")
;; ;
;; ;  (= (__ "15,16,25,36,37") "16,25,36")

;; (fn [S]
;;   (apply str (interpose ","
;;                         (for [[x, y] (map (fn [s]
;;                                             (let [i (Integer/parseInt s),
;;                                                   rx (Math/sqrt i),
;;                                                   ri (int rx)]
;;                                               [i (= (* ri ri) i)])) (re-seq #"\d+" S)) :when y] x))))




;; ; Answer from Repl (obvious upon inspection):
;; [1 3 5 7 9 11]
