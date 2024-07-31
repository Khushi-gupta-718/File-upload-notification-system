package dev.khushi;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class S3EventHandler implements RequestHandler<S3Event, String> {

    private final AmazonSNS snsClient;
    private final ObjectMapper objectMapper;

    // SNS Topic ARN (update with your SNS topic ARN)
    private static final String SNS_TOPIC_ARN = "arn:aws:sns:ap-south-1:928836847222:notificationsystem";

    public S3EventHandler() {
        this.snsClient = AmazonSNSClientBuilder.defaultClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String handleRequest(S3Event event, Context context) {
        for (S3Event.S3EventNotificationRecord record : event.getRecords()) {
            String s3Bucket = record.getS3().getBucket().getName();
            String s3Key = record.getS3().getObject().getKey();
            // Event time might not be directly available; use appropriate method or metadata if needed
            String eventTime = record.getEventSource(); // Update this according to correct data source

            try {
                // Prepare SNS message
                String notificationMessage = String.format(
                        "New file uploaded to S3 bucket '%s' with key '%s' at '%s'",
                        s3Bucket, s3Key, eventTime
                );

                // Publish message to SNS
                PublishRequest publishRequest = new PublishRequest()
                        .withTopicArn(SNS_TOPIC_ARN)
                        .withMessage(notificationMessage)
                        .withSubject("File Upload Notification");

                snsClient.publish(publishRequest);

            } catch (Exception e) {
                context.getLogger().log("Error processing record: " + e.getMessage());
                return "Error processing record";
            }
        }

        return "Processing complete";
    }
}
