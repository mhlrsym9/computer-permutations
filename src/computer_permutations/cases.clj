(ns computer-permutations.cases)

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


