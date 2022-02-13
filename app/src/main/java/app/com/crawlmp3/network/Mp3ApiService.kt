package app.com.crawlmp3.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val  BASE_URL = "https://mp3.zing.vn/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface Mp3ApiService{
    @GET("xhr/{key}/{request}")
    fun getPropertiesAsync(
            @Path("key") keySearch : String,
            @Path("request") request : String?="",
            @Query("type") type : String?=null,
            @Query("key") key : String?=null,
            @Query("playlists") list : String?="") : Deferred<Raw>

}

object Mp3Api{
    val retrofitService : Mp3ApiService by lazy {
        retrofit.create(Mp3ApiService::class.java)
    }
}

private val retrofitSearch = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl("http://ac.mp3.zing.vn/")
        .build()


interface Mp3ApiServiceSearch{
    @GET("complete")
    fun searchKeyAsync(@Query("type") type : String,
                       @Query("num") num : Int,
                       @Query("query") query: String) : Deferred<RawSearch>
}

object Mp3ApiSearch{
    val retrofitServiceSearch : Mp3ApiServiceSearch by lazy {
        retrofitSearch.create(Mp3ApiServiceSearch::class.java)
    }
}

private val retrofitViewSource = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl("https://mp3.zing.vn/")
        .build()

//or album
interface Mp3ApiServiceViewSource{
    @GET("playlist/{id}")
    fun getKeyAsync(
        @Path("id") idAlbum: String,
        @Query("playlists") query: String?="") : Deferred<String>
}

object Mp3ApiViewSource{
    val retrofitServiceViewSource : Mp3ApiServiceViewSource by lazy {
        retrofitViewSource.create(Mp3ApiServiceViewSource::class.java)
    }
}