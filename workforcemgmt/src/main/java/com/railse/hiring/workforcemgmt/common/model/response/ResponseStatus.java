package com.railse.hiring.workforcemgmt.common.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseStatus {
   private Integer code;
   private String message;
   
   
   public ResponseStatus(Integer code, String message) {
	super();
	this.code = code;
	this.message = message;
}
   public Integer getCode() {
	return code;
   }
   public void setCode(Integer code) {
	this.code = code;
   }
   public String getMessage() {
	return message;
   }
   public void setMessage(String message) {
	this.message = message;
   }
   
   
   
   
}

