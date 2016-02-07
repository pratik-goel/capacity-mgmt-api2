package com.homeretailgroup.microservices.capacitymanagement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.boot.json.JsonJsonParser;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

public class ConsumerRunnable implements Runnable{
	private KafkaStream m_stream;
    private int m_threadNumber;
    private CollectionPointRepository repository;
 
    public ConsumerRunnable(KafkaStream a_stream, int a_threadNumber, CollectionPointRepository a_repository) {
        m_threadNumber = a_threadNumber;
        m_stream = a_stream;
        repository = a_repository;
    }
 
    public void run() {
        ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
        String collectionPointId = null;
        String quantity = null;
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        while (it.hasNext()) {
            
            String message = new String(it.next().message());
            System.out.println("Thread " + m_threadNumber + ": " + message);
            JsonJsonParser jp = new JsonJsonParser();
            Map<String, Object> parsedMap = jp.parseMap(message);
            if(parsedMap.containsKey("placeOrder")) {
            	Map<String, String> mapContents = (Map)parsedMap.get("placeOrder");
            	collectionPointId = mapContents.get("collectionPointId");
            	quantity = mapContents.get("quantity");
            	try {
					date = sdf.parse(mapContents.get("date"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
            }
            Capacity capacity = repository.findByCollectionPointId(collectionPointId);
            Availability availability = capacity.getAvailability(date);
            availability.setCapacity(Integer.toString(Integer.parseInt(availability.getCapacity()) - Integer.parseInt(quantity)));
            capacity.updateAvailability(availability);
            repository.save(capacity);
        }
        System.out.println("Shutting down Thread: " + m_threadNumber);
    }
}
