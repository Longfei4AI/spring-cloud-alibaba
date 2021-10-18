package com.altomni.apn.talent.web.rest;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.altomni.apn.talent.handler.BlockExceptionHandler;
import com.altomni.apn.talent.service.TalentService;
import com.altomni.apn.talent.service.dto.TalentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/api/v3")
public class TalentResourceTest {

    @Resource
    private TalentService candidateService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/test")
    public ResponseEntity<TalentDTO> test(){
        log.info("request to get Candidate");
        //UserDTO userDTO = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(principal.toString());
        TalentDTO candidateDTO = candidateService.findOne(1L).get();
        log.info(candidateDTO.toString());
        return ResponseEntity.ok(candidateDTO);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<TalentDTO> saveTest(){
        log.info("request to save Candidate");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(principal.toString());
        return ResponseEntity.ok(candidateService.save(new TalentDTO(null, "Tom", 18, 1)));
    }

    @GetMapping("/test-a")
    private String testA(){
        return "test A";
    }

    @GetMapping("/test-b")
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

    @GetMapping("/test-c")
    @SentinelResource(value = "myCustomHandler", blockHandlerClass = BlockExceptionHandler.class, blockHandler = "myHandler1")
    public String testC(){
        return "test C";
    }

    @GetMapping("/test-mq-publisher")
    public String testMQPublisher(){
        String queueName = "candidate-queue";
        String message = "This is my first message";
        rabbitTemplate.convertAndSend(queueName, message);
        return "test-mq-publisher";
    }
}
