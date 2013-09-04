{:type :basic
 :name (str "Тэгнуть " (translation :big-guns))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:big-guns (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
