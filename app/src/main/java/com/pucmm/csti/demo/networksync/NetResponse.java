package com.pucmm.csti.demo.networksync;

import retrofit2.Call;
import retrofit2.Response;

public interface NetResponse<T> {
//Listener
    /**
     * Callback interface for delivering parsed responses.
     * @param response
     */
    void onResponse(T response);

    /**
     * Callback interface for delivering error responses.
     */
    void onFailure(Throwable t);

}