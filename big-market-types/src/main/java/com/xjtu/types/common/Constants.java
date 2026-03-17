package com.xjtu.types.common;

public class Constants {

    /**
     * 定义标点符号常量
     */
    public final static String SPLIT = ",";
    public final static String COLON = ":";
    public final static String SPACE = " ";
    public final static String UNDERLINE = "_";

    /**
     * 定义redis中前缀标识常量
     */
    public static class RedisKey {
        public static String STRATEGY_KEY = "big_market_strategy_key_";
        public static String STRATEGY_AWARD_KEY = "big_market_strategy_award_key_";
        public static String STRATEGY_RATE_TABLE_KEY = "big_market_strategy_rate_table_key_";
        public static String STRATEGY_RATE_RANGE_KEY = "big_market_strategy_rate_range_key_";
        public static String RULE_TREE_VO_KEY = "big_market_rule_tree_vo_key_";
        public static String STRATEGY_AWARD_COUNT_KEY = "big_market_strategy_award_count_key_";
        public static String STRATEGY_AWARD_COUNT_QUERY_KEY = "big_market_strategy_award_count_query_key";
    }
}
