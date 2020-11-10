
package com.vedas.spectrocare.DataBase;

import android.util.Log;

import com.j256.ormlite.stmt.UpdateBuilder;
import com.vedas.spectrocare.Controllers.PhysicalServerObjectDataController;
import com.vedas.spectrocare.DataBaseModels.AppointmentModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.ArrayList;

public class AppointmentDataController {
    public ArrayList<AppointmentModel> allAppointmentList = new ArrayList<>();
    public AppointmentModel currentAppointmentModel;
    public static AppointmentDataController myObj;

    public static AppointmentDataController getInstance() {
        if (myObj == null) {
            myObj = new AppointmentDataController();
        }
        return myObj;
    }

    public boolean insertAppointmentData(AppointmentModel userdata) {
        try {
            MedicalProfileDataController.getInstance().helper.getAppointmentModelsDao().create(userdata);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<AppointmentModel> fetchAppointmentData(PatientlProfileModel physicalExamsDataModel) {
        allAppointmentList = null;
        allAppointmentList = new ArrayList<>();
        if (physicalExamsDataModel != null) {
            ArrayList<AppointmentModel> urineresultsModels = new ArrayList<AppointmentModel>(physicalExamsDataModel.getAppointmentModels());
            if (urineresultsModels != null) {
                allAppointmentList = urineresultsModels;
                Log.e("fetchAppointmentData", "Appointment fectched successfully" + allAppointmentList.size());
            }
        }
        return allAppointmentList;
    }

    public void deleteAppointmentData(PatientlProfileModel patientlProfileModels) {
        if (patientlProfileModels != null) {
            ArrayList<AppointmentModel> urineresultsModels = new ArrayList<AppointmentModel>(patientlProfileModels.getAppointmentModels());
            if (urineresultsModels != null) {
                for (int i = 0; i < urineresultsModels.size(); i++) {
                    AppointmentModel appointmentModel = urineresultsModels.get(i);
                    Log.e("dbchandu", "Appointment" + appointmentModel.getPatientID());
                    Log.e("deletefetching", "Appointment fectched successfully" + deleteMemberData(appointmentModel));
                    deleteMemberData(appointmentModel);
                    fetchAppointmentData(PatientProfileDataController.getInstance().currentPatientlProfile);
                }
                try {
                    MedicalProfileDataController.getInstance().helper.getAppointmentModelsDao().delete(urineresultsModels);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Log.e("deletefetching", "Appointment fectched successfully" + allAppointmentList.size());
            }
        }

    }

    public void deleteAppointmentModelData(PatientlProfileModel patientlProfileModel, AppointmentModel appointmentModel) {
        if (patientlProfileModel != null) {
            ArrayList<AppointmentModel> list = new ArrayList<AppointmentModel>(patientlProfileModel.getAppointmentModels());
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getAppointmentID().equals(appointmentModel.getAppointmentID())) {
                        AppointmentModel appointmentModel1 = list.get(i);
                        Log.e("deletefetching", "Appointment fectched successfully" + deleteMemberData(appointmentModel1));
                        deleteMemberData(appointmentModel1);
                        fetchAppointmentData(PatientProfileDataController.getInstance().currentPatientlProfile);
                    }
                }
            }
        }
    }

    public boolean deleteMemberData(AppointmentModel member) {
        try {
            MedicalProfileDataController.getInstance().helper.getAppointmentModelsDao().delete(member);
            Log.e("Delete", "delete all Appointment");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateAppointmentData(AppointmentModel appointmentModel) {
        try {
            UpdateBuilder<AppointmentModel, Integer> updateBuilder = MedicalProfileDataController.getInstance().helper.getAppointmentModelsDao().updateBuilder();
            updateBuilder.updateColumnValue("cancelledPersonID", appointmentModel.getCancelledPersonID());
            updateBuilder.updateColumnValue("cancelReason", appointmentModel.getCancelReason());
            updateBuilder.updateColumnValue("cancelledByWhom", appointmentModel.getCancelledByWhom());
            updateBuilder.updateColumnValue("appointmentStatus", appointmentModel.getAppointmentStatus());
            updateBuilder.updateColumnValue("appointmentID",appointmentModel.getAppointmentID() );
            updateBuilder.updateColumnValue("createrMedicalPersonnelID",appointmentModel.getCreaterMedicalPersonnelID() );
            updateBuilder.updateColumnValue("creatorName",appointmentModel.getCreatorName() );
            updateBuilder.updateColumnValue("medicalRecordID", appointmentModel.getMedicalRecordID());
            updateBuilder.updateColumnValue("reasonForVisit",appointmentModel.getReasonForVisit() );
            updateBuilder.updateColumnValue("department",appointmentModel.getDepartment() );
            updateBuilder.updateColumnValue("patientID", appointmentModel.getPatientID());
            updateBuilder.updateColumnValue("patientName", appointmentModel.getPatientName());
            updateBuilder.updateColumnValue("doctorMedicalPersonnelID", appointmentModel.getDoctorMedicalPersonnelID());
            updateBuilder.updateColumnValue("doctorProfilePic", appointmentModel.getDoctorProfilePic());
            updateBuilder.updateColumnValue("doctorName", appointmentModel.getDoctorName());
            updateBuilder.updateColumnValue("visitType",appointmentModel.getVisitType() );
            updateBuilder.updateColumnValue("appointmentTimeTo", appointmentModel.getAppointmentTimeTo());
            updateBuilder.updateColumnValue("appointmentTimeFrom",appointmentModel.getAppointmentTimeFrom() );
            updateBuilder.updateColumnValue("appointmentDate", appointmentModel.getAppointmentDate());
            updateBuilder.updateColumnValue("hospital_reg_num", appointmentModel.getHospital_reg_num());
            updateBuilder.where().eq("appointmentID", appointmentModel.getAppointmentID());
            updateBuilder.update();
            Log.e("update data", "updated the Appointment data sucessfully" + appointmentModel.getAppointmentID());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
