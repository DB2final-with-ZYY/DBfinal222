package com.shu.mapper;

import com.shu.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    /**
     * id查admin
     * @param id
     * @return
     */
    Admin selectById(Integer id);
} 
