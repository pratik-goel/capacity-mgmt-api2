package com.homeretailgroup.microservices.capacitymanagement;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 
 * @author pratik.goel
 *
 */
public interface CollectionPointRepository extends MongoRepository<Capacity, String> {
	public Capacity findByCollectionPointId(String collectionPointId);
}
