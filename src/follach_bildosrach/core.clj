(ns follach-bildosrach.core
 (:gen-class)
 (:import [java.util Calendar])
 (:use [clojure.java.io :only [file]])
 (:use [seesaw core dev border behave graphics font color make-widget cells])
 (:require [seesaw.bind :as bind]))

;;; adding some basic fns
(defn printout-change [obj printout-fn]
 (let [res (with-meta obj
            {:type (keyword (str *ns*) (str printout-fn))})]
  (defmethod print-method (keyword (str *ns*) (str printout-fn)) [o w]
   (print-simple (printout-fn o) w))
  res))
(defn get-time-string []
 (let [c (Calendar/getInstance)
       h (.get c Calendar/HOUR_OF_DAY)
       m (.get c Calendar/MINUTE)
       s (.get c Calendar/SECOND)]
  (format "%02d:%02d:%02d" h m s)))
(defn encode [ve]
 (reduce
  (fn [acc arg]
   (if (= (peek acc) arg)
    (if (number? (get acc (- (count acc) 2)))
     (update-in acc [(- (count acc) 2)] inc)
     (conj (pop acc) 2 arg))
    (conj acc arg)))
   []
   ve))
(defn decode [ve]
 (reduce
  (fn [acc arg]
   (if (number? (peek acc))
    (into (pop acc) (repeat (peek acc) arg))
    (conj acc arg)))
  []
  ve))

;;; file-related
(defn this-jar
 [& [ns]]
 (file
  (java.net.URLDecoder/decode
   (-> (or ns (class *ns*))
    .getProtectionDomain
    .getCodeSource
    .getLocation
    .getPath))))
(defn this-dir []
 (file
  (.getParent
   (this-jar))))
(defn up-load [file]
 (load-string (slurp file)))
