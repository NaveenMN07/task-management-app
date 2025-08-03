package com.railse.hiring.workforcemgmt.dto;

import lombok.Data;

@Data
public class AddCommentRequest {
    private Long taskId;
    private String commenter;
    private String comment;
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
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
    
    
    
}