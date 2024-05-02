(ns bug
  (:require [dynne.sampled-sound :as sound]
            [quil.core :as q]
            [repulsor]))

(def spring (sound/read-sound "onespring.mp3"))
(def alert (sound/read-sound "alert.mp3"))

(def config (atom nil))

(defn setup [cfg]
  (reset! config cfg)
  cfg)

(defn make []
  (let [vx (+ 0.02 (* (rand) 0.02))
        vy (- (* (rand) 0.004) 0.002)]
    {:velocity [vx vy] :position [0.0 0.0]}))

(defn make-bugs []
  (repeatedly 20 make))

(defn reflect [bug]
  (let [{:keys [position velocity]} bug
        [x y] position
        [vx vy] velocity
        {:keys [left-wall top-wall bottom-wall]} @config
        [nx nvx ny nvy] (cond
                          (< x left-wall)
                          [left-wall (- vx) y vy]

                          (> y bottom-wall)
                          [x vx bottom-wall (- vy)]

                          (< y top-wall)
                          [x vx top-wall (- vy)]

                          :else
                          [x vx y vy])]
    (when (or (not= vx nvx) (not= vy nvy))
      (sound/play spring 64000))
    (assoc bug :velocity [nvx nvy]
               :position [nx ny])))

(defn escape [bug]
  (let [right-wall (:right-wall @config)
        [x _y] (:position bug)
        bug (if (> x right-wall) nil bug)]
    (when (nil? bug)
      (sound/play alert 64000))
    bug))

(defn update-bug [ms world bug]
  (let [{:keys [velocity position]} bug
        [x y] position
        repulsor-force (repulsor/total-force ms world bug)
        [vx vy] (vector/add velocity repulsor-force)
        dx (* ms vx)
        dy (* ms vy)
        bug (assoc bug :position [(+ x dx) (+ y dy)]
                       :velocity [vx vy])
        bug (reflect bug)
        bug (escape bug)]
    bug))

(defn maybe-add [world]
  (if (< 0.98 (rand))
    (update world :bugs conj (make))
    world))

(defn update-state [ms {:keys [bugs] :as world}]
  (let [updated-bugs (map #(update-bug ms world %) bugs)
        updated-bugs (remove nil? updated-bugs)
        world (assoc world :bugs updated-bugs)]
    (maybe-add world)))

(defn draw-bug [bug]
  (let [{:keys [position]} bug
        [x y] position]
    (q/ellipse x y 5 5)))

(defn draw [{:keys [bugs] :as world}]
  (q/fill 0 0 0)
  (doseq [bug bugs] (draw-bug bug))
  world)


