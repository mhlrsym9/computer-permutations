(ns computer-permutations.core
  (:require [clojure.math.combinatorics :as combo])
  (:gen-class))

(def motherboards
  (list {:name "ASRock Z270M Extreme4" :size "mATX" :optane? true :owned? true :thunderbolt-on-board? false :only-thunderbolt-card "ASRock Thunderbolt3" :additional-cost 0}
        {:name "Asus Z170 WS" :size "ATX" :optane? false :owned? true :thunderbolt-on-board? false :only-thunderbolt-card "Asus Thunderbolt3" :additional-cost 0}
        {:name "ASRock Z270 Supercarrier" :size "ATX" :optane? true :owned? false :thunderbolt-on-board? true :only-cpu "i7-7700K" :only-case "Phanteks Pro" :additional-cost 350}
        {:name "ASRock Z270 ITX/AC" :size "mITX" :optane? true :owned? false :thunderbolt-on-board? true :additional-cost 140}
        {:name "Gigabyte GA-Z170X-Gaming 7" :size "ATX" :optane? false :owned? false :thunderbolt-on-board? true :additional-cost 107}
        {:name "Gigabyte GA-Z270-UD5" :size "ATX" :optane? true :owned? false :thunderbolt-on-board? true :additional-cost 200}
        {:name "Some other 100 series ATX MB" :size "ATX" :optane? false :owned? false :thunderbolt-on-board? false :additional-cost 100}
        {:name "Some other 100 series mATX MB" :size "mATX" :optane? false :owned? false :thunderbolt-on-board? false :additional-cost 100}
        {:name "Some other 200 series ATX MB" :size "ATX" :optane? true :owned? false :thunderbolt-on-board? false :additional-cost 100}
        {:name "Some other 200 series mATX MB" :size "mATX" :optane? true :owned? false :thunderbolt-on-board? false :additional-cost 100}))

(def cases
  (list {:name "Phanteks Pro" :size "ATX" :additional-cost 0}
        {:name "Cooler Master Silencio 352" :size "mATX" :additional-cost 0}
        {:name "Phanteks P400S" :size "ATX" :additional-cost 0}
        {:name "Some other ATX case" :size "ATX" :additional-cost 90}))

(def cpus
  (list {:name "i7-7700K" :licensed-to "Asus Z170 WS" :additional-cost 0}
        {:name "i5-7500" :licensed-to "ASRock Z270M Extreme4" :additional-cost 0}
        {:name "i5-6500t" :additional-cost 0}))

(def no-thunderbolt-card {:name "No Thunderbolt card" :additional-cost 0})

(def thunderbolt-cards
  (list {:name "ASRock Thunderbolt3" :additional-cost 80}
        {:name "Asus Thunderbolt3" :additional-cost 0}
        no-thunderbolt-card))

(def optane-cards
  (list {:name "32 GB optane" :optane? true :additional-cost 0}
        {:name "no optane" :optane? false :additional-cost 0}))

(def optical-drives
  (list {:name "long blu-ray" :additional-cost 0}
        {:name "short blu-ray" :additional-cost 43}))

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
  (if (and (= "ASRock Z270M Extreme4" (:name mb)) (= "Cooler Master Silencio 352" (:name case)))
    (= "short blu-ray" (:name optical-drive))
    (= "long blu-ray" (:name optical-drive))))

(defn- all-different-mbs? [c1 c2 c3]
  (let [c1-check (:name (:mb c1))
        c2-check (:name (:mb c2))
        c3-check (:name (:mb c3))]
    (and (not= c1-check c2-check)
         (not= c2-check c3-check)
         (not= c3-check c1-check))))

(defn- all-different-cases? [c1 c2 c3]
  (let [c1-check (:name (:case c1))
        c2-check (:name (:case c2))
        c3-check (:name (:case c3))]
    (and (not= c1-check c2-check)
         (not= c2-check c3-check)
         (not= c3-check c1-check))))

(defn- valid-game-pc? [game]
  (let [mb-name (:name (:mb game))
        case-name (:name (:case game))
        cpu-name (:name (:cpu game))]
    (and (= "i7-7700K" cpu-name)
         (= "Phanteks Pro" case-name)
         (or (= "ASRock Z270 Supercarrier" mb-name)
             (= "Asus Z170 WS" mb-name)))))

(defn- valid-media-pc? [{{:keys [name]} :case}]
  (= "Phanteks P400S" name))

(defn- are-two-mbs-owned? [c1 c2 c3]
  (let [l (map (fn [{{:keys [owned?]} :mb}] owned?) (list c1 c2 c3))]
    (= 2 (count (filter true? l)))))

(defn- only-one-optane-card? [c1 c2 c3]
  (let [l (map (fn [{{:keys [name]} :optane-card}] name) (list c1 c2 c3))]
    (= 1 (count (filter #(= % "32 GB optane") l)))))

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

(defn- no-thunderbolt-card-in-pc? [{{:keys [name]} :thunderbolt-card}]
  (= (:name no-thunderbolt-card) name))

(defn- is-thunderbolt-pc? [{{:keys [name]}                                        :thunderbolt-card
                            {:keys [only-thunderbolt-card thunderbolt-on-board?]} :mb}]
  (or thunderbolt-on-board? (= name only-thunderbolt-card)))

(defn- create-pc-permutations [[game-pcs capture-pcs media-pcs]]
  (for [game (filter is-thunderbolt-pc? game-pcs)
        capture (filter is-thunderbolt-pc? capture-pcs)
        media (filter no-thunderbolt-card-in-pc? media-pcs)
        :when (and (all-different-mbs? game capture media)
                   (all-different-cases? game capture media)
                   (valid-game-pc? game)
                   (valid-media-pc? media)
                   (are-two-mbs-owned? game capture media)
                   (only-one-optane-card? game capture media))]
    {:game game :capture capture :media media :total-additional-cost (calculate-additional-cost game capture media)}))

(defn- create-all-pc-permutations [[cpus1 cpus2 cpus3]]
  (mapcat create-pc-permutations (combo/permutations (list cpus1 cpus2 cpus3))))

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
                                     (clojure.string/join " " (list (str "Cost: " total-additional-cost "\n")
                                                                    (produce-description "Game" game)
                                                                    (produce-description "Capture" capture)
                                                                    (produce-description "Media" media))))
                                   (sort total-cost-comparator permutations-of-three-computers))]
    (dorun (map #(println (count %)) permutations-per-cpu))
    (dorun (map #(println %) names-of-permutations))
    (println (count names-of-permutations))))
