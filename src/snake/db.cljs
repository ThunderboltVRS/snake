(ns snake.db)

(defn place [row column]
  [row column])

(defn places [row columns]
  (map #(place row %) columns))

(defn new-food-position [snake board-size]
  (let [food (->> [(rand-int board-size) (rand-int board-size)]
                  (mapv inc))] ; not zero-indexed
    (if (some #(= food %) snake)
      (new-food-position snake board-size)
      food)))

(def default-db
  (let [board-size 18
        rows (range 1 (+ 1 board-size))
        columns (range 1 (+ 1 board-size))
        board (->> rows 
                   (map #(places % columns)))
        snake '([5 6] [5 5] [5 4] [5 3] [5 2])]
  {:food (new-food-position snake board-size)
   :board board
   :snake snake
   :direction :right
   :previous-direction :right
   :board-size board-size
   :state :paused}))

(defn reset-db[db]
  (merge db default-db))
