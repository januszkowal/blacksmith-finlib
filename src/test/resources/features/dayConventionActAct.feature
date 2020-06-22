Feature: Act/Act Conventions
  Scenario: ACT/ACT - 3a - semi-annual
    Given Interest accrual - frequency 6M settlement date 2003-11-01 maturity date 2008-05-01
    And For Day Convention ACT_ACT_ISMA
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 2003-11-01 | 2004-05-01 | 182  | 182/364d          |
    And For Day Convention ACT_ACT_ISDA
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 2003-11-01 | 2004-05-01 | 182  | 61/365d+121/366d  |
    And For Day Convention ACT_ACT_AFB
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 2003-11-01 | 2004-05-01 | 182  | 182/366d          |

  Scenario: ACT/ACT - 3a2 - semi-annual
    Given Interest accrual - frequency 6M settlement date 1998-11-01 maturity date 2008-05-01
    And For Day Convention ACT_ACT_ISMA
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 2003-11-01 | 2004-05-01 | 182  | 182/364d          |
    And For Day Convention ACT_ACT_ISDA
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 2003-11-01 | 2004-05-01 | 182  | 61/365d+121/366d  |
    And For Day Convention ACT_ACT_AFB
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 2003-11-01 | 2004-05-01 | 182  | 182/366d          |

  Scenario: ACT/ACT - 3b - short first calculation period
    Given Interest accrual - frequency 1Y settlement date 1999-02-01 maturity date 2000-07-01
    And For Day Convention ACT_ACT_ISMA
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 1999-02-01 | 1999-07-01 | 150  | 150/365d          |
      | 1999-07-01 | 2000-07-01 | 366  | 366/366d          |
    And For Day Convention ACT_ACT_ISDA
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 1999-02-01 | 1999-07-01 | 150  | 150/365d          |
      | 1999-07-01 | 2000-07-01 | 366  | 184/365d+182/366d |
    And For Day Convention ACT_ACT_AFB
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 1999-02-01 | 1999-07-01 | 150  | 150/365d          |
      | 1999-07-01 | 2000-07-01 | 366  | 366/366d          |

  Scenario: ACT/ACT - 3c - long first calculation period
    Given Interest accrual - frequency 6M settlement date 2002-08-15 maturity date 2004-05-01
    And For Day Convention ACT_ACT_ISMA
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 2002-08-15 | 2003-07-15 | 334  | 181/362d+153/368d |
      | 2003-07-15 | 2004-01-15 | 184  | 184/368d          |
    And For Day Convention ACT_ACT_ISDA
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 2002-08-15 | 2003-07-15 | 334  | 334/365d          |
      | 2003-07-15 | 2004-01-15 | 184  | 170/365d+14/366d  |
    And For Day Convention ACT_ACT_AFB
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 2002-08-15 | 2003-07-15 | 334  | 334/365d          |
      | 2003-07-15 | 2004-01-15 | 184  | 184/365d          |

  Scenario: ACT/ACT - 3d - short final calculation period
    Given Interest accrual - frequency 6M settlement date 1999-07-30 maturity date 2000-06-30
    And For Day Convention ACT_ACT_ISMA
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 1999-07-30 | 2000-01-30 | 184  | 184/368d          |
      | 2000-01-30 | 2000-06-30 | 152  | 152/364d          |
    And For Day Convention ACT_ACT_ISDA
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 1999-07-30 | 2000-01-30 | 184  | 155/365d+29/366d  |
      | 2000-01-30 | 2000-06-30 | 152  | 152/366d          |
    And For Day Convention ACT_ACT_AFB
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 1999-07-30 | 2000-01-30 | 184  | 184/365d          |
      | 2000-01-30 | 2000-06-30 | 152  | 152/366d          |

  Scenario: ACT/ACT - 3e - long final calculation period
    Given Interest accrual - frequency 3M settlement date 1999-11-30 maturity date 2000-05-31
    And For Day Convention ACT_ACT_ISMA
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 1999-11-30 | 2000-04-30 | 152  | 91/364d+61/368d   |
    And For Day Convention ACT_ACT_ISDA
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 1999-11-30 | 2000-04-30 | 152  | 32/365d+120/366d   |
    And For Day Convention ACT_ACT_AFB
    Then Day Convention verification
      | start      | end        | days | fraction          |
      | 1999-11-30 | 2000-04-30 | 152  | 152/366d          |
