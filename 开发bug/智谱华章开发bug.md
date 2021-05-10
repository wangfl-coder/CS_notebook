# Nacos启动

sh startup.sh -m standalone

# Redis启动/关闭

redis-server redis.conf /  redis-cli shutdown

# Feign方法调用，参数为null

参数必须加上注解，比如参数不需要绑定就required=false

# Java函数参数设置默认值

只能通过函数重载完成。

# macOS升级后git 出现xcrun:error

Xcode-select --install	

# 消息队列rabbitmq

## 适用场景

1. 应用解耦：比如存在订单系统和库存系统，传统模式中，当用户下单，订单系统就会调用库存系统的API，若库存系统存在异常，就会导致订单失败，订单系统也就与库存系统耦合。应用消息队列，就能将订单消息写入消息队列，并返回下单成功，库存系统从消息队列拉取订单信息；所以即使库存系统是否异常都不影响订单系统的正常使用，即订单系统和库存系统相互解耦。
2. 异步流量：将不同业务放入消息队列，业务相互并行处理。
3. 流量削峰：常用于秒杀活动。

# spring cloud feign 启动报错 java.lang.IllegalStateException: No fallback instance of type class

解决办法：在Application加上@ComponentScan(basePackages = {"业务模块包名/org.springblade.*"})或者重新rebuild project

产生原因：@Component的fallback实例扫描不到，Spring boot默认只扫描Application同级目录，但是业务模块包名不一致

# git命令使用时，中文显示为数字

解决办法：**git config --global core.quotepath false**