## 设计思路

### 使用者

- 引入自定义的框架jar
- 编写配置文件
  1. dbconfig.xml：数据库配置信息，存放mapper.xml位置
  2. mapper.xml：sql配置信息

### 框架

- 加载配置文件：以字节流存储在内存中
  - 创建Resources类：InputStrean getResourceAsStream(String path)
- 创建两个Bean：容器对象，存放配置文件解析结果
  - Configuration：核心配置类，dbconfig.xml解析结果
  - MapperStatement：映射配置类，mapper.xml解析结果
- 解析配置文件：dom4j
  - 创建SqlSessionFactoryBuilder：build(InputStream in)
  - 使用dom4j解析配置文件，将结果存放在容器对象
  - 创建SqlSessionFactory对象：生产SqlSession会话对象（工厂模式）
- 创建SqlSessionFactory接口及实现类DefaultSqlSessionFactory
  - openSession()：生产sqlSession
- 创建SqlSession接口及实现类DefaultSqlSession
  - 定义crud操作
    - selectList()
    - selectOne()
    - update()
    - delete()
- 创建Executor接口及实现类SimpleExecutor
  - query(Configuration, MapperStatement, Object... params)：执行JDBC代码