/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.code.http.api;



import com.example.code.login.model.LoginReq;
import com.example.code.login.model.RegisterReq;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface ApiService {
    //一键登录
    @POST("/skyapi/user/sy/login")
    Observable<ResponseBody> userLogin(@QueryMap Map<String, Object> queryMap, @Body LoginReq body);

    //手机号注册
    @POST("/skyapi/user/register/mobile")
    Observable<ResponseBody> mobileRegister(@FieldMap Map<String, Object> queryMap, @Body RegisterReq body);

}
