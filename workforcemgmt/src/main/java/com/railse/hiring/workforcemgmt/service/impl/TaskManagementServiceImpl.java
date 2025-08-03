package com.railse.hiring.workforcemgmt.service.impl;
import com.railse.hiring.workforcemgmt.common.exception.ResourceNotFoundException;
import com.railse.hiring.workforcemgmt.dto.*;
import com.railse.hiring.workforcemgmt.mapper.ITaskManagementMapper;
import com.railse.hiring.workforcemgmt.model.TaskActivity;
import com.railse.hiring.workforcemgmt.model.TaskComment;
import com.railse.hiring.workforcemgmt.model.TaskManagement;
import com.railse.hiring.workforcemgmt.model.enums.Priority;
import com.railse.hiring.workforcemgmt.model.enums.Task;
import com.railse.hiring.workforcemgmt.model.enums.TaskStatus;
import com.railse.hiring.workforcemgmt.repository.TaskRepository;
import com.railse.hiring.workforcemgmt.service.TaskManagementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TaskManagementServiceImpl implements TaskManagementService {

  
   private final TaskRepository taskRepository;
   
   
   private final ITaskManagementMapper taskMapper;

   private boolean isActive(TaskManagement task) {
	    return task.getStatus() == TaskStatus.ASSIGNED || task.getStatus() == TaskStatus.STARTED;
	}


   public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper) {
       this.taskRepository = taskRepository;
       this.taskMapper = taskMapper;
   }
   
   @Override
   public TaskManagementDto findTaskById(Long id) {
       TaskManagement task = taskRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
       return taskMapper.modelToDto(task);
   }


   @Override
   public List<TaskManagementDto> createTasks(TaskCreateRequest createRequest) {
       List<TaskManagement> createdTasks = new ArrayList<>();
       for (TaskCreateRequest.RequestItem item : createRequest.getRequests()) {
           TaskManagement newTask = new TaskManagement();
           newTask.setReferenceId(item.getReferenceId());
           newTask.setReferenceType(item.getReferenceType());
           newTask.setTask(item.getTask());
           newTask.setAssigneeId(item.getAssigneeId());
           newTask.setPriority(item.getPriority());
           newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
           newTask.setStatus(TaskStatus.ASSIGNED);
           newTask.setDescription("New task created.");
           createdTasks.add(taskRepository.save(newTask));
       }
       return taskMapper.modelListToDtoList(createdTasks);
   }


   @Override
   public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
       List<TaskManagement> updatedTasks = new ArrayList<>();
       for (UpdateTaskRequest.RequestItem item : 
    	   
    	   updateRequest.getRequests()) {
           TaskManagement task = taskRepository.findById(item.getTaskId())
                   .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));


           if (item.getTaskStatus() != null) {
               task.setStatus(item.getTaskStatus());
           }
           if (item.getDescription() != null) {
               task.setDescription(item.getDescription());
           }
           updatedTasks.add(taskRepository.save(task));
       }
       return taskMapper.modelListToDtoList(updatedTasks);
   }


   @Override
   public String assignByReference(AssignByReferenceRequest request) {
       List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
       List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());


       for (Task taskType : applicableTasks) {
           List<TaskManagement> tasksOfType = existingTasks.stream()
                   .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED)
                   .collect(Collectors.toList());


           // BUG #1 is here. It should assign one and cancel the rest.
           // Instead, it reassigns ALL of them.
 
          
        	    // Cancel all existing tasks of this type (except already CANCELLED/COMPLETED)
        	    for (TaskManagement existingTask : tasksOfType) {
        	        if (existingTask.getStatus() != TaskStatus.CANCELLED && existingTask.getStatus() != TaskStatus.COMPLETED) {
        	            existingTask.setStatus(TaskStatus.CANCELLED);
        	            taskRepository.save(existingTask);
        	        }
        	    }
        	

        	// Always create a new task for the new assignee
        	TaskManagement newTask = new TaskManagement();
        	newTask.setReferenceId(request.getReferenceId());
        	newTask.setReferenceType(request.getReferenceType());
        	newTask.setTask(taskType);
        	newTask.setAssigneeId(request.getAssigneeId());
        	newTask.setStatus(TaskStatus.ASSIGNED);
