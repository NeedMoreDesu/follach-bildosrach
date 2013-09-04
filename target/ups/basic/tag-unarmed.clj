{:type :basic
 :name (str "Тэгнуть " (translation :unarmed))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:unarmed (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
