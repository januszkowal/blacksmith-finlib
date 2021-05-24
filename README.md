# org.blacksmith.finlib

Set of financial libraries

## 1. Market data

### 1.1. Fx Rates

rates/fxrates

FxRateService - service that returns FxRates. One of the important parts of FxRate is Currency Pair, which defines the quotation between two
different currencies. Currency Pair has the following attributes:

- base currency - eg. EUR, USD, JPY
- counter currency - eg. EUR, USD, JPY
- factor - defines additional factor. Some pairs, e.g. USD/JPY HUF/PLN are quoted at 100 to avoid low/high numbers
- cross flag - if true, then fx rate must be calculated using three currencies: base, counter and domestic

Moreover currency pair defines how FxRates should be stored.

#### Example 1 - the domestic currency is EUR and the following pairs and rates are defined

##### Currency pairs

|Base currency|Counter currency|Is cross|Factor|
|:---:|:---:|:---:|:---:|
|EUR|USD|No|1|
|EUR|JPY|No|1|
|EUR|HUF|No|1|
|EUR|PLN|No|1|
|USD|PLN|Yes|-|
|USD|JPY|Yes|-|
|USD|HUF|Yes|-|
|JPY|HUF|Yes|-|
|JPY|PLN|Yes|-|
|HUF|PLN|Yes|-|

#### Exchange rates

|Date|Base currency|Counter currency|Exchange rate|
|---|:---:|:---:|---:|
|2020/05/15|EUR|USD|1.215|
|2020/05/15|EUR|JPY|132.85|
|2020/05/15|EUR|HUF|348.669|
|2020/05/15|EUR|PLN|4.55|

#### Effective rates calculated using source rates and pairs

|Date|From currency|To currency|Exchange rate|Description|
|---|:---:|:---:|---:|:---:|
|2020/05/15|EUR|HUF|348.669000|direct|
|2020/05/15|EUR|JPY|132.850000|direct|
|2020/05/15|EUR|PLN|4.550000|direct|
|2020/05/15|EUR|USD|1.215000|direct|
|2020/05/15|HUF|EUR|0.002868|indirect|
|2020/05/15|HUF|JPY|0.381020|cross|
|2020/05/15|HUF|PLN|0.013050|cross|
|2020/05/15|HUF|USD|0.003485|cross|
|2020/05/15|JPY|EUR|0.007527|indirect|
|2020/05/15|JPY|HUF|2.624531|cross|
|2020/05/15|JPY|PLN|0.034249|cross|
|2020/05/15|JPY|USD|0.009146|cross|
|2020/05/15|PLN|EUR|0.219780|indirect|
|2020/05/15|PLN|HUF|76.630549|cross|
|2020/05/15|PLN|JPY|29.197802|cross|
|2020/05/15|PLN|USD|0.267033|cross|
|2020/05/15|USD|EUR|0.823045|indirect|
|2020/05/15|USD|HUF|286.970370|cross|
|2020/05/15|USD|JPY|109.341564|cross|
|2020/05/15|USD|PLN|3.744856|cross|

#### Example 2 - the domestic currency is PLN  and the following pairs and rates are defined

##### Currency pairs

|Base currency|Counter currency|Is cross|Factor|
|:---:|:---:|:---:|:---:|
|EUR|PLN|No|1|
|USD|PLN|No|1|
|JPY|PLN|No|100|
|HUF|PLN|No|100|
|EUR|USD|Yes|-|
|EUR|HUF|Yes|-|
|EUR|JPY|Yes|-|
|USD|HUF|Yes|-|
|USD|JPY|Yes|-|
|JPY|HUF|Yes|-|

#### Effective rates calculated using source rates and pairs
|Date|From currency|To currency|Exchange rate|Description|
|---|:---:|:---:|---:|:---:|
|2021-05-15|EUR|HUF|353.700249|cross|
|2021-05-15|EUR|JPY|132.846715|cross|
|2021-05-15|EUR|PLN|4.550000|direct|
|2021-05-15|EUR|USD|1.213333|cross|
|2021-05-15|HUF|EUR|0.002827|cross|
|2021-05-15|HUF|JPY|0.375591|cross|
|2021-05-15|HUF|PLN|0.012864|direct|
|2021-05-15|HUF|USD|0.003430|cross|
|2021-05-15|JPY|EUR|0.007527|cross|
|2021-05-15|JPY|HUF|2.662469|cross|
|2021-05-15|JPY|PLN|0.034250|direct|
|2021-05-15|JPY|USD|0.009133|cross|
|2021-05-15|PLN|EUR|0.219780|indirect|
|2021-05-15|PLN|HUF|77.736318|indirect|
|2021-05-15|PLN|USD|0.266667|indirect|
|2021-05-15|PLN|JPY|29.197080|indirect|
|2021-05-15|USD|EUR|0.824176|cross|
|2021-05-15|USD|HUF|291.511194|cross|
|2021-05-15|USD|PLN|3.750000|direct|
|2021-05-15|USD|JPY| 109.489051|cross|
### 2. Xirr

math/xirr

Calculates Xirr - the internal rate of return for cash flows. One of two methods can be used:

- BiSection method
- Newton-Raphson method

Newton-Raphson method requires 2-3x fewer iterations than BiSection

How to use? Look at the tests

- XirrBuilderTest.java - how to use builders
- XirrCalculatorTest.java - it uses cucumber tests definition (xirr.feature)

### 3. Interest calculation

Calculates interest fraction between two dates double InterestBasis.yearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo
scheduleInfo)

