package com.railse.hiring.workforcemgmt.dto;

import lombok.Data;

@Data
public class TaskCommentDto {
    private String commenter;
    private String comment;
    private Long timestamp;
	public String getCommenter() {
		return commenter;
	}
	public void setCommenter(String commenter) {
		this.commenter = commenter;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
    
    
}