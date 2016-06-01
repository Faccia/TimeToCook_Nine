package it.extremegeneration.timetocook.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.extremegeneration.timetocook.R;
import it.extremegeneration.timetocook.dataModel.CookingContract;


public class AdapterSearchable
        extends RecyclerView.Adapter<AdapterSearchable.ViewHolderSearchable> {

    final private Context myContext;
    private Cursor myCursor;

    final private AdapterOnClickHandler myAdapterOnClickHandler;

    public interface AdapterOnClickHandler {
        void onClick(Long foodID, ViewHolderSearchable viewHolder);
    }

    public AdapterSearchable(Context myContext, AdapterOnClickHandler myAdapterOnClickHandler) {
        this.myContext = myContext;
        this.myAdapterOnClickHandler = myAdapterOnClickHandler;
    }

    //It creates new View
    @Override
    public ViewHolderSearchable onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchable_item, parent, false);
            view.setFocusable(true);
            return new ViewHolderSearchable(view);
        } else {
            throw new RuntimeException("No bound to SearchableItemView");
        }
    }

    //Replace the content of a View
    @Override
    public void onBindViewHolder(ViewHolderSearchable holder, int position) {
        myCursor.moveToPosition(position);

        int foodNameIndex = myCursor.getColumnIndex(CookingContract.FoodEntry.COLUMN_NAME);
        String nameFood = myCursor.getString(foodNameIndex);

        holder.nameFoodTextView.setText(nameFood);


    }

    @Override
    public int getItemCount() {
        if (myCursor == null) return 0;
        return myCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        myCursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return myCursor;
    }

    public class ViewHolderSearchable extends RecyclerView.ViewHolder implements View.OnClickListener {

        final public TextView nameFoodTextView;
        final public ImageView pictureFoodView;

        public ViewHolderSearchable(View itemView) {
            super(itemView);
            nameFoodTextView = (TextView) itemView.findViewById(R.id.text_food_searchable);
            pictureFoodView = (ImageView) itemView.findViewById(R.id.icon_food_searchable);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            myCursor.moveToPosition(adapterPosition);
        }
    }
}
