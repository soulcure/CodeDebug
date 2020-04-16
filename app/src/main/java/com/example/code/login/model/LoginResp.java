package com.example.code.login.model;

public class LoginResp {
    /**
     * msg : 登录成功
     * code : 1
     * data : {"access_token":"at.a232d400c2434ad2844fe815dbe7145a","refresh_token":"rt.12","expires_in":"31104000"}
     */

    private String msg;
    private int code;
    private DataBean data;

    public boolean isSuccess() {
        return code == 1;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * access_token : at.a232d400c2434ad2844fe815dbe7145a
         * refresh_token : rt.12
         * expires_in : 31104000
         */
        private String access_token;
        private String refresh_token;
        private String expires_in;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }
    }
}
