package com.vedas.spectrocare.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vedas.spectrocare.DataBaseModels.AllergieModel;
import com.vedas.spectrocare.DataBaseModels.AllergyTrackInfoModel;
import com.vedas.spectrocare.DataBaseModels.AppointmentModel;
import com.vedas.spectrocare.DataBaseModels.BMIModel;
import com.vedas.spectrocare.DataBaseModels.DoctorInfoModel;
import com.vedas.spectrocare.DataBaseModels.FamilyHistoryModel;
import com.vedas.spectrocare.DataBaseModels.FamilyTrackInfoModel;
import com.vedas.spectrocare.DataBaseModels.IllnessRecordModel;
import com.vedas.spectrocare.DataBaseModels.MedicalProfileModel;
import com.vedas.spectrocare.DataBaseModels.MedicalScreeningRecordModel;
import com.vedas.spectrocare.DataBaseModels.MedicationAttachmentModel;
import com.vedas.spectrocare.DataBaseModels.MedicationRecordModel;
import com.vedas.spectrocare.DataBaseModels.MedicinesRecordModel;
import com.vedas.spectrocare.DataBaseModels.PatientModel;
import com.vedas.spectrocare.DataBaseModels.PatientlProfileModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalExamsDataModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalTrackInfoModel;
import com.vedas.spectrocare.DataBaseModels.PhysicalCategoriesRecords;
import com.vedas.spectrocare.DataBaseModels.ScreeningRecordModel;
import com.vedas.spectrocare.DataBaseModels.SurgeryAttachModel;
import com.vedas.spectrocare.DataBaseModels.SurgicalRecordModel;
import com.vedas.spectrocare.DataBaseModels.TrackInfoModel;
import com.vedas.spectrocare.DataBaseModels.VaccineModel;
import com.vedas.spectrocare.DataBaseModels.VaccineTrackInfoModel;

