
**Log Search API Service**:
This service provides a RESTful API to search log files stored in AWS S3 storage, based on specified criteria. It allows users to search for log lines containing a specific keyword within a specified time range.

**Assumptions**:
1. Latency is not an issue.
2. Not added support for and/or operations, user can only search one keyword

**Instructions**:
Follow these steps to build and run the service:

1. Clone this repository: git clone https://github.com/rahulyadav26/logsearch.git
2. Navigate to the project directory: cd logsearch
3. Install dependencies using Maven: mvn clean install
4. Configure environment variables for the remote storage connection.
5. Build and run the service using Spring Boot: mvn spring-boot:run
6. Access the API endpoints to search for log lines using the provided search criteria.

**Example**:
Suppose the log data is stored in an AWS S3 bucket named app-logs-bucket with the following folder structure:
- 2023-11-25
  - 01.txt
  - 02.txt
  - 15.txt
- 2023-11-26
  - 01.txt
  - 02.txt
  - 14.txt
    
To search for the text "abc" between 1st November 2023 and 31st November 2023, you can make a sample curl request to the API endpoint:
curl --location 'localhost:8082/search/logs' \
--header 'Content-Type: application/json' \
--data '{
    "search_keyword": "abc",
    "start_time": 1714411949,
    "end_time": 1814421949
}'

This will return a response similar to the following:
{
  "count": 10,
  "matched_log_lines":[
    "abc deg acdhbcs"
  ]
}
The response contains the count of matched log lines and an array of the matched log lines.

**Environment variables to be configured**:
1. server.port: Port on which you want to run the application
2. amazonproperties.s3.access.key.id: Access key id of user to access the s3 bucket
3. amazonproperties.s3.secret.key: Secret key of user to access the s3 bucket.
4. amazonproperties.region: Region of s3 bucket.
5. amazonproperties.s3.bucket.name: Name of s3 bucket

Dummy env variables:
1. server.port=8082
2. amazonproperties.s3.access.key.id=abc
3. amazonproperties.s3.secret.key=abc
4. amazonproperties.region=abc
5. amazonproperties.s3.bucket.name=abc

