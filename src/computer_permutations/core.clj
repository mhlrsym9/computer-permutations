(ns computer-permutations.core
  (:require [clojure.math.combinatorics :as combo])
  (:gen-class))

(def motherboards
  (list {:name "ASRock Z270M Extreme4" :size "mATX" :optane? true :thunderbolt-on-board? false :only-thunderbolt-card "ASRock Thunderbolt3" :additional-cost 0}
        {:name "Asus Z170 WS" :size "ATX" :optane? false :thunderbolt-on-board? false :only-thunderbolt-card "Asus Thunderbolt3" :additional-cost 0}
        {:name "Core 2 Duo MB" :size "ATX" :optane? false :thunderbolt-on-board? false :only-cpu "Core 2 Duo" :additional-cost 0}
        ;        {:name "Asus B150M-C D3" :size "mATX" :optane? false :thunderbolt-on-board? false :additional-cost 0}

        {:name "Asus Z270 WS" :size "ATX" :optane? true :thunderbolt-on-board? false :only-cpu "i7-7700K" :only-thunderbolt-card "Asus Thunderbolt3" :additional-cost 379.99}
        {:name "ASRock Z270 Supercarrier" :size "ATX" :optane? true :thunderbolt-on-board? true :only-cpu "i7-7700K" :only-case "Phanteks Pro" :additional-cost 349.99}
        {:name "ASRock Z270 ITX/AC" :size "mITX" :optane? true :thunderbolt-on-board? true :additional-cost 138.99}
        {:name "Gigabyte GA-Z170X-Gaming 7" :size "ATX" :optane? false :thunderbolt-on-board? true :additional-cost (- (* 119.99 1.0625) 20.) :from "MicroCenter"}
        ;        {:name "Gigabyte GA-Z270-UD5" :size "ATX" :optane? true :thunderbolt-on-board? true :additional-cost (* 199.99 1.0625)}
        ;        {:name "ASRock H170 Pro4S" :size "ATX" :optane? false :thunderbolt-on-board? false :additional-cost 69.99}
        ;        {:name "Gigabyte GA-H110M-M.2" :size "mATX" :optane? false :thunderbolt-on-board? false :additional-cost 46.99}
        ;        {:name "Gigabyte GA-B250-HD3" :size "ATX" :optane? true :thunderbolt-on-board? false :additional-cost 59.99}
        ;        {:name "ASRock H270M Pro4" :size "mATX" :optane? true :thunderbolt-on-board? false :additional-cost 64.99 :from "NewEgg"}
        ;        {:name "ASRock H270M Pro4" :size "mATX" :optane? true :thunderbolt-on-board? false :additional-cost 63.74 :from "MicroCenter"}
        ))

(def cases
  (list {:name "Phanteks Pro" :size "ATX" :additional-cost 0}
        {:name "Cooler Master Silencio 352" :size "mATX" :additional-cost 0}
        {:name "Phanteks P400S" :size "ATX" :additional-cost 0}

        {:name "be quiet PURE BASE 600 - Black" :size "ATX" :additional-cost 89.90}
        ;        {:name "Corsair Carbide Series 300R" :size "ATX" :additional-cost (- (* 72.99 1.0625) 10.) :from "MicroCenter"}
        {:name "Corsair Carbide Series 300R" :size "ATX" :additional-cost 59.99 :from "NewEgg"}
        {:name "Phanteks Enthoo Pro Titanium Green" :size "ATX" :additional-cost 69.99}))

(def low-power-cpu {:name "i5-6500t" :optane? false :additional-cost 0})
(def low-power-cpu-name (:name low-power-cpu))

(def cpus
  (list {:name "i7-7700K" :licensed-to "Asus Z170 WS" :optane? true :additional-cost 0}
        {:name "i5-7500" :licensed-to "ASRock Z270M Extreme4" :optane? true :additional-cost 0}
        low-power-cpu
        {:name "Core 2 Duo" :licensed-to "Core 2 Duo MB" :optane? false :additional-cost 0}
        ))

