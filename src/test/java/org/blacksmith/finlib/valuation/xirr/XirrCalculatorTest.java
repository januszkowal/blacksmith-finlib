package org.blacksmith.finlib.valuation.xirr;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty" },
    glue = { "org.blacksmith.finlib.cucumber" },
    features = { "src/test/resources/features/valuation/xirr.feature" }
)
public class XirrCalculatorTest {
}
