package com.c42.poc.vts.mapper;

import com.c42.poc.vts.dto.CreateVisitorRequest;
import com.c42.poc.vts.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
  User toEntity(CreateVisitorRequest dto);
}

