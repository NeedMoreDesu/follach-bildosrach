{:type :basic
 :name (str "Тэгнуть " (translation :doctor))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:doctor (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
