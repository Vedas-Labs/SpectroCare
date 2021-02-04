package com.vedas.spectrocare;

/**
 * Created by jacobliu on 10/29/17.
 */

public class Constants {
    public static String aboutus_url = "https://www.spectrochips.com";

    public enum SpectroSocketEvents {
        SliceUpload("sliceUpload"),
        RequestSliceUpload("requestSliceUpload"),
        EndUpload("endUpload"),
        UploadError("uploadError");

        private final String name;

        private SpectroSocketEvents(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    public enum StatusTypes {
        Accepted("Accepted"),
        Rejected("Rejected"),
        Cancelled("Cancelled"),
        Completed("Completed"),
        Reschedule("Reschedule"),
        Waiting("Waiting");

        private final String name;

        private StatusTypes(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }


    public enum AppLanguages {

        English("English"),
        Traditional("Traditional"),
        Simplified("Simplified"),
        French("French"),
        Portuguese("Portuguese");


        private final String name;

        private AppLanguages(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    public enum GooglePlacesTypes {
        dentist("dentist"),
        doctor("doctor"),
        health("health"),
        veterinary_care("veterinary_care"),
        pharmacy("pharmacy"),
        hospital("hospital");

        private final String name;

        private GooglePlacesTypes(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    public enum JSONTYPES {

        no5_3518_urineTest("no5_3518_urineTest"),
        no9_3518_urineTest("no9_3518_urineTest");

        private final String name;

        private JSONTYPES(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    public enum CLIENTTYPES {

        Human("Human"),
        Pet("Pet");

        private final String name;

        private CLIENTTYPES(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }


    public static enum UrineTestItems {
        LEUKOCYTES("LEUKOCYTES"),
        Nitrite("Nitrite"),
        Urobilinogen("Urobilinogen"),
        Protein("Protein"),
        PH("PH"),
        OccultBlood("Occult Blood"),
        SpecificGravity("Specific Gravity"),
        Ketone("Ketone"),
        Bilirubin("Bilirubin"),
        Glucose("Glucose");

        private final String name;

        private UrineTestItems(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public final String toString() {
            return this.name;
        }
    }



    public enum UrineAndBloodNames {
        urine("Urine"),
        blood("Blood");

        private final String name;

        private UrineAndBloodNames(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    public enum AppointmentTypes {
        appointment("Appointment"),
        videoCall("Calling"),
        invoice("Invoice");

        private final String name;

        private AppointmentTypes(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }
}
