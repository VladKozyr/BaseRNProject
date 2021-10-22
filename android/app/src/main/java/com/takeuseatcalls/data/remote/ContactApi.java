package com.takeuseatcalls.data.remote;

import com.takeuseatcalls.model.CallContact;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ContactApi {
    @GET("/contact")
    Single<CallContact> getContact(@Query("number") String contactNumber);
}
