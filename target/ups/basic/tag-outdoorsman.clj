{:type :basic
 :name (str "Тэгнуть " (translation :outdoorsman))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:outdoorsman (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
