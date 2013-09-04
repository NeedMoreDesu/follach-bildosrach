{:type :basic
 :name (str "Тэгнуть " (translation :traps))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:traps (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
