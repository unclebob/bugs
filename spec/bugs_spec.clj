(ns bugs-spec
  (:require [bugs :refer :all]
            [repulsor]
            [speclj.core :refer :all]))

(describe "repulsor"
  (it "repels bugs"
    (should= [-0.005 -0.005] (repulsor/force-on
                   1
                   {:position [0 0]}
                   {:position [1 1]}))
    (should= [0.005 0.005] (repulsor/force-on
                       1
                       {:position [1 1]}
                       {:position [0 0]}))
    )

  (it "totals many repulsors"
    (should= [-0.01 0.0] (repulsor/total-force
                   1 {:repulsors [{:position [1 1]}
                                  {:position [1 -1]}]}
                      {:position [0 0]})))
  )

