package com.example.proiect.asyncTask;

public interface Callback<R> {

    public void runResultOnUiThread(R result);
}
