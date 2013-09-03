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
  :name "+outdoorsman"
  :requirenments
  {:skills {:outdoorsman [0 299]}
   :fn
   (fn [char]
    (if (<
         (-> char :skills :free)
         (cost (-> char :skills :outdoorsman)))
     (str
      "Need "
      (cost (-> char :skills :outdoorsman))
      " skillpoints, got "
      (-> char :skills :free))))}
  :change
  {:skills
   (array-map
    :outdoorsman
    (fn [char val]
     (min
      (+ val
       (if (some #{"tag outdoorsman"} (:build char))
        2
        1))
      300))
    :free
    (fn [char val]
     (- val (cost (-> char :skills :outdoorsman)))))}})
