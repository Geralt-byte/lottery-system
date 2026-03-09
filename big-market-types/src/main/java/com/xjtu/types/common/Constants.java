package com.xjtu.types.common;

public class Constants {

    /**定义标点符号常量*/
    public final static String SPLIT = ",";
    public final static String COLON = ":";
    public final static String SPACE = " ";

    /**定义redis中前缀标识常量*/
    public static class RedisKey{
        public static String STRATEGY_KEY="big_market_strategy_key_";
        public static String STRATEGY_AWARD_KEY="big_market_strategy_award_key_";
        public static String STRATEGY_RATE_TABLE_KEY="big_market_strategy_rate_table_key_";
        public static String STRATEGY_RATE_RANGE_KEY="big_market_strategy_rate_range_key_";
    }
}
