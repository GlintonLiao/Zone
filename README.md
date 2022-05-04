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

## 好友空间

1) 根据 `id` 获取指定 `userBasic` 信息，查询这个 `userBasic` 的 `topicList`，然后覆盖 `friend` 对应的 `value`
2) main 页面应该展示 `friend` 中的 `topicList`，而不是 `userBasic` 中的 `topicList`
3) 跳转后，在左侧（`left`）中显示整个 `index` 页面
   - 问题：在 `left` 页面显示整个 `index` 布局
   - 解决：给超链接添加 `target` 属性：`target="_top"` 保证在顶层窗口显示整个 `index` 页面

4) `top.html` 页面需要修改： `欢迎进入${session.friend}`
   `top.html` 页面的返回自己空间的超链接需要修改：
   `<a th:href="@{|/user.do?operate=friend&id=${session.userBasic.id}|}" target="_top">`

## 日志详情页面实现

1) 已知 topic 的 id，需要根据 topic 的 id 获取特定 topic
2) 获取这个 topic 关联的所有的回复
3) 如果某个回复有主人回复，需要查询出来
   - 在 TopicController 中获取指定的 topic
   - 具体这个 topic 中关联多少个 Reply，由 ReplyService 内部实现
4) 获取到的 topic 中的 author 只有 id，那么需要在 topicService 的 getTopic 方法中封装，在查询 topic 本身信息时，同时调用 userBasicService 中的获取 userBasic 方法，给 author 属性赋值
5) 同理，在 reply 类中也有 author，而且这个 author 也是只有 id，那么我们也需要根据 id 查询得到 author，最后设置关联

## 删除回复

1) 如果回复有关联的主人回复，需要先删除主人回复
2) 删除回复
   ```
   Cannot delete or update a parent row: a foreign key constraint fails
   (`qqzonedb`.`t_host_reply`, CONSTRAINT `FK_host_reply` FOREIGN KEY (`reply`) REFERENCES `t_reply` (`id`))
   ```
   我们在删除回复表记录时，发现删除失败，原因是：在主人回复表中仍然有记录引用待删除的回复这条记录
   如果需要删除主表数据，需要首先删除子表数据

## 删除日志

1) 删除日志，首先需要考虑是否有关联的回复
2) 删除回复，首先需要考虑是否有关联的主人回复
3) 另外，如果不是自己的空间，则不能删除日志

## 杂项

1. 日期和字符串之间的格式化
   
   ```java
   // String -> java.util.Date
   String dateStr1 = "2021-12-30 12:59:59";
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   try {
   Date date1 = sdf.parse(dateStr1);
   } catch (ParseException e) {
   e.printStackTrace();
   }

   // Date -> String
   Date date2 = new Date();
   String dateStr2 = sdf.format(date2);
   ```

2. thymeleaf 中使用 #dates 这个公共的内置对象

   ```html
   ${#dates.format(topic.topicDate ,'yyyy-MM-dd HH:mm:ss')}
   ```

3. 系统启动时，我们访问的页面是： 
   http://localhost:8080/pro23/page.do?operate=page&page=login
   为什么不是： http://localhost:8080/pro23/login.html  ?
   答： 如果是后者，那么属于直接访问静态页面。那么页面上的 thymeleaf 表达式（标签）浏览器是不能识别的
   我们访问前者的目的其实就是要执行 ViewBaseServlet 中的 processTemplate()

4. http://localhost:8080/pro23/page.do?operate=page&page=login 访问这个URL，执行的过程是什么样的？
   
   ```
   答：
   http://  localhost   :8080   /pro23          /page.do                        ?operate=page&page=login
   协议       ServerIP   port    context root    request.getServletPath()         query string
   ```
   
   1) `DispatcherServlet -> urlPattern`:  `*.do`  拦截 `/page.do`
   2) `request.getServletPath() -> /page.do`
   3) 解析处理字符串，将 `/page.do -> page`
   4) 拿到 `page` 这个字符串，然后去 IOC 容器（`BeanFactory`）中寻找 `id=page` 的那个 bean 对象 -> PageController.java
   5) 获取 `operate` 的值 -> `page`  因此得知，应该执行 `PageController` 中的 `page()` 方法
   6) `PageController` 中的 `page` 方法定义如下：
   
      ```java
      public String page(String page) {
         return page;
      }
      ```
      
   7) 在 queryString: `?operate=page&page=login` 中获取请求参数，参数名是 page，参数值是 login
      因此 `page` 方法的参数 `page` 值会被赋上 "login"
      然后 `return "login"` , return 给谁？
   8) 因为 `PageController` 的 `page` 方法是 `DispatcherServlet` 通过反射调用的
      `method.invoke(....);`
      因此，字符串 "login" 返回给 `DispatcherServlet`
   9) `DispatcherServlet` 接收到返回值，然后处理视图
      目前处理视图的方式有两种： 
      1.带前缀 `redirect`:    
      2.不带前缀
        当前，返回 "login"，不带前缀
        那么执行 `super.processTemplate("login",request,response);`
   10) 此时 `ViewBaseServlet` 中的 `processTemplate` 方法会执行，效果是：
       + 在 "login" 这个字符串前面拼接 "/"  (其实就是配置文件中 view-prefix 配置的值)
       + 在 "login" 这个字符串后面拼接 ".html" (其实就是配置文件中 view-suffix 配置的值)
       最后进行服务器转发

