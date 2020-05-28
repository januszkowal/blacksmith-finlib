package org.blacksmith.finlib.math.xirr;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"},
    glue = {"org.blacksmith.finlib.math.xirr.helper"},
    features = {"src/test/resources/features/xirr.feature"}
    )
public class XirrCalculatorTest {
}