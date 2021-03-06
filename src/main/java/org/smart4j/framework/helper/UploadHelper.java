package org.smart4j.framework.helper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.bean.FileParam;
import org.smart4j.framework.bean.FormParam;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.FileUtil;
import org.smart4j.framework.util.StreamUtil;
import org.smart4j.framework.util.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: UploadHelper
 * @description: 文件上传助手类
 * @author: Created by Autumn
 * @create: 2018-12-14 16:21
 */
public final class UploadHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);

    /**
     * Apache Commons FileUpload提供的Servlet文件上传对象
     */
    private static ServletFileUpload servletFileUpload;

    /**
     * 初始化
     */
    public static void init(ServletContext servletContext){
        /*获取tomcat的work目录*/
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");

        /**
         * DiskFileItemFactory构造的两个参数
         *  第一个参数：sizeThreadHold - 设置缓存(内存)保存多少字节数据，默认为10240字节，即10K
         *    如果一个文件没有大于10K，则直接使用内存直接保存成文件就可以了。
         *    如果一个文件大于10K，就需要将文件先保存到临时目录中去。
         *  第二个参数 File 是指临时目录位置 - 可以不用tomcat的work目录可以用任意一个目录
         */
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository);
        servletFileUpload = new ServletFileUpload(fileItemFactory);

        int uploadLimit = ConfigHelper.getAppUploadLimit();  //获取文件上传限制默认为10（M）
        if (uploadLimit != 0){
            servletFileUpload.setFileSizeMax(uploadLimit*1024*1024);   //设置单文件最大大小为10M
        }
    }

    /**
     * 判断请求是否为multipart类型
     */
    public static boolean isMultipart(HttpServletRequest request){
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * 创建请求对象
     * 将request转换为Param参数
     * @return
     */
    public static Param createParam(HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<FormParam>();
        List<FileParam> fileParamList = new ArrayList<FileParam>();

        try{
            /*解析request*/
            Map<String,List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);   //将request转换为Map
            if (CollectionUtil.isNotEmpty(fileItemListMap)){
                //遍历Map集合，一个表单名可能有多个文件
                for (Map.Entry<String,List<FileItem>> fileItemListEntry : fileItemListMap.entrySet()){
                    String fieldName = fileItemListEntry.getKey();    //获取表单字段名
                    List<FileItem> fileItemList = fileItemListEntry.getValue();   //文件集合

                    if (CollectionUtil.isNotEmpty(fileItemListMap)){
                        for (FileItem fileItem:fileItemList){   //遍历文件集合
                            if (fileItem.isFormField()){   //如果是表单字段
                                String fieldValue = fileItem.getString("UTF-8");
                                formParamList.add(new FormParam(fieldName,fieldValue));
                            }else{   //如果是文件
                                String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(),"UTF-8"));   //获取文件名
                                if (StringUtil.isNotEmpty(fileName)){  //如果文件名不为空
                                    long fileSize = fileItem.getSize();  //获取文件大小
                                    String contentType = fileItem.getContentType();   //获取文件类型
                                    InputStream inputStream = fileItem.getInputStream();   //获取文件输入流
                                    fileParamList.add(new FileParam(fieldName,fileName,fileSize,contentType,inputStream));
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            LOGGER.error("create param failure",e);
            throw new RuntimeException(e);
        }
        return new Param(formParamList,fileParamList);
    }


    /**
     * 上传文件
     * @param basePath
     * @param fileParam
     */
    public static void uploadFile(String basePath,FileParam fileParam){
        try{
            if (fileParam != null){
                String filePath = basePath + fileParam.getFileName();   //路径+文件名
                FileUtil.createFile(filePath);  //创建文件
                InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());  //获取文件的输入流
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));   //获取输出流
                StreamUtil.copyStream(inputStream,outputStream);   //输入流拷贝到输出流中
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("upload file failure",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量上传文件
     * @param basePath
     * @param fileParamList
     */
    public static void uploadFile(String basePath,List<FileParam> fileParamList){
        try {
            if (CollectionUtil.isNotEmpty(fileParamList)){
                for (FileParam fileParam : fileParamList){
                    uploadFile(basePath,fileParam);
                }
            }
        }catch (Exception e){
            LOGGER.error("upload file failure",e);
            throw new RuntimeException(e);
        }

    }
}
