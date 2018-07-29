package com.medicine.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.medicine.common.Const;
import com.medicine.common.ResponseCode;
import com.medicine.common.ServerResponse;
import com.medicine.controller.portal.request.MultipartFileParam;
import com.medicine.dao.UploadFileMapper;
import com.medicine.pojo.UploadFile;
import com.medicine.pojo.User;
import com.medicine.service.IFileUploadService;
import com.medicine.util.FTPUtil;
import com.medicine.util.MultipartFileUploadUtil;
import com.medicine.vo.UploadFileListVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service("iFileUploadService")
public class FileUploadServiceImpl implements IFileUploadService {

    private Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private static AtomicLong counter = new AtomicLong(0L);

    @Autowired
    private UploadFileMapper uploadFileMapper;

    @Override
    public ServerResponse<String> upload(HttpServletRequest request, HttpSession session) {
        String prefix = "req_count:" + counter.incrementAndGet() + ":";
        System.out.println(prefix + "start !!!");
        //使用 工具类解析相关参数，工具类代码见下面
        try {
            MultipartFileParam param = MultipartFileUploadUtil.parse(request);
            System.out.println(prefix + "chunks= " + param.getChunks());
            System.out.println(prefix + "chunk= " + param.getChunk());
            System.out.println(prefix + "FileName= " + param.getFileName());

            String fileType = this.getFileType(param.getFileName());

            //这个必须与前端设定的值一致
            long chunkSize = 2048000;
            System.out.println(prefix + "isMultipart= " + param.isMultipart());
            if (param.isMultipart()) {
                String finalDirPath = "/developer/apache-tomcat-7.0.73/temp/";
                String tempDirPath = finalDirPath + param.getId();
                String tempFileName = param.getFileName() + "_tmp";
                File confFile = new File(tempDirPath, param.getFileName() + ".conf");
                File tmpDir = new File(tempDirPath);
                File tmpFile = new File(tempDirPath, tempFileName);

                if (!tmpDir.exists()) {
                    tmpDir.mkdirs();
                }

                RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");
                RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");

                long offset = chunkSize * param.getChunk();
                //定位到该分片的偏移量
                accessTmpFile.seek(offset);
                //写入该分片数据
                accessTmpFile.write(param.getFileItem().get());

                //把该分段标记为 true 表示完成
                System.out.println(prefix + "set part " + param.getChunk() + " complete");
                accessConfFile.setLength(param.getChunks());
                accessConfFile.seek(param.getChunk());
                accessConfFile.write(Byte.MAX_VALUE);

                //completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
                byte[] completeList = FileUtils.readFileToByteArray(confFile);
                byte isComplete = Byte.MAX_VALUE;
                for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
                    //与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
                    isComplete = (byte) (isComplete & completeList[i]);
                    System.out.println(prefix + "check part " + i + " complete?:" + completeList[i]);
                }

                accessTmpFile.close();
                accessConfFile.close();

                if (isComplete == Byte.MAX_VALUE) {
                    this.handleUploadedFile(tmpFile, fileType, session);
                    System.out.println(prefix + "upload complete !!");
                }
            }
            System.out.println(prefix + "end !!!");

            return ServerResponse.createBySuccess(param.getFileName() + "上传成功");
        } catch (Exception e) {
            logger.error("上传失败", e);

            return null;
        }
    }

    @Override
    public ServerResponse<PageInfo> getUploadFileList(int pageNum, int pageSize) {
        //startPage--start
        //填充自己的sql查询逻辑
        //pageHelper-收尾
        PageHelper.startPage(pageNum, pageSize);
        List<UploadFile> uploadFilesList = uploadFileMapper.selectList();

        List<UploadFileListVo> uploadFileListVoList = Lists.newArrayList();
        for (UploadFile uploadFileItem : uploadFilesList) {
            UploadFileListVo uploadFileListVo = assembleProductListVo(uploadFileItem);
            uploadFileListVoList.add(uploadFileListVo);
        }
        PageInfo pageResult = new PageInfo(uploadFilesList);
        pageResult.setList(uploadFileListVoList);

        return ServerResponse.createBySuccess(pageResult);
    }

    private UploadFileListVo assembleProductListVo(UploadFile upload) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        UploadFileListVo uploadListVo = new UploadFileListVo();
        uploadListVo.setId(upload.getId());
        uploadListVo.setUserName(upload.getUserName());
        uploadListVo.setFileName(upload.getFileName());
        uploadListVo.setPath(upload.getPath());
        uploadListVo.setSize(upload.getSize());
        uploadListVo.setCreateTime(dateFormat.format(upload.getCreateTime()));
        uploadListVo.setUpdateTime(dateFormat.format(upload.getUpdateTime()));

        return uploadListVo;
    }

    @Override
    public ServerResponse<String> ftpUpload(String fileIds) {
        List<String> fileList = Splitter.on(",").splitToList(fileIds);
        if (CollectionUtils.isEmpty(fileList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<UploadFile> uploadFileList = uploadFileMapper.getFileListByFileIds(fileList);

        for (UploadFile uploadFile : uploadFileList) {
            this.ftpUpload(uploadFile);
        }

        return ServerResponse.createBySuccess("上传成功");
    }

    private void ftpUpload(UploadFile uploadFile) {
        File localFile = new File(uploadFile.getPath());
        File targetFile = new File(localFile.getParent(), localFile.getName());
        try {
            FTPUtil.uploadFile(Lists.newArrayList(targetFile), "upload/" + this.getPathByDate());
        } catch (IOException e) {
            logger.error("ftp上传错误", e);
        }
    }

    private void handleUploadedFile(File file, String fileType, HttpSession session) {
        try {
            File uploadDir = new File(this.getNewFileDir());
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = this.getNewFileName(fileType);
            File toFile = new File(uploadDir.getPath() + "/" + fileName);
            Files.copy(file, toFile);

            String parentPath = file.getParent();

            // 删除临时文件目录
            this.deleteDir((new File(parentPath)));

            // 保存上传文件记录到mysql
            this.saveMysqlData(file, toFile, fileType, session);

        } catch (Exception e) {
            logger.error("移动文件失败", e);
        }
    }

    private String getPathByDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return dateFormat.format(date) + "/";
    }

    private String getNewFileDir() {

        return "/developer/apache-tomcat-7.0.73/uploads/" + this.getPathByDate();
    }

    private String getNewFileName(String fileType) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        int rand = (int) (Math.random() * 90 + 10);

        return dateFormat.format(date) + rand + fileType;
    }

    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = this.deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    private void saveMysqlData(File file, File toFile, String fileType, HttpSession session) {
        UploadFile uploadFile = new UploadFile();
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        String fileName = this.getFileNameExceptFileType(file.getName()) + fileType;

        uploadFile.setUserId(currentUser.getId());
        uploadFile.setFileName(fileName);
        uploadFile.setPath(toFile.getPath());
        uploadFile.setSize((int) toFile.length());

        int resultCount = uploadFileMapper.insert(uploadFile);
        if (resultCount == 0) {
            logger.error("数据插入失败");
        } else {
            logger.error("数据插入成功");
        }
    }

    private String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    private String getFileNameExceptFileType(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}
