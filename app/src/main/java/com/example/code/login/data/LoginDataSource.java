package com.example.code.login.data;

import android.text.TextUtils;
import android.util.Log;

import com.example.code.http.NetWorkManager;
import com.example.code.http.common.Constants;
import com.example.code.http.util.SignCore;
import com.example.code.login.login.model.LoggedInUser;
import com.example.code.login.model.LoginReq;
import com.example.code.login.model.LoginResp;
import com.example.code.util.GsonUtil;

import java.io.IOException;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private static final String TAG = "Login";

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }


    private void login(String client_id, int page_index, int page_size) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("client_id", client_id);
        params.put("time", System.currentTimeMillis() / 1000);
        String sign = SignCore.buildRequestSign(params, Constants.KYP_CLIENT_KEY);
        params.put("sign", sign);

        LoginReq body = new LoginReq();
        body.setSystemType(1);
        body.setToken("1234");

        NetWorkManager.getInstance()
                .getApiService()
                .userLogin(params, body)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String response = "";
                        try {
                            response = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "user login response = " + response);
                        if (!TextUtils.isEmpty(response)) {
                            LoginResp resp = GsonUtil.parse(response, LoginResp.class);
                            if (resp != null && resp.isSuccess()) {

                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != e)
                            Log.d(TAG, "getVideoEpisodes,onFailure,statusCode:" + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
