package hoang.myapp.routes

import hoang.myapp.config.Config.AWS_BUCKET_NAME
import hoang.myapp.storage.StorageService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.ByteArrayOutputStream

fun Route.uploadProfilePic(storageService: StorageService) {
    post("upload-profile-pic") {
        val multipartData = try {
            call.receiveMultipart()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Bad request")
            return@post
        }
        var fileName = ""
        var fileDescription = ""
        val byteArrayOutputStream = ByteArrayOutputStream()

        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    fileDescription = part.value
                }

                is PartData.FileItem -> {
                    fileName = part.originalFileName as String
                    val fileBytes = part.streamProvider().readBytes()
                    byteArrayOutputStream.writeBytes(fileBytes)
                }

                else -> {}
            }
            part.dispose()
        }
        val byteArray = byteArrayOutputStream.toByteArray()
        if (byteArray.isEmpty()) {
            call.respond(HttpStatusCode.InternalServerError, "Cannot read file")
            return@post
        }
        val uploadResult = storageService.uploadToBucket(
            AWS_BUCKET_NAME,
            "user-profile-pics/${fileName}",
            byteArray
        )
        uploadResult
            .onSuccess { call.respond(HttpStatusCode.OK, it) }
            .onFailure { call.respond(HttpStatusCode.InternalServerError, it.toString()) }
    }
}

fun Route.getProfilePic(storageService: StorageService) {
    get("get-profile-pic") {
        val objectKey = call.request.queryParameters["objectKey"]
        if (objectKey == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing parameters")
            return@get
        }

        val getResult = storageService.getFromBucket(
            AWS_BUCKET_NAME,
            objectKey
        )
        getResult
            .onSuccess { call.respond(HttpStatusCode.OK, it) }
            .onFailure { call.respond(HttpStatusCode.InternalServerError, it.toString()) }
    }
}

fun Route.uploadUserMedia(storageService: StorageService) {
    post("upload-post-image") {
        val multipartData = try {
            call.receiveMultipart()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Bad request")
            return@post
        }
        var fileName = ""
        var fileDescription = ""
        val byteArrayOutputStream = ByteArrayOutputStream()

        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    fileDescription = part.value
                }

                is PartData.FileItem -> {
                    fileName = part.originalFileName as String
                    val fileBytes = part.streamProvider().readBytes()
                    byteArrayOutputStream.writeBytes(fileBytes)
                }

                else -> {}
            }
            part.dispose()
        }
        val byteArray = byteArrayOutputStream.toByteArray()
        if (byteArray.isEmpty()) {
            call.respond(HttpStatusCode.InternalServerError, "Cannot read file")
            return@post
        }
        val uploadResult = storageService.uploadToBucket(
            AWS_BUCKET_NAME,
            "user-media/${fileName}",
            byteArray
        )
        uploadResult
            .onSuccess { call.respond(HttpStatusCode.OK, it) }
            .onFailure { call.respond(HttpStatusCode.InternalServerError, it.toString()) }
    }
}