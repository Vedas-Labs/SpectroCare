package com.vedas.spectrocare.PatientServerApiModel;
import java.util.ArrayList;
/*{//invoice update params
        "byWhom":"admin",
        "byWhomID":"viswanath3344",
        "invoiceNumber":"invc000002",
        "hospital_reg_num":"AP2317293903",
        "invoiceIssueDate":"1585446059",
        "invoicePaymentDueDate":"1585449059",
        "subTotal":"23.364",
        "VAT":"2.596",
        "totalAmount":"25.96",
        "isSavedInDraft":false,
        "paymentStatus":"paid",
        "clientDetails":{
        "name" :"Viswa",
        "phoneNumber":{
        "countryCode":"+91",
        "phoneNumber":"9700800307"
        },
        "emailID":"Viswanath3344@gmail.com",
        "address":"Sairadha residency, Anantapur"
        },
        "serviceItems":[{
        "serviceID":"SID1",
        "serviceName":"ON-LINE CONSULTATION STANDARD APPOINTMENT 15 MINS",
        "serviceNetCost":"18.864",
        "category":"Human",
        "serviceUnit":"1",
        "serviceVAT":"2.096",
        "serviceGrossCost":"20.96"
        },
        {
        "serviceID":"SID9",
        "serviceName":"BOOKING SERVICE FEE",
        "serviceNetCost":"4.5",
        "category":"Human",
        "serviceUnit":"1",
        "serviceVAT":"0.5",
        "serviceGrossCost":"5.0"
        }
        ]
        }*/
public class InvoiceModel {
    // use this two for invocie update
    private String  byWhom;
    private String  byWhomID;
    ///
    ArrayList<InvoiceServiceItemsModel> serviceItems;
    private String  invoiceNumber;
    private String  invoiceIssueDate;
    private String  invoicePaymentDueDate;
    private String  hospital_reg_num;
    private String  subTotal;
    private String  VAT;
    private String  isSavedInDraft;
    private String  totalAmount;
    InvoiceClientDetails clientDetails;
    private String  paymentStatus;

    private String  response;
    private String  message;

    public ArrayList<InvoiceServiceItemsModel> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(ArrayList<InvoiceServiceItemsModel> serviceItems) {
        this.serviceItems = serviceItems;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceIssueDate() {
        return invoiceIssueDate;
    }

    public void setInvoiceIssueDate(String invoiceIssueDate) {
        this.invoiceIssueDate = invoiceIssueDate;
    }

    public String getInvoicePaymentDueDate() {
        return invoicePaymentDueDate;
    }

    public void setInvoicePaymentDueDate(String invoicePaymentDueDate) {
        this.invoicePaymentDueDate = invoicePaymentDueDate;
    }

    public String getHospital_reg_num() {
        return hospital_reg_num;
    }

    public void setHospital_reg_num(String hospital_reg_num) {
        this.hospital_reg_num = hospital_reg_num;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getVAT() {
        return VAT;
    }

    public void setVAT(String VAT) {
        this.VAT = VAT;
    }

    public String getIsSavedInDraft() {
        return isSavedInDraft;
    }

    public void setIsSavedInDraft(String isSavedInDraft) {
        this.isSavedInDraft = isSavedInDraft;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public InvoiceClientDetails getClientDetails() {
        return clientDetails;
    }

    public void setClientDetails(InvoiceClientDetails clientDetails) {
        this.clientDetails = clientDetails;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getByWhom() {
        return byWhom;
    }

    public void setByWhom(String byWhom) {
        this.byWhom = byWhom;
    }

    public String getByWhomID() {
        return byWhomID;
    }

    public void setByWhomID(String byWhomID) {
        this.byWhomID = byWhomID;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


