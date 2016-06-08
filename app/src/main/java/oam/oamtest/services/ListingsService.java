package oam.oamtest.services;

import oam.oamtest.models.ListingsResponseModel;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class ListingsService {

    private static ListingsService mListingsService = new ListingsService();

    private ListingsService(){

    }

    public static ListingsService getInstance( ) {
        return mListingsService;
    }

    private Retrofit buildRestAdapter() {
        String apiUrl = "http://empty-bush-3943.getsandbox.com/";

        return new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ListingsResponseModel getListings() throws Exception {
        ListingsServices service = buildRestAdapter().create(ListingsServices.class);
        Call<ListingsResponseModel> call = service.getListings();
        return call.execute().body();
    }


    private interface ListingsServices {
        @GET("listings")
        Call<ListingsResponseModel> getListings();
    }

}
