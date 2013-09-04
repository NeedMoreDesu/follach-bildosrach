{:type :basic
 :name (str "Тэгнуть " (translation :lockpicks))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:lockpicks (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
