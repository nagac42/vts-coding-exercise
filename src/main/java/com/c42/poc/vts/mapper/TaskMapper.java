package com.c42.poc.vts.mapper;

import com.c42.poc.vts.dto.CreateTaskRequest;
import com.c42.poc.vts.dto.CreateVisitorRequest;
import com.c42.poc.vts.entity.Task;
import com.c42.poc.vts.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
  Task toEntity(CreateTaskRequest dto);
}

