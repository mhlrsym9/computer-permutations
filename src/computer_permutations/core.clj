(ns computer-permutations.core
  (:require [clojure.string :as s])
  (:require [clojure.math.combinatorics :as combo])
  (:gen-class))

(def asrock-z270m-extreme4-mb {:name "ASRock Z270M Extreme4" :size "mATX" :socket 1151 :optane? true :thunderbolt-on-board? false :only-thunderbolt-card "ASRock Thunderbolt3" :lost-cost 138.99})
(def asrock-z270m-extreme4-mb-name (:name asrock-z270m-extreme4-mb))

(def asrock-z270m-extreme4-mb-2 {:name "ASRock Z270M Extreme4 2" :size "mATX" :socket 1151 :optane? true :thunderbolt-on-board? false :only-thunderbolt-card "ASRock Thunderbolt3" :additional-cost 197.95})
(def asrock-z270m-extreme4-mb-2-name (:name asrock-z270m-extreme4-mb-2))

(def asus-z170-ws-mb {:name "Asus Z170 WS" :size "ATX" :socket 1151 :optane? false :thunderbolt-on-board? false :only-thunderbolt-card "Asus Thunderbolt3" :lost-cost 266.87})
(def asus-z170-ws-mb-name (:name asus-z170-ws-mb))

(def asus-z270-ws-mb {:name "Asus Z270 WS" :size "ATX" :socket 1151 :optane? true :thunderbolt-on-board? false :only-cpu "i7-7700K" :only-thunderbolt-card "Asus Thunderbolt3" :additional-cost 379.99})
(def asus-z270-ws-mb-name (:name asus-z270-ws-mb))

(def core-2-duo-mb {:name "Core 2 Duo MB" :size "ATX" :socket 775 :optane? false :thunderbolt-on-board? false :only-cpu "Core 2 Duo E8400" :lost-cost 0})
(def core-2-duo-mb-name (:name core-2-duo-mb))

(def asrock-z270-supercarrier-mb {:name "ASRock Z270 Supercarrier" :size "ATX" :socket 1151 :optane? true :thunderbolt-on-board? true :only-cpu "i7-7700K" :only-case "Phanteks Pro" :additional-cost 349.99})
(def asrock-z270-supercarrier-mb-name (:name asrock-z270-supercarrier-mb))

(def motherboards
  (list asrock-z270m-extreme4-mb
        asus-z170-ws-mb
        core-2-duo-mb

        asus-z270-ws-mb
        asrock-z270-supercarrier-mb

        {:name "ASRock Z270 ITX/AC" :size "mITX" :socket 1151 :optane? true :thunderbolt-on-board? true :additional-cost 138.99}
        {:name "Gigabyte GA-Z170X-Gaming 7" :size "ATX" :socket 1151 :optane? false :thunderbolt-on-board? true :additional-cost (- (* 119.99 1.0625) 20.) :from "MicroCenter"}
        {:name "ASRock H270 Pro4" :size "mTX" :socket 1151 :optane? false :thunderbolt-on-board? false :only-thunderbolt-card "ASRock Thunderbolt3" :additional-cost 89.99}
        asrock-z270m-extreme4-mb-2

        {:name "Gigabyte GA-Z270-UD5" :size "ATX" :socket 1151 :optane? true :thunderbolt-on-board? true :additional-cost (* 199.99 1.0625)}
        ;        {:name "ASRock H170 Pro4S" :size "ATX" :socket 1151 :optane? false :thunderbolt-on-board? false :additional-cost 69.99}
        ;        {:name "Gigabyte GA-H110M-M.2" :size "mATX" :socket 1151 :optane? false :thunderbolt-on-board? false :additional-cost 46.99}
        ;        {:name "Gigabyte GA-B250-HD3" :size "ATX" :socket 1151 :optane? true :thunderbolt-on-board? false :additional-cost 59.99}
        ;        {:name "ASRock H270M Pro4" :size "mATX" :socket 1151 :optane? true :thunderbolt-on-board? false :additional-cost 64.99 :from "NewEgg"}
        ;        {:name "ASRock H270M Pro4" :size "mATX" :socket 1151 :optane? true :thunderbolt-on-board? false :additional-cost 63.74 :from "MicroCenter"}
        ))

