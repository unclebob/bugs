(ns world
  (:require [bug]
            [quil.core :as q]
            [repulsor]))

(defn make-state []
  {:bugs (bug/make-bugs)
   :repulsors (repulsor/make-repulsors)})

(defn update-state [ms world]
  (->> world
       (bug/update-bugs ms)
       (repulsor/update-repulsors ms)
       (bug/maybe-add)))

(defn draw-state [world]
  [0 500]
  (q/fill 0 255 0)
  (q/rect 0 0 5 5)
  (let [{:keys [bugs repulsors]} world]
    (bug/draw bugs)
    (repulsor/draw repulsors)))

(defn mouse-clicked [[x y] world]
  (repulsor/add-repulsor x y world))
