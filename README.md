# 用户空间 Zone

## 业务需求

1. 用户登录
2. 登录成功，显示主界面，左侧好友列表，上端显示欢迎词
3. 查看日志详情
   + 日志本身的信息（作者头像、昵称、日志标题、日志内容、日志日期）
   + 回复列表（回复者头像、昵称、回复内容、回复日期）
   + 主人回复信息
4. 删除日志
5. 删除特定回复
6. 删除特定主人回复
7. 添加日志
8. 点击左侧好友链接进入对方空间

## 数据库设计

1. 抽取实体：用户登录信息、用户详细信息、日志、回帖、主人回复
2. 分析其中的属性：
    + 用户登录信息：账号、密码、头像、昵称
    + 用户详细信息：真实姓名、星座、血型、邮箱、手机号......
    + 日志：标题、内容、日期、作者
    + 主人回复：内容、日期、作者、回复
3. 分析实体之间的关系
    + 用户登录信息 ：用户详情信息    1 ：1 PK
    + 用户 ：日志                 1 ：N
    + 日志 ：回复                 1 ：N
    + 回复 ：主人回复              1 ：1 UK
    + 用户 ：好友                 M ：N

## 数据库的范式

1. 第一范式：列不可再分
2. 第二范式：一张表只表达一层含义（只描述一件事情）
3. 第三范式：表中的每一列和主键都是直接依赖关系，而不是间接依赖

## 设计范式具体选择

+ 查询频次不高的情况下，我们倾向于提高数据库的设计范式，从而提高存储效率
+ 查询频次较高的情况下，可以牺牲数据库的规范度，允许特定冗余，提高查询的性能