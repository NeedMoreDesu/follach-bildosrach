{:type :basic
 :name (str "+" (translation :ch))
 :requirenments
 {:stats {:ch [1 9]
          :free 1}}
 :change
 {:stats {:ch +1
          :free -1}
  :skills {:barter +4
           :speech +5}}}
