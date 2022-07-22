package mackatozis.cassandra.demo.controller;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import mackatozis.cassandra.demo.model.Sample;
import mackatozis.cassandra.demo.repository.SampleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SampleController {

    private final SampleRepository sampleRepository;

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sample> test() {
        var entity = Sample.builder()
                .externalId("externalId")
                .name("name")
                .updateTimestamp(Instant.now())
                .build();

        entity = sampleRepository.save(entity);

        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

}
