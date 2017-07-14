(ns computer-permutations.core
  (:require [clojure.math.combinatorics :as combo])
  (:gen-class))

(def motherboards
  (list {:name "ASRock Z270M Extreme4" :size "mATX" :optane? true :owned? true :thunderbolt-on-board? false :only-thunderbolt-card "ASRock Thunderbolt3" :additional-cost 0}
        {:name "Asus Z170 WS" :size "ATX" :optane? false :owned? true :thunderbolt-on-board? false :only-thunderbolt-card "Asus Thunderbolt3" :additional-cost 0}
        {:name "ASRock Z270 Supercarrier" :size "ATX" :optane? true :owned? false :thunderbolt-on-board? true :only-cpu "i7-7700K" :only-case "Phanteks Pro" :additional-cost 349.99}
        {:name "ASRock Z270 ITX/AC" :size "mITX" :optane? true :owned? false :thunderbolt-on-board? true :additional-cost 138.99}
        {:name "Gigabyte GA-Z170X-Gaming 7" :size "ATX" :optane? false :owned? false :thunderbolt-on-board? true :additional-cost (- (* 119.99 1.0625) 20.) :from "MicroCenter"}
        {:name "Gigabyte GA-Z270-UD5" :size "ATX" :optane? true :owned? false :thunderbolt-on-board? true :additional-cost (* 199.99 1.0625)}
        {:name "ASRock H170 Pro4S" :size "ATX" :optane? false :owned? false :thunderbolt-on-board? false :additional-cost 69.99}
        {:name "Gigabyte GA-H110M-M.2" :size "mATX" :optane? false :owned? false :thunderbolt-on-board? false :additional-cost 46.99}
        {:name "Gigabyte GA-B250-HD3" :size "ATX" :optane? true :owned? false :thunderbolt-on-board? false :additional-cost 59.99}
        ;        {:name "ASRock H270M Pro4" :size "mATX" :optane? true :owned? false :thunderbolt-on-board? false :additional-cost 64.99 :from "NewEgg"}
        {:name "ASRock H270M Pro4" :size "mATX" :optane? true :owned? false :thunderbolt-on-board? false :additional-cost 63.74 :from "MicroCenter"}))

(def cases
  (list {:name "Phanteks Pro" :size "ATX" :additional-cost 0}
        {:name "Cooler Master Silencio 352" :size "mATX" :additional-cost 0}
        {:name "Phanteks P400S" :size "ATX" :additional-cost 0}
        {:name "be quiet PURE BASE 600 - Black" :size "ATX" :additional-cost 89.90}
        ;        {:name "Corsair Carbide Series 300R" :size "ATX" :additional-cost (- (* 72.99 1.0625) 10.) :from "MicroCenter"}
        {:name "Corsair Carbide Series 300R" :size "ATX" :additional-cost 59.99 :from "NewEgg"}
        {:name "Phanteks Enthoo Pro Titanium Green" :size "ATX" :additional-cost 69.99}))

(def cpus
  (list {:name "i7-7700K" :licensed-to "Asus Z170 WS" :additional-cost 0}
        {:name "i5-7500" :licensed-to "ASRock Z270M Extreme4" :additional-cost 0}
        {:name "i5-6500t" :additional-cost 0}))

(def no-thunderbolt-card {:name "No Thunderbolt card" :additional-cost 0})

(def thunderbolt-cards
  (list {:name "ASRock Thunderbolt3" :additional-cost 79.99}
        {:name "Asus Thunderbolt3" :additional-cost 0}
        no-thunderbolt-card))

(def no-optane-card {:name "no optane" :optane? false :additional-cost 0})
(def no-optane-card-name (:name no-optane-card))

(def optane-cards
  (list {:name "32 GB optane" :optane? true :additional-cost 0}
        no-optane-card))

(def optical-drives
  (list {:name "long blu-ray" :additional-cost 0}
        {:name "short blu-ray" :additional-cost 42.99}))

(defn- optane-check? [mb optane-card]
  (let [optane-mb? (:optane? mb)
        optane-card? (:optane? optane-card)]
    (or optane-mb? (not optane-card?))))

(defn- thunderbolt-card-check? [{:keys [only-thunderbolt-card]} {:keys [name]}]
  (or
    (= only-thunderbolt-card name)
    (= (:name no-thunderbolt-card) name)))

(defn- size-check? [mb case]
  (let [mb-size (:size mb)
        case-size (:size case)]
    (cond (= case-size "ATX") true
          (= case-size "mATX") (not= mb-size "ATX")
          :else (= mb-size "mITX"))))

(defn- cpu-check? [mb cpu]
  (if-let [only-cpu (:only-cpu mb)]
    (= only-cpu (:name cpu))
    true))

(defn- optical-drive-check? [mb case optical-drive]
  (if (and (= "ASRock Z270M Extreme4" (:name mb))
           (= "Cooler Master Silencio 352" (:name case)))
    (= "short blu-ray" (:name optical-drive))
    (= "long blu-ray" (:name optical-drive))))

