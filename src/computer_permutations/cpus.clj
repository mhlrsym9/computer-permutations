(ns computer-permutations.cpus
  (:use computer-permutations.motherboards))

(def game-cpu {:name "i7-7700K" :socket 1151 :licensed-to asus-z170-ws-mb-name :optane? true :lost-cost (* 299.99 1.0625)})
(def game-cpu-name (:name game-cpu))

(def high-power-kaby-lake-cpu {:name "i5-7500" :socket 1151 :licensed-to asrock-z270m-extreme4-mb-name :optane? true :lost-cost (* 179.99 1.0625)})
(def high-power-kaby-lake-cpu-name (:name high-power-kaby-lake-cpu))

(def low-power-kaby-lake-cpu {:name "i5-7400t" :socket 1151 :optane? true :lost-cost 135.87})
(def low-power-kaby-lake-cpu-name (:name low-power-kaby-lake-cpu))

(def low-power-skylake-cpu {:name "i5-6500t" :socket 1151 :optane? false :lost-cost (+ 107.50 6.65)})
(def low-power-skylake-cpu-name (:name low-power-skylake-cpu))

(def i7-920-cpu {:name "i7-920" :socket 1366 :licensed-to gigabyte-ga-ex58-ud4p-name :optane? false :lost-cost 0})
(def i7-920-cpu-name (:name i7-920-cpu))

(def core-2-duo-cpu {:name "Core 2 Duo E8400" :socket 775 :licensed-to core-2-duo-mb-name :optane? false :lost-cost 0})
(def core-2-duo-cpu-name (:name core-2-duo-cpu))

(def core-2-quad-cpu {:name "Intel Core 2 Quad Q9500" :socket 775 :licensed-to core-2-quad-mb-name :optane? false :lost-cost 0})
(def core-2-quad-cpu-name (:name core-2-quad-cpu))

(def cpus (list game-cpu high-power-kaby-lake-cpu low-power-kaby-lake-cpu low-power-skylake-cpu i7-920-cpu
                ; core-2-duo-cpu core-2-quad-cpu
                ))


