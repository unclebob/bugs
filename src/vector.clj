(ns vector)

(defn add [[x1 y1] [x2 y2]]
  [(+ x1 x2) (+ y1 y2)])

(defn subtract [[x1 y1] [x2 y2]]
  [(- x1 x2) (- y1 y2)])

(defn scale [[x1 y1] f]
  [(* x1 f) (* y1 f)])