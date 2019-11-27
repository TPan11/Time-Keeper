package com.btbsolutions.timekeeper.adapter;

import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.btbsolutions.timekeeper.AddDailyTask;
import com.btbsolutions.timekeeper.DisplayTodoDetail;
import com.btbsolutions.timekeeper.R;
import com.btbsolutions.timekeeper.asyncClasses.DeleteTodoAsync;
import com.btbsolutions.timekeeper.utility.HeaderItem;
import com.btbsolutions.timekeeper.utility.ToDo;
import com.btbsolutions.timekeeper.utility.ListItem;
import com.btbsolutions.timekeeper.utility.ToDoItem;
import com.btbsolutions.timekeeper.utility.ToDoRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ToDoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater mInflater;
    private List<ListItem> mToDoItems;
    private Context mContext;
    private ToDoRepository mRepository;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private String selectedDate = null;

    public ToDoListAdapter(Context context, Application application){
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mRepository = new ToDoRepository(application);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case ListItem.TYPE_HEADER: {
                View itemView = mInflater.inflate(R.layout.view_list_item_header, viewGroup, false);
                return new HeaderViewHolder(itemView);
            }
            case ListItem.TYPE_EVENT:{
                View itemView = mInflater.inflate(R.layout.recyclerview_item, viewGroup, false);
                return new ToDoItemViewHolder(itemView);
            }
            default:
                throw new IllegalStateException("unsupported item type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ListItem.TYPE_EVENT: {
                if (mToDoItems != null) {
                    ToDoItem toDoItem = (ToDoItem) mToDoItems.get(position);
                    final ToDo current = toDoItem.getToDo();
                    final ToDoItemViewHolder toDoItemViewHolder = (ToDoItemViewHolder)viewHolder;
                    toDoItemViewHolder.toDoItemView.setText(current.getTodotask());
                    toDoItemViewHolder.toDoPriorityView.setBackgroundColor(
                            getColorByPriority(current.getPriority())
                    );
                    toDoItemViewHolder.toDoDetailsView.setText(current.getDetailed_notes());
                    if (current.getComplete() == 1) {
                        toDoItemViewHolder.imageButtonCheckComplete.setImageResource(R.drawable.ic_check_black_24dp);
                    } else {
                        toDoItemViewHolder.imageButtonCheckComplete.setImageResource(0);
                    }

                    toDoItemViewHolder.textViewOptions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu popup = new PopupMenu(mContext, toDoItemViewHolder.textViewOptions);
                            popup.inflate(R.menu.options_menu);
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    switch (menuItem.getItemId()) {
                                        case R.id.menu1:
                                            //handle menu1 click
                                            //Toast.makeText(mContext, "Delete Clicked", Toast.LENGTH_SHORT).show();
                                            mRepository.delete(current);
                                            DeleteTodoAsync backupDailyTaskAsync = new DeleteTodoAsync(mContext);
                                            String email = current.getUseremail();
                                            String id = String.valueOf(current.getId());
                                            backupDailyTaskAsync.execute(email, id);
                                            notifyDataSetChanged();
                                            break;
                                        case R.id.menu2:
                                            //handle menu2 click
                                            //Toast.makeText(mContext, "Forward Clicked", Toast.LENGTH_SHORT).show();
                                            Calendar calendar = Calendar.getInstance();
                                            DatePickerDialog datePickerDialog = new DatePickerDialog(
                                                    mContext,
                                                    new DatePickerDialog.OnDateSetListener() {
                                                        @Override
                                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                            Calendar c = Calendar.getInstance();
                                                            c.set(year, month, dayOfMonth, 0, 0, 0);
                                                            selectedDate = simpleDateFormat.format(new Date(c.getTimeInMillis()));
                                                            Log.d("SelectedDate", selectedDate);
                                                            current.setDate(selectedDate);
                                                            current.setId(0);
                                                            current.setComplete(0);
                                                            current.setBackup(false);
                                                            mRepository.insert(current);
                                                            notifyDataSetChanged();
                                                        }
                                                    },
                                                    calendar.get(Calendar.YEAR),
                                                    calendar.get(Calendar.MONTH),
                                                    calendar.get(Calendar.DAY_OF_MONTH));

                                            datePickerDialog.show();
                                            break;

                                        case R.id.menu3:
                                            //toDoItemViewHolder.completeTask(current);
                                            Intent addTaskIntent = new Intent(mContext, AddDailyTask.class);
                                            addTaskIntent.putExtra("ActivityName", "CalendarLongClick");
                                            addTaskIntent.putExtra("taskid", current.getId());
                                            addTaskIntent.putExtra("taskname", current.getTodotask());
                                            addTaskIntent.putExtra("duration", current.getDuration());
                                            mContext.startActivity(addTaskIntent);
                                            ((Activity) mContext).finish();
                                            break;
                                    }
                                    return true;
                                }
                            });
                            popup.show();
                        }
                    });
                }
                break;
            }

            case ListItem.TYPE_HEADER:{
                HeaderItem header = (HeaderItem) mToDoItems.get(position);
                HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
                // your logic here
                double group = header.getGroup();
                String[] grouping = mContext.getResources().getStringArray(R.array.duration_group);
                holder.txt_header.setText(grouping[(int)group]);
                break;
            }
        }
    }

    private int getColorByPriority(int priority){
        if (priority == 1){
            return mContext.getResources().getColor(R.color.priority1);
        }
        else if(priority == 2){
            return mContext.getResources().getColor(R.color.priority2);
        }
        else if(priority == 3){
            return mContext.getResources().getColor(R.color.priority3);
        }
        return 0;
    }

    public void setToDoItems(List<ListItem> words){
        mToDoItems = words;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mToDoItems != null){
            return mToDoItems.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mToDoItems.get(position).getType();
    }

    class ToDoItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final TextView toDoItemView;
        private final TextView toDoDetailsView;
        private final View toDoPriorityView;
        private ImageButton imageButtonCheckComplete;
        private TextView textViewOptions;

        ToDoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            toDoItemView = itemView.findViewById(R.id.textView);
            toDoDetailsView = itemView.findViewById(R.id.textviewDetails);
            toDoPriorityView = itemView.findViewById(R.id.priorityView);
            imageButtonCheckComplete = itemView.findViewById(R.id.imageButtonCheckComplete);
            textViewOptions = itemView.findViewById(R.id.textViewOptions);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            ToDoItem toDoItem = (ToDoItem) mToDoItems.get(getAdapterPosition());
            ToDo currentToDoItem = toDoItem.getToDo();

            if(currentToDoItem.getComplete() == 0) {
                Intent detailIntent = new Intent(mContext, DisplayTodoDetail.class);

                //         Log.d("TODO_ITEM_ID",currentToDoItem.getGroup());
                detailIntent.putExtra("Id", Long.toString(currentToDoItem.getId()));
                detailIntent.putExtra("Item", currentToDoItem.getTodotask());
                detailIntent.putExtra("Priority", Integer.toString(currentToDoItem.getPriority()));
                detailIntent.putExtra("Complete", Integer.toString(currentToDoItem.getComplete()));
                detailIntent.putExtra("End_date", currentToDoItem.getDate());
                detailIntent.putExtra("Details", currentToDoItem.getDetailed_notes());
                detailIntent.putExtra("Duration", currentToDoItem.getDuration());

                mContext.startActivity(detailIntent);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            ToDoItem toDoItem = (ToDoItem) mToDoItems.get(getAdapterPosition());
            ToDo currentToDoItem = toDoItem.getToDo();
            String d = currentToDoItem.getDate();
            Calendar now = Calendar.getInstance();
            String today = simpleDateFormat.format(new Date(now.getTimeInMillis()));

            if(today.equals(d) && currentToDoItem.getComplete() == 0) {
                completeTask(currentToDoItem);

                return true;
            }
            else{
                return false;
            }
        }

        void completeTask(ToDo currentToDoItem){
            Intent addTaskIntent = new Intent(mContext, AddDailyTask.class);
            addTaskIntent.putExtra("ActivityName", "CalendarLongClick");
            addTaskIntent.putExtra("taskid", currentToDoItem.getId());
            addTaskIntent.putExtra("taskname", currentToDoItem.getTodotask());
            mContext.startActivity(addTaskIntent);
            ((Activity) mContext).finish();
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView txt_header;

        HeaderViewHolder(View itemView) {
            super(itemView);
            txt_header = (TextView) itemView.findViewById(R.id.txt_header);
        }

    }
}
