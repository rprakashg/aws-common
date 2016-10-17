package com.rprakashg.cloud.aws.common;

import com.rprakashg.cloud.aws.common.models.ContactSqsMessage;
import com.rprakashg.cloud.aws.common.sqs.SqsRepository;
import com.rprakashg.cloud.aws.common.sqs.SqsRepositoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SqsRepositoryTests.class})
public class SqsRepositoryTests {

    @Test
    public void addMessageTest(){
        SqsRepository<ContactSqsMessage> repository = new SqsRepositoryImpl<>(ContactSqsMessage.class, "contacts", 200);
        repository.ensureExists();

    }
}
