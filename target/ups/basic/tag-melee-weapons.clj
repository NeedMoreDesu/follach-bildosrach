{:type :basic
 :name (str "Тэгнуть " (translation :melee-weapons))
 :requirenments
 {:max 1
  :info {:free-skill-tags 1}}
 :change
 {:skills {:tags {:melee-weapons (fn [char arg] true)}}
  :info {:free-skill-tags -1}}}
