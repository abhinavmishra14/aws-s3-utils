# amazon-aws-s3-utils
Utility to work with Amazon S3. This utility has features to access S3 using IAM role.


###Note: By default test cases will be skipped.
###Use access keys to build in order to pass the test cases and use skip test parameter (-Dmaven.test.skip=false).

To force run tests: e.g. mvn clean install -Dmaven.test.skip=false



For testing IAM services on EC2 instance which is already mapped with IAM role, use the default constructor call to create instance of AwsS3IamService.



Example:
AwsS3IamService awsS3IamService = new AwsS3IamServiceImpl();



For testing IAM services anywhere else use the parameterized constructor call to create instance of AwsS3IamService.


Example:
AwsS3IamService awsS3IamService = new AwsS3IamServiceImpl(AWS_ACCESS_KEY,AWS_SECRET_KEY);



###For more details visit:

http://javaworld-abhinav.blogspot.in/2015/07/using-iam-roles-for-amazon-services.html
