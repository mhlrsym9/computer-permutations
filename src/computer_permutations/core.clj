(ns computer-permutations.core
  (:require [clojure.string :as s])
  (:require [clojure.math.combinatorics :as combo])
  (:require [clojure.set :as set])
  (:use [computer-permutations.motherboards])
  (:use [computer-permutations.cases])
  (:use [computer-permutations.cpus])
  (:use [computer-permutations.thunderbolt-cards])
  (:use [computer-permutations.optane-cards])
  (:use [computer-permutations.optical-drives])
  (:gen-class))

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
  (let [[mb-name case-name optical-drive-name] (map :name (list mb case optical-drive))
        restricted-size (and (= silencio-case-name case-name)
                             (= asrock-z270m-extreme4-mb-name mb-name))]
    (cond (= short-blu-ray-name optical-drive-name) restricted-size
          (some #{optical-drive-name} long-blu-ray-drive-names) (not restricted-size)
          :else true)))

(defn- valid-game-pc? [{{:keys [mb case cpu optical-drive]} :game :as c}]
  (let [[mb-name case-name cpu-name optical-drive-name] (map :name (list mb case cpu optical-drive))]
    (and (= game-cpu-name cpu-name)
         (= phanteks-enthoo-pro-name case-name)
         (some #{optical-drive-name} long-blu-ray-drive-names)
         (some #{mb-name} high-end-motherboard-names))))

(defn- valid-capture-pc? [{{:keys [optical-drive cpu case mb]} :capture :as c}]
  (let [[case-name cpu-name optical-drive-name mb-name] (map :name (list case cpu optical-drive mb))]
    (and (some #{cpu-name} [low-power-kaby-lake-cpu-name high-power-kaby-lake-cpu-name])
         (not= no-optical-drive-name optical-drive-name)
         (not-any? #{mb-name} high-end-motherboard-names)
         (some #{case-name} [silencio-case-name bequiet-purebase600-name]))))

(defn- valid-sleep-pc? [{{:keys [case optane-card optical-drive cpu mb]} :sleep}]
  (let [[case-name optane-card-name optical-drive-name cpu-name mb-name] (map :name (list case optane-card optical-drive cpu mb))]
    (and (= phanteks-eclipse-p400s-name case-name)
         (= no-optane-card-name optane-card-name)
         (= no-optical-drive-name optical-drive-name)
         (not-any? #{mb-name} high-end-motherboard-names)
         (some #{cpu-name} [core-2-duo-cpu-name low-power-skylake-cpu-name]))))

(defn- valid-media-pc? [{{:keys [case optane-card optical-drive cpu mb]} :media}]
  (let [[case-name optane-card-name optical-drive-name cpu-name mb-name] (map :name (list case optane-card optical-drive cpu mb))]
    (and (= fractal-design-define-c-name case-name)
         (= no-optane-card-name optane-card-name)
         (= no-optical-drive-name optical-drive-name)
         (not-any? #{mb-name} high-end-motherboard-names)
         (some #{cpu-name} [i7-920-cpu-name low-power-kaby-lake-cpu-name high-power-kaby-lake-cpu-name]))))

(defn- valid-play-pc? [{{:keys [mb case cpu optical-drive]} :play}]
  (let [[mb-name case-name cpu-name optical-drive-name] (map :name (list mb case cpu optical-drive))]
    (and (some #{cpu-name} [core-2-quad-cpu-name i7-920-cpu-name])
         (= corsair-100r-name case-name)
         (some #{optical-drive-name} long-blu-ray-drive-names))))

(defn- valid-dive-pc? [{{:keys [mb case cpu optical-drive]} :dive}]
  (let [[mb-name case-name cpu-name optical-drive-name] (map :name (list mb case cpu optical-drive))]
    (and (some #{cpu-name} [intel-e5-2609-v3-cpu-name high-power-skylake-cpu-name])
         (some #{mb-name} (cons gigabyte-ga-x99p-sli-mb-name high-end-motherboard-names))
         (= phanteks-enthoo-pro-2-name case-name)
         (= no-optical-drive-name optical-drive-name))))

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

(defn- count-number-of-thunderbolt-cards [m name]
  (count (filter (fn [c] (= name (get-in c [:thunderbolt-card :name]))) (vals m))))

; The capture pc needs to have the possibility of having high-power LGA1151 CPUs
; in order to pass the thunderbolt configuration, but I don't actually want
; those CPUs in the capture PC, unless I absolutely need to flip one of the
; other two boards (media & sleep) if I need a processing boost...
(defn- is-capture-low-power-cpu? [{{{:keys [name]} :cpu} :capture}]
  (some #{name} (list low-power-kaby-lake-cpu-name low-power-skylake-cpu-name)))

(defn- count-of-number-of-owned-motherboard-used [m]
  (let [lost-cost-mbs (map #(get-in % [:mb :lost-cost]) (vals m))]
    (count (filter identity lost-cost-mbs))))

(defn- count-of-number-of-optane-cards-used [m]
  (let [optane-card-names (map #(get-in % [:optane-card :name]) (vals m))]
    (count (filter #(not= % no-optane-card-name) optane-card-names))))

(defn- use-the-one-optane-card-if-allowed? [m]
  (or (empty? (filter (fn [{:keys [mb cpu]}] (and (:optane? mb) (:optane? cpu))) (vals m)))
      (= 1 (count-of-number-of-optane-cards-used m))))

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

(defn- is-correct-thunderbolt-configuration? [{:keys [game capture sleep media play dive]}]
  (and (is-thunderbolt-pc? game)
       (is-thunderbolt-pc? dive)
       (is-thunderbolt-pc? capture)
       (no-thunderbolt-card-in-pc? sleep)
       (no-thunderbolt-card-in-pc? media)
       (not (is-thunderbolt-pc? play))
       (or (does-flipped-pcs-still-work? capture sleep) (does-flipped-pcs-still-work? capture media))
       ))

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
        play-pcs (filter #(valid-play-pc? {:play %}) all-pcs)
        dive-pcs (filter #(valid-dive-pc? {:dive %}) all-pcs)]
    (->>
      (map (fn [[game-pc capture-pc sleep-pc media-pc play-pc dive-pc]]
             {:game game-pc :capture capture-pc :sleep sleep-pc :media media-pc :play play-pc :dive dive-pc})
           (combo/cartesian-product game-pcs capture-pcs sleep-pcs media-pcs play-pcs dive-pcs))
      (filter #(and (all-different-mbs? %)
                    (all-different-cases? %)
                    (all-different-cpus? %)
                    (all-different-optical-drives? %)
                    (> 2 (count-number-of-thunderbolt-cards % asrock-thunderbolt3-card-name))
                    (> 2 (count-number-of-thunderbolt-cards % asus-thunderbolt-card-name))
                    (is-capture-low-power-cpu? %)
                    (< 2 (count-of-number-of-owned-motherboard-used %))
                    (is-correct-thunderbolt-configuration? %)
                    (use-the-one-optane-card-if-allowed? %)))
      (map #(assoc % :total-additional-cost (calculate-additional-cost %) :total-lost-cost (calculate-lost-cost %))))))

(defn- total-cost-comparator [el1 el2]
  (< (+ (get-in el1 [:total-additional-cost :cost]) (get-in el1 [:total-lost-cost :cost]))
     (+ (get-in el2 [:total-additional-cost :cost]) (get-in el2 [:total-lost-cost :cost]))))

(defn- names-of-all-components [{:keys [game capture sleep media play dive]}]
  (apply str (map #(let [{:keys [mb case cpu optical-drive optane-card thunderbolt-card]} %
                         optical-drive-name (:name optical-drive)]
                     (str (:name mb)
                          (:name case)
                          (:name cpu)
                          (:name optane-card)
                          (:name thunderbolt-card)
                          (if (some #{optical-drive-name} long-blu-ray-drive-names) long-blu-ray-name optical-drive-name)))
                  (list game capture sleep media play dive))))

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
        all-unique-valid-pc-collections (map #(first %) (partition-by names-of-all-components (sort-by names-of-all-components permutations-of-three-computers)))
        names-of-permutations (map (fn [{:keys [game capture sleep media play dive total-additional-cost total-lost-cost]}]
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
                                                                      (produce-description "Play" play)
                                                                      (produce-description "Dive" dive)))))
                                   (sort total-cost-comparator all-unique-valid-pc-collections))]
    (dorun (map #(println %) names-of-permutations))
    (println (count permutations-per-cpu))
    (println (count permutations-of-three-computers))
    (println (count names-of-permutations))))
