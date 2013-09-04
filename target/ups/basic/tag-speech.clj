{:type :basic
 :name (str "Тэгнуть " (translation :speech))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:speech (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
