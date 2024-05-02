(ns bugs-spec
  (:require [bugs :refer :all]
            [dynne.sampled-sound :as sound]
            [repulsor]
            [speclj.core :refer :all]))

(describe "repulsor"
  (it "repels bugs"
    (with-redefs [repulsor/force-constant 1]
      (should= [-1/2 -1/2] (repulsor/force-on
                             1
                             {:position [0 0]}
                             {:position [1 1]}))
      (should= [1/2 1/2] (repulsor/force-on
                           1
                           {:position [1 1]}
                           {:position [0 0]}))
      ))

  (it "totals many repulsors"
    (with-redefs [repulsor/force-constant 1]
      (should= [-1 0] (repulsor/total-force
                        1 {:repulsors [{:position [1 1]}
                                       {:position [1 -1]}]}
                        {:position [0 0]}))))
  )

(describe "bugs"
  (with-stubs)
  (it "reflects off walls"
    (with-redefs [sound/play (stub :play)]
      (reset! bug/config {:right-wall 10
                          :left-wall 0
                          :top-wall -10
                          :bottom-wall 10})
      (should= {:position [11 0], :velocity [1 1]} ;not reflected
               (bug/reflect {:position [11 0] :velocity [1 1]}))
      (should= {:position [10 0], :velocity [1 1]}
               (bug/reflect {:position [10 0] :velocity [1 1]}))
      (should= {:position [0 0], :velocity [-1 1]}
               (bug/reflect {:position [-1 0] :velocity [1 1]}))
      (should= {:position [0 0], :velocity [1 1]}
               (bug/reflect {:position [0 0] :velocity [1 1]}))
      (should= {:position [0 -10], :velocity [1 -1]}
               (bug/reflect {:position [0 -11] :velocity [1 1]}))
      (should= {:position [0 -10], :velocity [1 1]}
               (bug/reflect {:position [0 -10] :velocity [1 1]}))
      (should= {:position [0 10], :velocity [1 -1]}
               (bug/reflect {:position [0 11] :velocity [1 1]}))
      (should= {:position [0 10], :velocity [1 1]}
                     (bug/reflect {:position [0 10] :velocity [1 1]}))
      ))
  )

