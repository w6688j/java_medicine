package com.medicine.dao;

import com.medicine.pojo.UploadFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UploadFileMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UploadFile record);

    int insertSelective(UploadFile record);

    UploadFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UploadFile record);

    int updateByPrimaryKey(UploadFile record);

    List<UploadFile> selectList();

    int uploadFIleCount();

    List<UploadFile> getFileListByFileIds(@Param("fileIdList") List<String> fileIdList);
}