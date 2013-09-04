{:type :basic
 :name (str "+" (translation :st))
 :requirenments
 {:stats {:st [1 9]
          :free 1}}
 :change
 {:stats {:st +1
          :free -1}
  :skills {:unarmed +2
           :melee-weapons +2}
  :info {:melee-damage
         (fn [char arg]
          (+ arg
           (if (<= 7 (-> char :stats :st))
            1
            0)))
         :max-weight
         (fn [char arg]
          (+ arg
           (if (= 0 (rem (-> char :stats :st) 3))
            12
            11)))
         :hitpoints +1}}}
