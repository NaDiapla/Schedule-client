package com.example.hyunwook.schedulermacbooktroops.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.calendar.widget.calendar.schedule.ScheduleRecyclerView;
import com.example.common.base.app.BaseFragment;
import com.example.common.bean.EventSet;
import com.example.common.bean.Schedule;
import com.example.common.util.DeviceUtils;
import com.example.common.util.ToastUtils;
import com.example.hyunwook.schedulermacbooktroops.R;
import com.example.hyunwook.schedulermacbooktroops.adapter.ScheduleAdapter;
import com.example.hyunwook.schedulermacbooktroops.dialog.SelectDateDialog;
import com.example.hyunwook.schedulermacbooktroops.listener.OnTaskFinishedListener;
import com.example.hyunwook.schedulermacbooktroops.task.schedule.AddScheduleTask;

import java.util.Calendar;
import java.util.List;

/**
 * 18-06-19
 * 스케줄 목록을 보고자 할때 표시되는 프레그먼트
 *
 */
public class EventSetFragment extends BaseFragment implements View.OnClickListener, SelectDateDialog.OnSelectDateListener {

    static final String TAG = EventSetFragment.class.getSimpleName();
    private ScheduleRecyclerView rvScheduleList;
    private EditText etInput;
    private RelativeLayout rlNoTask;

    private ScheduleAdapter mScheduleAdapter;

    private EventSet mEventSet;

    private SelectDateDialog mSelectDateDialog;

    private int mPosition = -1;

    private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;
    private long mTime;
    public static String EVENT_SET_OBJ = "event.set.obj";
    /**
     * http://milkissboy.tistory.com/34
     * @param eventSet
     * @return
     */
    public static EventSetFragment getInstance(EventSet eventSet) {
        EventSetFragment fragment = new EventSetFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EVENT_SET_OBJ, eventSet);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    protected View initContentView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.fragment_event_set, container, false);
    }

    @Override
    protected void bindView() {
        rvScheduleList = searchViewById(R.id.rvScheduleList);
        rlNoTask = searchViewById(R.id.rlNoTask);
        etInput = searchViewById(R.id.etInputContent);
        searchViewById(R.id.ibMainClock).setOnClickListener(this);
        searchViewById(R.id.ibMainOK).setOnClickListener(this);
        initBottomInputBar();
        initScheduleList();
    }

    //스케줄 메모 부분 InputBar
    private void initBottomInputBar() {

        //http://egloos.zum.com/killins/v/3008925  --> TextWatcher
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //start 지점에서 시작되는 count 갯수만큼의
                //글자들이 after 길이만큼의 글자로 대치되려고 할 때 호출된다

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //start 지점에서 시작되는 before 갯수만큼의 글자들이
                //count 갯수만큼의 글자들로 대치되었을 때 호출된다.

            }

            //EditText의 텍스트가 변경되면 호출된다.
            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged --->" + s.toString());
                etInput.setGravity(s.length() == 0 ? Gravity.CENTER : Gravity.CENTER_VERTICAL);
            }
        });

        etInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
    }

    //스케줄 리스트 설정
    private void initScheduleList() {
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        rvScheduleList.setLayoutManager(manager);

        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);

        rvScheduleList.setItemAnimator(itemAnimator);
        mScheduleAdapter = new ScheduleAdapter(mActivity, this);
        rvScheduleList.setAdapter(mScheduleAdapter);


    }

    @Override
    protected void initData() {
        super.initData();
        mEventSet = (EventSet) getArguments().getSerializable(EVENT_SET_OBJ);

    }

    @Override
    protected void bindData() {
        super.bindData();
//        new GetScheduleTask
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibMainClock:
                showSelectDateDialog();
                break;
            case R.id.ibMainOK:
                addSchedule();
                break;
        }
    }

    //시작 시간 설정 다이얼로그.
    private void showSelectDateDialog() {
        if (mSelectDateDialog == null) {
            Calendar calendar = Calendar.getInstance();
            mSelectDateDialog = new SelectDateDialog(mActivity, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), mPosition);
        }

        mSelectDateDialog.show();
    }

    private void closeSoftInput() {
        etInput.clearFocus();
        DeviceUtils.closeSoftInput(mActivity, etInput);
    }

    //ok버튼 클릭 시 스케줄 등록
    private void addSchedule() {
        String content = etInput.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShortToast(mActivity, R.string.schedule_input_null);
        } else {
            closeSoftInput();

            //스케줄 저장
            Schedule schedule = new Schedule();
            schedule.setTitle(content);
            schedule.setState(0);
            schedule.setColor(mEventSet.getColor());
            schedule.setEventSetId(mEventSet.getId());
            schedule.setTime(mTime);
            schedule.setYear(mCurrentSelectYear);
            schedule.setMonth(mCurrentSelectMonth);
            schedule.setDay(mCurrentSelectDay);

            new AddScheduleTask(mActivity, com.example.common.listener.OnTaskFinishedListener<Schedule>() {
                @Override
                public void onTaskFinished(Schedule data) {
                    if (data != null) {
                        mScheduleAdapter.insertItem(data);

                    }
                }
            }
        }

    }

    //현재 날짜
    private void setCurrentSelectDate(int year, int month, int day) {
        mCurrentSelectYear = year;
        mCurrentSelectMonth = month;
        mCurrentSelectDay = day;
    }

    @Override
    public void onSelectDate(int year, int month, int day, long time, int position) {
        setCurrentSelectDate(year, month, day);
        mTime = time;
        mPosition = position;
    }

    @Override
    public void onTaskFinished(List<Schedule> data) {
        mScheduleAdapter.change
    }
}