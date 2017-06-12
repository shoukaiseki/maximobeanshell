作为一个2013年的产物,一直没有去发布,只有我一个人用,太可惜了.
主要写这个的文档太麻烦了,毕竟我是最讨厌写文档的
所以事例的文档相对来说丑了点

---演示1---{{{1
流程开始的时候判断,如果不为"新建"和"等待批准"时,不允许发送
成功发送将状态改为"状态1"
![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/testbshtapp001.png)

---配置方案---{{{2
开始节点的操作线新增操作
action:TESTBSH_001
DESCRIPTION:是否新建?
OBJECTNAME:TESTBSHT
TYPE:定制类
DISPVALUE:org.shoukaiseki.workflow.ActionAutoBeanShell
![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/testbsh001.png)
![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/action001.png)

然后在bsht(类在对应应用触发BeanShell脚本)中添加脚本记录
CLASS:ActionCustomClass
TABLEFIELD:TESTBSH_001
DESCRIPTION:是否新建?
SOURCE:
if(!StringExpand.binarySearch(new String[]{"新建","等待批准"},absMbo.getString("STATUS"))){
	return new MXApplicationException("#","只有状态为新建,等待批准才能发送工作流");
}
absMbo.setValue("STATUS","状态1",11);

说明:
org.shoukaiseki.workflow.ActionAutoBeanShell 操作脚本触发类中只对 CLASS:ActionCustomClass 和 TABLEFIELD 进行判断
TABLEFIELD名必须与操作名一致,而且区分大小写
![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/bshtscript001.png)


把状态改为"新建"再重新发送,发现状态名已经变为"状态1"了
![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/testbshtapp002.png)
![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/testbshtapp003.png)

---演示2---{{{1
在状态为"状态1"时,往下发必须填写"name"字段,否则不允许发送,提示  必须填写'名称'才能发送工作流
成功发送将状态改为"状态2",并将ALN01字段设置为审核人的 personid,ALN02字段设置为当前时间
![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/testbshtapp004.png)

---配置方案---{{{2
开始节点的操作线新增操作
action:TESTBSH_002
DESCRIPTION:是否新建?
OBJECTNAME:TESTBSHT
TYPE:定制类
DISPVALUE:org.shoukaiseki.workflow.ActionAutoBeanShell
![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/testbsh002.png)
![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/action002.png)

然后在bsht(类在对应应用触发BeanShell脚本)中添加脚本记录
CLASS:ActionCustomClass
TABLEFIELD:TESTBSH_002
DESCRIPTION:是否新建?
SOURCE:
if(StringExpand.binarySearch(new String[]{"状态1"},absMbo.getString("STATUS"))&&absMbo.isNull("NAME")){
	return new MXApplicationException("#","必须填写'名称'才能发送工作流");
}
absMbo.setValue("STATUS","状态2",11L);
absMbo.setValue("ALN01",absMbo.getUserInfo().getPersonId(),11L);
absMbo.setValue("ALN02", MXServer.getMXServer().getDate(),11L);

说明:
MXServer.getMXServer().getDate() 为取数据库时间
当应用于数据库不在同一台服务器时,如果时间不同步,容易引发不必要的错误,maximo中任何当前时间都是通过该方式获取到

![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/bshtscript002.png)


填写好name字段再发送
![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/testbshtapp005.png)
![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/testbshtapp006.png)






---脚本说明---{{{1
 absMbo                           主对象mbo
 aobj                             操作类中 applyCustomAction(MboRemote mbo, Object[] aobj) 的 aobj参数





---maximo.ear改动---{{{1
将src编译成class加入到maximo环境中
并将 lib/bsh-2.0b4.jar 放入maximo.ear/lib 下
然后编辑 maximo.ear/businessobjects.jar 中的 META-INF/MANIFEST.MF 添加 lib/bsh-2.0b4.jar 保存后即可




---新增域---{{{1
---记录1---{{{2
域类型:ALN
domainid:BSHTWHEN
description:BeanShell何时触发
MAXTYPE:ALN
LENGTH:10
---域值记录---{{{3
VALUE:BEFORE
DESCRIPTION:函数前触发


VALUE:AFTER
DESCRIPTION:函数后触发


---创建表---{{{1
使用 迁移sql/maximo75/table.sql 的语句创建表信息
然后进行数据库配置


---bshtcf应用新增配置---{{{1
---记录1---{{{2
类名: Mbo
描述:主对象类

---函数子表---{{{3
类名: Mbo
函数:add
描述:add方法

类名: Mbo
函数:init
描述:init方法

![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/bshtcf001.png)


---记录2---{{{2
类名: ActionCustomClass
描述:操作绑定类

无函数子表

![image](http://git.oschina.net/shoukaiseki/maximobeanshell/raw/master/image/bshtcf002.png)