(defn- all-different-names? [names]
  (every? true? (map #(apply not= %) (combo/combinations names 2))))

(defn- all-different-mbs? [l]
  (all-different-names? (map #(:name (:mb %)) l)))

(defn- all-different-cases? [l]
  (all-different-names? (map #(:name (:case %)) l)))

(defn- valid-game-pc? [[{:keys [mb case cpu]} _ _]]
  (let [[mb-name case-name cpu-name] (map :name (list mb case cpu))]
    (and (= "i7-7700K" cpu-name)
         (= "Phanteks Pro" case-name)
         (or (= "ASRock Z270 Supercarrier" mb-name)
             (= "Asus Z170 WS" mb-name)))))

(defn- valid-media-pc? [[_ _ {:keys [case optane-card]}]]
  (let [case-name (:name case)
        optane-card-name (:name optane-card)]
    (and (= "Phanteks P400S" case-name)
         (= no-optane-card-name optane-card-name))))

(defn- are-two-mbs-owned? [l]
  (let [owned-l (map (fn [{{:keys [owned?]} :mb}] owned?) l)]
    (= 2 (count (filter true? owned-l)))))

(defn- at-most-one-optane-card? [l]
  (let [optane-l (map (fn [{{:keys [name]} :optane-card}] name) l)]
    (> 2 (count (filter #(= % "32 GB optane") optane-l)))))

(defn- already-licensed? [c]
  (if-let [licensed-to (:licensed-to (:cpu c))]
    (= licensed-to (:name (:mb c)))
    false))

(defn- calculate-the-additional-cost [c]
  (+ (reduce-kv (fn [m _ {:keys [additional-cost]}] (+ m additional-cost)) 0 c)
     (if (already-licensed? c)
       0
       100)))

(defn- calculate-additional-cost [game capture media]
  (let [l (list game capture media)]
    ; I already own one license so subtract that cost.
    (- (apply + (map calculate-the-additional-cost l))
       100)))

(defn- no-thunderbolt-card-in-pc? [{{:keys [name]} :thunderbolt-card}]
  (= (:name no-thunderbolt-card) name))

(defn- is-thunderbolt-pc? [{{:keys [name]}                                        :thunderbolt-card
                            {:keys [only-thunderbolt-card thunderbolt-on-board?]} :mb}]
  (or thunderbolt-on-board? (= name only-thunderbolt-card)))

(defn- is-correct-thunderbolt-configuration? [[game capture media]]
  (and (is-thunderbolt-pc? game)
       (is-thunderbolt-pc? capture)
       (no-thunderbolt-card-in-pc? media)))

(defn- create-pcs-map [[game capture media]]
  {:game game :capture capture :media media :total-additional-cost (calculate-additional-cost game capture media)})

(defn- create-all-pc-permutations [l]
  (->>
    (mapcat #(apply combo/cartesian-product %) (combo/permutations l))
    (filter is-correct-thunderbolt-configuration?)
    (filter all-different-mbs?)
    (filter all-different-cases?)
    (filter valid-game-pc?)
    (filter valid-media-pc?)
    (filter are-two-mbs-owned?)
    (filter at-most-one-optane-card?)
    (map create-pcs-map)))

(defn- total-cost-comparator [el1 el2]
  (< (:total-additional-cost el1) (:total-additional-cost el2)))

(defn- produce-description [desc c]
  (str "\t"
       desc
       ": "
       (:name (:mb c))
       " in "
       (:name (:case c))
       " with "
       (:name (:optical-drive c))
       " and "
       (:name (:cpu c))
       " and "
       (:name (:thunderbolt-card c))
       " and "
       (:name (:optane-card c))
       "\n"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [permutations-per-cpu (map #(for [mb motherboards case cases thunderbolt-card thunderbolt-cards optane-card optane-cards optical-drive optical-drives
                                         :when (and (optane-check? mb optane-card)
                                                    (thunderbolt-card-check? mb thunderbolt-card)
                                                    (size-check? mb case)
                                                    (cpu-check? mb %)
                                                    (optical-drive-check? mb case optical-drive))]
                                     {:mb mb :case case :cpu % :thunderbolt-card thunderbolt-card :optane-card optane-card :optical-drive optical-drive}) cpus)
        permutations-of-three-computers (create-all-pc-permutations permutations-per-cpu)
        names-of-permutations (map (fn [{:keys [game capture media total-additional-cost]}]
                                     (clojure.string/join " " (list (format "Cost: %.2f\n" total-additional-cost)
                                                                    (produce-description "Game" game)
                                                                    (produce-description "Capture" capture)
                                                                    (produce-description "Media" media))))
                                   (sort total-cost-comparator permutations-of-three-computers))]
    (dorun (map #(println (count %)) permutations-per-cpu))
    (dorun (map #(println %) names-of-permutations))
    (println (count names-of-permutations))))
