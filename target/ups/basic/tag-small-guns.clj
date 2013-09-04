{:type :basic
 :name (str "Тэгнуть " (translation :small-guns))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:small-guns (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
