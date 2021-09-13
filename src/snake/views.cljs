(ns snake.views
  (:require
   [re-frame.core :as re-frame]
   [snake.subs :as subs]))

(defn snake-classes [item head tail]
  (let [direction @(re-frame/subscribe [::subs/direction])]
    (cond
      (= item head) (str "snake head-" (name direction))
      (= item tail) "snake tail"
      :else "snake")))

(defn table-item [item]
  (let [snake @(re-frame/subscribe [::subs/snake])
        food @(re-frame/subscribe [::subs/food])
        is-snake? (some #(= item %) snake)
        is-food? (= item food)]
    ^{:key (str (first item) (second item))}
    (cond
      is-snake? [:td {:class (str "tile " (snake-classes item (first snake) (last snake)))}]
      is-food? [:td {:class "tile food"}]
      :else [:td {:class "tile"}])))

(defn table-row [row]
  [:tr (doall (map table-item row))])

(defn state-to-string [state]
  (case state
    :paused "Paused, Press Space to Play"
    :playing "Playing"
    :eaten-tail "Lost! You ate your tail. R key to play again."
    :hit-wall "Lost! You bumped into the wall. R key to play again."
    ""))

(defn main-panel []
  (let [board @(re-frame/subscribe [::subs/board])
        state @(re-frame/subscribe [::subs/state])
        score @(re-frame/subscribe [::subs/score])]
    [:div
     [:<>
      [:h1
       (state-to-string state)]
      [:table.table
       [:tbody
        (doall (map table-row board))]]
      [:h2
       (str "Score: " score)]]]))
