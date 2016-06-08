package oam.oamtest.services;

import oam.oamtest.models.ListingModel;
import oam.oamtest.models.ListingsResponseModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class ListingsService {

    private static ListingsService mListingsService = new ListingsService();

    private ListingsService() {

    }

    public static ListingsService getInstance() {
        return mListingsService;
    }

    private Retrofit buildRestAdapter() {
        String apiUrl = "http://empty-bush-3943.getsandbox.com/";

        return new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void getListings(Callback<ListingsResponseModel> callback) throws Exception {
        ListingsServices service = buildRestAdapter().create(ListingsServices.class);
        Call<ListingsResponseModel> call = service.getListings();
        call.enqueue(callback);
    }


    private interface ListingsServices {
        @GET("listings")
        Call<ListingsResponseModel> getListings();
    }

}
