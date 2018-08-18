package com.good_gaming.task.util;

/**
 * Very generic Callback class. Can be used for pretty much anything.
 * Currently used for calling back after queries have finished executing.
 *
 * Created by Josh (MacBook).
 */
public interface Callback<T> {

    void call(T result);

}