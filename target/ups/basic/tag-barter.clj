{:type :basic
 :name (str "Тэгнуть " (translation :barter))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:barter (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
