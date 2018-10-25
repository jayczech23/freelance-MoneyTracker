package org.assignment.ctse.moneytracker.View;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.assignment.ctse.moneytracker.BorrowerDetailsActivity;
import org.assignment.ctse.moneytracker.Model.Borrower;
import org.assignment.ctse.moneytracker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Adapter for displaying Borrower
 * Data in ListView
 */
public class BorrowerAdapter extends ArrayAdapter<Borrower> {

    Context mContext;
    private List<Borrower> borrowerList = new ArrayList<>();

    public BorrowerAdapter(@NonNull Context context, ArrayList<Borrower> borrowers) {
        super(context, 0, borrowers);

        this.mContext = context;
        borrowerList = borrowers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.adapter_custom_borrower,
                    parent,
                    false);
        }

        final Borrower currentBorrower = borrowerList.get(position);

        // set Borrower initial
        TextView initial = listItem.findViewById(R.id.adapter_borrower_initial);
        char borrowerInitial = currentBorrower.getName().charAt(0);
        String initialString = Character.toString(borrowerInitial);
        initial.setText(initialString);
        // set random color for background
        setRandomColor(initial);

        // set Borrower Name
        TextView name = listItem.findViewById(R.id.adapter_borrower_name_label);
        name.setText(currentBorrower.getName());

        // set Date
        TextView date = listItem.findViewById(R.id.adapter_date_label);
        date.setText(currentBorrower.getDate());

        // set Amount
        TextView amount = listItem.findViewById(R.id.adapter_amount_label);
        amount.setText(currentBorrower.getAmount());

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BorrowerDetailsActivity.class);
                intent.putExtra("borrower_name", currentBorrower.getName());
                intent.putExtra("amount_owed", currentBorrower.getAmount());
                intent.putExtra("date", currentBorrower.getDate());
                mContext.startActivity(intent);
            }
        });

        return listItem;
    }

    private void setRandomColor(TextView tv) {

        // set color
        Random rnd = new Random();
        int color = Color.rgb(255, rnd.nextInt(256), rnd.nextInt(256));

        // set drawable
        ShapeDrawable circle = new ShapeDrawable();
        circle.setShape(new OvalShape());
        circle.getPaint().setColor(color);

        // apply drawable
        tv.setBackground(circle);
    }
}
