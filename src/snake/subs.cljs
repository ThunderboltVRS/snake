(ns snake.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::board-size
 (fn [db]
   (:board-size db)))

(re-frame/reg-sub
 ::food
 (fn [db]
   (:food db)))

(re-frame/reg-sub
 ::state
 (fn [db]
   (:state db)))

(re-frame/reg-sub
 ::direction
 (fn [db]
   (:direction db)))

(re-frame/reg-sub
 ::board
 (fn [db]
   (:board db)))

(re-frame/reg-sub
 ::snake
 (fn [db]
   (:snake db)))

(re-frame/reg-sub
 ::score
 (fn [db]
   (count (:snake db))))