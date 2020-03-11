package org.blacksmith.finlib.rounding;

import java.math.BigDecimal;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Test;

public class RoundPoc {
  
  public static double roundAvoid(double value, int places) {
    double scale = Math.pow(10, places);
    return Math.round(value * scale) / scale;
  }
  
  private void roundValue(double value, double expected, int precision, int fraction) {
    Rounding bsr = RoundingFactory.of(RoundingMode.UP, precision, fraction);
    Rounding bst = RoundingFactory.of(RoundingMode.DOWN, precision);
    //System.out.println("Double     rounder:" + DoubleRounder.rounding(value, precision, java.math.RoundingMode.HALF_UP));
    System.out.println("### Round         :" + value + " precision " + precision);
    System.out.println("Commons    rounder:" + Precision.round(value,precision));
    System.out.println("Blacksmith BSR    :" + bsr.round(value));
    System.out.println("Blacksmith BST    :" + bst.round(value));
  }
  
  @Test
  public void roundComparision() {
    
//    System.out.println(new BigDecimal(value));
//    System.out.println(BigDecimal.valueOf(value));
    roundValue(256.025d,256.03d,-2,0);
    roundValue(5.46497d,5.46d,2,0);
    roundValue(5.46497d,5.46d,1,0);
    roundValue(0.1,5.46d,9,0);
    double value = 0.585;
    roundValue(value,value,3,0);
    roundValue(value,value,2,0);
    roundValue(value,value,1,0);
    roundValue(value,value,3,4);
    roundValue(value,value,2,4);
    roundValue(value,value,1,4);
  }
  
  //@Test
  public void roundComparision2() {
    int digits = 80;
    final BigDecimal TWO = BigDecimal.valueOf(2);

    BigDecimal low = BigDecimal.ZERO;
    BigDecimal high = BigDecimal.ONE;

    for (int i = 0; i <= 10 * digits / 3; i++) {
        BigDecimal mid = low.add(high).divide(TWO, digits, java.math.RoundingMode.HALF_UP);
        if (mid.equals(low) || mid.equals(high))
            break;
        if (Math.round(Double.parseDouble(mid.toString())) > 0)
            high = mid;
        else
            low = mid;
    }

    System.out.println("Math.rounding(" + low + ") is " +
            Math.round(Double.parseDouble(low.toString())));
    System.out.println("Math.rounding(" + high + ") is " +
            Math.round(Double.parseDouble(high.toString())));
  }
  
  //@Test
  public void roundComparision3() {
    for (int i = 10; i >= 0; i--) {
      long l = Double.doubleToLongBits(i + 0.5);
      double x;
      do {
          x = Double.longBitsToDouble(l);
          System.out.println(x + " rounded is " + Math.round(x));
          l--;
      } while (Math.round(x) > i);
    }
  }
  
  @Test
  public void test3() {
    BigDecimal amount = new BigDecimal("100.05"); 
    //BigDecimal amount = new BigDecimal(100.05d);
    BigDecimal discountPercent = new BigDecimal("0.10"); 
    BigDecimal discount = amount.multiply(discountPercent); 
    discount = discount.setScale(2, java.math.RoundingMode.HALF_UP); 
    BigDecimal total = amount.subtract(discount);
    total = total.setScale(2, java.math.RoundingMode.HALF_UP); 
    BigDecimal taxPercent = new BigDecimal("0.05"); 
    BigDecimal tax = total.multiply(taxPercent); 
    tax = tax.setScale(2, java.math.RoundingMode.HALF_UP); 
    BigDecimal taxedTotal = total.add(tax);
    taxedTotal = taxedTotal.setScale(2, java.math.RoundingMode.HALF_UP);
    System.out.println("Subtotal : " + amount);
    System.out.println("Discount : " + discount);
    System.out.println("Total : " + total); 
    System.out.println("Tax : " + tax); 
    System.out.println("Tax+Total: " + taxedTotal); 
  } 
  
  @Test
  public void test4() {
    BigDecimal amount3 = new BigDecimal(2.15);
    BigDecimal amount4 = new BigDecimal(1.10) ;
    System.out.println("difference between is: " + (amount3.subtract(amount4)));
    BigDecimal amount5 = new BigDecimal(Double.valueOf(2.15).toString());
    BigDecimal amount6 = new BigDecimal(Double.valueOf(1.10).toString()) ;
    System.out.println("difference between is: " + (amount5.subtract(amount6)));
  }

}
