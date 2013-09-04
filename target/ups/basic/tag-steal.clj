{:type :basic
 :name (str "Тэгнуть " (translation :steal))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:steal (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
