(ns bugs
  (:require [bug]
            [quil.core :as q]
            [quil.middleware :as m]
            [world]))

(def screen-size [1000 1000])
(def screen-origin [0 500])
(def frame-rate 30)
(def frame-ms (/ 1000 frame-rate))
(def max-frame-ms (* 2 frame-ms))

(defn setup []
  (q/frame-rate frame-rate)
  (q/color-mode :rgb)
  (assoc (world/make-state)
    ::last-update-time (System/currentTimeMillis)
    ::mouse-state :mouse-up))

(defn mouse-clicked [world]
  (let [[ox oy] screen-origin
        mx (- (q/mouse-x) ox)
        my (- (q/mouse-y) oy)]
    (world/mouse-clicked [mx my] world)))

(defn get-ms-since-last-update [world]
  (let [time (System/currentTimeMillis)
        ms (- time (::last-update-time world))
        ms (min max-frame-ms ms)
        world (assoc world ::last-update-time time)]
    [ms world]))

(defn check-for-mouse-click [world]
  (let [mouse-current-state (if (q/mouse-pressed?)
                              :mouse-down :mouse-up)
        mouse-old-state (::mouse-state world)
        world (assoc world ::mouse-state mouse-current-state)
        mouse-changed? (not= mouse-current-state mouse-old-state)
        mouse-down? (= mouse-current-state :mouse-down)
        mouse-clicked? (and mouse-changed? mouse-down?)]
    (if mouse-clicked?
      (mouse-clicked world)
      world)))

(defn update-state [world]
  (let [[ms world] (get-ms-since-last-update world)]
    (->> world check-for-mouse-click (world/update-state ms))))

(defn draw-state [world]
  (q/background 240)
  (q/with-translation
    screen-origin
    (world/draw-state world)))

(q/defsketch bugs
             :title "Bugs!"
             :size screen-size
             :setup setup
             :update update-state
             :draw draw-state
             :features [:keep-on-top]
             :middleware [m/fun-mode])

(defn -main [& args]
  (println "bugs has begun."))
