package com.example.weatherapp.data


import com.example.weatherapp.utils.Resource
import kotlinx.coroutines.flow.FlowCollector
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

suspend inline fun <reified T> FlowCollector<Resource<T>>.handleError(throwable: Throwable) {
    Timber.i("handleError: ${throwable.message}")
    this.apply {
        try {
            var errorMessage = ""
            var statusCode = 500
            val response = (throwable as? HttpException)?.response()

            response?.errorBody()?.let { responseBody ->
                try {
                    val jObjError = JSONObject(responseBody.string())
                    statusCode = jObjError.getString("statusCode").toInt()
                    errorMessage = jObjError.getString("message")

                    emit(Resource.Error(message = errorMessage, statusCode = statusCode))
                    return

                } catch (ex: Throwable) {
                    emit(Resource.Error(message = errorMessage, statusCode = statusCode))
                    return
                }
            }
            emit(
                Resource.Error(
                    message = "No address associated with hostname",
                    statusCode = statusCode
                )
            )


        } catch (ex: Exception) {
            emit(
                Resource.Error(
                    message = "No address associated with hostname",
                    statusCode = 500
                )
            )

        }
    }
}



suspend inline fun FlowCollector<Resource<Response<ResponseBody>>>.handleErrorStream(response: Response<ResponseBody>?, throwable: Throwable?) {

    val statusCode: Int
    val errorMessage: String

    if (response != null) {
        statusCode = response.code()
        errorMessage = response.errorBody()?.let { responseBody ->
            try {
                val jObjError = JSONObject(responseBody.string())
                jObjError.getString("message")
            } catch (ex: Exception) {
                "Error parsing response"
            }
        } ?: "No address associated with hostname"
    } else {
        statusCode = 500
        errorMessage = throwable?.message ?: "Unknown error"
    }

    emit(Resource.Error<Response<ResponseBody>>(message = errorMessage, statusCode = statusCode))
}