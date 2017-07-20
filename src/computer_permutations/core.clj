(ns computer-permutations.core
  (:require [clojure.string :as s])
  (:require [clojure.math.combinatorics :as combo])
  (:require [clojure.set :as set])
  (:gen-class))

(def asrock-z270m-extreme4-mb {:name "ASRock Z270M Extreme4" :size "mATX" :socket 1151 :optane? true :thunderbolt-on-board? false :only-thunderbolt-card "ASRock Thunderbolt3" :lost-cost 138.99})
(def asrock-z270m-extreme4-mb-name (:name asrock-z270m-extreme4-mb))

(def asus-z170-ws-mb {:name "Asus Z170 WS" :size "ATX" :socket 1151 :optane? false :thunderbolt-on-board? false :only-thunderbolt-card "Asus Thunderbolt3" :lost-cost 266.87})
(def asus-z170-ws-mb-name (:name asus-z170-ws-mb))

(def gigabyte-ga-ex58-ud4p {:name "Gigabyte GA-EX58-UD4P" :socket 1366 :optane? false :thunderbolt-on-board? false :only-cpu "i7-920" :lost-cost 0})
(def gigabyte-ga-ex58-ud4p-name (:name gigabyte-ga-ex58-ud4p))

(def core-2-duo-mb {:name "Core 2 Duo MB" :size "ATX" :socket 775 :optane? false :thunderbolt-on-board? false :only-cpu "Core 2 Duo E8400" :lost-cost 0})
(def core-2-duo-mb-name (:name core-2-duo-mb))

(def asus-z270-ws-mb {:name "Asus Z270 WS" :size "ATX" :socket 1151 :optane? true :thunderbolt-on-board? false :only-cpu "i7-7700K" :only-thunderbolt-card "Asus Thunderbolt3" :additional-cost 379.99})
(def asus-z270-ws-mb-name (:name asus-z270-ws-mb))

(def asrock-z270-supercarrier-mb {:name "ASRock Z270 Supercarrier" :size "ATX" :socket 1151 :optane? true :thunderbolt-on-board? true :only-cpu "i7-7700K" :only-case "Phanteks Pro" :additional-cost 349.99})
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

        asus-z270-ws-mb
        asrock-z270-supercarrier-mb

        gigabyte-ga-z170x-gaming7
        asrock-h270-pro4
        asrock-b150m-pro4

        asrock-z270-itx
        asrock-z270m-extreme4-mb-2
        gigabyte-ga-z270-ud5
        ))

(def phanteks-enthoo-pro {:name "Phanteks Enthoo Pro" :size "ATX" :lost-cost 84.99})
(def phanteks-enthoo-pro-name (:name phanteks-enthoo-pro))

(def silencio-case {:name "Cooler Master Silencio 352" :size "mATX" :lost-cost 57.99})
(def silencio-case-name (:name silencio-case))

(def phanteks-eclipse-p400s {:name "Phanteks P400S" :size "ATX" :lost-cost (+ 79.99 15.80 -8.80 2.99)})
(def phanteks-eclipse-p400s-name (:name phanteks-eclipse-p400s))

(def bequiet-purebase600 {:name "be quiet! Pure Base 600" :size "ATX" :lost-cost 89.90})
(def bequiet-purebase600-name (:name bequiet-purebase600))

(def fractal-design-define-c {:name "Fractal Design Define C" :size "ATX" :lost-cost 79.98})
(def fractal-design-define-c-name (:name fractal-design-define-c))

(def corsair-100r {:name "Corsair 100R" :size "ATX" :lost-cost 59.99})
(def corsair-100r-name (:name corsair-100r))

(def fractal-design-core2300 {:name "Fractal Design Core 2300" :size "ATX" :additional-cost 59.99})
(def fractal-design-core2300-name (:name fractal-design-core2300))

(def cases
  (list phanteks-enthoo-pro
        silencio-case
        phanteks-eclipse-p400s
        bequiet-purebase600
        fractal-design-define-c
        corsair-100r

        fractal-design-core2300
        ))

(def game-cpu {:name "i7-7700K" :socket 1151 :licensed-to asus-z170-ws-mb-name :optane? true :lost-cost (* 299.99 1.0625)})
(def game-cpu-name (:name game-cpu))

(def high-power-kaby-lake-cpu {:name "i5-7500" :socket 1151 :licensed-to asrock-z270m-extreme4-mb-name :optane? true :lost-cost (* 179.99 1.0625)})
(def high-power-kaby-lake-cpu-name (:name high-power-kaby-lake-cpu))

(def low-power-kaby-lake-cpu {:name "i5-7400t" :socket 1151 :optane? true :lost-cost 135.87})
(def low-power-kaby-lake-cpu-name (:name low-power-kaby-lake-cpu))