//        	newTask.setPriority(Priority.MEDIUM); // Default priority (or make it part of request if needed)
        	newTask.setDescription("Task reassigned.");
        	newTask.setTaskDeadlineTime(System.currentTimeMillis() + 86400000); // 1 day from now
        	taskRepository.save(newTask);

           
           
            
           }
       
       return "Tasks assigned successfully for reference " + request.getReferenceId();
   }


   @Override
   public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
       List<TaskManagement> allTasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());

       
       long start = request.getStartDate();
       long end = request.getEndDate();
       
       
      

       
       List<TaskManagement> filteredTasks = allTasks.stream()
    	        .filter(task ->
    	            task.getStatus() != TaskStatus.CANCELLED &&
    	            task.getStatus() != TaskStatus.COMPLETED &&
    	            (
    	                (task.getTaskDeadlineTime() >= start && task.getTaskDeadlineTime() <= end) // active tasks within range
    	                ||
    	                (task.getTaskDeadlineTime() < start) // started earlier, still pending
    	            )
    	        )
    	        .collect(Collectors.toList());

       return taskMapper.modelListToDtoList(filteredTasks);
   }
   
   
   @Override
   public TaskManagementDto updatePriority(UpdatePriorityRequest request) {
       TaskManagement task = taskRepository.findById(request.getTaskId())
           .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + request.getTaskId()));

       task.setPriority(request.getNewPriority());
       TaskManagement updated = taskRepository.save(task);
       return taskMapper.modelToDto(updated);
   }

   @Override
   public List<TaskManagementDto> fetchTasksByPriority(Priority priority) {
       List<TaskManagement> allTasks = taskRepository.findAll();

       List<TaskManagement> filtered = allTasks.stream()
           .filter(task -> task.getPriority() == priority)
           .collect(Collectors.toList());

       return taskMapper.modelListToDtoList(filtered);
   }

   @Override
   public TaskDetailsResponse findTaskWithHistory(Long id) {
       TaskManagement task = taskRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

       List<TaskActivityDto> activities = taskRepository.findActivityByTaskId(id).stream()
               .map(activity -> {
                   TaskActivityDto dto = new TaskActivityDto();
                   dto.setDescription(activity.getDescription());
                   dto.setTimestamp(activity.getTimestamp());
                   return dto;
               }).collect(Collectors.toList());

       List<TaskCommentDto> comments = taskRepository.findCommentsByTaskId(id).stream()
               .map(comment -> {
                   TaskCommentDto dto = new TaskCommentDto();
                   dto.setComment(comment.getComment());
                   dto.setCommenter(comment.getCommenter());
                   dto.setTimestamp(comment.getTimestamp());
                   return dto;
               }).collect(Collectors.toList());

       TaskDetailsResponse response = new TaskDetailsResponse();
       response.setTask(taskMapper.modelToDto(task));
       response.setActivities(activities);
       response.setComments(comments);

       return response;
   }

   @Override
   public TaskCommentDto addComment(AddCommentRequest request) {
       TaskManagement task = taskRepository.findById(request.getTaskId())
               .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + request.getTaskId()));

       TaskComment comment = new TaskComment();
       comment.setTaskId(request.getTaskId());
       comment.setComment(request.getComment());
       comment.setCommenter(request.getCommenter());
       comment.setTimestamp(System.currentTimeMillis());
       taskRepository.saveComment(comment);

       TaskCommentDto dto = new TaskCommentDto();
       dto.setComment(comment.getComment());
       dto.setCommenter(comment.getCommenter());
       dto.setTimestamp(comment.getTimestamp());
       return dto;
   }

   
   private void logActivity(Long taskId, String description) {
	    TaskActivity activity = new TaskActivity();
	    activity.setTaskId(taskId);
	    activity.setDescription(description);
	    activity.setTimestamp(System.currentTimeMillis());
	    taskRepository.saveActivity(activity);
	}


}