(def silencio-case {:name "Cooler Master Silencio 352" :size "mATX" :lost-cost 57.99})
(def silencio-case-name (:name silencio-case))

(def cases
  (list {:name "Phanteks Pro" :size "ATX" :lost-cost 84.99}
        silencio-case
        {:name "Phanteks P400S" :size "ATX" :lost-cost (+ 79.99 15.80 -8.80 2.99)}

        {:name "be quiet PURE BASE 600 - Black" :size "ATX" :additional-cost 89.90}
        ;        {:name "Corsair Carbide Series 300R" :size "ATX" :additional-cost (- (* 72.99 1.0625) 10.) :from "MicroCenter"}
        ;        {:name "Corsair Carbide Series 300R" :size "ATX" :additional-cost 59.99 :from "NewEgg"}
        ;        {:name "Phanteks Enthoo Pro Titanium Green" :size "ATX" :additional-cost 69.99}
        ))

(def game-cpu {:name "i7-7700K" :socket 1151 :licensed-to asus-z170-ws-mb-name :optane? true :lost-cost (* 299.99 1.0625)})
(def game-cpu-name (:name game-cpu))

(def high-power-i5-cpu {:name "i5-7500" :socket 1151 :licensed-to asrock-z270m-extreme4-mb-name :optane? true :lost-cost (* 179.99 1.0625)})
(def high-power-i5-cpu-name (:name high-power-i5-cpu))

(def low-power-cpu {:name "i5-6500t" :socket 1151 :optane? false :lost-cost (+ 107.50 6.65)})
(def low-power-cpu-name (:name low-power-cpu))

(def old-cpu {:name "Core 2 Duo E8400" :socket 775 :licensed-to core-2-duo-mb-name :optane? false :lost-cost 0})
(def old-cpu-name (:name old-cpu))

(def cpus (list game-cpu high-power-i5-cpu low-power-cpu old-cpu))

(def asrock-thunderbolt3-card {:name "ASRock Thunderbolt3" :additional-cost 79.99})
(def asrock-thunderbolt3-card-name (:name asrock-thunderbolt3-card))

(def asus-thunderbolt-card {:name "Asus Thunderbolt3" :lost-cost 59.49})
(def asus-thunderbolt-card-name (:name asus-thunderbolt-card))

(def no-thunderbolt-card {:name "No Thunderbolt card" :additional-cost 0 :lost-cost 0})
(def no-thunderbolt-card-name (:name no-thunderbolt-card))

(def thunderbolt-cards (list asrock-thunderbolt3-card asus-thunderbolt-card no-thunderbolt-card))

(def optane-card-32GB {:name "32 GB optane" :optane? true :lost-cost (* 69.99 1.0625)})
(def optane-card-32GB-name (:name optane-card-32GB))

(def no-optane-card {:name "no optane" :optane? false :additional-cost 0 :lost-cost 0})
(def no-optane-card-name (:name no-optane-card))

(def optane-cards (list optane-card-32GB no-optane-card))

(def no-optical-drive {:name "no optical drive" :additional-cost 0 :lost-cost 0})
(def no-optical-drive-name (:name no-optical-drive))

(def short-blu-ray {:name "short blu-ray" :additional-cost 42.99})
(def short-blu-ray-name (:name short-blu-ray))

(def long-blu-ray {:name "long blu-ray" :lost-cost 0})
(def long-blu-ray-name (:name long-blu-ray))

(def optical-drives (list long-blu-ray short-blu-ray no-optical-drive))

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
  (if (= (:socket mb) (:socket cpu))
    (if-let [only-cpu (:only-cpu mb)]
      (= only-cpu (:name cpu))
      true)
    false))

(defn- optical-drive-check? [mb case optical-drive]
  (let [[mb-name case-name optical-drive-name] (map :name (list mb case optical-drive))]
    (or (not= long-blu-ray-name optical-drive-name)
        (and (not= asrock-z270m-extreme4-mb-name mb-name)
             (not= asrock-z270m-extreme4-mb-2-name mb-name))
        (not= silencio-case-name case-name))))

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
    (and (= game-cpu-name cpu-name)
         (= "Phanteks Pro" case-name)
         (= long-blu-ray-name optical-drive-name)
         (or (= asrock-z270-supercarrier-mb-name mb-name)
             (= asus-z170-ws-mb-name mb-name)
             (= asus-z270-ws-mb-name mb-name)))))

