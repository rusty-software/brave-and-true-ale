(ns brave-and-true-ale.core-test
  (:require [clojure.test :refer :all]
            [brave-and-true-ale.core :refer :all]))

(deftest error-messages-for-test
  (testing "Returns errors for missing or malformed fields"
    (is (= ["Please enter a name"] (error-messages-for "" (:name order-details-validations))))
    (is (= ["Please enter an email address"] (error-messages-for "" (:email order-details-validations))))
    (is (= ["Your email address doesn't look like an email address"] (error-messages-for "foobar.baz" (:email order-details-validations)))))
  (testing "Returns empty collection for fields that pass validation"
    (is (empty? (error-messages-for "Test Name" (:name order-details-validations))))
    (is (empty? (error-messages-for "test@name.com" (:email order-details-validations))))))
