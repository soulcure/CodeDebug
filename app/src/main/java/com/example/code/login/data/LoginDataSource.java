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




}
