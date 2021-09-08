package com.apn.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class BlockExceptionHandler {
    public static String myHandler1(BlockException blockException){
        return "my handler --- 1";
    }
    public static String myHandler2(BlockException blockException){
        return "my handler --- 2";
    }
}