(def low-power-skylake-cpu {:name "i5-6500t" :socket 1151 :optane? false :lost-cost (+ 107.50 6.65)})
(def low-power-cpu-name (:name low-power-skylake-cpu))

(def i7-920-cpu {:name "i7-920" :socket 1366 :licensed-to gigabyte-ga-ex58-ud4p-name :optane? false :lost-cost 0})
(def i7-920-cpu-name (:name i7-920-cpu))

(def old-cpu {:name "Core 2 Duo E8400" :socket 775 :licensed-to core-2-duo-mb-name :optane? false :lost-cost 0})
(def old-cpu-name (:name old-cpu))

(def cpus (list game-cpu high-power-kaby-lake-cpu low-power-kaby-lake-cpu low-power-skylake-cpu i7-920-cpu old-cpu))

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

(def long-blu-ray-2 {:name "long blu-ray 2" :lost-cost 0})
(def long-blu-ray-2-name (:name long-blu-ray))

(def long-blu-ray-3 {:name "long blu-ray 3" :lost-cost 0})
(def long-blu-ray-3-name (:name long-blu-ray))

(def optical-drives (list long-blu-ray long-blu-ray-2 long-blu-ray-3 short-blu-ray no-optical-drive))
(def long-blu-ray-drives (list long-blu-ray long-blu-ray-2 long-blu-ray-3))

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

