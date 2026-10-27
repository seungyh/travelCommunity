package com.travel.community.pages.alarm.repository.mongo;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.travel.community.pages.alarm.entity.AlarmDocument;

public interface AlarmRepository extends MongoRepository<AlarmDocument, ObjectId> {

    List<AlarmDocument> findTop5ByReceiverIdAndReadFalseOrderByCreatedAtDesc(String userId);

    int countByReceiverIdAndReadFalse(String userId);

}
