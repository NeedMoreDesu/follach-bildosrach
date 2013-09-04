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
  :name (str "+" (translation :doctor))
  :requirenments
  {:skills {:doctor [0 299]}
   :fn
   (fn [char]
    (if (<
         (-> char :skills :free)
         (cost (-> char :skills :doctor)))
     (str
      "Нужно "
      (cost (-> char :skills :doctor))
      " скиллпоинтов, есть "
      (-> char :skills :free))))}
  :change
  (fn [char]
   (let [char (update-in char [:skills :doctor]
               (fn [val]
                (min
                 (+ val
                  (if (-> char :skills :tags :doctor)
                   2
                   1))
                 300)))
         char (update-in char [:skills :free]
               (fn [val]
                (- val (cost (-> char :skills :doctor)))))]
    char))})
