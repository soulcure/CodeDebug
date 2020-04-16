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

/**
 * ================================================
 * Created by wuhaiyuan on 28/11/2019 11:49
 * ================================================
 */
public interface Api {
    String APP_DEFAULT_DOMAIN = "https://tvpi.coocaa.com";
    //String APP_DEFAULT_DOMAIN = "http://172.20.155.103:3000";


    String XIAOWEI_DOMAIN = "https://user.coocaa.com";
    String XIAOWEI_DUMAIN_NAME = "xiaowei";

    /**
     * 酷开用户登录
     **/
    String COOCAA_ACCOUNT_DOMAIN = "https://passport.coocaa.com";
//    String COOCAA_ACCOUNT_DOMAIN = "https://beta.passport.coocaa.com";
    String COOCAA_ACCOUNT_DOMAIN_NAME = "coocaa_account";

    //应用圈
    String APP_STORE_DOMAIN = "http://tc.skysrt.com/appstore/appstorev3";
    String APP_STORE_DOMAIN_NAME = "appstore";

    //应用圈
    String WX_COOCAA_DOMAIN = "https://wx.coocaa.com";
    String WX_COOCAA_DOMAIN_NAME = "wxcaacoo";
}
