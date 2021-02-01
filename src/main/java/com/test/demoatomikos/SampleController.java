package com.test.demoatomikos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author blackjack
 */
@Slf4j
@RestController
public class SampleController {
    private final SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GetMapping("finish")
    public Integer finish(@RequestParam("name") String name) {
        return sampleService.finish(name);
    }

    @GetMapping("exception")
    public Integer rollbackFroException(@RequestParam("name") String name) {
        return sampleService.rollbackForException(name);
    }

    @GetMapping("rollback")
    public Integer rollbackManually(@RequestParam("name") String name) {
        return sampleService.rollbackManually(name);
    }
}
