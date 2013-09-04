{:type :basic
 :name (str "+" (translation :in))
 :requirenments
 {:stats {:in [1 9]
          :free 1}}
 :change
 {:stats {:in +1
          :free -1}
  :skills {:science +4
           :repair +3
           :outdoorsman +2
           :first-aid +2
           :doctor +1}
  :info {:skillpoints-per-level +2}}}