(defn ups-load [dir]
 (reduce
  (fn [ups up]
   (assoc ups (:name up) (printout-change up #(str "#<" (:name %) ">"))))
  (sorted-map)
  (pmap
   up-load
   (filter
    (fn [file] (not (.isDirectory file)))
    (file-seq dir)))))
(defn ups []
 (ups-load (file (this-dir) "ups")))


;;; build core
(defn build-create []
 (with-meta
  []
  {:type :build}))
(defn build-add [build up]
 (conj build up))
(defn build-occur [build up]
 (first
  (reduce
   (fn [[count mul] up-name]
    ;; (if
    ;;  (and
    ;;   (integer? up-name)
    ;;   (<= 0 up-name))
    ;;  [count up-name]
    (if
     (= up-name up)
     [(+ count mul) 1]
     [count 1])
    ;; )
    )
   [0 1]
   build)))

(defrecord Stats [st pe en ch in ag lk free])
(defrecord Skills [small-guns big-guns energy-weapons unarmed melee-weapons throwing first-aid doctor sneak lockpicks steal traps science repair speech barter gambling outdoorsman free])
(defrecord Player [stats skills level free-traits free-skill-tags perk perk-every skillpoints-per-level hitpoints-per-level broken build ups textuals])
(defrecord Info [])

(defn check-stat-deficiency [player build-step up]
 (some
  (fn [stat]
   (let [req-min (get (:stats (:requirenments up)) stat)
         player-stat (get (:stats player) stat)]
    (if req-min
     (if (vector? req-min)
      (let [[req-min req-max] req-min]
       (cond
        (> player-stat req-max)
        (str "Player's " (name stat) " is more than " req-max)
        (< player-stat req-min)
        (str "Player's " (name stat) " is less than " req-min)))
      (if (< player-stat req-min)
       (str "Player's " (name stat) " is less than " req-min))))))
  (map keyword (Stats/getBasis))))
(defn check-requirenments [player build-step up]
 (or
  (if (:hardcap (:requirenments up))
   (if (= (:perk (:info player)) :softcap)
    "Hardcap perk after softcap"))
  (if (and
       (:max (:requirenments up))
       (>
        (build-occur
         (subvec (:build player) 0 build-step)
         (:name up))
        (:max (:requirenments up))))
   "Maximum number reached")
  (some
   (fn [stat]
    (let [req-min (get (:stats (:requirenments up)) stat)
          player-stat (get (:stats player) stat)]
     (if req-min
      (if (vector? req-min)
       (let [[req-min req-max] req-min]
        (cond
         (> player-stat req-max)
         (str "Player's " (name stat) " is more than " req-max)
         (< player-stat req-min)
         (str "Player's " (name stat) " is less than " req-min)))
       (if (< player-stat req-min)
        (str "Player's " (name stat) " is less than " req-min))))))
   (map keyword (Stats/getBasis)))
  (some
   (fn [stat]
    (let [req-min (get (:skills (:requirenments up)) stat)
          player-stat (get (:skills player) stat)]
     (if req-min
      (if (vector? req-min)
       (let [[req-min req-max] req-min]
        (cond
         (> player-stat req-max)
         (str "Player's " (name stat) " is more than " req-max)
         (< player-stat req-min)
         (str "Player's " (name stat) " is less than " req-min)))
       (if (< player-stat req-min)
        (str "Player's " (name stat) " is less than " req-min))))))
   (map keyword (Skills/getBasis)))
  (some
   (fn [stat]
    (let [req-min (get (:info (:requirenments up)) stat)
          player-stat (get (:info player) stat)]
     (if req-min
      (if (vector? req-min)
       (let [[req-min req-max] req-min]
        (cond
         (> player-stat req-max)
         (str "Player's " (name stat) " is more than " req-max)
         (< player-stat req-min)
         (str "Player's " (name stat) " is less than " req-min)))
       (if (< player-stat req-min)
        (str "Player's " (name stat) " is less than " req-min))))))
   (keys (:info player)))
  (if (and (:level (:requirenments up))
       (< (:level (:info player)) (:level (:requirenments up))))
   (str "Low level, need " (:level (:requirenments up))))
  (if (:fn (:requirenments up))
   ((:fn (:requirenments up)) player))
  (if (and
       (not (:perk (:info player)))
       (= (:type up) :perk))
   "No perk points left")))
(letfn [(keyseqs [m seq]
         (if (map? m)
          (reverse
           (reduce
            (fn [acc arg]
             (if (vector? arg)
              (conj acc arg)
              (into acc arg)))
            '()
            (map
             (fn [arg]
              (keyseqs (get m arg) (conj seq arg)))
             (keys m))))
          seq))]
 (defn apply-change [player up]
  (if (map? (:change up))
   (reduce
    (fn [player keyseq]
     (update-in player keyseq
      (fn [val]
       (if (number? (get-in (:change up) keyseq))
        (+ val (get-in (:change up) keyseq))
        ((get-in (:change up) keyseq) player val)))))
    player
    (keyseqs (:change up) []))
   ((:change up) player))))
(defn build-gen
 ([build ups-map]
  (first
   (reduce
    (fn [[player i] up-name]
     [(let [up (get ups-map up-name)]
       (if up
        (if (not (check-requirenments player (inc i) up))
         (apply-change player up)
         (update-in
          (apply-change player up)
          [:broken]
          #(assoc % i (check-requirenments player (inc i) up))))
        (update-in
         player
         [:broken]
         #(assoc % i "404: perk not found"))))
      (inc i)])
    [(with-meta
      (map->Player
       {:stats
        (map->Stats
         {:st 1
          :pe 1
          :en 1
          :ch 1
          :in 1
          :ag 1
          :lk 1
          :free 33})
        :skills
        (map->Skills
         {:small-guns 9
          :big-guns 2
          :energy-weapons 2
          :unarmed 34
          :melee-weapons 24
          :throwing 4
          :first-aid 4
          :doctor 7
          :sneak 8
          :lockpicks 12
          :steal 3
          :traps 12
          :science 4
          :repair 3
          :speech 5
          :barter 4
          :gambling 5
          :outdoorsman 4
          :free 0})
        :info
        (map->Info
         (sorted-map
          :radius-of-visibility 23
          :healing-rate 1
          :level 1
          :hitpoints 18
          :hitpoints-per-level 2
          :skillpoints-per-level 7
          :free-traits 2
          :free-skill-tags 3
          :perk false
          :perk-every 3
          :sequence 2
          :armor-class 1
          :action-points 5
          :max-weight 22
          :melee-damage 1
          :poison-resistance 5
          :radiation-resistance 2
          :critical 1))
        :broken {}
        :build build
        :ups ups-map
        :textuals []})
      {:timestamp (get-time-string)})
     0]
    build))))

;;; graphen
(declare set-player)
(defn string-renderer [f]
 (default-list-cell-renderer (fn [this {:keys [value]}] (.setText this (str (f value))))))
(defn pack-if-lesser! [frame]
 (if (= 0 (.getExtendedState frame))
  (let [size (. frame getSize)
        width (.getWidth size)
        height (.getHeight size)]
   (. frame pack)
   (let [size (. frame getSize)
         new-width (.getWidth size)
         new-height (.getHeight size)
         width (max new-width width)
         height (max new-height height)
         size (java.awt.Dimension. width height)]
    (. frame setSize size)
    frame))))

(extend-type Long
 MakeWidget
 (make-widget* [long]
  (make-widget* (str long))))

(extend-type clojure.lang.Keyword
 MakeWidget
 (make-widget* [keyw]
  (make-widget* (name keyw))))

(extend-type (type (map->Stats {}))
 MakeWidget
 (make-widget* [val]
  (grid-panel
   :border "Stats"
   :columns 2
   :hgap 10
   :items (mapcat
           (fn [arg] [(clojure.string/upper-case (name arg)) (get val (keyword arg))])
           (Stats/getBasis)))))

(extend-type (type (map->Skills {}))
 MakeWidget
 (make-widget* [skills]
  (grid-panel
   :border "Skills"
   :columns 2
   :hgap 10
   :items (mapcat
           (fn [arg] [(name arg) (get skills (keyword arg))])
           (Skills/getBasis)))))

(extend-type (type (map->Info {}))
 MakeWidget
 (make-widget* [info]
  (grid-panel
   :border "Info"
   :columns 2
   :hgap 10
   :items (into
           []
           (mapcat
            (fn [arg] [(name arg) (str (get info (keyword arg)))])
            (keys info))))))

(extend-type (type (map->Player {}))
 MakeWidget
 (make-widget* [player]
  (border-panel
   :north
   (if (seq (:broken player))
    "ABSOLUTELY BROKEN"
    "Looks legit")
   :west
   (border-panel
    :size [250 :by 100]
    :center
    (scrollable
     (listbox
      :selection-mode :multi-interval
      :id :build
      :border "Build"
      :model
      (reverse
       (cons
        [[] []]
        (map-indexed
         (fn [idx arg]
          [(get (:build player) idx)
           idx
           (get (:broken player) idx)])
         (:build player))))
      :renderer
      (string-renderer
       (fn [[build _ broken-text]]
        (if (seq build)
         (str build
          (if broken-text (str " !!! " broken-text)))
         "beginning")))
      :listen
      (let [f (fn [w]
               (if (= \newline (.getKeyChar w))
                (let [v (vec (.toArray (seesaw.core/config (seesaw.core/select (to-root w) [:#build]) :model)))
                      v (pop v)
                      sel (selection (seesaw.core/select (to-root w) [:#build]) {:multi? true})
                      sel-filter (set sel)
                      v (vec (reverse (map first (remove sel-filter v))))]
                 (set-player (build-gen v (:ups player))))))]
       [:key-released f]))))
   :center
   (border-panel
    :west
    (border-panel
     :west
     (scrollable
      (:stats player))
     :center
     (scrollable
      (text
       :multi-line? true
       :wrap-lines? true
       :editable? false
       :text
       (clojure.string/join "\n-----\n"
        (:textuals player))))
     :south
     (border-panel
      :center
      (:skills player)
      :west
      (:info player)))
    :center
    (let [{:keys [available
                  not-available
                  stat-deficient]}
          (reduce
           (fn [{:keys [available
                        not-available
                        stat-deficient]
                 :as res}
                up]
            (let [player
                  (update-in player [:build]
                   (fn [build] (build-add build (:name up))))
                  not-available?
                  (check-requirenments
                   player
                   (count (:build player))
                   up)
                  stat-deficient?
                  (check-stat-deficiency
                   player
                   (count (:build player))
                   up)]
             (cond
              (and stat-deficient? (= :perk (:type up)))
              (assoc res :stat-deficient
               (conj stat-deficient [(:build player) stat-deficient?]))
              not-available?
              (assoc res :not-available
               (conj not-available [(:build player) not-available?]))
              :else
              (assoc res :available
               (conj available (:build player))))))
           {:available []
            :not-available []
            :stat-deficient []}
           (vals (:ups player)))]
     (border-panel
      :center
      (scrollable
       (listbox
        :border "Available"
        :model available
        :renderer
        (string-renderer
         (fn [build]
          (str (last build))))
        :listen
        (let [f (fn [w]
                 (set-player (build-gen (selection w) (:ups player))))]
         [:mouse-clicked f
          :key-pressed f])))
      :south
      (horizontal-panel
       :items
       [(scrollable
         (listbox
          :border "Not available"
          :model not-available
          :renderer
          (string-renderer
           (fn [[build broken-text]]
            (str (last build) " # " broken-text)))
          :listen
          (let [f (fn [w]
                   (set-player (build-gen (first (selection w)) (:ups player))))]
           [:mouse-clicked f
            :key-pressed f])))
        (scrollable
         (listbox
          :border "Stat deficient"
          :model stat-deficient
          :renderer
          (string-renderer
           (fn [[build broken-text]]
            (str (last build) " # " broken-text)))
          :listen
          (let [f (fn [w]
                   (set-player (build-gen (first (selection w)) (:ups player))))]
           [:mouse-clicked f
            :key-pressed f])))])))))))

(def player
 (atom ""))
(def text-area-1
 (text
  :multi-line? true
  :wrap-lines? true
  :listen [:key-typed
           (fn [w]
            (Thread/sleep 1)
            (if (not=
                 (count (remove #{\newline \tab} (text w)))
                 (count (text w)))
             (text! w (apply str (remove #{\newline \tab} (text w))))))
           :key-released
           (fn [w]
            (if (= \newline (.getKeyChar w))
             (if (seq (text w))
              (set-player (build-gen (decode (read-string (text w))) (:ups @player))))))]))
(def text-area-2
 (text
  :multi-line? true
  :wrap-lines? true
  :listen [:key-typed
           (fn [w]
            (Thread/sleep 1)
            (if (not=
                 (count (remove #{\newline \tab} (text w)))
                 (count (text w)))
             (text! w (apply str (remove #{\newline \tab} (text w))))))
           :key-released
           (fn [w]
            (cond
             (= \newline (.getKeyChar w))
             (if (seq (text w))
              (set-player (build-gen (conj (:build @player) (text w)) (:ups @player))))
             (= (.getKeyCode w) 9)
             (if (seq (text w))
              (set-player (build-gen
                           (loop [build (:build @player) counter 10]
                            (if (or
                                 (check-requirenments
                                  (build-gen build (:ups @player))
                                  (count build)
                                  ((:ups @player) (text w)))
                                 (= 0 counter))
                             build
                             (recur (conj build (text w)) (dec counter))))
                           (:ups @player))))))]))
(def main-context
 (vertical-panel
  :items
  [@player]))
(def history-vec
 (atom
  (sorted-set-by
   (fn [arg1 arg2]
    (-
     (if
      (=
       (:timestamp (meta arg1))
       (:timestamp (meta arg2)))
      (if (=
           (count (:build arg1))
           (count (:build arg2)))
       (compare
        (hash arg1)
        (hash arg2))
       (compare
        (count (:build arg1))
        (count (:build arg2))))
      (compare
       (:timestamp (meta arg1))
       (:timestamp (meta arg2)))))))))
(def history
 (listbox
  :border "History"
  :model []
  :renderer
  (string-renderer
   (fn [arg] (str
              (:timestamp (meta arg))
              "L"(count (:build arg)))))
  :listen (let [f (fn [w] (set-player (selection w)))]
           [:mouse-clicked f
            :key-pressed f])))
(def main-frame
 (frame
  :title "Divine strategiest(tm) Follach(R) Buildosrach generator(C)"
  :on-close :dispose
  :content
  (border-panel
   :north
   (border-panel
    :size [75 :by 75]
    :west
    (border-panel
     :size [200 :by 50]
     :center
     (scrollable text-area-2))
    :center
    (scrollable text-area-1))
   :west
   (scrollable
    history)
   :center
   main-context)))
(listen main-frame
 :window-closed
 (fn [w]
  (System/exit 0)))
(bind/bind
 player
 (bind/transform (fn [player] [player]))
 (bind/property main-context :items))
(bind/bind
 history-vec
 (bind/property history :model))
(defn set-player [ch]
 (reset! player ch)
 (swap! history-vec conj ch)
 (text! text-area-1 (str (encode (:build ch))))
 (let [ch
       (update-in ch [:build]
        (fn [build]
         (build-add
          build
          (last (:build ch)))))
       failed?
       (check-requirenments
        ch
        (count (:build ch))
        (get (ups) (last (:build ch))))]
  (if failed?
   (text! text-area-2 "")
   (text! text-area-2 (last (:build ch))))))

(defn -main [& args]
 (set-player (build-gen [] (ups)))
 (->
  main-frame
  pack-if-lesser!
  show!))
