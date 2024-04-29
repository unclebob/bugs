(ns bug
  (:require [quil.core :as q]
            [repulsor]))

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
        vx (if (or (neg? x) (> x 1000)) (- vx) vx)
        vy (if (or (< y -500) (> y 500)) (- vy) vy)]
    (assoc bug :velocity [vx vy])))

(defn update-bug [ms world bug]
  (let [{:keys [velocity position]} bug
        [x y] position
        repulsor-force (repulsor/total-force ms world bug)
        [vx vy] (vector/add velocity repulsor-force)
        dx (* ms vx)
        dy (* ms vy)
        bug (assoc bug :position [(+ x dx) (+ y dy)]
                       :velocity [vx vy])
        bug (reflect bug)]
    bug))

(defn update-bugs [ms {:keys [bugs] :as world}]
  (assoc world :bugs (map #(update-bug ms world %) bugs)))

(defn draw-bug [bug]
  (let [{:keys [position]} bug
        [x y] position]
    (q/ellipse x y 5 5)))

(defn draw [bugs]
  (q/fill 0 0 0)
  (doseq [bug bugs] (draw-bug bug)))

(defn maybe-add [world]
  (if (< 0.98 (rand))
    (update world :bugs conj (make))
    world))
