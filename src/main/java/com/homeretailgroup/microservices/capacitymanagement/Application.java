package com.homeretailgroup.microservices.capacitymanagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableIntegration
@SpringBootApplication
public class Application {
	@Autowired
	private CollectionPointRepository repository;
	
	/*@Component
	public static class KafkaConfig {

	    @Value("${kafka.topic:test}")
	    private String topic;

	    @Value("${kafka.address:192.168.99.100:9092}")
	    private String brokerAddress;

	    @Value("${zookeeper.address:192.168.99.100:2181}")
	    private String zookeeperAddress;

	    KafkaConfig() {
	    }

	    public KafkaConfig(String t, String b, String zk) {
	        this.topic = t;
	        this.brokerAddress = b;
	        this.zookeeperAddress = zk;
	    }

	    public String getTopic() {
	        return topic;
	    }

	    public String getBrokerAddress() {
	        return brokerAddress;
	    }

	    public String getZookeeperAddress() {
	        return zookeeperAddress;
	    }
	}
	
	
	@Configuration
	public static class ProducerConfiguration {

	    @Autowired
	    private KafkaConfig kafkaConfig;

	    private static final String OUTBOUND_ID = "outbound";

	    private Log log = LogFactory.getLog(getClass());

	    @Bean
	    @DependsOn(OUTBOUND_ID)
	    CommandLineRunner kickOff( 
	           @Qualifier(OUTBOUND_ID + ".input") MessageChannel in) {
	        return args -> {
	            for (int i = 0; i < 1000; i++) {
	                in.send(new GenericMessage<>("#" + i));
	                log.info("sending message #" + i);
	            }
	        };
	    }

	    @Bean(name = OUTBOUND_ID)
	    IntegrationFlow producer() {

	      log.info("starting producer flow..");
	      return flowDefinition -> {

	        Consumer<KafkaProducerMessageHandlerSpec.ProducerMetadataSpec> spec =
	          (KafkaProducerMessageHandlerSpec.ProducerMetadataSpec metadata)->
	            metadata.async(true)
	              .batchNumMessages(10)
	              .valueClassType(String.class)
	              .<String>valueEncoder(String::getBytes);

	        KafkaProducerMessageHandlerSpec messageHandlerSpec =
	          Kafka.outboundChannelAdapter(
	               props -> props.put("queue.buffering.max.ms", "15000"))
	            .messageKey(m -> m.getHeaders().get(IntegrationMessageHeaderAccessor.SEQUENCE_NUMBER))
	            .addProducer(this.kafkaConfig.getTopic(), 
	                this.kafkaConfig.getBrokerAddress(), spec);
	        flowDefinition
	            .handle(messageHandlerSpec);
	      };
	    }
	}
	
	@Configuration
	public static class ConsumerConfiguration {

	    @Autowired
	    private KafkaConfig kafkaConfig;

	    private Log log = LogFactory.getLog(getClass());

	    @Bean
	    IntegrationFlow consumer() {

	      log.info("starting consumer..");

	      KafkaHighLevelConsumerMessageSourceSpec messageSourceSpec = Kafka.inboundChannelAdapter(
	          new ZookeeperConnect(this.kafkaConfig.getZookeeperAddress()))
	            .consumerProperties(props ->
	                props.put("auto.offset.reset", "smallest")
	                     .put("auto.commit.interval.ms", "100"))
	            .addConsumer("myGroup", metadata -> metadata.consumerTimeout(100)
	              .topicStreamMap(m -> m.put(this.kafkaConfig.getTopic(), 1))
	              .maxMessages(10)
	              .valueDecoder(String::new));

	      Consumer<SourcePollingChannelAdapterSpec> endpointConfigurer = e -> e.poller(p -> p.fixedDelay(100));

	      return IntegrationFlows
	        .from(messageSourceSpec, endpointConfigurer)
	        .<Map<String, List<String>>>handle((payload, headers) -> {
	            payload.entrySet().forEach(e -> log.info(e.getKey() + '=' + e.getValue()));
	            return null;
	        })
	        .get();
	    }
	}*/
	

	public static void main(String[] args) {
		/*String zooKeeper = "192.168.99.100:2181";//args[0];
        String groupId = "testGroup";//args[1];
        String topic = "test";//args[2];
        int threads = 3;//Integer.parseInt(args[3]);
 
        KafkaConsumer kafkaConsumer = new KafkaConsumer(zooKeeper, groupId, topic, repository);
        kafkaConsumer.run(threads);*/
		SpringApplication.run(Application.class, args);
	}

