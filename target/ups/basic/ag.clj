{:type :basic
 :name (str "+" (translation :ag))
 :requirenments
 {:stats {:ag [1 9]
          :free 1}}
 :change
 {:stats {:ag +1
          :free -1}
  :skills {:small-guns +4
           :big-guns +2
           :energy-weapons +2
           :unarmed +2
           :melee-weapons +2
           :throwing +4
           :sneak +3
           :lockpicks +1
           :steal +3
           :traps +1}
  :info {:armor-class +1
         :action-points
         (fn [char arg]
          (+ arg
           (if (= 0 (rem (-> char :stats :ag) 2))
            1
            0)))}}}
