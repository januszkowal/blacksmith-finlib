Feature: Act/Act Isma Day Count Conventions
  Scenario: ACT/ACT ISMA - simple 4Y/1Y
    Given Day Convention is ACT_ACT_ISMA
    And Schedule parameters frequency 1Y settlement date 2019-03-01 maturity date 2023-03-01
    Then Day Convention verification
      | start      | end        | cend       | days | fraction    |
      | 2019-03-01 | 2020-03-01 | 2020-03-01 | 366  | 1.0         |
      | 2020-03-01 | 2021-03-01 | 2021-03-01 | 365  | 1.0         |
      | 2021-03-01 | 2022-03-01 | 2022-03-01 | 365  | 1.0         |
      | 2022-03-01 | 2023-03-01 | 2023-03-01 | 365  | 1.0         |

  Scenario: ACT/ACT ISMA - simple 4Y/6M
    Given Day Convention is ACT_ACT_ISMA
    And Schedule parameters frequency 6M settlement date 2019-03-01 maturity date 2023-03-01
    Then Day Convention verification
      | start      | end        | cend       | days | fraction           |
      #start 0d/1Y
      | 2019-03-01 | 2019-03-01 | 2020-03-01 | 0    | 0d                 |
      #start 1d/1Y
      | 2019-03-01 | 2019-03-02 | 2020-03-01 | 1    | 1/368d             |
      #start 1Y/1Y
      | 2019-03-01 | 2020-03-01 | 2020-03-01 | 366  | 182/364d+184/368d  |
      #start 3M/1Y
      | 2019-03-01 | 2019-06-01 | 2019-06-01 | 92   | 92/364d            |
      #start 6M/1Y
      | 2019-03-01 | 2019-09-01 | 2020-03-01 | 184  | 184/368d           |
      #start 8M/1Y
      | 2019-03-01 | 2019-11-01 | 2020-03-01 | 245  | 61/364d+184/368d   |
      #start <1Y/1Y
      | 2019-03-01 | 2020-02-29 | 2020-03-01 | 365  | 181/364d+184/368d  |
      #start 0d/2M
      | 2019-03-01 | 2019-03-01 | 2019-05-01 | 0    | 0d                 |
      #start 1d/2M
      | 2019-03-01 | 2019-03-02 | 2019-05-01 | 1    | 1/362d             |
      #start 38d/2M
      | 2019-03-01 | 2019-04-08 | 2019-05-01 | 38   | 38/362d            |
      #start 122d/2M
      | 2019-03-01 | 2019-05-01 | 2019-05-01 | 61   | 61/362d            |
