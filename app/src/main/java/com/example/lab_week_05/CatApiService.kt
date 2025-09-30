import retrofit2.Call
import retrofit2.http.GET

interface CatApiService {
    @GET("v1/images/search")
    fun getRandomCat(): Call<String> // return JSON mentah
}
