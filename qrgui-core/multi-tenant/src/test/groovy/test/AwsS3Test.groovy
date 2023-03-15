package test

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import it.cambi.qrgui.services.WorkBookService
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared
import spock.lang.Specification

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3

@Testcontainers
class AwsS3Test extends Specification {

    @Shared
    LocalStackContainer localstack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
            .withServices(S3)

    def s3 = AmazonS3ClientBuilder.standard().withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
            localstack.getEndpointOverride(S3).toString(),
            localstack.getRegion()
    )).withCredentials(
            new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(localstack.getAccessKey(), localstack.getSecretKey())
            )
    ).build()

    String bucketName = "test"

    WorkBookService workBookService;

    def setup() {
        s3.createBucket(bucketName)
        workBookService = new WorkBookService(s3);
    }

    def "should add file to bucket"() {
        given:
        def fileName = "filename.txt"
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName), "utf-8"))) {
            writer.write("something");
        }

        when:
        workBookService.uploadToS3Bucket(bucketName, "/documents/" + fileName, fileName)
        new File(fileName).delete()

        then:
        s3.getObject(bucketName, "/documents/" + fileName) != null
    }
}
