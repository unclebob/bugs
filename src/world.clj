(ns world
  (:require [bug]
            [dynne.sampled-sound :as sound]
            [quil.core :as q]
            [repulsor]))

(def screen-size [1000 1000])
(def screen-origin [0 500])
(def left-wall 0)
(def right-wall 1000)
(def top-wall -500)
(def bottom-wall 500)

(def shot (sound/trim (sound/read-sound "shot.mp3") 0.4 0.8))

(defn setup [config]
  (let [config (assoc config :left-wall left-wall
                             :right-wall right-wall
                             :top-wall top-wall
                             :bottom-wall bottom-wall)]
    (->> config
         bug/setup
         repulsor/setup)))

(defn make-world []
  {:bugs (bug/make-bugs)
   :repulsors (repulsor/make-repulsors)})

(defn update-state [ms world]
  (->> world
       (bug/update-state ms)
       (repulsor/update-state ms)))

(defn draw-state [world]
  [0 500]
  (q/fill 0 255 0)
  (q/rect 0 0 5 5)
  (->> world bug/draw repulsor/draw))

(defn mouse-clicked [[x y] world]
  (sound/play shot 64000)
  (repulsor/add-repulsor x y world))
