package com.edward.navigation.Model.response;


/**
 * Created by AMing on 15/12/22.
 * Company RongCloud
 */
public class MobVerifyCodeResponse {
    /**
     * status:200 或者其他？
     * result : {"verification_token":"86bd3a00-b80a-11e5-b5ab-433619959d67"}
     */

    private int status;
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private String error;
    private ResultEntity result;
    public ResultEntity getResult() {
        return result;
    }
    public void setResult(ResultEntity result) {
        this.result = result;
    }
    public static class ResultEntity {
        private String verification_token;

        public void setVerification_token(String verification_token) {
            this.verification_token = verification_token;
        }

        public String getVerification_token() {
            return verification_token;
        }
    }
}
