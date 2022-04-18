package myssm.basedao;

import java.sql.*;

public abstract class BaseDAO<T> {
    protected Connection conn;
    protected PreparedStatement psmt;
    protected ResultSet rs;

    // T 的 Class 对象
    private Class entityClass;

    public BaseDAO() {

    }

}
