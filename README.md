# org.blacksmith.finlib
Set of financia libraries

### 1.1 Xirr
math/xirr

Calculates Xirr - the internal rate of return for cash flows
One of two methods can be used:
- BiSection method
- Newton-Raphson method

Newton-Raphson method requires 2-3x fewer iterations than BiSection

How to use? Look at the tests
- XirrBuilderTest.java - how to use builders
- XirrCalculatorTest.java - it uses cucumber tests definition (xirr.feature)

### 1.2 Interest calculation
Calculates interest fraction between two dates
double InterestBasis.yearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo)

