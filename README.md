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
