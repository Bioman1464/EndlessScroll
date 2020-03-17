package com.example.endlessscroll.networking

import com.example.endlessscroll.models.EventList

import kotlinx.coroutines.Deferred
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface Service {

    @GET("api/events")
    suspend fun getEventsByPage(
        @Query ("page") page: Int
    ) : Response<EventList>

    @Multipart

    @POST("api/media")
    suspend fun postMedia (
        @Header("Accept") accept: String = "application/json",
        @Header("Authorization") accessToken: String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiYjJjZjE0ZWE4ZmU1ZjJmNDEzZDM5NDBkYjE4Njg0MGNiOTM1M2U3OThiN2I5NWE3ZjEyZjU0YmVhNmQ2YzQzNWM5OTg5MGNlZGQ1MDBkYzMiLCJpYXQiOjE1ODEyNTM3MDEsIm5iZiI6MTU4MTI1MzcwMSwiZXhwIjoxNjEyODc2MTAxLCJzdWIiOiIxMSIsInNjb3BlcyI6W119.jOaw91cXcrajYw4Njx0AZ-RrqUWaEcYslR1cRQa4lFPMaIauET1S-H_7kQh5Foh4Wf17RqOxIdhGh3kAQNWtjj7oQPlsBc3wqheYvNfiUCBtRrtJ6uGqDg-CarRKvC-CX4WBjLC8awpRxrNsdUl2b4o2-hqXMKTHrLCLAdgkt7e7Jf000rxhJHuHMJDUI4allQWbDeVYbV2FfWlaa01N1gWLBEOKH-DCzKHpJraRBKYDSWy3HQCyEBFp7WmQ64xBt8oLorpGAV52uiCvcHcUkXfMbibv6ARAY2PFPPeO8bK1YM1Zsd_AJzs_Ud32WqAESi1CT8W6UGzcC4hWfRtFHfybGiyeeB54eQ65seWfOSbVZzpwRKmD2xMymH6Op_xD_XO2ioPWyjoTxaSdUDnkDz3_ZeQBKbwdItHZsYmxyJzK3zNFswmE9G-QSO-IWQB3yT-P7jsVYV95lLQsVJs4kkD4es3A2R9ulota87YBhLTZYJ3iBwC3QHb5c2Vw9fxm2hIXb3eTWsgyNvH0x0bXkSTgWz_8NSNW98hRtPEPuh3ddCg8YubvT8CIzmkPVy2KFfw48yfXIH3YtUB4S9MhV800HiW5TMP_I1WzDDQAg2k9063XjNSptL1keO-ZLzQzQkCeO2m1zSjwnql8PZ3uLPOXCyn28rQ46IWHq8Vna9E",
//        @Part name: RequestBody = RequestBody.create(MediaType.parse("image/*"),
//            File("content://com.android.providers.media.documents/document/image%3A290751")
//        )
        @Part multipartBody: MultipartBody.Part
    ) : Response<Any>

}