## JavaWeb 整体框架

1. 拷贝 `myssm` 包
2. 新建配置文件 `applicationContext.xml` 或者可以不叫这个名字，在 `web.xml` 中指定文件名
3. 在 `web.xml` 文件中配置：
   1) 配置前缀和后缀，这样 `thymeleaf` 引擎就可以根据我们返回的字符串进行拼接，再跳转
      
      ```html
      <context-param>
      <param-name>view-prefix</param-name>
      <param-value>/</param-value>
      </context-param>
      <context-param>
      <param-name>view-suffix</param-name>
      <param-value>.html</param-value>
      </context-param>
      ```
      
   2) 配置监听器要读取的参数，目的是加载 IOC 容器的配置文件（也就是 `applicationContext.xml`）
      
      ```html
      <context-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>applicationContext.xml</param-value>
      </context-param>
      ```
      
4. 开发具体的业务模块：
   1. 一个具体的业务模块纵向上由几个部分组成：
      - html 页面
      - POJO 类
      - DAO 接口和实现类
      - Service 接口和实现类
      - Controller 控制器组件
   2. 如果 html 页面有 thymeleaf 表达式，一定不能够直接访问，必须要经过 `PageController`
   3. 在 `applicationContext.xml` 中配置 DAO、Service、Controller，以及三者之间的依赖关系
   4. DAO 实现类中，继承 `BaseDAO`，然后实现具体的接口, 需要注意，`BaseDAO` 后面的泛型不能写错。
      例如：
      ```java
        public class UserDAOImpl extends BaseDAO<User> implements UserDAO{}
      ```
   5. `Service` 是业务控制类，这一层我们只需要记住一点：
      - 业务逻辑我们都封装在 `service` 这一层，不要分散在 `Controller` 层。也不要出现在 `DAO` 层（我们需要保证 `DAO` 方法的单精度特性）
      - 当某一个业务功能需要使用其他模块的业务功能时，尽量的调用别人的 `service`，而不是深入到其他模块的 `DAO` 细节
   6. `Controller` 类的编写规则
      - 在 `applicationContext.xml` 中配置 `Controller`
         ```html
         <bean id="user" class="com.atguigu.qqzone.controllers.UserController>
         ```
      那么，用户在前端发请求时，对应的 `servletpath` 就是   /user.do   , 其中的 “user” 就是对应此处的 bean 的 id 值
      - 在 `Controller` 中设计的方法名需要和 `operate` 的值一致
      ```java
      public String login(String loginId , String pwd , HttpSession session){
         return "index";
      }
      ```
      因此，我们的登录验证的表单如下：
         ```html
         <form th:action="@{/user.do}" method="post">
         <inut type="hidden" name="operate" value="login"/>
         </form>
         ```
      ③ 在表单中，组件的 `name` 属性和 `Controller` 中方法的参数名一致
      ```
      <input type="text" name="loginId"/>
      public String login(String loginId, String pwd, HttpSession session) {
      ```
      ④ 另外，需要注意的是： 
         `Controller` 中的方法中的参数不一定都是通过请求参数获取的
      ```java
      if ("request".equals...) else if ("response".equals....) else if ("session".equals....) {
         直接赋值
      } else {
         此处才是从 request 的请求参数中获取
         request.getParameter("loginId") .....
      }
      ```
   7. `DispatcherServlet` 中步骤大致分为：
      1. 从 `application` 作用域获取 IOC 容器
      2. 解析 `servletPath`，在 IOC 容器中寻找对应的 `Controller` 组件
      3. 准备 `operate` 指定的方法所要求的参数
      4. 调用 `operate` 指定的方法
      5. 接收到执行 `operate` 指定的方法的返回值，对返回值进行处理 - 视图处理
      6. 为什么 `DispatcherServlet` 能够从 `application` 作用域获取到 IOC 容器？
   
   8. `ContextLoaderListener` 在容器启动时会执行初始化任务，而它的操作就是：
      1. 解析 IOC 的配置文件，创建一个一个的组件，并完成组件之间依赖关系的注入
      2. 将 IOC 容器保存到 `application` 作用域
