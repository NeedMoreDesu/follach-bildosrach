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
  :name "+energy-weapons"
  :requirenments
  {:skills {:energy-weapons [0 299]}
   :fn
   (fn [char]
    (if (<
         (-> char :skills :free)
         (cost (-> char :skills :energy-weapons)))
     (str
      "Need "
      (cost (-> char :skills :energy-weapons))
      " skillpoints, got "
      (-> char :skills :free))))}
  :change
  {:skills
   (array-map
    :energy-weapons
    (fn [char val]
     (min
      (+ val
       (if (some #{"tag energy-weapons"} (:build char))
        2
        1))
      300))
    :free
    (fn [char val]
     (- val (cost (-> char :skills :energy-weapons)))))}})
