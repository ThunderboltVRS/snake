(ns snake.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [snake.events :as events]
   [snake.views :as views]
   [snake.config :as config]
   [re-pressed.core :as rp]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root)
  (re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keydown"]))

(re-frame/dispatch
 [::rp/set-keydown-rules
  {:event-keys [[[:pause]
                 [{:keyCode 32}]] ; 'Space' key
                [[:change-direction :up]
                 [{:keyCode 38}]]
                [[:change-direction :left]
                 [{:keyCode 37}]]
                [[:change-direction :right]
                 [{:keyCode 39}]]
                [[:change-direction :down]
                 [{:keyCode 40}]]
                [[:reset]
                 [{:keyCode 82}]] ; 'R' key
                ]}])