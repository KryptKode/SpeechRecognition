package com.text.speech.ui.dialogs;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.text.speech.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class GuideDialog extends DialogFragment {

    private TableLayout tableLayout;
    private GuideListener guideListener;

    public void setGuideListener(GuideListener guideListener) {
        this.guideListener = guideListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_phones, null, false);
        tableLayout = view.findViewById(R.id.table);
        populateTable();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton(R.string.dismiss, (dialog, which) -> {
                    if (guideListener != null) {
                        guideListener.onDismiss();
                    }
                  dialog.dismiss();
                });

        return builder.create();

    }


    private void populateTable(){
        String []  englishWords = getContext().getResources().getStringArray(R.array.words_english);
        String []  igboWords = getContext().getResources().getStringArray(R.array.words_igbo);
        String []  phonemes = getContext().getResources().getStringArray(R.array.phonemes);

        int wordCount = igboWords.length;

        //ensure that they are the same size
        if(!(englishWords.length == wordCount) && !(phonemes.length == wordCount  )){
            throw  new IllegalStateException("The three arrays for words in igbo and english and the corresponding igbo phones must be the same");
        }

        //populate the englishWords
        for (int i = 0; i < englishWords.length; i++) {
            String englishWord = englishWords[i];
            String igboWord = igboWords[i];
            String phone = phonemes[i];

            TextView englishTextView = new TextView(getContext(), null, R.style.TableTextViewStyle);
            englishTextView.setText(englishWord);
            englishTextView.setBackgroundResource(R.drawable.border);
            englishTextView.setGravity(Gravity.CENTER);
            englishTextView.setPadding(4, 4, 4, 4);

            TextView igboTextView = new TextView(getContext(), null, R.style.TableTextViewStyle);
            igboTextView.setText(igboWord);
            igboTextView.setBackgroundResource(R.drawable.border);
            igboTextView.setGravity(Gravity.CENTER);
            igboTextView.setPadding(4, 4, 4, 4);


            TextView phoneTextView = new TextView(getContext(), null, R.style.TableTextViewStyle);
            phoneTextView.setText(phone);
            phoneTextView.setBackgroundResource(R.drawable.border);
            phoneTextView.setGravity(Gravity.CENTER);
            phoneTextView.setPadding(4, 4, 4, 4);
            phoneTextView.setTypeface(phoneTextView.getTypeface(), Typeface.BOLD);

            TableRow tableRow = new TableRow(getContext());
            tableRow.addView(englishTextView);
            tableRow.addView(igboTextView);
            tableRow.addView(phoneTextView);

            tableLayout.addView(tableRow);
        }
    }

    public interface GuideListener {
        void onDismiss();
    }
}
