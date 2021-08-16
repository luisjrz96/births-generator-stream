package com.luisjrz96.streaming.birthsgenerator;

import com.luisjrz96.streaming.birthsgenerator.config.BirthConfig;
import com.luisjrz96.streaming.birthsgenerator.models.BirthInfo;
import com.luisjrz96.streaming.birthsgenerator.producer.BirthProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BirthProducerTests {

    @InjectMocks
    private BirthProducer birthProducer;

    @Mock
    private BirthConfig birthConfig;

    @Mock
    private KafkaTemplate<String, BirthInfo> kafkaTemplate;


    @Test
    public void testProduceOneRecord() {
        birthProducer.setTopic("topic");
        birthProducer.setRecordQuantity(1);
        Map<String, Integer> delayMsConfig = new HashMap<String, Integer>(){{put("delayMs", 600);}};
        Mockito.doReturn(delayMsConfig).when(birthConfig).getBirth();
        ListenableFuture<ProducerRecord<String, BirthInfo>> record = Mockito.mock(ListenableFuture.class);
        Mockito.doReturn(record).when(kafkaTemplate)
                .send(Mockito.anyString(), Mockito.anyString(), Mockito.any(BirthInfo.class));
        ArgumentCaptor<ProducerRecord<String, BirthInfo>> producerRecordArgumentCaptor = ArgumentCaptor.forClass(ProducerRecord.class);

        birthProducer.run();
        Assertions.assertThat(record).isNotNull();
        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(producerRecordArgumentCaptor.capture());
        Logger.getAnonymousLogger().info(producerRecordArgumentCaptor.getValue().value().toString());
    }


    @Test
    public void testProducingMultipleRecords() {
        Random random = new Random();
        int quantity = random.nextInt(5)+1;
        birthProducer.setTopic("topic");
        birthProducer.setRecordQuantity(quantity);
        Map<String, Integer> delayMsConfig = new HashMap<String, Integer>(){{put("delayMs", 600);}};
        Mockito.doReturn(delayMsConfig).when(birthConfig).getBirth();
        ListenableFuture<ProducerRecord<String, BirthInfo>> record = Mockito.mock(ListenableFuture.class);
        Mockito.doReturn(record).when(kafkaTemplate)
                .send(Mockito.anyString(), Mockito.anyString(), Mockito.any(BirthInfo.class));

        birthProducer.run();
        Assertions.assertThat(record).isNotNull();
        Mockito.verify(kafkaTemplate, Mockito.times(quantity)).send(Mockito.any(ProducerRecord.class));

    }

}
