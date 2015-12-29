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
  "Returns a map with a vector of errors for each key in data to be validated"
  [to-validate validations]
  (reduce (fn [errors validation]
            (let [[field-name message-validator-pairs] validation
                  value (get to-validate field-name)
                  error-messages (error-messages-for value message-validator-pairs)]
              (if (empty? error-messages)
                errors
                (assoc errors field-name error-messages))))
          {}
          validations))

(defmacro if-valid
  "Handle validation more concisely"
  [to-validate validations error-name & then-else]
  `(let [~error-name (validate ~to-validate ~validations)]
     (if (empty? ~error-name)
       ~@then-else)))

(defmacro when-valid
  "when-like macro"
  [to-validate validations & when-body]
  `(when (empty? (validate ~to-validate ~validations))
     ~@when-body))

(defmacro my-or
  "macro for 'or'"
  [& args]
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
