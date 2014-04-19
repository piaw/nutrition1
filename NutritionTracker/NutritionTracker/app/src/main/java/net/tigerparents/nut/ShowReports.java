package net.tigerparents.nut;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import net.tigerparents.nut.nutritioninfo.NutritionData;
import net.tigerparents.nut.nutritioninfo.NutritionInformation;
import net.tigerparents.nut.nutritioninfo.NutritionReport;

import java.util.ArrayList;


public class ShowReports extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reports);
        Spinner spinner = (Spinner) findViewById(R.id.reportsSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.report_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new ReportSelectedListener(this));
    }

    void updateReport(String report_type) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_reports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ReportSelectedListener implements AdapterView.OnItemSelectedListener {
        Activity _activity;

        ReportSelectedListener(Activity activity) {
            _activity = activity;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String report_type = (String) adapterView.getItemAtPosition(i);
            Log.d("report_type selected:", report_type);
            NutritionData.ReportTypes type;
            if (report_type.equals("Daily")) {
                type = NutritionData.ReportTypes.DAILY;
            } else if (report_type.equals("Weekly")) {
                type = NutritionData.ReportTypes.WEEKLY;
            } else if (report_type.equals("Monthly")) {
                type = NutritionData.ReportTypes.MONTHLY;
            } else {
                Log.e("Invalid report type", report_type);
                return;
            }
            ArrayList<NutritionInformation> report =
                    NutritionReport.getNutritionInformationReport(type);
            ListView listview = (ListView) findViewById(R.id.reports_lv);
            String header_text = String.format("Report: %s", report_type);
            UIUtils.showNutritionInfo(_activity, listview, report, header_text);
            updateReport(report_type);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}
