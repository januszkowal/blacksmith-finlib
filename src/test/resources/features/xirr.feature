Feature: Xirr calculations
  Scenario: 1 year, 2 cashflows no growth
    Given Create schedule
      | on | amount |
      | 2010-01-01 | -1000 |
      | 2011-01-01 |  1000 |
    When Xirr calculate
    Then Xirr result must be 0.0

  Scenario: 1 year, 2 cashflows with growth
    Given Create schedule
      | on | amount |
      | 2010-01-01 | -1000 |
      | 2011-01-01 |  1100 |
    When Xirr calculate
    Then Xirr result must be 0.1
    Then Xirr result must be 0.1

  Scenario: 1 year decline #1
    Given Create schedule
      | on | amount |
      | 2010-01-01 | -1000 |
      | 2011-01-01 |   900 |
    When Xirr calculate
    Then Xirr result must be -0.1

  Scenario: 1 year decline #2
    Given Create schedule
      | on | amount |
      | 2010-01-01 |  1000 |
      | 2011-01-01 |  -900 |
    When Xirr calculate
    Then Xirr result must be -0.1

  Scenario: 1 year decline #3
    Given Create schedule
      | on | amount |
      | 2010-01-01 |  1000 |
      | 2011-01-01 |    -1 |
    When Xirr calculate
    Then Xirr result must be -0.9990000000000236

  Scenario: 1 year credit #1
    Given Create schedule
      | on | amount |
      | 2010-01-01 |  1000 |
      | 2011-01-01 | -1200 |
    When Xirr calculate
    Then Xirr result must be 0.2

  Scenario: 1 year credit #2
    Given Create schedule
      | on | amount |
      | 2010-01-01 | -1000 |
      | 2011-01-01 |  1200 |
    When Xirr calculate
    Then Xirr result must be 0.2

  Scenario: 200% growth
    Given Create schedule
      | on | amount |
      | 2010-01-01 | -1000 |
      | 2011-01-01 |  3000 |
    When Xirr calculate
    Then Xirr result must be 2.0

  Scenario: 200% loss
    Given Create schedule
      | on | amount |
      | 2010-01-01 |  1000 |
      | 2011-01-01 | -3000 |
    When Xirr calculate
    Then Xirr result must be 2.0

  Scenario: Total loss one year
    Given Create schedule
      | on | amount |
      | 2010-01-01 | -1000 |
      | 2011-01-01 |     0 |
    When Xirr calculate
    Then Xirr result must be -1

  Scenario: Total loss two years
    Given Create schedule
      | on | amount |
      | 2010-01-01 | -1000 |
      | 2012-01-01 |     0 |
    When Xirr calculate
    Then Xirr result must be -1

  Scenario: Total loss half year
    Given Create schedule
      | on | amount |
      | 2010-01-01 | -1000 |
      | 2010-07-01 |     0 |
    When Xirr calculate
    Then Xirr result must be -1

  Scenario: 4 cashflows irregular
    Given Create schedule
      | on | amount |
      | 2016-01-15 | -1000 |
      | 2016-02-08 | -2500 |
      | 2016-04-17 | -1000 |
      | 2016-08-24 |  5050 |
    When Xirr calculate
    Then Xirr result must be 0.2504234710540838

  Scenario: 18 cashflows irregular
    Given Create schedule
      | on | amount |
      | 2000-05-24 | -10000.00 |
      | 2000-06-05 |   3027.25 |
      | 2001-04-09 |    630.68 |
      | 2004-02-24 |   2018.20 |
      | 2005-03-18 |   1513.62 |
      | 2006-02-15 |   1765.89 |
      | 2007-01-10 |   4036.33 |
      | 2007-11-14 |   4036.33 |
      | 2008-12-17 |   1513.62 |
      | 2010-01-15 |   1513.62 |
      | 2011-01-14 |   2018.16 |
      | 2012-02-03 |   1513.62 |
      | 2013-01-18 |   1009.08 |
      | 2014-01-24 |   1513.62 |
      | 2015-01-30 |   1513.62 |
      | 2016-01-22 |   1765.89 |
      | 2017-01-20 |   1765.89 |
      | 2017-06-05 |  22421.55 |
    When Xirr calculate
    Then Xirr result must be 0.2126861

  Scenario: Issue 5a1
    Given Create schedule
      | on         |   amount |
      | 2001-06-22 |    -2610 |
      | 2001-07-03 |    -2589 |
      | 2001-07-05 |    -5110 |
      | 2001-07-06 |    -2550 |
      | 2001-07-09 |    -5086 |
      | 2001-07-10 |    -2561 |
      | 2001-07-12 |    -5040 |
      | 2001-07-13 |    -2552 |
      | 2001-07-16 |    -2530 |
      | 2001-07-17 |    29520 |
    When Xirr calculate
    Then Xirr result must be -0.7640294

  Scenario: Issue 5a2
    Given Create schedule
      | on         |   amount |
      | 2001-06-22 |     2610 |
      | 2001-07-03 |     2589 |
      | 2001-07-05 |     5110 |
      | 2001-07-06 |     2550 |
      | 2001-07-09 |     5086 |
      | 2001-07-10 |     2561 |
      | 2001-07-12 |     5040 |
      | 2001-07-13 |     2552 |
      | 2001-07-16 |     2530 |
      | 2001-07-17 |   -29520 |
    When Xirr calculate
    Then Xirr result must be -0.7640294

  Scenario: Issue 5b1
    Given Create schedule
      | on         |   amount |
      | 2001-06-22 |    -2610 |
      | 2001-07-03 |    -2589 |
      | 2001-07-05 |    -5110 |
      | 2001-07-06 |    -2550 |
      | 2001-07-09 |    -5086 |
      | 2001-07-10 |    -2561 |
      | 2001-07-12 |    -5040 |
      | 2001-07-13 |    -2552 |
      | 2001-07-16 |    -2530 |
      | 2001-07-17 |    -9840 |
      | 2001-07-18 |    38900 |
    When Xirr calculate
    Then Xirr result must be -0.8353404

  Scenario: Issue 5b2
    Given Create schedule
      | on         |   amount |
      | 2001-06-22 |     2610 |
      | 2001-07-03 |     2589 |
      | 2001-07-05 |     5110 |
      | 2001-07-06 |     2550 |
      | 2001-07-09 |     5086 |
      | 2001-07-10 |     2561 |
      | 2001-07-12 |     5040 |
      | 2001-07-13 |     2552 |
      | 2001-07-16 |     2530 |
      | 2001-07-17 |     9840 |
      | 2001-07-18 |   -38900 |
    When Xirr calculate
    Then Xirr result must be -0.8353404

  Scenario: Schedule in end # in>out
    Given Create schedule
      | on         |   amount |
      | 2010-01-01 |    -1000 |
      | 2010-04-01 |    -1000 |
      | 2010-07-01 |    -1000 |
      | 2010-10-01 |    -1000 |
      | 2011-01-01 |     4300 |
    When Xirr calculate
    Then Xirr result must be 0.1212676

  Scenario: Schedule out end # in<out
    Given Create schedule
      | on         |   amount |
      | 2010-01-01 |     1000 |
      | 2010-04-01 |     1000 |
      | 2010-07-01 |     1000 |
      | 2010-10-01 |     1000 |
      | 2011-01-01 |    -4300 |
    When Xirr calculate
    Then Xirr result must be 0.1212676
