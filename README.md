# 第三方考勤项目


## 一、运行环境

1. jdk 8

   **说明**: 8 以下不能运行
   
2. tomcat 8

   **说明**: 8 以下不能运行

----

## 二、日志

1. 日志文件存储目录

   日志会存储到 tomcat 安装目录下的 logs 文件夹中的 client_attendance 文件夹
   
----

## 三、配置

1. 配置文件所在目录

  > 注：文件中有注释说明，需要改动部分参照注释修改即可。配置文件目录结构如下
   * client-attendance
     * main  
       * resources
         * log
           * logback.xml ---- 日志，一般不做更改
         * map
           * device_id_map.properties ---- 部署环境中的设备 id 和开放平台的设备 id 映射关系配置       
         * tasks
           * open_center.properties ---- 开放平台访问凭证配置
           * attendance.properties ---- 考勤数据上传配置
           * dept.properties ---- 组织数据上传配置
           * user.properties ---- 用户数据上传配置
    

local-dir 这个选项填写配置存放的路径，必须存在这个文件夹才行

----

## 四、部署

   打包后部署运行即可


