package com.markety.platform.mapper;


import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper {

    boolean existsByUserName(String userName);
}
