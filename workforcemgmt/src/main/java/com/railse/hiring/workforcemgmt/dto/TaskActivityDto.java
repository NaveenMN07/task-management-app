package com.railse.hiring.workforcemgmt.dto;

import lombok.Data;

@Data
public class TaskActivityDto {
    private String description;
    private Long timestamp;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
    
    
}
