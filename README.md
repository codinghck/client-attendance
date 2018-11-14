# 第三方考勤 技术支持文档(详细部署过程)

## 步骤一、确认程序部署环境

### 一、java 环境

(一) 打开 cmd
   * win 7
     * 开始 - 运行 - 输入 cmd 即可打开
   * win 10
     * 右键 程序 - 搜索 - 输入 cmd - 打开 命令提示符

(二) 输入 `java -version`

   如果有 java 版本的输出信息且 java 版本为 1.8, 则已经安装好了 java 环境, 否则需要安装 java 8
   
(三) java 8 的安装和配置
   1. 请选择合适的 jdk 版本下载
   2. jdk 下载地址: [点我下载](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
   3. 如下载过慢, 可以选择百度网盘下载: [地址(在环境目录下)](https://pan.baidu.com/s/1CSSGoGnVOTIAf7RCqsEA1g) 密码: taai
   4. 下载完成后双击运行
   5. 选择合适安装位置, 其他的都点下一步, 并记住安装位置如 `E:\Program Files\Java\jdk1.8.0_40`
   6. 安装完成后开始配置环境变量, 右击我的电脑, 点击属性 --> 系统高级设置
      1. 需要配置的环境变量 JAVA_HOME: 在下方的系统变量中, 查看是否已有 JAVA_HOME 环境变量. 有则修改变量值; 无
         则单击新建, 输入变量名 `JAVA_HOME`, 变量值为 jdk 的安装路径 (本例中是 `E:\Program Files\Java\jdk1.8.0_40`)
      2. 需要配置的环境变量 Path: 在下方的系统变量中, 查看是否已有 Path 环境变量. 无则单击新建, 输入变量名 `Path`,
         变量值为 `%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;` ; 如已存在, 则编辑该变量值, 将光标移动到变量值末尾,
         如果末尾是英文 `;` 号, 则在末尾添加 `%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;`, 如果末尾无英文 `;` 号,
         则在末尾添加 `;%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;` . 点击确定即可.
      3. 需要配置的环境变量 CLASSPATH: 在下方的系统变量中, 查看是否已有 CLASSPATH 环境变量. 有则修改变量值(不
         同于第二点, 这里不是在末尾追加, 而是删除原来的, 改为如下所示的新的值); 无则单击新建, 输入
         变量名 `CLASSPATH`, 变量值为 `.;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar` , 注意分号前有个点
   7. 点击确定 - 确定 - 确定, 使以上的环境变量生效
   8. 验证是否安装成功: 开始 - 运行 - 输入 cmd 打开命令行, 输入 `java -version` , 如果有 java 版本的输出信息
      且 java 版本为 1.8, 则已经安装好了 java 环境, 否则请检查以上步骤是否有误

(四) 有两种部署方式可以选择: 分别为 war 包和 jar 包方式。如果按照 jar 包形式部署, 则可以跳过下面 tomcat 环境; 
     建议 war 包形式部署, 即不要跳过下面步骤(本文档没有关于 jar 部署的详细说明哦😯)

### 二、tomcat 环境

(一) 查看本地是否有 tomcat 安装目录, 且版本为 8, 如果没有或版本过低, 则需重新安装
     建议直接在选定部署位置(如 `E:\Program Files\`)下新建部署目录, 然后在该目录下安装 tomcat8

(二) tomcat 8 的安装和配置
 
   1. 下载 Tomcat8 的安装包
   
      [点我到达下载地址](https://tomcat.apache.org/download-80.cgi)
      
      如下载过慢, 可以选择百度网盘下载: [地址(在环境目录下)](https://pan.baidu.com/s/1CSSGoGnVOTIAf7RCqsEA1g) 密码: taai
      
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
        
      2.提示启动 Tomcat 失败或命令行窗口一闪而过

      无论哪种情况都继续执行下一步骤, 彻底安装 Tomcat, 防止以后出现报错
      
   5. 配置环境变量()
      右键我的电脑 - 属性 - 高级系统设置 - 高级 - 环境变量 - 系统变量
      1. 需要配置的环境变量 CATALINA_HOME: 在下方的系统变量中, 查看是否已有 CATALINA_HOME 环境变量. 有则修
         改变量值; 无则单击新建, 输入变量名 `CATALINA_HOME`, 变量值为 tomcat8 的安装路径 (本例中是 `D:\tomcat8`)
      2. 需要配置的环境变量 Path: 在下方的系统变量中, 查看是否已有 Path 环境变量. 无则单击新建, 输入
         变量名 `Path`, 变量值为 `D:\tomcat8\bin;` ; 如已存在, 则编辑该变量值, 将光标移动到变量值末尾,
         如果末尾是英文 `;` 号, 则在末尾添加 `D:\tomcat8\bin;`, 如果末尾无英文 `;` 号,
         则在末尾添加 `;D:\tomcat8\bin;` . 点击确定即可.

   6. 安装完成
   
      双击打开安装目录下的 bin 目录(本例为 `D:\tomcat8\bin`)下的 startup.bat, 提示启动了 Tomcat 后, 即
      安装成功, 确认完毕后, 双击同目录下(本例为 `D:\tomcat8\bin`)的 shutdown.bat, 关闭 Tomcat, 安装完毕
      
   7. 将 tomcat 配置为服务
      尽管到了这一步, tomcat 已经算安装完了, 但为了能防止 tomcat 的意外关闭, 或解决不能开机启动等问题, 还需要将
      tomcat 配置成 windows 服务. 
      1. 开始 - 运行 - 输入 cmd 打开命令行窗口
      2. 使用`cd` + tomcat 的 bin 目录命令, 以在 cmd 窗口进入安装的 tomcat 的 bin 目录, 本例中的示例
         为`cd D:\tomcat8\bin`
      3. 运行命令 `service.bat install` , 出现结果 `The service ‘tomcat8’ has been installed` 则说明
         服务安装成功
      4. 打开 windows 服务, 如果上一步安装成功, 则我们可以在服务中找到 Apache Tomcat 的服务,
         修改它的运行级别为自动即可
      5. 将 tomcat 下面 bin 目录下的 tomcat8w.exe 放到开机自动启动项的注册表里即可开机启动 tomcat: 开始 --> 
         运行 --> 输入 regedit --> 点击确定进入注册表 --> 打开 
         HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Run --> 右键"新建字符串值" --> 
         取名 tomcat8 --> 数值数据改为 tomcat8w.exe 的目录（本例为 `D:\tomcat8\bin\tomcat8w.exe`） --> 
         点击确定, 关闭注册表即可. 到此, tomcat 环境已准备完毕

----


## 步骤二、程序包配置

1. 以下为部署所需 war 包或 jar 包的网盘下载地址(如链接失效或其他问题需要支持, 则请找 @技术支持 或 基础组 @洪采康)

   [点我前往下载(在部署包目录下)](https://pan.baidu.com/s/1CSSGoGnVOTIAf7RCqsEA1g) 密码: taai

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


## 步骤三、启动运行

(一) war 包方式
  1. 将步骤二中改好配置文件的 war 包放到tomcat 安装目录下的 webapps 目录下
  2. 打开 windows 服务, 找到 Apache Tomcat 的服务并启动该服务
  3. 关于是否部署成功的判断请参考步骤四

(二) jar 包方式
  1. 将 jar 包放到合适位置, 双击运行即可, 浏览器访问 http://localhost:8080/test/go, 
  如果有返回, 则说明部署成功
  2. 开机自启等等步骤暂时略, 如有需要, 后续补充

----

## 步骤四、运行维护

1. 运行
   如果项目成功运行, 并产生了日志文件, 检查错误日志和启动日志, 如无异常(例如没有配置文件或配置文件配置错误
   时系统会抛出异常, 此时就会产生 error 级别的日志); 且在浏览器访问 http://localhost:8080/test/go 时有
   成功的数据返回, 则说明该项目部署成功

2. 程序运行日志
   * 日志文件目录为部署目录的上两级, 例如是 tomcat 方式部署, 且部署在 tomcat 的 webapps 目录下, 
     则日志文件所在目录为 tomcat 安装同级目录下 --> logs 文件夹 --> client_attendance 文件夹, 如启动服务
     一会儿后仍没有产生日志, 说明程序部署可能出现问题. 
   * 按需选择需要查看的日志文件后, 双击就可以使用浏览器打开查看, 如需查看更新, 刷新网页即可

