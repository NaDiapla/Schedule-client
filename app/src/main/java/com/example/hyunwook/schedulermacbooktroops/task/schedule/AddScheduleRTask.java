package com.example.hyunwook.schedulermacbooktroops.task.schedule;

import android.content.Context;
import android.util.Log;

import com.example.common.base.task.BaseAsyncTask;
import com.example.common.data.ScheduleRealm;
import com.example.common.listener.OnTaskFinishedListener;
import com.example.common.realm.RealmArrayList;
import com.example.common.realm.ScheduleR;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * 18-08-04
 * Realm insert시도 하는 AsyncTask
 */
public class AddScheduleRTask extends BaseAsyncTask<ScheduleR> {

    static final String TAG = AddScheduleRTask.class.getSimpleName();
    Realm realm;

//    ArrayList<RealmArrayList> resArr = new ArrayList<RealmArrayList>();
//    private ScheduleR mSchedule;
    private ScheduleR mSchedule;

    //Call Constructor ScheduleFragment
    public AddScheduleRTask(Context context, OnTaskFinishedListener<ScheduleR> onTaskFinishedListener, ScheduleR schedule) {
//    public AddScheduleRTask(Context context, OnTaskFinishedListener<ScheduleR> onTaskFinishedListener, ArrayList<RealmArrayList> arr) {
        super(context, onTaskFinishedListener);

        mSchedule = schedule;
//        resArr = arr;

        Log.d(TAG, "AddSchedule ==>" +mSchedule.getTitle());
    }

    //AsyncTask 실행
    @Override
    protected ScheduleR doInBackground(Void... params) {
//        Log.d(TAG, "mSchedule =======" + mSchedule);

        if (mSchedule != null) {
            Log.d(TAG, "resArr doInBackground");
//            ScheduleRealm sr = ScheduleRealm.getInstance(mContext);

//            sr.addSchedule();
//            sr.addSchedule(resArr);
/*
            if (id != 0) {
                mSchedule.setId(id);*/
           /* } else {
//        if (mSchedule)
                return null;
            }
        } else {
            return null;*/
        }
        return mSchedule;
    }
//    }

}
