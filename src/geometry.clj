(ns geometry)

(defn square [x] (* x x))

(defn distance [[x1 y1] [x2 y2]]
  (Math/sqrt (+ (square (- x1 x2)) (square (- y1 y2)))))
