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
  :name "+big-guns"
  :requirenments
  {:skills {:big-guns [0 299]}
   :fn
   (fn [char]
    (if (<
         (-> char :skills :free)
         (cost (-> char :skills :big-guns)))
     (str
      "Need "
      (cost (-> char :skills :big-guns))
      " skillpoints, got "
      (-> char :skills :free))))}
  :change
  {:skills
   (array-map
    :free
    (fn [char val]
     (- val (cost (-> char :skills :big-guns))))
    :big-guns
    (fn [char val]
     (min
      (+ val
       (if (some #{"tag big-guns"} (:build char))
        2
        1))
      300)))}})
