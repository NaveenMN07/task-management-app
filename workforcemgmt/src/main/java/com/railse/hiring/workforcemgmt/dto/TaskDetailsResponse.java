package com.railse.hiring.workforcemgmt.dto;

import lombok.Data;
import java.util.List;

@Data
public class TaskDetailsResponse {
    private TaskManagementDto task;
    private List<TaskActivityDto> activities;
    private List<TaskCommentDto> comments;
	public TaskManagementDto getTask() {
		return task;
	}
	public void setTask(TaskManagementDto task) {
		this.task = task;
	}
	public List<TaskActivityDto> getActivities() {
		return activities;
	}
	public void setActivities(List<TaskActivityDto> activities) {
		this.activities = activities;
	}
	public List<TaskCommentDto> getComments() {
		return comments;
	}
	public void setComments(List<TaskCommentDto> comments) {
		this.comments = comments;
	}
    
    
}