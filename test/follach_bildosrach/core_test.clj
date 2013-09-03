(ns follach-bildosrach.core-test
 (:use clojure.test
  follach-bildosrach.core)
 (:require [clojure.java.io :refer [file]]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))


(def qq (build-create))
(def up (load-string (slurp (file "d:/end06/follach-bildosrach/target/test-perk.clj"))))
(def ups-map {(:name up) up})
(def qq (build-add qqq (:name up)))
(def qq (assoc-in qqq [:stats :st] 10)
(build-gen qq ups-map)
(build-gen ["asd" "Получить харизму" "Получить харизму"] ups)

(file-seq (this-dir))
(file-seq (file "d:/end06/follach-bildosrach"))
(def ups (ups-load (file "d:/end06/follach-bildosrach/target/ups")))



(printout-change [1 2 3] (fn [arg] (second arg)))
