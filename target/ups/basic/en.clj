{:type :basic
 :name (str "+" (translation :en))
 :requirenments
 {:stats {:en [1 9]
          :free 1}}
 :change
 {:stats {:en +1
          :free -1}
  :skills {:outdoorsman +2}
  :info {:hitpoints +2
         :hitpoints-per-level
         (fn [char arg]
          (+ arg
           (if (= 0 (rem (-> char :stats :en) 2))
            1
            0)))
         :poison-resistance +5
         :radiation-resistance +2
         :healing-rate
         (fn [char arg]
          (+ arg
           (if (#{6 9} (-> char :stats :en))
            1
            0)))}}}
