package com.cs371m.bookmark.api

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import android.util.Log
import retrofit2.http.Path

interface OpenLibraryApi {
    // This function needs to be called from a coroutine, hence the suspend
    // in its type.  Also note the @Query annotation, which says that when
    // called, retrofit will add "&difficulty=%s".format(level) to the URL
    // Thanks, retrofit!
    // Hardcode several parameters in the GET for simplicity
    // So URL can have & and ? characters
    // XXX Write me: Retrofit annotation, see CatNet

    // https://openlibrary.org/dev/docs/api/books
    @GET("/api/books?format=json&jscmd=details")
    suspend fun getBook(@Query("bibkeys") bibkeys: String) : Map<String, Book>

    @GET("/search.json?limit=100")
    suspend fun searchBookByTitle(@Query("title") title: String) : SearchResult

    companion object {
        // Leave this as a simple, base URL.  That way, we can have many different
        // functions (above) that access different "paths" on this server
        // https://square.github.io/okhttp/4.x/okhttp/okhttp3/-http-url/
        var url = HttpUrl.Builder()
            .scheme("https")
            .host("openlibrary.org").addPathSegment("")
            .build()



        // Public create function that ties together building the base
        // URL and the private create function that initializes Retrofit
        fun create(): OpenLibraryApi = create(url)
        private fun create(httpUrl: HttpUrl): OpenLibraryApi {
            Log.d("OpenLibraryApi", httpUrl.toString())
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenLibraryApi::class.java)
        }
    }
}