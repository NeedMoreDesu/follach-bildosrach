{:type :basic
 :name (str "Тэгнуть " (translation :sneak))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:sneak (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
