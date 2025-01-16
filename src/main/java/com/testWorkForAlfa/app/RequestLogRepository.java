package com.testWorkForAlfa.app;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestLogRepository extends MongoRepository<RequestLogEntry, String> {

}
