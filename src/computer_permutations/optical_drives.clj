(ns computer-permutations.optical-drives)

(def no-optical-drive {:name "no optical drive" :additional-cost 0 :lost-cost 0})
(def no-optical-drive-name (:name no-optical-drive))

(def short-blu-ray {:name "short blu-ray" :additional-cost 42.99})
(def short-blu-ray-name (:name short-blu-ray))

(def long-blu-ray {:name "long blu-ray" :lost-cost 0})
(def long-blu-ray-name (:name long-blu-ray))

(def long-blu-ray-2 {:name "long blu-ray 2" :lost-cost 0})
(def long-blu-ray-2-name (:name long-blu-ray))

(def long-blu-ray-3 {:name "long blu-ray 3" :lost-cost 0})
(def long-blu-ray-3-name (:name long-blu-ray))

(def optical-drives (list long-blu-ray long-blu-ray-2 long-blu-ray-3 short-blu-ray no-optical-drive))
(def long-blu-ray-drives (list long-blu-ray long-blu-ray-2 long-blu-ray-3))
(def long-blu-ray-drive-names (map #(:name %) long-blu-ray-drives))

