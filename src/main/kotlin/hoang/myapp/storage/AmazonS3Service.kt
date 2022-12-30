package hoang.myapp.storage

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.GetObjectRequest
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.content.toByteArray

class AmazonS3Service(
    private val s3Client: S3Client
) : StorageService {
    override suspend fun uploadToBucket(bucketName: String, objectKey: String, byteArray: ByteArray): Result<String> {
        return try {
            val request = PutObjectRequest {
                bucket = bucketName
                key = objectKey
                body = ByteStream.fromBytes(byteArray)
            }
            s3Client.let { s3 ->
                s3.putObject(request)
                Result.success(objectKey)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFromBucket(bucketName: String, objectKey: String): Result<ByteArray> {
        return try {
            val request = GetObjectRequest {
                bucket = bucketName
                key = objectKey
            }
            s3Client.let { s3 ->
                s3.getObject(request) { response ->
                    if (response.body != null) {
                        Result.success(response.body!!.toByteArray())
                    } else {
                        Result.failure(Exception("Cannot read file"))
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}