package cn.patterncat.job.admin.model;

import lombok.Data;

/**
 * Created by patterncat on 2017-11-18.
 */
@Data
public class RestResp<T> {

    private boolean success;

    private T data;

    private String message;

    private Integer errorCode;

    public static <T> RestRespBuilder<T> builder(){
        return new RestRespBuilder<>();
    }

    public static class RestRespBuilder<T>{
        RestResp<T> toBuild;

        public RestRespBuilder(){
            toBuild = new RestResp<>();
        }

        public RestResp<T> build(){
            return toBuild;
        }

        public RestRespBuilder<T> success(boolean value){
            toBuild.setSuccess(value);
            return this;
        }

        public RestRespBuilder<T> data(T data){
            toBuild.setData(data);
            return this;
        }

        public RestRespBuilder<T> message(String message){
            toBuild.setMessage(message);
            return this;
        }

        public RestRespBuilder<T> errorCode(int errorCode){
            toBuild.setErrorCode(errorCode);
            return this;
        }
    }
}
