package com.xjtu.infrastructure.persistent.dao;

import com.xjtu.infrastructure.persistent.po.RaffleActivitySku;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author mlei@xjtu
 * @description IRaffleActivitySkuDao
 * @create 2026/3/24 04:28
 */
@Mapper
public interface IRaffleActivitySkuDao {
    RaffleActivitySku queryActivitySku(Long sku);

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);
}