(defn- valid-capture-pc? [[_ {:keys [optical-drive mb]} _]]
  (let [optical-drive-name (:name optical-drive)
        socket (:socket mb)]
    (and (= 1151 socket)
         (not= no-optical-drive-name optical-drive-name))))

(defn- valid-media-pc? [[_ _ {:keys [case optane-card optical-drive]}]]
  (let [[case-name optane-card-name optical-drive-name] (map :name (list case optane-card optical-drive))]
    (and (= "Phanteks P400S" case-name)
         (= no-optane-card-name optane-card-name)
         (= no-optical-drive-name optical-drive-name))))

(defn- valid-media-pc-even-if-has-optane? [[_ _ {{:keys [name]} :case}]]
  (= "Phanteks P400S" name))

(defn- count-of-number-of-owned-motherboard-used [l]
  (let [lost-cost-mbs (map #(get-in % [:mb :lost-cost]) l)]
    (count (filter identity lost-cost-mbs))))

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

(defn- already-licensed? [c]
  (if-let [licensed-to (get-in c [:cpu :licensed-to])]
    (= licensed-to (get-in c [:mb :name]))
    false))

(def zero-cost-component {:cost 0. :components []})
(defn- merge-cost-data [v1 v2]
  {:cost       (+ (:cost v1) (:cost v2))
   :components (concat (:components v1) (filter identity (:components v2)))})

(defn- calculate-additional-cost-of-pc [c]
  (let [additional-cost-components-of-c (into {} (filter #(:additional-cost (val %)) c))]
    (merge-cost-data
      (reduce-kv (fn [m _ {:keys [additional-cost name]}]
                   (merge-cost-data m {:cost additional-cost :components [(when (> additional-cost 0.) name)]}))
                 zero-cost-component
                 additional-cost-components-of-c)
      (if (already-licensed? c)
        zero-cost-component
        {:cost 100. :components ["Windows license"]}))))

(defn- is-at-least-one-pc-unlicensed? [l]
  (seq (filter false? (map #(already-licensed? %) l))))

(defn- calculate-additional-cost [l]
  (merge-cost-data (reduce merge-cost-data (map calculate-additional-cost-of-pc l))
                   (if (is-at-least-one-pc-unlicensed? l)
                     {:cost -100. :components ["Owned Windows license"]}
                     zero-cost-component)))

(defn- calculate-lost-cost-of-missing-component [bag-of-components component l]
  (let [look-for-lost-costs-in-bag (filter :lost-cost bag-of-components)
        names-of-the-component (map #(get-in % [component :name]) l)]
    (reduce merge-cost-data
            (map (fn [component]
                   (let [name (:name component)]
                     (if (seq (filter #(= name %) names-of-the-component))
                       zero-cost-component
                       {:cost (:lost-cost component) :components [name]})))
                 look-for-lost-costs-in-bag))))

(defn- calculate-lost-cost [l]
  (reduce merge-cost-data
          (map #(calculate-lost-cost-of-missing-component %1 %2 l)
               [motherboards cpus cases optane-cards optical-drives thunderbolt-cards]
               [:mb :cpu :case :optane-card :optical-drive :thunderbolt-card])))

(defn- no-thunderbolt-card-in-pc? [{{:keys [name]} :thunderbolt-card}]
  (= (:name no-thunderbolt-card) name))

(defn- is-thunderbolt-pc? [{{:keys [name]}                                        :thunderbolt-card
                            {:keys [only-thunderbolt-card thunderbolt-on-board?]} :mb}]
  (or thunderbolt-on-board? (= name only-thunderbolt-card)))

(defn- use-the-one-asus-thunderbolt-card-if-possible? [l]
  (let [asus-mbs (filter #(let [name (get-in % [:mb :name])]
                            (or (= asus-z170-ws-mb-name name)
                                (= asus-z270-ws-mb-name name))) l)
        asus-tbs (filter #(let [name (get-in % [:thunderbolt-card :name])]
                            (= asus-thunderbolt-card-name name)) l)]
    (or (empty? asus-mbs) (= 1 (count asus-tbs)))))

(defn- flip-components [pc1 pc2]
  (apply assoc pc1 (flatten (map #(list % (% pc2)) (list :mb :cpu :optane-card)))))

(defn- is-flipped-capture-valid? [flipped-capture]
  (and (thunderbolt-card-check? (:mb flipped-capture) (:thunderbolt-card flipped-capture))
       (size-check? (:mb flipped-capture) (:case flipped-capture))
       (optical-drive-check? (:mb flipped-capture) (:case flipped-capture) (:optical-drive flipped-capture))
       (valid-capture-pc? (list nil flipped-capture nil))
       (is-thunderbolt-pc? flipped-capture)))

(defn- does-flipped-capture-and-media-pcs-still-work? [[game capture media]]
  (let [flipped-capture (flip-components capture media)
        flipped-capture2 (assoc flipped-capture :thunderbolt-card (:thunderbolt-card media))]
    (some is-flipped-capture-valid? (list flipped-capture flipped-capture2))))

(defn- is-media-pc-thunderbolt-capable-backup-to-capture-pc? [[_ capture media]]
  (if (= (get-in capture [:cpu :name]) high-power-i5-cpu-name)
    (and (= (get-in media [:cpu :name]) low-power-cpu-name)
         (or (get-in media [:mb :thunderbolt-on-board?])
             (when-let [otc-name (get-in media [:mb :only-thunderbolt-card])]
               (= otc-name (get-in capture [:thunderbolt-card :name])))))
    true))

(defn- is-correct-thunderbolt-configuration? [[game capture media :as l]]
  (and (is-thunderbolt-pc? game)
       (is-thunderbolt-pc? capture)
       (no-thunderbolt-card-in-pc? media)
       (use-the-one-asus-thunderbolt-card-if-possible? l)
       (does-flipped-capture-and-media-pcs-still-work? l)
       ))

(defn- create-pcs-map [[game capture media :as l]]
  (let [lost-data (calculate-lost-cost l)]
    {:game                  game
     :capture               capture
     :media                 media
     :total-additional-cost (calculate-additional-cost l)
     :total-lost-cost       (calculate-lost-cost l)}))

(defn- create-all-pc-permutations [all-pcs]
  (let [game-pcs (filter (fn [c] (valid-game-pc? (list c nil nil))) (filter #(= game-cpu-name (get-in % [:cpu :name])) all-pcs))
        other-pcs (remove #(= game-cpu-name (get-in % [:cpu :name])) all-pcs)]
    (->>
      (map flatten (combo/cartesian-product game-pcs (mapcat #(combo/permutations %) (combo/combinations other-pcs 2))))
      (filter all-different-mbs?)
      (filter all-different-cases?)
      (filter all-different-cpus?)
      (filter valid-capture-pc?)
      (filter valid-media-pc?)
      (filter are-at-least-two-mbs-owned?)
      (filter is-correct-thunderbolt-configuration?)
      (filter at-most-one-optane-card?)
      (map create-pcs-map))))

(defn- total-cost-comparator [el1 el2]
  (< (+ (get-in el1 [:total-additional-cost :cost]) (get-in el1 [:total-lost-cost :cost]))
     (+ (get-in el2 [:total-additional-cost :cost]) (get-in el2 [:total-lost-cost :cost]))))

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
        names-of-permutations (map (fn [{:keys [game capture media total-additional-cost total-lost-cost]}]
                                     (let [additional-cost (:cost total-additional-cost)
                                           additional-components (:components total-additional-cost)
                                           lost-cost (:cost total-lost-cost)
                                           lost-components (:components total-lost-cost)]
                                       (clojure.string/join " " (list (format "Cost: %.2f [Additional Cost: %.2f Lost Cost: %.2f]\n" (+ additional-cost lost-cost) additional-cost lost-cost)
                                                                      (str "Additional components: " (s/join ", " additional-components) "\n")
                                                                      (str "Lost components: " (s/join ", " lost-components) "\n")
                                                                      (produce-description "Game" game)
                                                                      (produce-description "Capture" capture)
                                                                      (produce-description "Media" media)))))
                                   (sort total-cost-comparator permutations-of-three-computers))]
    (dorun (map #(println %) names-of-permutations))
    (println (count names-of-permutations))))
