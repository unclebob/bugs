(ns repulsor
  (:require [quil.core :as q]
            [vector]))


(def force-constant 0.00005)

(defn setup [cfg]
  cfg)

(defn make-repulsor [position]
  {:position position :life 1000})

(defn make-repulsors []
  [])

(defn add-repulsor [x y world]
  (update world :repulsors conj (make-repulsor [x y])))

(defn draw-repulsor [repulsor]
  (let [[x y] (:position repulsor)]
    (q/ellipse x y 8 8)))

(defn draw [{:keys [repulsors] :as world}]
  (q/fill 255 0 0)
  (doseq [repulsor repulsors] (draw-repulsor repulsor))
  world)

(defn update-repulsor [ms repulsor]
  (let [repulsor (update repulsor :life - ms)]
    (if (neg? (:life repulsor)) nil repulsor)))

(defn update-state [ms world]
  (let [repulsors (:repulsors world)]
    (assoc world :repulsors
                 (remove nil?
                         (map #(update-repulsor ms %)
                              repulsors)))))


(defn force-on [ms bug repulsor]
  (let [bug-pos (:position bug)
        repulsor-pos (:position repulsor)
        [dx dy] (vector/subtract bug-pos repulsor-pos)
        dist-squared (+ (* dx dx) (* dy dy))]
    (vector/scale [dx dy] (* force-constant (/ ms dist-squared)))))

(defn total-force [ms {:keys [repulsors]} bug]
  (let [forces (map #(force-on ms bug %) repulsors)
        total-force (reduce vector/add [0 0] forces)]
    total-force))