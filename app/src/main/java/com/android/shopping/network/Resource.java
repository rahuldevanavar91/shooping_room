package com.android.shopping.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.android.shopping.network.Status.ERROR;
import static com.android.shopping.network.Status.LOADING;
import static com.android.shopping.network.Status.SUCCESS;


public class Resource<T> {

    @NonNull
    private final Status status;

    @Nullable
    private T data;

    @Nullable
    private final String message;

    private RequestType requestType;
    private int position;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message, RequestType requestType) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.requestType = requestType;
    }

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message, RequestType requestType, int position) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.requestType = requestType;
        this.position = position;
    }


    public static <T> Resource<T> success(@NonNull T data, RequestType requestType) {
        return new Resource<>(SUCCESS, data, null, requestType);
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(SUCCESS, data, null, null);
    }

    public static <T> Resource<T> success(@NonNull T data, RequestType requestType, int position) {
        return new Resource<>(SUCCESS, data, null, requestType, position);
    }


    public static <T> Resource<T> loading(RequestType requestType) {
        return new Resource<>(LOADING, null, null, requestType);
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(LOADING, null, null, null);
    }

    public static <T> Resource<T> error(String msg, RequestType requestType) {
        return new Resource<>(ERROR, null, msg == null ?
                "internal server error" : msg, requestType);
    }

    public static <T> Resource<T> error(String msg) {
        return new Resource<>(ERROR, null, msg == null ?
                "internal server error" : msg, null);
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    public int getPosition() {
        return position;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}