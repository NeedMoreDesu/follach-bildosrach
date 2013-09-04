{:type :basic
 :name (str "Тэгнуть " (translation :science))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:science (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
