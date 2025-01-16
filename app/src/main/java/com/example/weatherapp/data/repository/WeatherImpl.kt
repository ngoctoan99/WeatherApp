package com.example.weatherapp.data.repository


import com.example.weatherapp.data.remote.WeatherApi
import com.example.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherImpl @Inject constructor(
    val api: WeatherApi
) : WeatherRepository {

//    override suspend fun getListGame(request: RequestGetListGame): Flow<Resource<ListGameDomain>> {
//        return flow {
//            emit(Resource.Loading())
//            val user = api.getListGame(authorization = request.accessToken, limit = request.data.limit , page = request.data.page).data?.toDomain()
//            user?.let { emit(Resource.Success(it)) }
//        }.catch {
//            this.handleError(it)
//        }
//    }
}