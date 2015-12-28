(ns brave-and-true-ale.core
  (:gen-class))

(def order-details-validations
  {:name ["Please enter a name" not-empty]
   :email ["Please enter an email address" not-empty
           "Your email address doesn't look like an email address" #(or (empty? %) (re-seq #"@" %))]})

(defn error-messages-for
  "Returns a seq of error messages"
  [value message-validator-pairs]
  (map first (filter #(not ((second %) value))
                     (partition 2 message-validator-pairs))))

(defn validate
  "Returns a map with a vector of errors for each key in an order detail"
  [order-detail validations]
  (reduce (fn [errors validation]
            (let [[field-name message-validator-pairs] validation
                  value (get order-detail field-name)
                  error-messages (error-messages-for value message-validator-pairs)]
              (if (empty? error-messages)
                errors
                (assoc errors field-name error-messages))))
          {}
          validations))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
