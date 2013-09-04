{:type :basic
 :name (str "Тэгнуть " (translation :energy-weapons))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:energy-weapons (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
