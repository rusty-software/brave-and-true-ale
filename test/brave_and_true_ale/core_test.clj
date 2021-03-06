(ns brave-and-true-ale.core-test
  (:require [clojure.test :refer :all]
            [clojure.walk :refer [macroexpand-all]]
            [brave-and-true-ale.core :refer :all]))

(deftest error-messages-for-test
  (testing "Returns errors for missing or malformed fields"
    (is (= ["Please enter a name"] (error-messages-for "" (:name order-details-validations))))
    (is (= ["Please enter an email address"] (error-messages-for "" (:email order-details-validations))))
    (is (= ["Your email address doesn't look like an email address"] (error-messages-for "foobar.baz" (:email order-details-validations)))))
  (testing "Returns empty collection for fields that pass validation"
    (is (empty? (error-messages-for "Test Name" (:name order-details-validations))))
    (is (empty? (error-messages-for "test@name.com" (:email order-details-validations))))))

(deftest validate-test
  (testing "Returns an errors collection for malformed entries"
    (is (= {:name ["Please enter a name"]
            :email ["Please enter an email address"]}
           (validate {:name "" :email ""} order-details-validations)))
    (is (= {:email ["Your email address doesn't look like an email address"]}
           (validate {:name "Test Name" :email "test.foo"} order-details-validations))))
  (testing "Returns empty for well-formed entries"
    (is (empty? (validate {:name "Test Name" :email "test@foo.bar"} order-details-validations)))))

(deftest if-valid-test
  (testing "macro is well-formed"
    (let [expanded (macroexpand
                     '(if-valid order-details order-details-validations my-error-name
                                (println :success)
                                (println :failure my-error-name)))]
      (is (=
           '(let*
              [my-error-name (brave-and-true-ale.core/validate order-details order-details-validations)]
              (if (clojure.core/empty? my-error-name)
                (println :success)
                (println :failure my-error-name)))
           expanded)))))

#_(deftest when-valid-test
  (testing "macro is well-formed"
    (is (=
          '(if (clojure.core/empty? (brave-and-true-ale.core/validate order-details order-details-validations))
             (do
               (println "It's a success!")
               (render :success)))
          (macroexpand
            '(when-valid order-details order-details-validations
                         (println "It's a success")
                         (println :success)))))))
