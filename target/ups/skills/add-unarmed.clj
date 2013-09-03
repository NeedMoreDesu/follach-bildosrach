(let [cost
      (fn [skill]
       (cond
        (<= 0 skill 100) 1
        (<= 101 skill 125) 2
        (<= 126 skill 150) 3
        (<= 151 skill 175) 4
        (<= 176 skill 200) 5
        (<= 201 skill 300) 6))]
 {:type :basic
  :name "+unarmed"
  :requirenments
  {:skills {:unarmed [0 299]}
   :fn
   (fn [char]
    (if (<
         (-> char :skills :free)
         (cost (-> char :skills :unarmed)))
     (str
      "Need "
      (cost (-> char :skills :unarmed))
      " skillpoints, got "
      (-> char :skills :free))))}
  :change
  {:skills
   (array-map
    :unarmed
    (fn [char val]
     (min
      (+ val
       (if (some #{"tag unarmed"} (:build char))
        2
        1))
      300))
    :free
    (fn [char val]
     (- val (cost (-> char :skills :unarmed)))))}})
