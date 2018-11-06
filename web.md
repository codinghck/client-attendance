# 接口说明文档

## 请假接口


### 接口说明

调用接口, 查看当前用户是否在请假时间内

### 请求方式

POST

### 请求地址

由协议加 ip 加端口号加请求路径组成, 如
http://127.0.0.1:8080/leave/is_on_leave

### 请求包体

请求示例如下
```
{
	"requestId": "EBC94144C4D0450FAB608380B15C3F1A",
	"data": {
		"time": "2018-09-03 11:23:42",
		"orgId": "330109-S000043",
		"userId": "EBC94144C4D0450FAB608380B15C3F1A"
	}
}
```

### 请求参数说明

|参数|类型|是否必须|说明|
|---|---|---|---|
|requestId|string|是|根据请求生成不重复的流水号|
|data|object|是|获取是否请假的请求对象|
|time|string|是|如果该时间在该用户的请假时间内则返回 1, 反之返回 0|
|orgId|string|是|用户所在学校的 id|
|userId|string|是|用户 id|

### 返回结果

```
{
    "code": 0,
    "msg": "",
    "tip": "",
    "result": {
        "requestId": "EBC94144C4D0450FAB608380B15C3F1A",
        "data": "0"
    }
}
```

### 返回参数说明

|参数|类型|说明|
|---|---|---|
|code|string|请求结果码, 0 为成功|
|msg|string|请求结果消息|
|tip|string|请求结果说明|
|result|object|请求结果对象|
|requestId|string|请求 id|
|data|string|请求结果, 0 表示不在请假时间内, 1 表示在请假时间内|

