package com.railse.hiring.workforcemgmt.mapper;

import java.util.List;

import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;

import com.railse.hiring.workforcemgmt.dto.TaskManagementDto;
import com.railse.hiring.workforcemgmt.model.TaskManagement;


@Component
public class TaskManagementMapperImpl  implements ITaskManagementMapper{

	@Override
	public TaskManagementDto modelToDto(TaskManagement model) {
		
		return null;
	}

	@Override
	public TaskManagement dtoToModel(TaskManagementDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskManagementDto> modelListToDtoList(List<TaskManagement> models) {
		// TODO Auto-generated method stub
		return null;
	}

}
