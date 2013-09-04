{:type :basic
 :name (str "Тэгнуть " (translation :first-aid))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:first-aid (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
