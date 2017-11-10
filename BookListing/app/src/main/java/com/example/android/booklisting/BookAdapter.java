package com.example.android.booklisting;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by Jacquelyn Gboyor on 11/3/2017.
 */

public class BookAdapter  extends ArrayAdapter<BookDetails>{

    public BookAdapter(Activity context, ArrayList<BookDetails> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_items, parent, false);
        }
        BookDetails currentGuide = getItem(position);

        ImageView bookpic = (ImageView) listItemView.findViewById(R.id.bookpic);
        Picasso.with(getContext()).load(currentGuide.getBookImage()).placeholder(R.drawable.noimage).error(R.drawable.noimage).fit().into(bookpic);


        TextView title = (TextView) listItemView.findViewById(R.id.bookTitle);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Uptown Elegance Bold.ttf");
        title.setTypeface(typeface);
        title.setText(currentGuide.getBookTitle());

        TextView category = (TextView) listItemView.findViewById(R.id.category);
        category.setText("Category: " + currentGuide.getBookCategory().replace("\"", "").replace("[", " ").replace("]", " ").trim());
        category.setTypeface(typeface);

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText("Author: " + currentGuide.getBookAuthor().replace("\"", "").replace("[", " ").replace("]", " ").trim());
        author.setTypeface(typeface);

        TextView pub = (TextView) listItemView.findViewById(R.id.publisher);
        pub.setText("Publisher: " + currentGuide.getBookPublisher());
        pub.setTypeface(typeface);

        return listItemView;

    }

}
