package com.company.joeliomason.projectme.Views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.joeliomason.projectme.Adapters.AddCardioAdapter;
import com.company.joeliomason.projectme.Database.CardDatabaseAdapter2;
import com.company.joeliomason.projectme.POJOs.Card;
import com.company.joeliomason.projectme.POJOs.Exercise;
import com.company.joeliomason.projectme.POJOs.Set;
import com.company.joeliomason.projectme.R;

import java.util.ArrayList;

/**
 * Created by joelmason on 18/04/2015.
 */
public class AddCardioFragment1 extends android.support.v4.app.Fragment {

    CardDatabaseAdapter2 mCardDatabaseAdapter2;
    TextView workoutName;
    EditText weight, reps;
    double weightCount;
    int pos = 0, repsCount;
    ListView list;
    Button plus, plus2, minus, minus2, update, edit, delete,  start, stop;
    String name, date;
    int type, category, count;
    Exercise ex;
    Card card;
    View rootView;
    TextView timer;
    ArrayList<Set> array = new ArrayList<>();
    MyCountDownTimer mMyCountDownTimer;
    private AddCardioAdapter mAddExerciseAdapter;
    Dialog dialog;
    long id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.edit_cardio_view, container, false);
        setHasOptionsMenu(true);
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("ExerciseName");
            type = extras.getInt("ExerciseType");
            category = extras.getInt("ExerciseCategory");
            date = extras.getString("date");
            Log.v("date", date);
        }
        mCardDatabaseAdapter2 = new CardDatabaseAdapter2(getActivity());

        Log.v("created", "item created");

        //mCardDatabaseAdapter2.insert(name, date);
        card = new Card("", name, date);
        Log.v("name & date", name + "  " + date);

        ex = new Exercise(name, type, category);
        Log.v("Exercise Recieved", ex.toString());
        workoutName = (TextView) rootView.findViewById(R.id.name);
        weight = (EditText) rootView.findViewById(R.id.textWeight);
        reps = (EditText) rootView.findViewById(R.id.textRep);
        list = (ListView) rootView.findViewById(R.id.rowReps);
        plus = (Button) rootView.findViewById(R.id.plus);
        minus = (Button) rootView.findViewById(R.id.minus);
        plus2 = (Button) rootView.findViewById(R.id.plus2);
        minus2 = (Button) rootView.findViewById(R.id.minus2);
        update = (Button) rootView.findViewById(R.id.update);
        edit = (Button) rootView.findViewById(R.id.edit);
        edit.setVisibility(View.GONE);
        delete = (Button) rootView.findViewById(R.id.delete);
        delete.setVisibility(View.GONE);

        weightCount = 0;
        repsCount = 0;
        count = 1;

        array = new ArrayList<>();
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!weight.getText().toString().equals("")) {
                    weightCount = Double.parseDouble(weight.getText().toString());
                    weightCount = weightCount + 1;
                    weight.setText(String.valueOf(weightCount));
                } else {
                    weight.setText(String.valueOf(1));
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weightCount <= 0 || weight.getText().toString().equals("")) {
                    weight.setText(String.valueOf(0));
                } else {
                    weightCount = Double.parseDouble(weight.getText().toString());
                    weightCount = weightCount - 1;
                    weight.setText(String.valueOf(weightCount));
                }
            }
        });

        plus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reps.getText().toString().equals("")) {
                    repsCount = Integer.parseInt(reps.getText().toString());
                    repsCount = repsCount + 5;
                    reps.setText(String.valueOf(repsCount));
                } else {
                    reps.setText(String.valueOf(5));
                }
            }
        });

        minus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repsCount <= 0 || reps.getText().toString().equals("")) {
                    reps.setText(String.valueOf(5));
                } else {
                    repsCount = Integer.parseInt(reps.getText().toString());
                    repsCount = repsCount - 5;
                    reps.setText(String.valueOf(repsCount));
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(weight.getText().toString().equals("")) {
                    weight.setText(String.valueOf(0));
                    Toast.makeText(getActivity(), "You need to enter a weight!", Toast.LENGTH_SHORT).show();
                }
                else if(reps.getText().toString().equals("")|| reps.getText().toString().equals(String.valueOf(0))) {
                    reps.setText(String.valueOf(0));
                    Toast.makeText(getActivity(), "You need to have at least 1 rep!", Toast.LENGTH_SHORT).show();
                } else {
                    double temp2 = Double.parseDouble(weight.getText().toString());
                    int temp3 = Integer.parseInt(reps.getText().toString());
                    Set s = new Set(0, name, temp2, temp3, date, category);
                    array.add(s);
                    //mCardDatabaseAdapter2.insert2(mCardDatabaseAdapter2.highestID(), name, s.getWeight(), s.getReps(), date);
                    //Log.v("inserted data", "id " + mCardDatabaseAdapter2.highestID() + " name " + name + " weight " + s.getWeight() + " reps " + s.getReps() + " date " + date);
                    //Log.v("id", mCardDatabaseAdapter2.highestID() + "");
                    mAddExerciseAdapter.notifyDataSetChanged();
                    count++;
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                array.get(pos).setWeight(Double.valueOf(weight.getText().toString()));
                array.get(pos).setReps(Integer.valueOf(reps.getText().toString()));
                update.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                mAddExerciseAdapter.notifyDataSetChanged();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
                mCardDatabaseAdapter2.deleteEntry(String.valueOf(array.get(pos).getId()));
                Log.v(String.valueOf(id), array.get(pos).getName() + String.valueOf(array.get(pos).getWeight()) + String.valueOf(array.get(pos).getReps()));
                array.remove(pos);
                mAddExerciseAdapter.notifyDataSetChanged();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weight.setText(array.get(position).getWeight() + "");
                reps.setText(array.get(position).getReps() + "");
                update.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                pos = position;
                Toast.makeText(getActivity(), "something" + pos, Toast.LENGTH_SHORT).show();
            }
        });
        mAddExerciseAdapter = new AddCardioAdapter(getActivity(), R.layout.cardio_row2, array);
        list.setAdapter(mAddExerciseAdapter);

        mAddExerciseAdapter.notifyDataSetChanged();

        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_exercise, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_done) {
            Intent intent = new Intent(getActivity(), MainMenuActivity.class);
            if(!array.isEmpty()) {
                mCardDatabaseAdapter2.insert(name, date);
                for (Set s : array) {
                    mCardDatabaseAdapter2.insert2(Long.toString(mCardDatabaseAdapter2.highestID()), name, s.getWeight(), s.getReps(), date, category);
                }
                mCardDatabaseAdapter2.resetID();
            }
            startActivity(intent);
        }
        if(id == R.id.action_timer) {
            dialog = new Dialog(getActivity());
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.countdown_timerr, null);
            dialog.setContentView(view);
            timer=(TextView)dialog.findViewById(R.id.timer);
            start=(Button)dialog.findViewById(R.id.start);
            stop=(Button)dialog.findViewById(R.id.stop);
            stop.setVisibility(View.GONE);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!timer.getText().toString().equals("")) {
                        mMyCountDownTimer = new MyCountDownTimer(Long.valueOf(timer.getText().toString()) * 1000, 1000);
                        mMyCountDownTimer.start();
                        stop.setVisibility(View.VISIBLE);
                        start.setVisibility(View.GONE);
                    }
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMyCountDownTimer.cancel();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        array = (ArrayList<Set>) args.getSerializable("sets");
        mAddExerciseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void updateArguments(Bundle args) {

        ArrayList<Set> x;
        x = (ArrayList<Set>) args.getSerializable("sets");
        for(Set p: x) {
            p.setDate(date);
            array.add(p);
        }
        mAddExerciseAdapter.notifyDataSetChanged();
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            stop.setVisibility(View.GONE);
            start.setVisibility(View.VISIBLE);
            timer.setHint("0");
            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);
            dialog.dismiss();
        }
        @Override
        public void onTick(long millisUntilFinished) {

            timer.setText(String.valueOf((millisUntilFinished / 1000)));

        }

    }


}
