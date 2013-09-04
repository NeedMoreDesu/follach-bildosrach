{:type :basic
 :name (str "Тэгнуть " (translation :repair))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:repair (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