(def asus-thunderbolt-card {:name "Asus Thunderbolt3" :additional-cost 0})
(def no-thunderbolt-card {:name "No Thunderbolt card" :additional-cost 0})
(def no-thunderbolt-card-name (:name no-thunderbolt-card))

(def thunderbolt-cards
  (list {:name "ASRock Thunderbolt3" :additional-cost 79.99}
        asus-thunderbolt-card
        no-thunderbolt-card))

(def optane-card-32GB {:name "32 GB optane" :optane? true :additional-cost 0})
(def optane-card-32GB-name (:name optane-card-32GB))

(def no-optane-card {:name "no optane" :optane? false :additional-cost 0})
(def no-optane-card-name (:name no-optane-card))

(def optane-cards
  (list optane-card-32GB
        no-optane-card))

(def no-optical-drive {:name "no optical drive" :additional-cost 0})
(def no-optical-drive-name (:name no-optical-drive))

(def short-blu-ray {:name "short blu-ray" :additional-cost 42.99})
(def short-blu-ray-name (:name short-blu-ray))

(def long-blu-ray {:name "long blu-ray" :additional-cost 0})
(def long-blu-ray-name (:name long-blu-ray))

(def optical-drives
  (list long-blu-ray
        short-blu-ray
        no-optical-drive))

(defn- optane-check? [mb cpu optane-card]
  (let [[optane-mb? optane-cpu? optane-card?] (map :optane? (list mb cpu optane-card))]
    (or (and optane-mb? optane-cpu?) (not optane-card?))))

(defn- thunderbolt-card-check? [{:keys [only-thunderbolt-card]} {:keys [name]}]
  (or
    (= only-thunderbolt-card name)
    (= no-thunderbolt-card-name name)))

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
  (let [[mb-name case-name optical-drive-name] (map :name (list mb case optical-drive))]
    (or (= no-optical-drive-name optical-drive-name)
        (if (and (= "ASRock Z270M Extreme4" mb-name)
                 (= "Cooler Master Silencio 352" case-name))
          (= short-blu-ray-name optical-drive-name)
          (= long-blu-ray-name optical-drive-name)))))

