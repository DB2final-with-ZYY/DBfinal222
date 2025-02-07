package com.shu.mapper;

import com.shu.pojo.Major;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MajorMapper {
    /**
     * 根据id查专业
     * @param MajorId
     * @return
     */
    Major selectMajorById(int MajorId);
    
} 