# amazon-aws-s3-utils
Utility to work with Amazon S3. This utility has features to access S3 using IAM role.


###Note: Use access keys before build in order to pass the test cases or use skip test parameter (-Dmaven.test.skip=true).

To skip the test:
e.g. mvn clean install -Dmaven.test.skip=true



For testing IAM services on EC2 instance which is already mapped with IAM role, use the default constructor call to create instance of AwsS3IamService.



Example:
AwsS3IamService awsS3IamService = new AwsS3IamServiceImpl();



For testing IAM services anywhere else use the parameterized constructor call to create instance of AwsS3IamService.


Example:
AwsS3IamService awsS3IamService = new AwsS3IamServiceImpl(AWS_ACCESS_KEY,AWS_SECRET_KEY);



###For more details visit:

http://javaworld-abhinav.blogspot.in/2015/07/using-iam-roles-for-amazon-services.html