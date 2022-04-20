package myssm.basedao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;

public abstract class BaseDAO<T> {
    protected Connection conn;
    protected PreparedStatement psmt;
    protected ResultSet rs;

    // T 的 Class 对象
    private Class entityClass;

    public BaseDAO() {
        //getClass() 获取 Class 对象，当前我们执行的是 new FruitDAOImpl() , 创建的是 FruitDAOImpl的实例
        //那么子类构造方法内部首先会调用父类（BaseDAO）的无参构造方法
        //因此此处的 getClass() 会被执行，但是 getClass 获取的是 FruitDAOImpl 的 Class
        //所以 getGenericSuperclass() 获取到的是 BaseDAO 的 Class
        Type genericType = getClass().getGenericSuperclass();
        // ParameterizedType 参数化类型
        Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
        // 获取到的 <T> 中的 T 的真实类型
        Type actualType = actualTypeArguments[0];

        try {
            entityClass = Class.forName(actualType.getTypeName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new DAOException("BaseDAO 构造方法出错了，可能的原因是没有指定 <> 中的类型");
        }
    }

    protected Connection getConn() {
        return ConnUtil.getConn();
    }

    protected void close(ResultSet rs, PreparedStatement psmt, Connection conn) {

    }

    // 给预处理命令对象设置参数
    private void setParams(PreparedStatement psmt, Object... params) throws SQLException {
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                psmt.setObject(i + 1, params[i]);
            }
        }
    }

    // 执行更新，返回影响行数
    protected int executeUpdate(String sql, Object... params) {
        boolean insertFlag = false;
        insertFlag = sql.trim().toUpperCase().startsWith("INSERT");

        conn = getConn();
        try {
            if (insertFlag) {
                psmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                psmt = conn.prepareStatement(sql);
            }
            setParams(psmt, params);
            int count = psmt.executeUpdate();

            if (insertFlag) {
                rs = psmt.getGeneratedKeys();
                if (rs.next()) {
                    return ((Long)rs.getLong(1)).intValue();
                }
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("BaseDAO executeUpdate 出错了");
        }
    }

}
