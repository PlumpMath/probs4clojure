(ns probs4clojure.test-util
  (:require [speclj.core :refer :all]
            [clojure.walk :refer [postwalk-replace]]))


(defmacro solves
  "
  The `solves` macro presents and tests a solution to a
  [4clojure.com](http://4clojure.com) problem by taking the first
  expression, substituting it for __ in all subsequent expressions,
  and evaluating each resulting expression for truthiness (i.e.,
  evaluating the resulting Speclj assertions).
  "
  [expr & tests]
  (let [replacef# (fn [t] (postwalk-replace {'__ expr} t))
        newtests# (map replacef# tests)]
    `(describe ~(str expr)
       ~@(for [t# newtests#
               :let [should# `(should ~t#)
                     shouldstr# (str t#)]]
           `(it ~(str "should " shouldstr#)
              ~should#)))))
