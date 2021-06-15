## Valuation
### Irr (Xirr)

The internal rate of return (IRR) is a metric used in financial analysis to estimate the profitability of potential investments. IRR is a discount rate that makes the net present value (NPV) of all cash flows equal to zero in a discounted cash flow analysis.

IRR calculations rely on the same formula as NPV does. Keep in mind that IRR is not the actual money value of the project. It is the annual return that makes the NPV equal to zero.

Generally speaking, the higher an internal rate of return, the more desirable an investment is to undertake. IRR is uniform for investments of varying types and, as such, can be used to rank multiple prospective investments or projects on a relatively even basis. In general, when comparing investment options with other similar characteristics, the investment with the highest IRR probably would be considered the best.

Two algorithms are available:
- BiSection method
- Newton-Raphson method

Newton-Raphson method consumes 2-3x fewer iterations than BiSection

[Xirr valuation Source code ...](../src/main/java/org/blacksmith/finlib/valuation/xirr)

[Xirr valuation Cucumber tests ...](../src/test/resources/features/valuation)

### Discount factor

Discounted cash flow (DCF) is a valuation method used to estimate the value of an investment based on its expected future cash flows. DCF analysis attempts to figure out the value of an investment today, based on projections of how much money it will generate in the future.

[Dcf valuation Source code ...](../src/main/java/org/blacksmith/finlib/valuation/dcf)

[Dcf valuation tests ...](../src/test/java/org/blacksmith/finlib/valuation/dcf)

