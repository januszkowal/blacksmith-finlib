package org.blacksmith.finlib.basic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.xml.bind.annotation.XmlValue;
import com.fasterxml.jackson.annotation.JsonValue;
import org.blacksmith.commons.arg.Validate;

public class Amount implements Comparable<Amount>, Serializable {
  private static final long serialVersionUID = 1L;

  private final BigDecimal value;

  private static final int MAX_SCALE = 2;

  public static final Amount ZERO = new Amount(BigDecimal.ZERO);
  public static final Amount ONE = new Amount(BigDecimal.ONE);
  public static final Amount TEN = new Amount(BigDecimal.TEN);
  public static final Amount HUNDRED = new Amount(100L);

  public Amount(Amount value) {   
    Validate.notNull(value);
    this.value = value.getValue();
  }

  public Amount(BigDecimal value) {
    Validate.notNull(value);
    this.value = alignBigDecimalValue(value);
  }

  /**
   * Constructs an instance based on text representation. Format with dot will be accepted, for
   * example 1.25
   * 
   * Fraction lower than precision will be truncate.
   */
  public Amount(String value) {
    this(new BigDecimal(value));
  }

  /**
   * Constructs and instance based on Double instance.
   * 
   * Fraction lower than precision will be truncate.
   */
  public Amount(double value) {
    this(BigDecimal.valueOf(value));
  }

  public Amount(long value) {
    this(BigDecimal.valueOf(value));
  }

  public static Amount of(Amount value) {
    return new Amount(value);
  }

  public static Amount of(BigDecimal value) {
    return new Amount(value);
  }

  /**
   * Constructs an instance based on text representation. Format with dot will be accepted, for
   * example 1.25
   * 
   * Fraction lower than precision will be truncate.
   */
  public static Amount of(String value) {
    return new Amount(value);
  }

  /**
   * Constructs and instance based on Double instance.
   * 
   * Fraction lower than precision will be truncate.
   */
  public static Amount of(double value) {
    return new Amount(value);
  }

  public static Amount of(long value) {
    return new Amount(value);
  }

  private BigDecimal alignBigDecimalValue(BigDecimal value) {
    return value.setScale(MAX_SCALE, RoundingMode.HALF_UP);
  }

  public Amount copy() {
    return new Amount(this.getValue());
  }

  @XmlValue
  @JsonValue
  // @Property(policy=PojomaticPolicy.ALL)
  public BigDecimal getValue() {
    return value;
  }

  /**
   * Converts an instance to String, format with dot will be used.
   */
  /*
   * public void setValue(BigDecimal value) { setBigDecimalValue(value); } public void
   * setValue(Amount value) { setBigDecimalValue(value.toBigDecimal()); } public void
   * setValue(Double value) { setBigDecimalValue(new BigDecimal(value)); }
   */
  public Amount add(BigDecimal augend) {
    return new Amount(this.value.add(augend));
  }

  public Amount add(Amount augend) {
    return add(augend.toBigDecimal());
  }

  public Amount subtract(Amount subtrahend) {
    return subtract(subtrahend.toBigDecimal());
  }

  public Amount subtract(BigDecimal subtrahend) {
    return new Amount(this.value.subtract(subtrahend));
  }

  public Amount multiply(BigDecimal multiplicand) {
    return new Amount(this.value.multiply(multiplicand));
  }

  public Amount divide(BigDecimal divisor) {
    return new Amount(this.value.divide(divisor));
  }

  public Amount negate() {
    return new Amount(this.value.negate());
  }

  public Amount abs() {
    return new Amount(this.value.abs());
  }

  @Override
  public String toString() {
    return this.value.toPlainString();
  }

  /**
   * Converts an instance to BigDecimal instance.
   */
  public BigDecimal toBigDecimal() {
    return value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      if (obj.getClass() == BigDecimal.class) {
        return value.equals((BigDecimal) obj);
      } else {
        return false;
      }
    }
    Amount other = (Amount) obj;
    if (this.value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!this.value.equals(other.value)) {
      return false;
    }
    return true;
  }

  public int compareTo(Amount o) {
    return this.value.compareTo(o.value);
  }

  public boolean isPositiveOrZero() {
    return this.value.signum() >= 0;
  }

  public boolean isPositive() {
    return this.value.signum() > 0;
  }

  public boolean isZero() {
    return this.value.signum() == 0;
  }

  public boolean isNegative() {
    return this.value.signum() < 0;
  }

  public boolean isNegativeOrZero() {
    return value.signum() <= 0;
  }
}
