{:type :basic
 :name "+ УРОВЕНЬ"
 :requirenments
 {:stats {:free [0 0]}
  :info {:free-skill-tags [0 0]}}
 :change
 (fn [char]
  (let [char (update-in char [:info :level]
              #(+ % 1))
        char (update-in char [:textuals]
              (fn [arg]
               (if
                (and
                 (-> char :info :perk)
                 (= 0
                  (rem
                   (-> char :info :level)
                   (-> char :info :perk-every))))
                (conj arg "Перк удачно прослоупочен")
                arg)))
        char (update-in char [:skills :free]
              (fn [arg]
               (+ arg
                (if (<= (-> char :info :level) 28)
                 (-> char :info :skillpoints-per-level)
                 0))))
        char (update-in char [:info :hitpoints]
              (fn [arg]
               (+ arg
                (if (<= (-> char :info :level) 28)
                 (-> char :info :hitpoints-per-level)
                 0))))
        char (update-in char [:info :perk]
              (fn [arg]
               (or arg
                (if
                 (= 0
                  (rem
                   (-> char :info :level)
                   (-> char :info :perk-every)))
                 (if (<= (-> char :info :level) 28)
                  :hardcap
                  :softcap)
                 false))))]
   char))}
