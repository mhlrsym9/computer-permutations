(ns computer-permutations.samples
  (:use [computer-permutations.motherboards])
  (:use [computer-permutations.cases])
  (:use [computer-permutations.cpus])
  (:use [computer-permutations.thunderbolt-cards])
  (:use [computer-permutations.optane-cards])
  (:use [computer-permutations.optical-drives]))

(def game-pc-sample {:mb               asrock-z270-supercarrier-mb
                     :case             phanteks-enthoo-pro
                     :cpu              game-cpu
                     :thunderbolt-card no-thunderbolt-card
                     :optane-card      no-optane-card
                     :optical-drive    long-blu-ray})

(def capture-pc-sample {:mb               asrock-h270-pro4
                        :case             bequiet-purebase600
                        :cpu              low-power-kaby-lake-cpu
                        :thunderbolt-card asrock-thunderbolt3-card
                        :optane-card      optane-card-32GB
                        :optical-drive    long-blu-ray-3})

(def sleep-pc-sample {:mb               asrock-b150m-pro4
                      :case             phanteks-eclipse-p400s
                      :cpu              low-power-skylake-cpu
                      :thunderbolt-card no-thunderbolt-card
                      :optane-card      no-optane-card
                      :optical-drive    no-optical-drive})

(def media-pc-sample {:mb               asus-z170-ws-mb
                      :case             fractal-design-define-c
                      :cpu              high-power-kaby-lake-cpu
                      :thunderbolt-card no-thunderbolt-card
                      :optane-card      no-optane-card
                      :optical-drive    no-optical-drive})

(def play-pc-sample {:mb               gigabyte-ga-ex58-ud4p
                     :case             corsair-100r
                     :cpu              i7-920-cpu
                     :thunderbolt-card no-thunderbolt-card
                     :optane-card      no-optane-card
                     :optical-drive    long-blu-ray-2})

(def sample-pc-collection {:game    game-pc-sample
                           :capture capture-pc-sample
                           :sleep   sleep-pc-sample
                           :media   media-pc-sample
                           :play    play-pc-sample})
