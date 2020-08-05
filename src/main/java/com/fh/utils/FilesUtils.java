package com.fh.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

public class FilesUtils {

 public static String fileUpload(File file) throws FileNotFoundException {

  String newFileNname = UUID.randomUUID().toString()+file.getName().substring(file.getName().lastIndexOf("."));

// Endpoint以杭州为例，其它Region请按实际情况填写。
  String endpoint = "http://oss-cn-beijing.aliyuncs.com";
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
  String accessKeyId = "LTAI4GBakurzGbzfWfjEETcz";
  String accessKeySecret = "nYm3UKuZhVgxYd6bKZWPO6dw9bugqu";
  String yourBucketName="1908a-houchaofan";
// 创建OSSClient实例。
  OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传文件流。
  InputStream inputStream = new FileInputStream(file.getPath());
  ossClient.putObject(yourBucketName, newFileNname, inputStream);

  //http://1908a-houchaofan.oss-cn-beijing.aliyuncs.com/0b308299-e6f2-45e2-9626-6d957cb0512e.jpg
  String substring = endpoint.substring(endpoint.indexOf("o"));

  String a="http://"+yourBucketName+"."+substring+"/"+newFileNname;
// 关闭OSSClient。
  ossClient.shutdown();
  return  a;
 }



 public static File readFiles(MultipartFile file) {
  int n;
  File newFile = new File(file.getOriginalFilename());
  try (InputStream in = file.getInputStream(); OutputStream os = new FileOutputStream(newFile)) {
   byte[] buffer = new byte[4096];
   while ((n = in.read(buffer, 0, 4096)) != -1) {
    os.write(buffer, 0, n);
   }
   System.out.println("获取文件成功，暂存目录" + newFile.getAbsolutePath());
  } catch (IOException e) {
   System.out.println("获取文件失败");
  }
  return newFile;
 }



}
