package org.blacksmith.finlib.accounting;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.blacksmith.finlib.basic.Currency;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;

@Getter
public class Register {
  private Long id;
  private final String name;
  private final Currency currency;
  private final String referenceType;
  private final Long referenceId;
  private final Map<String,Object> keys = new HashMap<>();
  private final Map<String,Object> attributes = new HashMap<>();
  
  public Register(Long id, String name, Currency currency, String referenceType, Long referenceId) {
    this.id = id;
    this.name = name;
    this.currency = currency;
    this.referenceType = referenceType;
    this.referenceId = referenceId;    
  }
  
  public Register(String name, Currency currency, String referenceType, Long referenceId) {
    this.id = null;
    this.name = name;
    this.currency = currency;
    this.referenceType = referenceType;
    this.referenceId = referenceId;    
  }
  
  public static Register of(Long id,String name, Currency currency, String referenceType, Long referenceId) {
    return new Register(id,name,currency,referenceType,referenceId);
  }
  public static Register ofNew(String name, Currency currency, String referenceType, Long referenceId) {
    return new Register(name,currency,referenceType,referenceId);
  }
  
  public void addKey(String attributeName, Object attributeValue) {
    this.keys.put(attributeName, attributeValue);
  }

  public void addAttribute(String attributeName, Object attributeValue) {
    this.attributes.put(attributeName, attributeValue);
  }
  public Map<String,Object> getKeys() {
    return Collections.unmodifiableMap(this.keys);
  }
  public Map<String,Object> getAttributes() {
    return Collections.unmodifiableMap(this.attributes);
  }
}
