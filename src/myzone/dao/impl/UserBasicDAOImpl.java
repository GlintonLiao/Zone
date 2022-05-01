package myzone.dao.impl;

import myssm.basedao.BaseDAO;
import myzone.dao.UserBasicDAO;
import myzone.pojo.UserBasic;

import java.util.List;

public class UserBasicDAOImpl extends BaseDAO<UserBasic> implements UserBasicDAO {
    @Override
    public UserBasic getUserBasic(String loginId, String pwd) {
        return super.load("SELECT * FROM t_user_basic WHERE loginId = ? AND pwd = ?", loginId, pwd);
    }

    @Override
    public List<UserBasic> getUserBasicList(UserBasic userBasic) {
        String sql = "SELECT fid as 'id' FROM t_friend WHERE uid = ?";
        return super.executeQuery(sql, userBasic.getId());
    }

    @Override
    public UserBasic getUserBasicById(Integer id) {
        return load("SELECT * FROM t_user_basic WHERE id = ?", id);
    }
}
