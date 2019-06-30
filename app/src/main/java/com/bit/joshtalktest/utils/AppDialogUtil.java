package com.bit.joshtalktest.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bit.joshtalktest.R;
import com.bit.joshtalktest.utils.Constants.SortSelection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppDialogUtil {
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder1;
    private static AppDialogUtil appDialogUtil;

    public static AppDialogUtil getAppDialogUtilInstance() {
        synchronized (AppDialogUtil.class) {
            if (appDialogUtil == null) {
                appDialogUtil = new AppDialogUtil();
            }
            return appDialogUtil;
        }
    }

    public void showCallingPopUp(final Context context,ChoiceSelectionCallback callback ) {
        final Dialog dialog = new Dialog(context);
        dialog.setTitle(context.getString(R.string.dialog_title));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sort_selection_view);

        ListView listView = dialog.findViewById(R.id.list);
        List<String> mStringList=new ArrayList<>();
        for (SortSelection sortSelection:SortSelection.values()){
            mStringList.add(sortSelection.getType());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, mStringList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            dialog.dismiss();
            callback.choice(parent.getItemAtPosition(position).toString());

        });
        dialog.show();
    }


    public interface ChoiceSelectionCallback{
        void choice(String choice);
    }
}
