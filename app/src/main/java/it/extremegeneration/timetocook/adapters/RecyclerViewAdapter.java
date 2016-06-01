package it.extremegeneration.timetocook.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import it.extremegeneration.timetocook.R;
import it.extremegeneration.timetocook.dataModel.CookingContract;


public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    final private Context myContext;
    private Cursor myCursor;

    final private AdapterOnClickHandler myAdapterOnClickHandler;

    public interface AdapterOnClickHandler {
        void onClick(Long foodID, RecyclerView.ViewHolder viewHolder);
    }


    public RecyclerViewAdapter(Context myContext, AdapterOnClickHandler myAdapterOnClickHandler) {
        this.myContext = myContext;
        this.myAdapterOnClickHandler = myAdapterOnClickHandler;
    }


    //It creates new View (invoked by the LayoutManager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
            view.setFocusable(true);
            return new ViewHolder(view);
        } else {
            throw new RuntimeException("No bound to RecyclerView");
        }

    }

    //Replace the content of a View (invoked by the LayoutManager)
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        myCursor.moveToPosition(position);

        int foodIDIndex = myCursor.getColumnIndex(CookingContract.FoodEntry._ID);
        int foodNameIndex = myCursor.getColumnIndex(CookingContract.FoodEntry.COLUMN_NAME);

        final int foodID = myCursor.getInt(foodIDIndex);
        Uri uriFavouriteId = CookingContract.FavouriteEntry.buildFavouriteUri(foodID);

        Log.v("uriFavouriteId",": " + uriFavouriteId);
        Cursor favouriteCursor = myContext.getContentResolver().query(
                uriFavouriteId,
                null,
                null,
                null,
                null
        );

        //Check if the food is favourite
        if (favouriteCursor.moveToFirst()) {
            //It is favourite
            holder.buttonFav.setText("Remove from Fav");
            holder.buttonFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Query: delete the ID from favourites TABLE
                    int rowDeleted = myContext.getContentResolver().delete(
                            CookingContract.FavouriteEntry.CONTENT_URI,
                            CookingContract.FavouriteEntry.COLUMN_ID_FAVOURITE + " =?",
                            new String[]{Integer.toString(foodID)}
                    );
                    if (rowDeleted > 0) {
                        Toast.makeText(myContext, "REMOVED to favourites", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();

                    } else {
                        Toast.makeText(myContext, "Failed to remove from favourites", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        } else {
            holder.buttonFav.setText("Add to Favourites");
            holder.buttonFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Query: ADD the ID to the favourites TABLE
                    ContentValues addFavouriteFood = new ContentValues();
                    addFavouriteFood.put(CookingContract.FavouriteEntry.COLUMN_ID_FAVOURITE, foodID);

                    Uri createFavourite = myContext.getContentResolver().insert(
                            CookingContract.FavouriteEntry.CONTENT_URI,
                            addFavouriteFood
                    );

                    if (createFavourite != null) {
                        Toast.makeText(myContext, "Added to favourites", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();

                    } else {
                        Toast.makeText(myContext, "FAILED to add to favourites", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        holder.textView.setText(myCursor.getString(foodNameIndex));


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


    ///INNER CLASS: ViewHolder///
    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final public TextView textView;
        final public Button buttonFav;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            buttonFav = (Button) itemView.findViewById(R.id.buttonFav);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(myContext, "puzzi!", Toast.LENGTH_SHORT).show();

        }


    }
}
