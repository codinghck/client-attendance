# 从零开始

## 一、环境

### java 环境

(一) 打开 cmd
   * win 7
     * 开始 - 运行 - 输入 cmd 即可打开
   * win 10
     * 右键 程序 - 搜索 - 输入 cmd - 打开 命令提示符
     
(二) 输入 java -version

   如果有 java 版本的输出信息且 java 版本为 1.8, 则已经安装好了 java 环境, 否则需要安装 java 8
   
(三) java 8 的安装和配置
   
   暂时略, 如有需要, 后续补充
   
(四) 有两种部署方式可以选择: 分别为 war 包和 jar 包方式。如果按照 jar 包形式部署, 则可以跳过下面 tomcat 环境; 
   建议 war 包形式部署
   

### tomcat 环境

(一) 查看本地是否有 tomcat 安装目录, 且版本为 8, 如果没有或版本过低, 则需重新安装

(二) tomcat 8 的安装和配置
 
   1. 下载 Tomcat8 的安装包
   
      [点我到达下载地址](https://tomcat.apache.org/download-80.cgi)
      
   2. 下载相应版本的安装包, 例如(这是 64 位 windows 电脑的示例, 如果是 32 位电脑, 请下载 32 位压缩包)
   
      下载名称: apache-tomcat-8.5.32-windows-x64.zip
      
   3. 打开压缩包解压到合适位置, 例如
   
       解压地址为 D 盘, 并且文件夹重命名(重命名有时可以防止目录找不到等问题), 例如
       重命名为 Tomcat8 如: "D:\tomcat8\"
   
   4. 试启动
      
      此时双击打开 D:\tomcat8\bin 目录下的 startup.bat, 一般有两种情况: 
      
      1.提示启动成功
      ```
      Tomcat: [main] org.apache.catalina.startup.Catalina.start Server startup in 859 ms；
      ```
        
      2.提示启动 Tomcat 失败
        
      无论哪种情况都继续执行下一步骤, 彻底安装 Tomcat, 防止以后出现报错
      
   5. 配置环境变量
   
      1. 右键我的电脑 - 属性 - 高级系统设置 - 高级 - 环境变量 - 系统变量 - 新建(如果已存在, 
      则是修改该环境变量, 修改前需了解该环境是否正在被使用中, 如未被使用方可修改, 否则将)
         * 变量名: CATALINA_HOME
         * 变量值(即前面 tomcat 的安装目录, 需要按实际的配置): D:\tomcat8
         * 确定 - 确定 - 确定
         
      2.  右键我的电脑 - 属性 - 高级系统设置 - 高级 - 环境变量 - 系统变量 - Path - 编辑 - 新建
         * 输入(即 tomcat 安装目录下的 bin 目录, 需要按实际的配置): D:\tomcat8\bin
         * 确定 - 确定 - 确定
         
   6. 安装完成
   
      双击打开 D:\tomcat8\bin 目录下的 startup.bat, 提示启动了 Tomcat 后, 然后打开浏览器输入: 
      http://localhost:8080/, 便进入到了 Tomcat 服务器的首页, 双击打开 D:\tomcat8\bin 目录
      下的 shutdown.bat, 关闭 Tomcat, 安装完毕

(三) tomcat 的运行和启动

   双击打开 tomcat 安装目录下的 bin 目录下的 startup.bat, 如下提示启动成功即可
   ```
   Tomcat: [main] org.apache.catalina.startup.Catalina.start Server startup in 859 ms；
   ```
   
(四) tomcat 的重新启动

   双击打开 tomcat 安装目录下的 bin 目录下的 shutdown.bat, tomcat 关闭后, 再双击打开 tomcat 安装
   目录下的 bin 目录下的 startup.bat, 如下提示启动成功即可
   ```
   Tomcat: [main] org.apache.catalina.startup.Catalina.start Server startup in 859 ms；
   ```
   

----


## 二、程序包配置

1. 到 github 上下载或 clone 整个项目在根目录下拿到程序 war/jar 包, 或者到根目录下只下载该 war/jar 包

   [点我到达项目地址](https://github.com/hckisagoodboy/client-attendance)

2. 使用 WinRAR 修改配置文件
   1. 首先用 WinRAR 打开 .war/.jar 文件
   2. 选中需要修改的文件拖出到桌面, 需要修改的文件其目录如下
       * client-attendance
           * main  
             * resources
               * log
                 * logback.xml ---- 日志, 一般不做更改
               * map
                 * device_id_map.properties ---- 部署环境中的设备 id 和开放平台的设备 id 映射关系配置
               * tasks
                 * open_center.properties ---- 开放平台访问凭证配置
                 * attendance.properties ---- 考勤数据上传配置
                 * dept.properties ---- 组织数据上传配置
                 * user.properties ---- 用户数据上传配置
   3. 找到以上四个一般情况下需要修改的 .properties 文件后, 参照文件中注释修改配置
   4. 保存后再次放到 war 中, 配置即修改完成


----


## 三、启动运行

(一) war 包方式
  1. 打开 cmd
  
     * win 7
       * 开始 - 运行 - 输入 cmd 即可打开
     * win 10
       * 右键 程序 - 搜索 - 输入 cmd - 打开 命令提示符
       
  2. 使用以下命令进入安装的 tomcat 的 bin 目录 
 
     ```
     cd D:\tomcat8\bin
     ```
  
  3. 进入目录成功后, 输入以下命令后回车执行
  
     ```
     service.bat install
     ```
     
     出现下面提示说明设置服务成功
     
     ```
     The service ‘tomcat8’ has been installed 
     ```
     
  4. 打开 windows 服务, 找到 Apache Tomcat 的服务修改它的运行级别为自动并启动该服务
  
  5. 将 tomcat 下面 bin 目录下的 tomcat8w.exe 放到开机自动启动项的注册表里即可开机启动 tomcat
     
     开始 --> 运行 --> 输入 regedit --> 点击确定进入注册表 --> 
     打开 HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Run --> 右键"新建字符串值" --> 
     取名 tomcat8 --> 数值数据改为 tomcat8w.exe 的目录例如: D:\tomcat8\bin\tomcat8w.exe --> 
     点击确定, 关闭注册表即可
  
  有以下方式
  1. 将 war 包放到 tomcat 安装目录下的 webapps 目录下, 运行启动 tomcat 即可, 如已启动, 则可以选择重启 tomcat
  2. 使用管理页面进行, 暂时略, 如有需要, 另行补充

(二) jar 包方式
  1. 将 jar 包放到合适位置, 双击运行即可, 浏览器访问 http://localhost:8080/test/go, 
  如果有返回, 则说明部署成功
  2. 开机自启等步骤暂时略, 如有需要, 后续补充

----


## 四、运行维护

1. 运行

   如果项目成功运行, 并产生了日志文件, 检查错误日志和启动日志, 如无异常(例如没有配置文件或配置文件配置错误
   时系统会抛出异常), 则该项目启动部署成功

1. 日志

   * 日志文件目录为部署目录的上两级(例如是 tomcat 方式部署, 则为 tomcat 安装目录)下 --> logs 文件夹 --> 
   client_attendance 文件夹
   * 按需选择需要查看的日志文件后, 双击就可以使用浏览器打开查看, 如需查看更新, 刷新网页即可
