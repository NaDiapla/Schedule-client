package com.example.hyunwook.schedulermacbooktroops.task.eventset;

import android.content.Context;

import com.example.common.base.task.BaseAsyncTask;
import com.example.common.bean.EventSet;
import com.example.common.data.EventSetDB;
import com.example.common.listener.OnTaskFinishedListener;

/**
 * 18-07-07
 * 스케줄분류 이벤트 셋 추가하는 AsyncTask
 */
public class AddEventSetTask extends BaseAsyncTask<EventSet> {

    private EventSet mEventSet;

    public AddEventSetTask(Context context, OnTaskFinishedListener<EventSet> onTaskFinishedListener, EventSet eventSet) {
        super(context, onTaskFinishedListener);
        mEventSet = eventSet;
    }

    @Override
    protected EventSet doInBackground(Void... params) {
        if (mEventSet != null) {
            EventSetDB db = EventSetDB.getInstance(mContext);

            int id = db.addEventSet(mEventSet);

            if (id != 0) {
                mEventSet.setId(id);
                return mEventSet;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
