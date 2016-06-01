package it.extremegeneration.timetocook.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import it.extremegeneration.timetocook.R;
import it.extremegeneration.timetocook.Utilities;
import it.extremegeneration.timetocook.dataModel.CookingContract;


public class AdapterCategories
        extends RecyclerView.Adapter<AdapterCategories.ViewHolderCategory> {

    final private Context myContext;
    private Cursor myCursor;

    final private CategoriesOnClickHandler myCategoriesOnClickHandler;

    public interface CategoriesOnClickHandler {
        void onClick(Long categoryID, RecyclerView.ViewHolder viewHolder);
    }

    public AdapterCategories(Context myContext, CategoriesOnClickHandler myCategoriesOnClickHandler) {
        this.myContext = myContext;
        this.myCategoriesOnClickHandler = myCategoriesOnClickHandler;
    }

    //It creates new View (invoked by the LayoutManager)
    @Override
    public AdapterCategories.ViewHolderCategory onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
            view.setFocusable(true);
            return new ViewHolderCategory(view);
        } else {
            throw new RuntimeException("No bound to RecyclerView");
        }
    }

    //Replace the content of a View
    @Override
    public void onBindViewHolder(AdapterCategories.ViewHolderCategory holder, int position) {
        myCursor.moveToPosition(position);

        int categoryIdIndex = myCursor.getColumnIndex(CookingContract.CategoryEntry._ID);
        int categoryNameIndex = myCursor.getColumnIndex(CookingContract.CategoryEntry.COLUMN_CATEGORY_NAME);
        int categoryPicIndex = myCursor.getColumnIndex(CookingContract.CategoryEntry.COLUMN_PIC_CATEGORY);


        int categoryID = myCursor.getInt(categoryIdIndex);
        String categoryName = myCursor.getString(categoryNameIndex);
        String categoryPic = myCursor.getString(categoryPicIndex);

        //CATEGORYPIC = fruits
        //categorypic = legumes
        //categorypic = tubers



        Uri uriCategoryId = CookingContract.CategoryEntry.buildCategoryUri(categoryID);

        int drawableResource = myContext.getResources().getIdentifier("drawable/"+ categoryPic, null, myContext.getPackageName());

        Picasso.with(myContext).load(drawableResource)
                .resize(200,200)
                .into(holder.categoryImageView);

        holder.categoryTextView.setText(categoryName);


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


    public class ViewHolderCategory extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final public TextView categoryTextView;
        final public ImageView categoryImageView;


        public ViewHolderCategory(View itemView) {
            super(itemView);
            categoryTextView = (TextView) itemView.findViewById(R.id.category_TextView);
            categoryImageView = (ImageView) itemView.findViewById(R.id.category_imageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            myCursor.moveToPosition(adapterPosition);


        }
    }
}
