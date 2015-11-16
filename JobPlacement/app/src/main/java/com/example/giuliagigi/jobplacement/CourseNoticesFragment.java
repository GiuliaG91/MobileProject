package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CourseNoticesFragment extends Fragment {

    private Course course;
    ArrayList<News> notices;
    private boolean isEdit;

    private View root;
    private RecyclerView noticesView;
    private NoticeAdapter noticeAdapter;


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CONSTRUCTOR ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static CourseNoticesFragment newInstance(Course course, boolean isEdit) {

        CourseNoticesFragment fragment = new CourseNoticesFragment();

        fragment.course = course;
        fragment.isEdit = isEdit;
        fragment.notices = new ArrayList<>();

        return fragment;
    }

    public CourseNoticesFragment() {}


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CALLBACKS ------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_course_notices, container, false);

        noticesView = (RecyclerView)root.findViewById(R.id.course_notices_recyclerView);
        noticesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        noticeAdapter = new NoticeAdapter(course);
        noticesView.setAdapter(noticeAdapter);

        return root;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- ADAPTER --------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{

        Course course;
        ArrayList<News> notices;

        /* ------- constructor ------------------------------------------------*/
        public NoticeAdapter(Course course){

            this.course = course;
            this.notices = new ArrayList<>();

            ParseQuery<News> newsQuery = new ParseQuery<News>(News.class);
            newsQuery.whereEqualTo(News.COURSE_FIELD, course);

            newsQuery.findInBackground(new FindCallback<News>() {
                @Override
                public void done(List<News> notices, ParseException e) {

                    if(e == null && notices != null){

                        NoticeAdapter.this.notices.addAll(notices);
                        NoticeAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        }

        /* ------- callbacks ------------------------------------------------*/

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item_row, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.message.setText(notices.get(position).getMessage());

            Calendar dateNews = notices.get(position).getDate();
            Calendar now = Calendar.getInstance();

            if(dateNews.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH) && dateNews.get(Calendar.MONTH) == now.get(Calendar.MONTH) && dateNews.get(Calendar.YEAR) == now.get(Calendar.YEAR))
                holder.date.setText(dateNews.get(Calendar.HOUR_OF_DAY) + ":" + dateNews.get(Calendar.MINUTE));
            else if(dateNews.get(Calendar.YEAR) == now.get(Calendar.YEAR))
                holder.date.setText(dateNews.get(Calendar.DAY_OF_MONTH) + " " + GlobalData.getContext().getResources().getStringArray(R.array.months)[dateNews.get(Calendar.MONTH)]);
            else
                holder.date.setText(dateNews.get(Calendar.DAY_OF_MONTH) + "/" + dateNews.get(Calendar.MONTH) + "/" + dateNews.get(Calendar.YEAR));

        }


        @Override
        public int getItemCount() {
            return notices.size();
        }

        /* ------- view holder ------------------------------------------------*/

        public static class ViewHolder extends RecyclerView.ViewHolder{

            View root;
            TextView message;
            TextView date;

            public ViewHolder(View itemView) {
                super(itemView);

                root = itemView;
                message = (TextView)root.findViewById(R.id.notice_message);
                date = (TextView)root.findViewById(R.id.notice_date);
            }
        }
    }

}
