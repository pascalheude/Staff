package com.assistanceinformatiquetoulouse.roulezrose.staff;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.assistanceinformatiquetoulouse.roulezrose.staff.NewsContent;

import java.util.HashMap;

// TODO : ajouter l'image
// TODO : changer le TextView en WebView
// Class ItemDetailFragment
public class ItemDetailFragment extends Fragment {
    // Attibuts publics
    public static final String ARG_ITEM_ID = "item_id";
    // Attributs privés
    private NewsContent.NewsItem mItem;

    // Constructeur
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        HashMap<String, Object> lHashMap;
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy date specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load date from a date provider.
            lHashMap = NewsContent.aListItem.get(getArguments().getInt(ARG_ITEM_ID));
            String s = (String) lHashMap.get("image");
            mItem = new NewsContent.NewsItem((String) lHashMap.get("titre"),
                    (String) lHashMap.get("date"),
                    (String) lHashMap.get("contenu"),
                    s);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.pTitre);
            }
            else {
            }
// TODO : déplacer les 3 lignes suivantes dans la méthode onCreateView et ajouter une image dans item_detail.xml
            ImageView lImageView = (ImageView) activity.findViewById(R.id.imageView);
            Bitmap lBitmap = BitmapFactory.decodeFile(mItem.pImage);
            lImageView.setImageBitmap(lBitmap);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy date as text in a TextView.
        if (mItem != null) {
            // ((TextView) rootView.findViewById(R.id.item_detail)).setText(aStaffeur.pContenu);
            ((WebView) rootView.findViewById(R.id.item_detail)).loadData(mItem.pContenu, "text/html", null);
            ImageView lImageView = (ImageView) rootView.findViewById(R.id.imageView2);
            Bitmap lBitmap = BitmapFactory.decodeFile(mItem.pImage);
            lImageView.setImageBitmap(lBitmap);
        }
        else {
        }
        return rootView;
    }
}
