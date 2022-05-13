package com.kapilguru.student.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.GsonBuilder
import com.kapilguru.student.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object RetrofitNetwork {


    //  private const val BASE_URL = "http://3.233.217.61:9000/"
//    private const val BASE_URL = "http://35.174.62.203:9000/"
//    private const val BASE_URL = "http://44.197.234.100:9000/"


    private fun getRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getRetroClient().build())
                .build() //Doesn't require the adapter
    }


    private fun getRetroClient(): OkHttpClient.Builder {
        val httpClient = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
        if (!BuildConfig.IS_RELEASE_BUILD) {
            val logging = HttpLoggingInterceptor()
            logging.level = (HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)
        }
        httpClient.addInterceptor(networkConnectionInterceptor())

       /* httpClient.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                var response = chain.proceed(chain.request())
                if (response.code == 401) {
                    val mContext = MyApplication.context
                    val pref = StorePreferences(mContext).clearStorePreferences()
                    MyApplication.context.startActivity(Intent(mContext,LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
                return response
            }

        })*/

        httpClient.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                // here you fetch the token from Shared Preferences
                // create method
                val token = fetchTokenFromSharedPreferences()
                Log.v("token_token", token.toString())
                val newRequest: Request = chain.request().newBuilder()
                        .header("Authorization", "$token")
                        .build()
                return chain.proceed(newRequest)
            }
        })
        return getUnsafeOkHttpClient(httpClient)
    }

    @Throws(NetworkConnectionError::class)
    private fun networkConnectionInterceptor() = object : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val connectivity = AppConnectivityCheck(MyApplication.context)
            if (connectivity.getConnectivityStatus() == 4) {
                throw NetworkConnectionError(message = "No internet connection")
            } else {
                return chain.proceed(chain.request())
            }
        }
    }

    private fun getUnsafeOkHttpClient(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {

                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val trustManagerFactory: TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + trustManagers.contentToString()
            }
            val trustManager = trustManagers[0] as X509TrustManager
            builder.sslSocketFactory(sslSocketFactory, trustManager)
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun fetchTokenFromSharedPreferences(): String {
        val mContext = MyApplication.context
        val sharedPreferences: SharedPreferences =
                mContext.getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE)
        val jwtToken = sharedPreferences.getString(JWT_TOKEN, "")
        return jwtToken!!
    }

    val API_KAPIL_TUTOR_SERVICE_SERVICE: ApiKapilTutorService =
            getRetrofit().create(ApiKapilTutorService::class.java)

    data class NetworkConnectionError(override var message: String, var code: Int? = NETWORK_CONNECTIVITY_EROR) : IOException(message)
}