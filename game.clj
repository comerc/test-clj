(def game-map
  (hash-map
   'living-room ["you are in the living room of a wizards house - there is a wizard snoring loudly on the couch - "
                 ["west door garden"
                  "upstairs stairway attic"]]
   'garden ["you are in a beautiful garden - there is a well in front of you - "
            ["east door living-room"]]
   'attic ["you are in the attic of the wizards house - there is a giant welding torch in the corner - "
           ["downstairs stairway living-room"]]))

(def object-locations (hash-map
                       'whiskey-bottle 'living-room
                       'bucket 'living-room
                       'chain 'garden
                       'frog 'garden))

(def objects ['whiskey-bottle 'bucket 'chain 'frog])

(def location 'living-room)

(defn describe-location [location game-map]
  (first (location game-map)))

(describe-location 'living-room game-map)

(defn describe-path [path]
  (str "there is a " (second path) " going " (first path) " from here - "))

(describe-path '(west door garden))

(defn describe-paths [location game-map]
  (apply concat (map describe-path (rest (get game-map location)))))

(apply str (describe-paths 'living-room game-map))

(defn is-at? [obj loc obj-loc] (= (obj obj-loc) loc))

(is-at? 'whiskey-bottle 'living-room object-locations)

(defn describe-floor [loc objs obj-loc]
  (apply concat (map (fn [x]
                       (str "you see a " x " on the floor. "))
                     (filter (fn [x]
                               (is-at? x loc obj-loc)) objs))))

(defn spel-print [list] (map (fn [x] (symbol (name x))) list))

(apply str (describe-floor 'living-room objects object-locations))

(defn look []
    (apply str
      (concat
        (describe-location location game-map)
        (describe-paths location game-map)
        (describe-floor location objects object-locations))))

(look)

(repeat 10 :x)

(-> "a b c"
  (.toUpperCase))

(.replace "A" "X"
  (.split " ")
  (first))

(-> "a b c"
    (.toUpperCase)
    (#(->> "X" (.replace % "A"))))

(-> "a" ((fn[v]{:k v})))

(-> "a" (#({:k %})))

(-> "a" (#(identity {:k %})))
