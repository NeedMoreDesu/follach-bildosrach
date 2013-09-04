{:type :basic
 :name (str "Тэгнуть " (translation :throwing))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:throwing (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
