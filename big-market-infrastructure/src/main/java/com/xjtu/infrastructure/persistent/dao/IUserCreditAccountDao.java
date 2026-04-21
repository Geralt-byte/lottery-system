package com.xjtu.infrastructure.persistent.dao;

import com.xjtu.infrastructure.persistent.po.UserCreditAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mlei@xjtu
 * @description 用户积分表
 * @create 2026/4/21 23:05
 */
@Mapper
public interface IUserCreditAccountDao {

    int updateUserCreditAccount(UserCreditAccount userCreditAccountReq);

    void insert(UserCreditAccount userCreditAccountReq);
}
