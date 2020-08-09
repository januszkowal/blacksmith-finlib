package org.blacksmith.finlib.cucumber;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleSteps {
  @Given("Create sample")
  public void createSample() {

  }

  @And("Sample list {stringList}")
  public void setSampleList(List<String> stringList) {
    log.info("list: size={} value={}",stringList.size(),stringList);
  }
}
