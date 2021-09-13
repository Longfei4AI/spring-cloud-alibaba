package com.altomni.apn.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.altomni.apn.handler.BlockExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SentinelController {

    @GetMapping("/sentinel/test-a")
    private String testA(){
        return "test A";
    }

    @GetMapping("/sentinel/test-b")
    @SentinelResource(value = "testHostKey", blockHandler = "handlerHostKey", fallback = "myFallback")
    public String testB(@RequestParam(value = "p1", required = false) String p1, @RequestParam(value = "p2", required = false) String p2){
        log.info(p1 + p2);
        return "test B";
    }
    public String handlerHostKey(String p1, String p2, BlockException blockException){

        return "hanker host key";
    }

    public String myFallback(String p1, String p2, Throwable throwable){

        return "my fallback";
    }

    @GetMapping("/sentinel/test-c")
    @SentinelResource(value = "myCustomHandler", blockHandlerClass = BlockExceptionHandler.class, blockHandler = "myHandler1")
    public String testC(){
        return "test C";
    }
}
