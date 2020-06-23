Feature: Day Count Conventions
  Scenario: ONE/ONE convention
    Given Interest coupon simple
    And For Day Convention ONE_ONE
    Then Day Convention verification
      | start      | end        | days | fraction |
      | 2019-01-01 | 2019-01-31 | 1    | 1.0      |
      | 2019-01-01 | 2019-02-01 | 1    | 1.0      |

  Scenario: ACT/360
    Given Interest coupon simple
    And For Day Convention ACT_360
    Then Day Convention verification
      | start      | end        | days | fraction |
      | 2019-01-01 | 2019-01-29 | 28   | 28/360d  |
      | 2019-01-01 | 2019-01-30 | 29   | 29/360d  |
      | 2019-01-01 | 2019-01-31 | 30   | 30/360d  |
      | 2019-01-01 | 2019-02-01 | 31   | 31/360d  |

  Scenario: ACT/364
    Given Interest coupon simple
    And For Day Convention ACT_364
    Then Day Convention verification
      | start      | end        | days | fraction |
      | 2019-01-01 | 2019-01-31 | 30   | 30/364d  |
      | 2019-01-01 | 2019-02-01 | 31   | 31/364d  |

  Scenario: ACT/365
    Given Interest coupon simple
    And For Day Convention ACT_365
    Then Day Convention verification
      | start      | end        | days | fraction |
      | 2019-01-01 | 2019-01-31 | 30   | 30/365d  |
      | 2019-01-01 | 2019-02-01 | 31   | 31/365d  |

  Scenario: ACT/365L
    Given Interest coupon simple
    And For Day Convention ACT_365L

  Scenario: ACT/365.25
    Given Interest coupon simple
    And For Day Convention ACT_365_25
    Then Day Convention verification
      | start      | end        | days | fraction    |
      | 2019-01-01 | 2019-01-31 | 30   | 30/365.25d  |
      | 2019-01-01 | 2019-02-01 | 31   | 31/365.25d  |

  Scenario: ACT/365 ACT
    Given Interest coupon simple
    And For Day Convention ACT_365_ACT

  Scenario: ACT/ACT ISDA
    Given Interest coupon simple
    And For Day Convention ACT_ACT_ISDA

  Scenario: ACT/ACT AFB
    Given Interest coupon simple
    And For Day Convention ACT_ACT_AFB

  Scenario: ACT/ACT YEAR
    Given Interest coupon simple
    And For Day Convention ACT_ACT_YEAR

  Scenario: ACT/365L
    Given Interest coupon simple
    And For Day Convention ACT_365L

  Scenario: 30/360 ISDA
    Given Interest coupon simple
    And For Day Convention D30_360_ISDA

  Scenario: 30E/360 ISDA
    Given Interest coupon simple
    And For Day Convention D30_E_360_ISDA

  Scenario: 30U/360
    Given Interest coupon simple
    And For Day Convention D30U_360

  Scenario: 30U/360 EOM
    Given Interest coupon simple
    And For Day Convention D30U_360_EOM

  Scenario: 30/360 PSA
    Given Interest coupon simple
    And For Day Convention D30_360_PSA

  Scenario: 30E/360
    Given Interest coupon simple
    And For Day Convention D30E_360

  Scenario: 30E/365
    Given Interest coupon simple
    And For Day Convention D30E_365

  Scenario: 30E+/360
    Given Interest coupon simple
    And For Day Convention D30EPLUS_360
    Then Day Convention verification
      | start      | end        | days | fraction |
      | 2018-12-30 | 2019-01-01 | 1    |  1/360d  |
      | 2018-12-31 | 2019-01-01 | 1    |  1/360d  |
      | 2018-12-31 | 2019-01-31 | 31   | 31/360d  |
      | 2019-01-01 | 2019-01-30 | 29   | 29/360d  |
      | 2019-01-01 | 2019-01-31 | 30   | 30/360d  |
      | 2019-01-01 | 2019-02-01 | 30   | 30/360d  |

  Scenario: NL/360
    Given Interest coupon simple
    And For Day Convention NL_360

  Scenario: NL/365
    Given Interest coupon simple
    And For Day Convention NL_365
