(ns computer-permutations.optane-cards)

(def optane-card-32GB {:name "32 GB optane" :optane? true :lost-cost (* 69.99 1.0625)})
(def optane-card-32GB-name (:name optane-card-32GB))

(def no-optane-card {:name "no optane" :optane? false :additional-cost 0 :lost-cost 0})
(def no-optane-card-name (:name no-optane-card))

(def optane-cards (list optane-card-32GB no-optane-card))


