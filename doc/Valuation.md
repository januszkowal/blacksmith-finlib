## Valuation
### Xirr

Calculates Xirr - the internal rate of return for cash flows. One of two algorithms can be used:
- BiSection method
- Newton-Raphson method

Newton-Raphson method consumes 2-3x fewer iterations than BiSection

How to use? Look at the tests

- XirrBuilderTest.java - how to use builders
- XirrCalculatorTest.java - it uses cucumber tests definition (xirr.feature)

[Xirr valuation Source code ...](../src/main/java/org/blacksmith/finlib/valuation/xirr)

[Xirr valuation Cucumber tests ...](../src/test/resources/features/valuation)

### Discount factor

Discounted cash flow (DCF) is a valuation method used to estimate the value of an investment based on its expected future cash flows. DCF analysis attempts to figure out the value of an investment today, based on projections of how much money it will generate in the future.

[Dcf valuation Source code ...](../src/main/java/org/blacksmith/finlib/valuation/dcf)

[Dcf valuation tests ...](../src/test/java/org/blacksmith/finlib/valuation/dcf)

