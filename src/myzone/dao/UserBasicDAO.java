package myzone.dao;

import myzone.pojo.UserBasic;

import java.util.List;

public interface UserBasicDAO {
    // 根据账号密码获取特定用户信息
    public UserBasic getUserBasic(String loginId, String pwd);
    // 获取指定用户的所有好友列表
    public List<UserBasic> getUserBasicList(UserBasic userBasic);
    // 根据 id 查询 UserBasic 的信息
    UserBasic getUserBasicById(Integer id);
}
