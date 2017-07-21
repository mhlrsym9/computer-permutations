(ns computer-permutations.thunderbolt-cards)

(def asrock-thunderbolt3-card {:name "ASRock Thunderbolt3" :additional-cost 79.99})
(def asrock-thunderbolt3-card-name (:name asrock-thunderbolt3-card))

(def asus-thunderbolt-card {:name "Asus Thunderbolt3" :lost-cost 59.49})
(def asus-thunderbolt-card-name (:name asus-thunderbolt-card))

(def no-thunderbolt-card {:name "No Thunderbolt card" :additional-cost 0 :lost-cost 0})
(def no-thunderbolt-card-name (:name no-thunderbolt-card))

(def thunderbolt-cards (list asrock-thunderbolt3-card asus-thunderbolt-card no-thunderbolt-card))