import java.sql.SQLException;

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "spectrocare.db";
    private static final int DATABASE_VERSION = 1;
    private Dao<MedicalProfileModel, Integer> medicalProfileDao = null;
    private Dao<PatientlProfileModel, Integer> patientlProfileDao = null;
    private Dao<BMIModel, Integer> bmiDao = null;
    private Dao<PhysicalCategoriesRecords, Integer> physicalCategoriesDao = null;
    private Dao<TrackInfoModel, Integer> trackInfoIntegerDao = null;
    private Dao<PhysicalTrackInfoModel, Integer> phisicalTrackInfoModels = null;
    private Dao<PhysicalExamsDataModel, Integer> physicalExamsDataModelsDao = null;
    private Dao<FamilyHistoryModel, Integer> familyHistoryModelDao = null;
    private Dao<FamilyTrackInfoModel, Integer> familyTrackInfoDao = null;
    private Dao<AllergieModel, Integer> allergieModelsDao = null;
    private Dao<AllergyTrackInfoModel, Integer> allergyTrackInfoDao = null;
    private Dao<VaccineModel, Integer> vaccineModelsDao = null;
    private Dao<VaccineTrackInfoModel, Integer> vaccineTrackInfoDao = null;
    private Dao<IllnessRecordModel, Integer> illnessRecordDao = null;
    private Dao<ScreeningRecordModel, Integer> screeningRecordModels = null;

    private Dao<MedicalScreeningRecordModel,Integer> medicalScreeningRecordModels = null;
    private Dao<SurgicalRecordModel,Integer> surgicalRecordModels = null;
   // private Dao<SurgeryAttachModel,Integer> surgeryAttachModels = null;

    private Dao<MedicationRecordModel,Integer> medicationRecordDao = null;
    private Dao<MedicinesRecordModel,Integer> medicinesRecordModelsDao = null;
    private Dao<MedicationAttachmentModel,Integer> medicationAttachmentModelsDao = null;
    private Dao<AppointmentModel,Integer> appointmentModelsDao = null;
    private Dao<DoctorInfoModel,Integer> doctorInfoModelsDao = null;

    private Dao<PatientModel, Integer> patientModels = null;


    ConnectionSource objConnectionSource;

    public DataBaseHelper(Context contex) {
        super(contex, DATABASE_NAME, null, DATABASE_VERSION);
        getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Log.e("DBStatusonCreate", "OnCreate" + connectionSource);
        try {
            TableUtils.createTable(connectionSource, MedicalProfileModel.class);
            TableUtils.createTable(connectionSource, PatientlProfileModel.class);
            TableUtils.createTable(connectionSource, BMIModel.class);
            TableUtils.createTable(connectionSource, PhysicalCategoriesRecords.class);
            TableUtils.createTable(connectionSource, TrackInfoModel.class);
            TableUtils.createTable(connectionSource, PhysicalTrackInfoModel.class);
            TableUtils.createTable(connectionSource, PhysicalExamsDataModel.class);
            TableUtils.createTable(connectionSource, FamilyHistoryModel.class);
            TableUtils.createTable(connectionSource, FamilyTrackInfoModel.class);
            TableUtils.createTable(connectionSource, AllergieModel.class);
            TableUtils.createTable(connectionSource, AllergyTrackInfoModel.class);
            TableUtils.createTable(connectionSource, VaccineModel.class);
            TableUtils.createTable(connectionSource, VaccineTrackInfoModel.class);
            TableUtils.createTable(connectionSource, IllnessRecordModel.class);
            TableUtils.createTable(connectionSource, ScreeningRecordModel.class);

            TableUtils.createTable(connectionSource, MedicalScreeningRecordModel.class);
            TableUtils.createTable(connectionSource,SurgicalRecordModel.class);
            TableUtils.createTable(connectionSource,MedicationRecordModel.class);
            TableUtils.createTable(connectionSource,MedicinesRecordModel.class);
            TableUtils.createTable(connectionSource,MedicationAttachmentModel.class);

            TableUtils.createTable(connectionSource,AppointmentModel.class);
            TableUtils.createTable(connectionSource,DoctorInfoModel.class);

            TableUtils.createTable(connectionSource,PatientModel.class);


            medicalProfileDao = DaoManager.createDao(connectionSource, MedicalProfileModel.class);
            patientlProfileDao = DaoManager.createDao(connectionSource, PatientlProfileModel.class);
            bmiDao = DaoManager.createDao(connectionSource, BMIModel.class);
            physicalCategoriesDao = DaoManager.createDao(connectionSource, PhysicalCategoriesRecords.class);
            trackInfoIntegerDao = DaoManager.createDao(connectionSource, TrackInfoModel.class);
            phisicalTrackInfoModels = DaoManager.createDao(connectionSource, PhysicalTrackInfoModel.class);
            physicalExamsDataModelsDao = DaoManager.createDao(connectionSource, PhysicalExamsDataModel.class);
            familyHistoryModelDao = DaoManager.createDao(connectionSource, FamilyHistoryModel.class);
            familyTrackInfoDao = DaoManager.createDao(connectionSource, FamilyTrackInfoModel.class);
            allergieModelsDao = DaoManager.createDao(connectionSource, AllergieModel.class);
            allergyTrackInfoDao = DaoManager.createDao(connectionSource, AllergyTrackInfoModel.class);
            vaccineModelsDao = DaoManager.createDao(connectionSource, VaccineModel.class);
            vaccineTrackInfoDao = DaoManager.createDao(connectionSource, VaccineTrackInfoModel.class);
            illnessRecordDao = DaoManager.createDao(connectionSource, IllnessRecordModel.class);
            screeningRecordModels = DaoManager.createDao(connectionSource, ScreeningRecordModel.class);

            medicalScreeningRecordModels = DaoManager.createDao(connectionSource,MedicalScreeningRecordModel.class);
            surgicalRecordModels = DaoManager.createDao(connectionSource,SurgicalRecordModel.class);
            medicationRecordDao = DaoManager.createDao(connectionSource, MedicationRecordModel.class);
             medicinesRecordModelsDao= DaoManager.createDao(connectionSource, MedicinesRecordModel.class);
            medicationAttachmentModelsDao= DaoManager.createDao(connectionSource, MedicationAttachmentModel.class);
            appointmentModelsDao= DaoManager.createDao(connectionSource, AppointmentModel.class);
            doctorInfoModelsDao= DaoManager.createDao(connectionSource, DoctorInfoModel.class);

            patientModels= DaoManager.createDao(connectionSource, PatientModel.class);

            objConnectionSource = connectionSource;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.e("DBStatus", "OnUpgrade" + connectionSource);
        try {
            TableUtils.dropTable(connectionSource, MedicalProfileModel.class, true);
            TableUtils.dropTable(connectionSource, PatientlProfileModel.class, true);
            TableUtils.dropTable(connectionSource, BMIModel.class, true);
            TableUtils.dropTable(connectionSource, PhysicalCategoriesRecords.class, true);
            TableUtils.dropTable(connectionSource, TrackInfoModel.class, true);
            TableUtils.dropTable(connectionSource, PhysicalExamsDataModel.class, true);
            TableUtils.dropTable(connectionSource, PhysicalTrackInfoModel.class, true);
            TableUtils.dropTable(connectionSource, FamilyHistoryModel.class, true);
            TableUtils.dropTable(connectionSource, FamilyTrackInfoModel.class, true);
            TableUtils.dropTable(connectionSource, AllergieModel.class, true);
            TableUtils.dropTable(connectionSource, AllergyTrackInfoModel.class, true);
            TableUtils.dropTable(connectionSource, VaccineModel.class, true);
            TableUtils.dropTable(connectionSource, VaccineTrackInfoModel.class, true);
            TableUtils.dropTable(connectionSource, IllnessRecordModel.class, true);
            TableUtils.dropTable(connectionSource, ScreeningRecordModel.class, true);

            TableUtils.dropTable(connectionSource, MedicalScreeningRecordModel.class,true);
            TableUtils.dropTable(connectionSource, SurgicalRecordModel.class,true);

            TableUtils.dropTable(connectionSource, MedicationRecordModel.class, true);
            TableUtils.dropTable(connectionSource, MedicinesRecordModel.class,false);

            TableUtils.dropTable(connectionSource, MedicationAttachmentModel.class,false);
            TableUtils.dropTable(connectionSource, AppointmentModel.class,false);
            TableUtils.dropTable(connectionSource, DoctorInfoModel.class,false);

            TableUtils.dropTable(connectionSource, PatientModel.class,false);

            onCreate(database, connectionSource);
            objConnectionSource = connectionSource;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   /* public Dao<SurgeryAttachModel, Integer> getSurgicalAttachDao(){
        if (surgeryAttachModels == null ){
            try {
                surgeryAttachModels = getDao(SurgeryAttachModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return surgeryAttachModels;
    }*/

    public Dao<PhysicalTrackInfoModel, Integer> getPhisicalTrackDao() {
        if (phisicalTrackInfoModels == null) {
            try {
                phisicalTrackInfoModels = getDao(PhysicalTrackInfoModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return phisicalTrackInfoModels;
    }

    public Dao<TrackInfoModel, Integer> getTrackInfoDao() {
        if (trackInfoIntegerDao == null) {
            try {
                trackInfoIntegerDao = getDao(TrackInfoModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return trackInfoIntegerDao;
    }

    public Dao<MedicalProfileModel, Integer> getMedicalProfileDao() {
        if (medicalProfileDao == null) {
            try {
               // userDao = DaoManager.createDao(objConnectionSource, User.class);
                medicalProfileDao = getDao(MedicalProfileModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return medicalProfileDao;
    }

    public Dao<PatientlProfileModel, Integer> getpatientlProfileDao() {
        if (patientlProfileDao == null) {
            try {
                patientlProfileDao = getDao(PatientlProfileModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return patientlProfileDao;
    }

    public Dao<BMIModel, Integer> getBmiDao() {
        if (bmiDao == null) {
            try {
                bmiDao = getDao(BMIModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bmiDao;
    }

    public Dao<PhysicalCategoriesRecords, Integer> getPhysicalCategoriesDao() {
        if (physicalCategoriesDao == null) {
            try {
                physicalCategoriesDao = getDao(PhysicalCategoriesRecords.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return physicalCategoriesDao;
    }

    public Dao<PhysicalExamsDataModel, Integer> getPhysicalExamsDataModelsDao() {
        if (physicalExamsDataModelsDao == null) {
            try {
                physicalExamsDataModelsDao = getDao(PhysicalExamsDataModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return physicalExamsDataModelsDao;
    }

    public Dao<FamilyHistoryModel, Integer> getFamilyHistoryModelDao() {
        if (familyHistoryModelDao == null) {
            try {
                familyHistoryModelDao = getDao(FamilyHistoryModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return familyHistoryModelDao;
    }

    public Dao<FamilyTrackInfoModel, Integer> getFamilyTrackInfoDao() {
        if (familyTrackInfoDao == null) {
            try {
                familyTrackInfoDao = getDao(FamilyTrackInfoModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return familyTrackInfoDao;
    }

    public Dao<AllergieModel, Integer> getAllergieModelsDao() {
        if (allergieModelsDao == null) {
            try {
                allergieModelsDao = getDao(AllergieModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return allergieModelsDao;
    }

    public Dao<AllergyTrackInfoModel, Integer> getAllergyTrackInfoDao() {
        if (allergyTrackInfoDao == null) {
            try {
                allergyTrackInfoDao = getDao(AllergyTrackInfoModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return allergyTrackInfoDao;
    }

    public Dao<VaccineModel, Integer> getVaccineModelsDao() {
        if (vaccineModelsDao == null) {
            try {
                vaccineModelsDao = getDao(VaccineModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return vaccineModelsDao;
    }

    public Dao<VaccineTrackInfoModel, Integer> getVaccineTrackInfoDao() {
        if (vaccineTrackInfoDao == null) {
            try {
                vaccineTrackInfoDao = getDao(VaccineTrackInfoModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return vaccineTrackInfoDao;
    }

    public Dao<IllnessRecordModel, Integer> getIllnessRecordDao() {
        if (illnessRecordDao == null) {
            try {
                illnessRecordDao = getDao(IllnessRecordModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return illnessRecordDao;
    }

    public Dao<ScreeningRecordModel, Integer> getScreeningRecordModels() {
        if (screeningRecordModels == null) {
            try {
                screeningRecordModels = getDao(ScreeningRecordModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return screeningRecordModels;
    }

    public  Dao<MedicalScreeningRecordModel, Integer> getMedicalScreeningRecordModels(){
        if (medicalScreeningRecordModels==null){
            try{
                medicalScreeningRecordModels = getDao(MedicalScreeningRecordModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }return medicalScreeningRecordModels;
    }

    public Dao<SurgicalRecordModel,Integer> getSurgicalRecordModels(){
        if (surgicalRecordModels==null){
            try {
                surgicalRecordModels = getDao(SurgicalRecordModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }return surgicalRecordModels;
    }

    public Dao<MedicationRecordModel,Integer> getMedicationRecordDao(){
        if (medicationRecordDao==null){
            try {
                medicationRecordDao = getDao(MedicationRecordModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }return medicationRecordDao;
    }

    public Dao<MedicinesRecordModel,Integer> getMedicinesRecordModelsDao(){
        if (medicinesRecordModelsDao==null){
            try {
                medicinesRecordModelsDao = getDao(MedicinesRecordModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }return medicinesRecordModelsDao;
    }

    public Dao<MedicationAttachmentModel,Integer> getMedicationAttachmentModelsDao(){
        if (medicationAttachmentModelsDao==null){
            try {
                medicationAttachmentModelsDao = getDao(MedicationAttachmentModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }return medicationAttachmentModelsDao;
    }
    public Dao<AppointmentModel,Integer> getAppointmentModelsDao(){
        if (appointmentModelsDao==null){
            try {
                appointmentModelsDao = getDao(AppointmentModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }return appointmentModelsDao;
    }
    public Dao<DoctorInfoModel,Integer> getDoctorInfoModelsDao(){
        if (doctorInfoModelsDao==null){
            try {
                doctorInfoModelsDao = getDao(DoctorInfoModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }return doctorInfoModelsDao;
    }
    public Dao<PatientModel, Integer> getpatientlModelDao() {
        if (patientModels == null) {
            try {
                patientModels = getDao(PatientModel.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return patientModels;
    }
}