(defn- all-different-names? [names]
  (every? true? (map #(apply not= %) (combo/combinations names 2))))

(defn- all-different-mbs? [l]
  (all-different-names? (map #(get-in % [:mb :name]) l)))

(defn- all-different-cases? [l]
  (all-different-names? (map #(get-in % [:case :name]) l)))

(defn- all-different-cpus? [l]
  (all-different-names? (map #(get-in % [:cpu :name]) l)))

(defn- valid-game-pc? [[{:keys [mb case cpu optical-drive]} _ _]]
  (let [[mb-name case-name cpu-name optical-drive-name] (map :name (list mb case cpu optical-drive))]
    (and (= "i7-7700K" cpu-name)
         (= "Phanteks Pro" case-name)
         (not= no-optical-drive-name optical-drive-name)
         (or (= "ASRock Z270 Supercarrier" mb-name)
             (= "Asus Z170 WS" mb-name)
             (= "Asus Z270 WS" mb-name)))))

(defn- valid-capture-pc? [[ _ {:keys [cpu optical-drive]} _]]
  (let [[cpu-name optical-drive-name] (map :name (list cpu optical-drive))]
    (and (= cpu-name low-power-cpu-name)
         (not= no-optical-drive-name optical-drive-name))))

(defn- valid-media-pc? [[_ _ {:keys [case optane-card optical-drive]}]]
  (let [[case-name optane-card-name optical-drive-name] (map :name (list case optane-card optical-drive))]
    (and (= "Phanteks P400S" case-name)
         (= no-optane-card-name optane-card-name)
         (= no-optical-drive-name optical-drive-name))))

(defn- valid-media-pc-even-if-has-optane? [[_ _ {{:keys [name]} :case}]]
  (= "Phanteks P400S" name))

(defn- count-of-number-of-owned-motherboard-used [l]
  (let [additional-cost-mbs (map #(get-in % [:mb :additional-cost]) l)]
    (count (filter #(= 0 %) additional-cost-mbs))))

(defn- are-two-mbs-owned? [l]
  (= 2 (count-of-number-of-owned-motherboard-used l)))

(defn- are-at-least-two-mbs-owned? [l]
  (< 1 (count-of-number-of-owned-motherboard-used l)))

(defn- count-of-number-of-optane-cards-used [l]
  (let [optane-card-names (map #(get-in % [:optane-card :name]) l)]
    (count (filter #(= % optane-card-32GB-name) optane-card-names))))

(defn- at-most-one-optane-card? [l]
  (> 2 (count-of-number-of-optane-cards-used l)))

(defn- use-the-one-optane-card? [l]
  (= 1 (count-of-number-of-optane-cards-used l)))

(defn is-6500t-cpu-used? [l]
  (seq (filter #(= low-power-cpu-name %) (map (fn [c] (get-in c [:cpu :name])) l))))

(defn- already-licensed? [c]
  (if-let [licensed-to (:licensed-to (:cpu c))]
    (= licensed-to (:name (:mb c)))
    false))

(defn- calculate-the-additional-cost [c]
  (+ (reduce-kv (fn [m _ {:keys [additional-cost]}] (+ m additional-cost)) 0 c)
     ; add cost of a Windows license
     (if (already-licensed? c)
       0
       100)))

(defn- calculate-additional-cost [l]
  (let [; if not using the paid-for optane card, add in its cost
        optane-cost (if (= 0 (count-of-number-of-optane-cards-used l)) 80 0)
        cpu-cost (if (is-6500t-cpu-used? l) 0 115)]
    ; I already own one license so subtract that cost.
    (- (apply + (cons cpu-cost (cons optane-cost (map calculate-the-additional-cost l))))
       100)))

(defn- no-thunderbolt-card-in-pc? [{{:keys [name]} :thunderbolt-card}]
  (= (:name no-thunderbolt-card) name))

(defn- is-thunderbolt-pc? [{{:keys [name]}                                        :thunderbolt-card
                            {:keys [only-thunderbolt-card thunderbolt-on-board?]} :mb}]
  (or thunderbolt-on-board? (= name only-thunderbolt-card)))

(defn- use-the-asus-thunderbolt-card? [l]
  (let [asus-tbs (filter (fn [n] (= (:name asus-thunderbolt-card) n)) (map #(:name (:thunderbolt-card %)) l))]
    (= 1 (count asus-tbs))))

(defn- is-correct-thunderbolt-configuration? [[game capture media :as l]]
  (and (is-thunderbolt-pc? game)
       (is-thunderbolt-pc? capture)
       (no-thunderbolt-card-in-pc? media)
       (use-the-asus-thunderbolt-card? l)))

(defn- create-pcs-map [[game capture media :as l]]
  {:game game :capture capture :media media :total-additional-cost (calculate-additional-cost l)})

(defn- create-all-pc-permutations [all-pcs]
  (->>
    (mapcat #(combo/permutations %) (combo/combinations all-pcs 3))
    ;    (mapcat #(apply combo/cartesian-product %) (combo/permutations l))
    (filter all-different-mbs?)
    (filter all-different-cases?)
    (filter all-different-cpus?)
    (filter valid-game-pc?)
    (filter valid-capture-pc?)
    (filter valid-media-pc?)
    (filter are-at-least-two-mbs-owned?)
    (filter is-correct-thunderbolt-configuration?)
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
  (let [permutations-per-cpu (mapcat #(for [mb motherboards case cases thunderbolt-card thunderbolt-cards optane-card optane-cards optical-drive optical-drives
                                            :when (and (optane-check? mb % optane-card)
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
    (dorun (map #(println %) names-of-permutations))
    (println (count names-of-permutations))))