	@RequestMapping("/")
	public String home() {
        
		/*long events = 10l;
        Random rnd = new Random();

        Properties props = new Properties();
        props.put("metadata.broker.list", "192.168.99.100:9092");
        props.put("producer.type", "sync");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "com.homeretailgroup.microservices.capacitymanagement.KafkaPartitioner");
        props.put("request.required.acks", "1");
 
        ProducerConfig config = new ProducerConfig(props);
 
        Producer<String, String> producer = new Producer<String, String>(config);
 
        for (long nEvents = 0; nEvents < events; nEvents++) { 
        	   System.out.println("creating event "+nEvents);
               long runtime = new Date().getTime();  
               String ip = "192.168.2."+ rnd.nextInt(255); 
               String msg = runtime + ",www.vulab.com," + ip; 
               KeyedMessage<String, String> data = new KeyedMessage<String, String>("test", ip, msg);
               producer.send(data);
        }
        producer.close();*/
		return "Capacity Management Microservice Running.  Connected to Mongo";
	}

    /**
     * Entry point for a GET Capacity details request.
     * A successful response returns the data including availabity on a particular date for a particular collection point.
     * @param collectionPointId Collection point Id for which availability is required.
     * @param date Date of availability
     * @return Capacity document from MongoDB
     */
	@RequestMapping(value="/capacity/{collectionPointId}/date/{availabilityDate}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity capacity(@PathVariable String collectionPointId, @PathVariable String availabilityDate) throws Exception{

		Capacity capacity = repository.findByCollectionPointId(collectionPointId);

        if(null == capacity || capacity.equals(null)) {
            HttpGenericException notFoundException =
                    new HttpGenericException(HttpStatus.NOT_FOUND, "Collection point not found", collectionPointId);
            return new ResponseEntity(notFoundException, HttpStatus.NOT_FOUND);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Availability availability = capacity.getAvailability(sdf.parse(availabilityDate));
		return new ResponseEntity(availability, HttpStatus.OK);
	}

    /**
     * Entry point for an Add New Customer PUT request.
     * A successful response returns the data of the newly added customer.
     * @param email Email address of the new customer
     * @param cards An ArrayList of multiple cards
     * @return Newly added customer document from MonogoDB
     */
	@RequestMapping(value="/capacity/{collectionPointId}", method = RequestMethod.PUT, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity addCapacity(@PathVariable String collectionPointId, @RequestBody (required=false) ArrayList<Availability> availabilities) {

		Capacity newCapacity = new Capacity(collectionPointId, availabilities);

        try {
            repository.insert(newCapacity);

        } catch(DuplicateKeyException duplicateKeyException) {

            HttpGenericException duplicateException =
                    new HttpGenericException(HttpStatus.CONFLICT, "Capacity already exists", newCapacity);
            return new ResponseEntity(duplicateException, HttpStatus.CONFLICT);
        }

        return new ResponseEntity(newCapacity, HttpStatus.CREATED);
	}


	/**
	 * Entry point for an Add/Update Card PUT request.
	 * A successful response returns the data of the customer, with the newly added or updated card.
	 * @param email Email address of the new customer
	 * @return Updated customer document from MonogoDB
	 */
	/*@RequestMapping(value="/customer/{email:.+}/card", method = RequestMethod.POST)
	public ResponseEntity updateCard(@PathVariable String email, @RequestBody Card card){

		Customer updateCustomer = repository.findByEmail(email);

		if(null == updateCustomer || updateCustomer.equals(null)) {
			HttpGenericException notFoundException =
					new HttpGenericException(HttpStatus.NOT_FOUND, "Customer not found", email);
			return new ResponseEntity(notFoundException, HttpStatus.NOT_FOUND);
		} else {

			updateCustomer.updateCard(card);
			repository.save(updateCustomer);
			return new ResponseEntity(updateCustomer, HttpStatus.OK);
		}

	}*/


	/**
	 * Entry point for an Remove Card DELETE request.
	 * A successful response returns the data of the customer, without the newly deleted card.
	 * @param email Email address of the new customer
	 * @param token Tokenised number of the card to delete
	 * @return Updated customer document from MonogoDB
	 */
	/*@RequestMapping(value="/customer/{email:.+}/card/{token}", method = RequestMethod.DELETE)
	public ResponseEntity deleteCard(@PathVariable String email, @PathVariable String token){

		Customer cardHolderCustomer = repository.findByEmail(email);

		if(null == cardHolderCustomer || cardHolderCustomer.equals(null)) {
			HttpGenericException notFoundException =
					new HttpGenericException(HttpStatus.NOT_FOUND, "Customer not found", email);
			return new ResponseEntity(notFoundException, HttpStatus.NOT_FOUND);
		} else {

			cardHolderCustomer.removeCard(token);
			repository.save(cardHolderCustomer);

			return new ResponseEntity(cardHolderCustomer, HttpStatus.OK);
		}
	}*/

}
