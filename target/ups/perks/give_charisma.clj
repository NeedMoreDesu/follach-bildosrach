{:type :perk
 :name "Получить харизму"
 :requirenments
 {:stats {:ch [1 9]}
  :level 12
  :max 1}
 :change
 (fn [char]
  (update-in char
   [:stats :ch]
   inc))}
