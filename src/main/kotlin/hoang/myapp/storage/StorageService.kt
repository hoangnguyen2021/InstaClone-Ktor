package hoang.myapp.storage

interface StorageService {
    suspend fun uploadToBucket(
        bucketName: String,
        objectKey: String,
        byteArray: ByteArray
    ): Result<String>
    suspend fun uploadToBucket(
        bucketName: String,
        objectKeys: List<String>,
        byteArrays: List<ByteArray>
    ): Result<List<String>>

    suspend fun getFromBucket(bucketName: String, objectKey: String): Result<ByteArray>
}