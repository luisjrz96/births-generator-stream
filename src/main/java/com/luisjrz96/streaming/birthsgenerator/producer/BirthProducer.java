package com.luisjrz96.streaming.birthsgenerator.producer;

import com.luisjrz96.streaming.birthsgenerator.config.BirthConfig;
import com.luisjrz96.streaming.birthsgenerator.models.BirthInfo;
import com.luisjrz96.streaming.birthsgenerator.models.Gender;
import com.luisjrz96.streaming.birthsgenerator.utils.DataSheet;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.UUID;

@Service
public class BirthProducer  implements Runnable{

    private Logger logger = LoggerFactory.getLogger(BirthProducer.class);

    @Value("${spring.application.sink.topic}")
    private String topic;

    @Autowired
    private BirthConfig birthConfig;

    @Autowired
    private KafkaTemplate<String, BirthInfo> birthProducer;

    @Override
    public void run() {
        int delay = birthConfig.getBirth().get("delayMs");
        Random random = new Random();
        Generator generator = new Generator();
        while (true){
            try {
                BirthInfo birthInfo = null;
                if(random.nextBoolean()){
                    birthInfo = generator.generate(Gender.MALE);
                }else{
                    birthInfo = generator.generate(Gender.FEMALE);
                }
                birthProducer.send(new ProducerRecord<String, BirthInfo>(topic, UUID.randomUUID().toString(), birthInfo));
                logger.info(String.format("NEW BIRTH PRODUCED: %s", birthInfo));
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    @PostConstruct
    public void init(){
        this.run();
    }




    private class Generator {
        private Random random = new Random();

        public BirthInfo generate(Gender gender){

            String country,state,name,lastName = "";
            country = DataSheet.countries.get(random.nextInt(DataSheet.countries.size()));
            state = DataSheet.states.get(country).get(random.nextInt(DataSheet.states.get(country).size()));
            lastName = DataSheet.lastNames.get(country).get(random.nextInt(DataSheet.lastNames.get(country).size()));
            lastName += " ".concat(DataSheet.lastNames.get(country).get(random.nextInt(DataSheet.lastNames.get(country).size())));
            if(gender.toString().equals(Gender.MALE.toString())){
                name = DataSheet.maleNames.get(country).get(random.nextInt(DataSheet.maleNames.get(country).size()));
            }else{
                name = DataSheet.femaleNames.get(country).get(random.nextInt(DataSheet.femaleNames.get(country).size()));
            }

            Long date = System.currentTimeMillis();
            float weight = random.nextFloat()+random.nextInt(2)+2;
            int height = random.nextInt(10)+25;
            BirthInfo birthInfo = BirthInfo.newBuilder()
                    .setCountry(country)
                    .setState(state)
                    .setGender(gender)
                    .setName(name.toUpperCase())
                    .setLastName(lastName)
                    .setDate(date)
                    .setHeight(height)
                    .setWeight(weight)
                    .build();

            return birthInfo;
        }

    }
}
