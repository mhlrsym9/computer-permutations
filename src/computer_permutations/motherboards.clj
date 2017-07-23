(ns computer-permutations.motherboards)

(def asrock-z270m-extreme4-mb {:name "ASRock Z270M Extreme4" :size "mATX" :socket 1151 :optane? true :thunderbolt-on-board? false :only-thunderbolt-card "ASRock Thunderbolt3" :lost-cost 138.99})
(def asrock-z270m-extreme4-mb-name (:name asrock-z270m-extreme4-mb))

(def asus-z170-ws-mb {:name "Asus Z170 WS" :size "ATX" :socket 1151 :optane? false :thunderbolt-on-board? false :only-thunderbolt-card "Asus Thunderbolt3" :lost-cost 266.87})
(def asus-z170-ws-mb-name (:name asus-z170-ws-mb))

(def gigabyte-ga-ex58-ud4p {:name "Gigabyte GA-EX58-UD4P" :socket 1366 :optane? false :thunderbolt-on-board? false :only-cpu "i7-920" :lost-cost 0})
(def gigabyte-ga-ex58-ud4p-name (:name gigabyte-ga-ex58-ud4p))

(def core-2-duo-mb {:name "Core 2 Duo MB" :size "ATX" :socket 775 :optane? false :thunderbolt-on-board? false :only-cpu "Core 2 Duo E8400" :lost-cost 0})
(def core-2-duo-mb-name (:name core-2-duo-mb))

(def core-2-quad-mb {:name "Core 2 Quad MB" :size "ATX" :socket 775 :optane? false :thunderbolt-on-board? false :only-cpu "Intel Core 2 Quad Q9500" :lost-cost 0})
(def core-2-quad-mb-name (:name core-2-quad-mb))

(def gigabyte-ga-x99p-sli-mb {:name "Gigabyte GA-X99P-SLI" :socket 2011 :optane? false :thunderbolt-on-board? true :only-cpu "E5-2609v3" :lost-cost 244.36})
(def gigabyte-ga-x99p-sli-mb-name (:name gigabyte-ga-x99p-sli-mb))

(def asus-z270-ws-mb {:name "Asus Z270 WS" :size "ATX" :socket 1151 :optane? true :thunderbolt-on-board? false :only-cpu "i7-7700K" :only-thunderbolt-card "Asus Thunderbolt3" :additional-cost 379.99})
(def asus-z270-ws-mb-name (:name asus-z270-ws-mb))

(def asrock-z270-supercarrier-mb {:name "ASRock Z270 Supercarrier" :size "ATX" :socket 1151 :optane? true :thunderbolt-on-board? true :only-cpu "i7-7700K" :additional-cost 349.99})
(def asrock-z270-supercarrier-mb-name (:name asrock-z270-supercarrier-mb))

(def gigabyte-ga-z170x-gaming7 {:name "Gigabyte GA-Z170X-Gaming 7" :size "ATX" :socket 1151 :optane? false :thunderbolt-on-board? true :additional-cost (- (* 119.99 1.0625) 20.) :from "MicroCenter"})

(def asrock-h270-pro4 {:name "ASRock H270 Pro4" :size "ATX" :socket 1151 :optane? true :thunderbolt-on-board? false :only-thunderbolt-card "ASRock Thunderbolt3" :additional-cost 89.99})

(def asrock-b150m-pro4 {:name "ASRock B150M Pro4" :size "mATX" :socket 1151 :optane? false :thunderbolt-on-board? false :additional-cost (- 78.99 15)})

(def asrock-z270-itx {:name "ASRock Z270 ITX/AC" :size "mITX" :socket 1151 :optane? true :thunderbolt-on-board? true :additional-cost 138.99})

(def asrock-z270m-extreme4-mb-2 {:name "ASRock Z270M Extreme4 2" :size "mATX" :socket 1151 :optane? true :thunderbolt-on-board? false :only-thunderbolt-card "ASRock Thunderbolt3" :additional-cost 197.95})
(def asrock-z270m-extreme4-mb-2-name (:name asrock-z270m-extreme4-mb-2))

(def gigabyte-ga-z270-ud5 {:name "Gigabyte GA-Z270-UD5" :size "ATX" :socket 1151 :optane? true :thunderbolt-on-board? true :additional-cost (* 199.99 1.0625)})

(def motherboards
  (list asrock-z270m-extreme4-mb
        asus-z170-ws-mb
        gigabyte-ga-ex58-ud4p
        core-2-duo-mb
        core-2-quad-mb
        gigabyte-ga-x99p-sli-mb

        asus-z270-ws-mb
        asrock-z270-supercarrier-mb

        gigabyte-ga-z170x-gaming7
        asrock-h270-pro4
        asrock-b150m-pro4

        asrock-z270-itx
        asrock-z270m-extreme4-mb-2
        gigabyte-ga-z270-ud5
        ))

