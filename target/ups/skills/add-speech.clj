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
  :name (str "+" (translation :speech))
  :requirenments
  {:skills {:speech [0 299]}
   :fn
   (fn [char]
    (if (<
         (-> char :skills :free)
         (cost (-> char :skills :speech)))
     (str
      "Нужно "
      (cost (-> char :skills :speech))
      " скиллпоинтов, есть "
      (-> char :skills :free))))}
  :change
  (fn [char]
   (let [char (update-in char [:skills :speech]
               (fn [val]
                (min
                 (+ val
                  (if (-> char :skills :tags :speech)
                   2
                   1))
                 300)))
         char (update-in char [:skills :free]
               (fn [val]
                (- val (cost (-> char :skills :speech)))))]
    char))})
