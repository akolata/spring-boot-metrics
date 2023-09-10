package pl.akolata.metrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A test controller created to present a chart showing HTTP traffic by an endpoint.
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/test")
class TestController {

    @GetMapping
    public ResponseEntity<String> test() {
        log.info("Received request: GET /api/test");
        return ResponseEntity.ok("Test");
    }
}