(defn- valid-game-pc? [{{:keys [mb case cpu optical-drive]} :game}]
  (let [[mb-name case-name cpu-name optical-drive-name] (map :name (list mb case cpu optical-drive))]
    (and (= game-cpu-name cpu-name)
         (= phanteks-enthoo-pro-name case-name)
         (some #(= % optical-drive) long-blu-ray-drives)
         (or (= asrock-z270-supercarrier-mb-name mb-name)
             (= asus-z170-ws-mb-name mb-name)
             (= asus-z270-ws-mb-name mb-name)))))

(defn- valid-capture-pc? [{{:keys [optical-drive mb case]} :capture}]
  (let [optical-drive-name (:name optical-drive)
        socket (:socket mb)
        case-name (:name case)]
    (and (= 1151 socket)
         (not= no-optical-drive-name optical-drive-name)
         (or (= silencio-case-name case-name)
             (= bequiet-purebase600-name case-name)))))

(defn- valid-sleep-pc? [{{:keys [case optane-card optical-drive mb]} :sleep}]
  (let [[case-name optane-card-name optical-drive-name] (map :name (list case optane-card optical-drive))
        socket (:socket mb)]
    (and (= phanteks-eclipse-p400s-name case-name)
         (= no-optane-card-name optane-card-name)
         (= no-optical-drive-name optical-drive-name)
         (or (= 775 socket)
             (= 1151 socket)))))

(defn- valid-media-pc? [{{:keys [case optane-card optical-drive mb]} :media}]
  (let [[case-name optane-card-name optical-drive-name] (map :name (list case optane-card optical-drive))
        socket (:socket mb)]
    (and (= fractal-design-define-c-name case-name)
         (= no-optane-card-name optane-card-name)
         (= no-optical-drive-name optical-drive-name)
         (or (= 1366 socket)
             (= 1151 socket)))))

(defn- valid-play-pc? [{{:keys [mb case cpu optical-drive]} :play}]
  (let [[mb-name case-name cpu-name optical-drive-name] (map :name (list mb case cpu optical-drive))]
    (and (= i7-920-cpu-name cpu-name)
         (= corsair-100r-name case-name)
         (some #(= (:name %) optical-drive-name) long-blu-ray-drives)
         (= gigabyte-ga-ex58-ud4p-name mb-name))))

(defn- all-different-names? [names]
  (every? true? (map #(apply not= %) (combo/combinations names 2))))

(defn- all-different-mbs? [m]
  (all-different-names? (map #(get-in % [:mb :name]) (vals m))))

(defn- all-different-cases? [m]
  (all-different-names? (map #(get-in % [:case :name]) (vals m))))

(defn- all-different-cpus? [m]
  (all-different-names? (map #(get-in % [:cpu :name]) (vals m))))

(defn- all-different-optical-drives? [m]
  (all-different-names? (remove (fn [n] (= no-optical-drive-name n)) (map #(get-in % [:optical-drive :name]) (vals m)))))

(defn- count-of-number-of-owned-motherboard-used [m]
  (let [lost-cost-mbs (map #(get-in % [:mb :lost-cost]) (vals m))]
    (count (filter identity lost-cost-mbs))))

(defn- count-of-number-of-optane-cards-used [l]
  (let [optane-card-names (map #(get-in % [:optane-card :name]) l)]
    (count (filter #(= % optane-card-32GB-name) optane-card-names))))

(defn- at-most-one-optane-card? [l]
  (> 2 (count-of-number-of-optane-cards-used l)))

(defn- use-the-one-optane-card? [l]
  (= 1 (count-of-number-of-optane-cards-used l)))

(defn- no-thunderbolt-card-in-pc? [{{:keys [name]} :thunderbolt-card}]
  (= (:name no-thunderbolt-card) name))

(defn- is-thunderbolt-pc? [{{:keys [name]}                                        :thunderbolt-card
                            {:keys [only-thunderbolt-card thunderbolt-on-board?]} :mb}]
  (or thunderbolt-on-board? (= name only-thunderbolt-card)))

(defn- flip-components [pc1 pc2]
  (apply assoc pc1 (flatten (map #(list % (% pc2)) (list :mb :cpu :optane-card)))))

(defn- is-flipped-capture-valid? [flipped-capture]
  (and (thunderbolt-card-check? (:mb flipped-capture) (:thunderbolt-card flipped-capture))
       (size-check? (:mb flipped-capture) (:case flipped-capture))
       (optical-drive-check? (:mb flipped-capture) (:case flipped-capture) (:optical-drive flipped-capture))
       (valid-capture-pc? {:capture flipped-capture})
       (is-thunderbolt-pc? flipped-capture)))

(defn- does-flipped-pcs-still-work? [pc1 pc2]
  (let [flipped-capture (flip-components pc1 pc2)
        flipped-capture2 (assoc flipped-capture :thunderbolt-card (:thunderbolt-card pc2))]
    (some is-flipped-capture-valid? (list flipped-capture flipped-capture2))))

(defn- is-correct-thunderbolt-configuration? [{:keys [game capture sleep media play]}]
  (and (is-thunderbolt-pc? game)
       (is-thunderbolt-pc? capture)
       (no-thunderbolt-card-in-pc? sleep)
       (no-thunderbolt-card-in-pc? media)
       (not (is-thunderbolt-pc? play))
       (or (does-flipped-pcs-still-work? capture sleep)
           (does-flipped-pcs-still-work? capture media))))

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

(defn- calculate-additional-cost [m]
  (merge-cost-data (reduce merge-cost-data (map calculate-additional-cost-of-pc (vals m)))
                   (if (is-at-least-one-pc-unlicensed? (vals m))
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

(defn- calculate-lost-cost [m]
  (reduce merge-cost-data
          (map #(calculate-lost-cost-of-missing-component %1 %2 (vals m))
               [motherboards cpus cases optane-cards optical-drives thunderbolt-cards]
               [:mb :cpu :case :optane-card :optical-drive :thunderbolt-card])))

(defn- create-all-pc-permutations [all-pcs]
  (let [game-pcs (filter #(valid-game-pc? {:game %}) all-pcs)
        capture-pcs (filter #(valid-capture-pc? {:capture %}) all-pcs)
        sleep-pcs (filter #(valid-sleep-pc? {:sleep %}) all-pcs)
        media-pcs (filter #(valid-media-pc? {:media %}) all-pcs)
        play-pcs (filter #(valid-play-pc? {:play %}) all-pcs)]
    (->>
      (map (fn [[game-pc capture-pc sleep-pc media-pc play-pc]]
             {:game game-pc :capture capture-pc :sleep sleep-pc :media media-pc :play play-pc})
           (combo/cartesian-product game-pcs capture-pcs sleep-pcs media-pcs play-pcs))
      (filter #(and (all-different-mbs? %)
                    (all-different-cases? %)
                    (all-different-cpus? %)
                    (all-different-optical-drives? %)
                    (< 2 (count-of-number-of-owned-motherboard-used %))
                    (is-correct-thunderbolt-configuration? %)
                    (at-most-one-optane-card? %)))
      (map #(assoc % :total-additional-cost (calculate-additional-cost %) :total-lost-cost (calculate-lost-cost %))))))

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
        names-of-permutations (map (fn [{:keys [game capture sleep media play total-additional-cost total-lost-cost]}]
                                     (let [additional-cost (:cost total-additional-cost)
                                           additional-components (:components total-additional-cost)
                                           lost-cost (:cost total-lost-cost)
                                           lost-components (:components total-lost-cost)]
                                       (clojure.string/join " " (list (format "Cost: %.2f [Additional Cost: %.2f Lost Cost: %.2f]\n" (+ additional-cost lost-cost) additional-cost lost-cost)
                                                                      (str "Additional components: " (s/join ", " additional-components) "\n")
                                                                      (str "Lost components: " (s/join ", " lost-components) "\n")
                                                                      (produce-description "Game" game)
                                                                      (produce-description "Capture" capture)
                                                                      (produce-description "Sleep" sleep)
                                                                      (produce-description "Media" media)
                                                                      (produce-description "Play" play)))))
                                   (sort total-cost-comparator permutations-of-three-computers))]
    (dorun (map #(println %) names-of-permutations))
    (println (count names-of-permutations))))
