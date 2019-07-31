package com.example.july30_rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    //4 component of RxJava, a library of observer pattern

    //1.observer  - listene to observables
    //2.observable - that provide/ emit data
    //3.subscrption - subscribe to the observable
    //4.Disposable (一次性) it is used to dispose the subsription when observer don't want to listen to observable
    //generally on OnStop and onDestroy

    Observable<ArrayList> mObservable;

    String TAG = "winnie";

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mObservable = getData();
        Observer<ArrayList> mObserver = getHereObserver();

         // scheduler.io is saying to call me on Scheduler.IO thread , which is basically a background thread
        // observe on - means to give result back to AndroidScheduler.mainThread so that we can have the observable data
        // and we can do UI operations
        // subscribe with is something like to give the result to that observer,return data back to observer
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(mObserver);
    }

    private Observer<ArrayList> getHereObserver(){

        return new Observer<ArrayList>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG,"onSubscribe");
                disposable=d;
            }

            @Override
            public void onNext(ArrayList s) {  // it will receive a string one by one from array declared in observable
                Log.e(TAG,"name is : " +s.get(0) );
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"Error is : " +e.getMessage() );
            }

            @Override
            public void onComplete() {
                Log.e(TAG ,"i am complete" );
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        //dipose the subscription and prevent memory leaks
    }

    //// observable will collect and emit data , we can call any api here or any opearation,logic here
    private Observable<ArrayList> getData(){
        ArrayList mlist=new ArrayList<Integer>();
        mlist.add(1);
        mlist.add(2);
        mlist.add(3);
        return Observable.fromArray(mlist);
    }
}
