{:type :basic
 :name (str "Тэгнуть " (translation :gambling))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:gambling (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
