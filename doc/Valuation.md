## Valuation
### Xirr

[Source code see here](../src/main/java/org/blacksmith/finlib/valuation/xirr)

Calculates Xirr - the internal rate of return for cash flows. One of two algorithms can be used:
- BiSection method
- Newton-Raphson method

Newton-Raphson method consumes 2-3x fewer iterations than BiSection

How to use? Look at the tests

- XirrBuilderTest.java - how to use builders
- XirrCalculatorTest.java - it uses cucumber tests definition (xirr.feature)

### Discount factor
