package com.amine.horaires;

import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.amine.horaires.models.Horaires;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;

class UpdateAdapter extends ArrayAdapter<Horaires> {


    private final ArrayList<Horaires> horaires;
    private final Context context;
    private final FragmentManager fm;

    public UpdateAdapter(Context context, ArrayList<Horaires> objects, FragmentManager fm) {
        super(context, R.layout.update_horaire_item, objects);
        this.context = context;
        this.horaires = objects;
        this.fm = fm;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.update_horaire_item, parent, false);
        final Horaires h = horaires.get(position);
        final TextView timeFrom = (TextView) rowView.findViewById(R.id.from);
        timeFrom.setText(String.format("%02d", h.getFrom_h()) + ":" + String.format("%02d", h.getFrom_m()));
        final TextView timeTo = (TextView) rowView.findViewById(R.id.to);
        timeTo.setText(String.format("%02d", h.getTo_h()) + ":" + String.format("%02d", h.getTo_m()));
        String[] days = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context, R.layout.spinner_item, days);
        final Spinner s = (Spinner) rowView.findViewById(R.id.spinner);
        s.setAdapter(adapter);
        s.setSelection(h.getDay() - 1);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                h.setDay(position + 1);
                s.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        timeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog f = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hours, int minutes) {
                        if (minutes % 5 == 0) {
                            timeFrom.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes));
                            h.setFrom_h(hours);
                            h.setFrom_m(minutes);
                        } else {
                            Toast.makeText(context, getContext().getResources().getString(R.string.time_slot_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, h.getFrom_h(), h.getFrom_m(), true);
                f.show(fm, "from");
            }
        });

        timeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog t = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hours, int minutes) {
                        if (minutes % 5 == 0) {
                            timeTo.setText(String.format("%02d", hours) + ":" + String.format("%02d", minutes));
                            h.setTo_h(hours);
                            h.setTo_m(minutes);
                        } else {
                            Toast.makeText(context, getContext().getResources().getString(R.string.time_slot_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, h.getTo_h(), h.getTo_m(), true);
                t.show(fm, "to");
            }
        });

        return rowView;
    }
}
