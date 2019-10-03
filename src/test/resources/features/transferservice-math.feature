@CurrencyTransfer
Feature: Transfer Service Features
  Scenario: Conform the maths of the Transfer Api
    Given api populates the beneficiary  and the remitter with  100 currency
    When remitter sends 10 currency to remitter
    Then remitter has 90 currencies and the beneficiary has 110