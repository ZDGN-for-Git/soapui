package com.eviware.soapui.actions;

import com.eviware.soapui.model.settings.Settings;
import com.eviware.soapui.settings.AllureReportSettings;
import com.eviware.soapui.support.components.DirectoryFormComponent;
import com.eviware.soapui.support.components.SimpleForm;
import com.eviware.soapui.support.types.StringToStringMap;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AllureReportPrefs implements Prefs {

    public static final String ALLURE_COMMAND_LINE_PATH = "Allure Report Command Line Path";
    public static final String EXPORT_ALLURE_RESULTS_FILES = "Export Allure Results Files";
    public static final String GENERATE_ALLURE_REPORT = "Automatically Generate Report";
    public static final String OPEN_ALLURE_REPORT = "Automatically Open Report";

    //private JTextField allureCommandLinePathTextField;
    private final String title;
    private JCheckBox exportCheckBox;
    private JCheckBox generateCheckBox;
    private JCheckBox openCheckBox;

    private SimpleForm allureForm;

    public AllureReportPrefs(String title) { this.title = title; }

    public String getTitle() {
        return title;
    }

    public SimpleForm getForm() {
        if (allureForm == null) {
            allureForm = new SimpleForm();
            allureForm.addSpace(5);
            //allureCommandLinePathTextField = allureForm.appendTextField(AllureReportPrefs.ALLURE_COMMAND_LINE_PATH,
            //        "Path to the executable command line of Allure Report");
            allureForm.append(AllureReportPrefs.ALLURE_COMMAND_LINE_PATH, new DirectoryFormComponent("Path to the executable command line of Allure Report"));
            exportCheckBox = allureForm.appendCheckBox(AllureReportPrefs.EXPORT_ALLURE_RESULTS_FILES,"Automatically create allure report results files", false);
            generateCheckBox = allureForm.appendCheckBox(AllureReportPrefs.GENERATE_ALLURE_REPORT,"Automatically generate allure report files", false);
            generateCheckBox.setEnabled(false);
            openCheckBox = allureForm.appendCheckBox(AllureReportPrefs.OPEN_ALLURE_REPORT,"Automatically open allure report", false);
            openCheckBox.setEnabled(false);

            exportCheckBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    generateCheckBox.setEnabled(exportCheckBox.isSelected());
                    openCheckBox.setEnabled(exportCheckBox.isSelected());
                }
            });
        }

        return allureForm;
    }

    public void getFormValues(Settings settings) {
        StringToStringMap values = new StringToStringMap();
        allureForm.getValues(values);
        storeValues(values, settings);
    }

    public void storeValues(StringToStringMap values, Settings settings) {
        //if (allureCommandLinePathTextField != null) {
        if (allureForm.getComponent(AllureReportPrefs.ALLURE_COMMAND_LINE_PATH) != null) {
            settings.setString(AllureReportSettings.ALLURE_COMMAND_LINE_PATH, "");
        }

        settings.setBoolean(AllureReportSettings.EXPORT_ALLURE_RESULTS_FILES, values.getBoolean(EXPORT_ALLURE_RESULTS_FILES));
        settings.setBoolean(AllureReportSettings.GENERATE_ALLURE_REPORT, values.getBoolean(GENERATE_ALLURE_REPORT));
        settings.setBoolean(AllureReportSettings.OPEN_ALLURE_REPORT, values.getBoolean(OPEN_ALLURE_REPORT));
    }

    public void setFormValues(Settings settings) {
        allureForm.setValues(getValues(settings));
    }

    public StringToStringMap getValues(Settings settings) {
        StringToStringMap values = new StringToStringMap();
        values.put(ALLURE_COMMAND_LINE_PATH, settings.getString(AllureReportSettings.ALLURE_COMMAND_LINE_PATH, ""));
        values.put(EXPORT_ALLURE_RESULTS_FILES, settings.getBoolean(AllureReportSettings.EXPORT_ALLURE_RESULTS_FILES));
        values.put(GENERATE_ALLURE_REPORT, settings.getBoolean(AllureReportSettings.GENERATE_ALLURE_REPORT));
        values.put(OPEN_ALLURE_REPORT, settings.getBoolean(AllureReportSettings.OPEN_ALLURE_REPORT));

        return values;
    }

}
