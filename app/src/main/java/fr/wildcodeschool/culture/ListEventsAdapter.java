package fr.wildcodeschool.culture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListEventsAdapter extends ArrayAdapter<Event> {

    public ListEventsAdapter(Context context, List<Event> event) {
        super(context, 0, event);
        Collections.sort(event, new Comparator<Event>() {
            public int compare(Event event1, Event event2) {
                return Float.compare(event1.getDistance(), event2.getDistance());
            }
        });

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Event event = getItem(position);

        if (null == convertView) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_list_events, parent, false);
        }

        TextView adresse = convertView.findViewById(R.id.tvAdresse);
        TextView descriptif = convertView.findViewById(R.id.tvDescriptif);
        TextView horaires = convertView.findViewById(R.id.tvHoraires);
        TextView name = convertView.findViewById(R.id.tvName);
        TextView tarif = convertView.findViewById(R.id.tvTarif);
        TextView distance = convertView.findViewById(R.id.tvDistanceEvent);


        adresse.setText(event.getAdresse());
        descriptif.setText(event.getDescriptif());
        horaires.setText(event.getHoraires());
        name.setText(event.getName());
        tarif.setText(event.getTarif());
        distance.setText(Float.toString(event.getDistance())+" m");


        return convertView;
    }
}
