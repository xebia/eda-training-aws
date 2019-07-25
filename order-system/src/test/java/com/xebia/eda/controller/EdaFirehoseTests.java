package com.xebia.eda.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.xebia.eda.domain.AuditEvent;
import com.xebia.eda.service.FirehoseAuditLogger;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles({"default", "test"})
public class EdaFirehoseTests {

    @Autowired
    FirehoseAuditLogger firehoseAuditLogger;

    @Autowired
    AmazonS3 s3;

    public static final String AUDIT_LOGGING_BUCKET = "auditLoggingBucket";

    @Test
    public void logToKinesis() throws Exception {
        String msg = "Sample message " + new Random().nextDouble();
        firehoseAuditLogger.put(msg);
        assertEquals(true, findMessage(20, 500, msg));
    }

    private boolean findMessage(int retryCount, int delayMs, String msg) throws Exception{
        System.out.println("Look for message=[" + msg + "] trial=[" + retryCount + "]");
            ObjectListing listing = s3.listObjects(AUDIT_LOGGING_BUCKET);
            Optional<S3ObjectSummary> content = findContent(listing, msg);
            if(content.isPresent()) {
                return true;
            }else  if(!content.isPresent() && retryCount > 0) {
                Thread.sleep(delayMs);
                return findMessage(retryCount - 1, delayMs, msg);
            } else {
                return false;
            }
    }

    private Optional<S3ObjectSummary> findContent(ObjectListing listing, String msg) throws Exception{
        List<S3ObjectSummary> summary = listing.getObjectSummaries().stream().filter(s -> {
            S3Object o = s3.getObject(AUDIT_LOGGING_BUCKET, s.getKey());
            String contents = null;
            try {
                contents = IOUtils.toString(o.getObjectContent());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return contents.startsWith(msg);
        }).collect(Collectors.toList());
        if(!summary.isEmpty())
            return Optional.of(summary.get(0));
        else if(listing.isTruncated())
            return findContent(s3.listNextBatchOfObjects(listing), msg);
        else
            return Optional.empty();

    }


}
