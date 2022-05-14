(ns snake.events
  (:require
   [re-frame.core :as re-frame]
   [snake.db :as db]
   [clojure.set :as s]))

(def direction-vector {:up [-1 0]
                 :down [1 0]
                 :left [0 -1]
                 :right [0 1]})

(defn add-vector [[x1 y1] [x2 y2]]
    [(+ x1 x2) (+ y1 y2)])

(def opposites [#{:left :right}
                #{:up :down}])

(defn move-in-direction [head direction]
  (add-vector head (direction direction-vector)))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 :reset
 (fn [db [_ _]]
   (db/reset-db db)))

(re-frame.core/reg-event-db
 :change-direction
 (fn [db [_ direction]]
   (let [attempt #{direction (:previous-direction db)}]
     (if (some #(s/superset? attempt %) opposites) ; Can't move in opposite direction
       db
       (assoc db :direction direction)))))

(re-frame.core/reg-event-db
 :pause
 (fn [db [_ _]]
   (assoc db :state (case (:state db)
                      :paused :playing
                      :playing :paused
                      (:state db)))))

(defn eating-self? [[head tail]]
    (some #(= % head) tail))

(defn out-of-bounds? [[x y] board-size]
    (cond
      (> x board-size) true
      (< x 1) true
      (> y board-size) true
      (< y 1) true
      :else false))

(re-frame.core/reg-event-db
 :tick
 (fn [db [_ new-time]]
   (case (:state db)
     :playing (let [snake (:snake db)
                    new-head (move-in-direction (first snake) (:direction db))
                    new-snake (cons new-head snake)
                    eating-food? (= (first new-snake) (:food db))]
                (cond
                  (eating-self? new-snake) (assoc db :state :eaten-tail)
                  (out-of-bounds? new-head (:board-size db)) (assoc db :state :hit-wall)
                  eating-food? (-> db
                                   (assoc :snake new-snake)
                                   (assoc :food (db/new-food-position new-snake (:board-size db))))
                  :else (-> db
                            (assoc :snake (drop-last new-snake))
                            (assoc :previous-direction (:direction db)))))
     db)))

(defn dispatch-timer-event
  []
  (let [now (js/Date.)]
    (re-frame/dispatch [:tick now])))

;; Call the dispatching function every second.
;; `defonce` is like `def` but it ensures only instance is ever
;; created in the face of figwheel hot-reloading of this file.
(defonce do-timer (js/setInterval dispatch-timer-event 150))



