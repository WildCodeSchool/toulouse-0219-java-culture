package fr.wildcodeschool.culture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class listFavoritesAdapter extends ArrayAdapter<Museum> {

    public listFavoritesAdapter(Context context, List<Museum> museum) {
        super(context, 0, museum);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Museum museum = getItem(position);

        if (null == convertView) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_list_favorites, parent, false);
        }

        TextView name = convertView.findViewById(R.id.tvName);
        TextView numero = convertView.findViewById(R.id.tvNumero);
        TextView horaires = convertView.findViewById(R.id.tvHoraires);
        TextView site = convertView.findViewById(R.id.tvSite);
        TextView metro = convertView.findViewById(R.id.tvMetro);

        name.setText(museum.getName());
        numero.setText(museum.getNumero());
        horaires.setText(museum.getHoraires());
        site.setText(museum.getSite());
        metro.setText(museum.getMetro());

        return convertView;
    }
}


