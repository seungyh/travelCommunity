package com.travel.community.pages.travelPlan.repository.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.travel.community.pages.travelPlan.entity.mongo.TravelPlanDocument;

public interface TravelPlanDetailRepository extends MongoRepository<TravelPlanDocument, Long> {

}
