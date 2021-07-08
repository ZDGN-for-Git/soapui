package com.eviware.soapui.settings;

import com.eviware.soapui.settings.Setting.SettingType;

/**
 * Allure Report settings constants
 *
 * @author David Guyon
 * @since july 2021
 * @version 1.0
 */

public interface AllureReportSettings {

    @Setting(name = "Command Line", description = "Allure Report Command Line Path", type = SettingType.FOLDER)
    public static final String ALLURE_COMMAND_LINE_PATH = AllureReportSettings.class.getSimpleName() + "@" + "Allure_command_line_path";

    @Setting(name = "Export Results", description = "Export Allure Results Files", type = SettingType.BOOLEAN)
    public static final String EXPORT_ALLURE_RESULTS_FILES = AllureReportSettings.class.getSimpleName() + "@" + "Exporet_allure_results_files";

    @Setting(name = "Generate Allure Report", description = "Automatically Generate Report", type = SettingType.BOOLEAN)
    public static final String GENERATE_ALLURE_REPORT = AllureReportSettings.class.getSimpleName() + "@" + "Generate_allure_report";

    @Setting(name = "Open Allure Report", description = "Automatically Open Report", type = SettingType.BOOLEAN)
    public static final String OPEN_ALLURE_REPORT = AllureReportSettings.class.getSimpleName() + "@" + "Open_allure_report";

}
